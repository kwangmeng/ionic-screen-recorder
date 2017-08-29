package com.unionpay.screenrecord;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import android.Manifest;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.util.Log;
import android.content.pm.PackageManager;

import com.unionpay.screenrecord.ScreenRecordService;

/**
 * This class echoes a string called from JavaScript.
 */
public class ScreenRecord extends CordovaPlugin {

    public final static String TAG = "ScreenRecord";

    public MediaProjectionManager mProjectionManager;

    public ScreenRecordService screenRecord;

    public CallbackContext callbackContext;

    public JSONObject options;

    public String filePath;

    public int width, height, bitRate, dpi;

    public static final int PERMISSION_DENIED_ERROR = 20;
    public static final int RECORD_AUDIO= 0;

    protected final static String permission = Manifest.permission.RECORD_AUDIO;

    public final static int SCREEN_RECORD_CODE = 0;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (action.equals("startRecord")) {
            options = args.getJSONObject(0);
            width = options.getInt("width");
            height = options.getInt("height");
            bitRate = options.getInt("bitRate");
            dpi = options.getInt("dpi");
            filePath = args.getString(1);
            this.startRecord(callbackContext);
            return true;
        }else if (action.equals("stopRecord")) {
            this.stopRecord(callbackContext);
            return true;
        }
        return false;
    }

    private void startRecord(CallbackContext callbackContext) {
        if (screenRecord != null) {
            callbackContext.error("screenRecord service is running");
        } else {
            if(cordova != null) {
                try {
                    callScreenRecord();
                }catch(IllegalArgumentException e) {
                    callbackContext.error("Illegal Argument Exception");
                    PluginResult r = new PluginResult(PluginResult.Status.ERROR);
                    callbackContext.sendPluginResult(r);
                }
            }
        }
    }

    private void stopRecord(CallbackContext callbackContext) {
        if(screenRecord != null){
            screenRecord.quit();
            screenRecord = null;
            callbackContext.success("ScreenRecord finish.");
        }else {
            callbackContext.error("no ScreenRecord in process");
        }
    }

    private void callScreenRecord() {
        mProjectionManager = (MediaProjectionManager)this.cordova.getActivity().getSystemService("media_projection");
        Intent captureIntent = mProjectionManager.createScreenCaptureIntent();
        cordova.startActivityForResult(this, captureIntent, SCREEN_RECORD_CODE);
    }

    /**
     * mediaprojection回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        MediaProjection mediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        if (mediaProjection == null) {
            Log.e(TAG, "media projection is null");
            callbackContext.error("no ScreenRecord in process");
            return;
        }
        if(requestCode == 0){
           try {
               File file = new File(filePath);
               screenRecord = new ScreenRecordService(width, height, bitRate, dpi,
                   mediaProjection, file.getAbsolutePath());
               screenRecord.start();
               Log.d(TAG, "screenrecord service is running");
               this.callbackContext.success("screenrecord service is running");
               cordova.getActivity().moveTaskToBack(true);
           }catch (Exception e){
              e.printStackTrace();
          }
      }
  }

}
