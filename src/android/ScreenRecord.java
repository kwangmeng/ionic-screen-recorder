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
import com.unionpay.screenrecord.MediaRecordService;


/**
 * This class echoes a string called from JavaScript.
 */
public class ScreenRecord extends CordovaPlugin {

    public final static String TAG = "ScreenRecord";

    public MediaProjectionManager mProjectionManager;

    public ScreenRecordService screenRecord;

    public MediaRecordService mediaRecord;

    public CallbackContext callbackContext;

    public JSONObject options;

    public String filePath;

    public boolean isAudio;     // true: MediaRecord, false: ScreenRecord

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
            Log.d(TAG, options.toString());
            isAudio = options.getBoolean("isAudio");
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
        }else if (action.equals("pauseRecord")) {
            this.pauseRecord(callbackContext);
            return true;
        }else if (action.equals("resumeRecord")) {
            this.resumeRecord(callbackContext);
            return true;
        }
        return false;
    }   


     private void pauseRecord(CallbackContext callbackContext) {
        if(isAudio){
            if(mediaRecord != null){
                mediaRecord.pause();
               // mediaRecord = null;
                callbackContext.success("Paused Invoked.");
            }else {
                callbackContext.error("paused invoked failed.");
            }
        }else{
             // you can manipulate screen record capability here
        }
    }



    private void resumeRecord(CallbackContext callbackContext) {
        if(isAudio){
            if(mediaRecord != null){
                mediaRecord.resumeVid();
               // mediaRecord = null;
                callbackContext.success("resume Invoked.");
            }else {
                callbackContext.error("paused invoked failed.");
            }
        }else{
            // you can manipulate screen record capability here
        }
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
        if(isAudio){
            if(mediaRecord != null){
                mediaRecord.release();
                mediaRecord = null;
                callbackContext.success("ScreenRecord finish.");
            }else {
                callbackContext.error("no ScreenRecord in process");
            }
        }else {
            if(screenRecord != null){
                screenRecord.quit();
                screenRecord = null;
                callbackContext.success("ScreenRecord finish.");
            }else {
                callbackContext.error("no ScreenRecord in process");
            }
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
                  String folder_main = "FOLDER_NAME"; //setting folder's name
            File f = new File(Environment.getExternalStorageDirectory(), folder_main);
            if (!f.exists()) {
                f.mkdirs();
            }

                 String name = System.currentTimeMillis() + ".mp4"; // this is to set the file name
                 File appDir = Environment.getExternalStorageDirectory();  // this is to obtain the public storage directory
            // 出力ファイルのパス 
                String path = new File(appDir+"/"+folder_main, name).getAbsolutePath(); // join the path and file name together to obtain absolute path. 


               if(isAudio){
                mediaRecord = new MediaRecordService(width, height, bitRate, dpi, mediaProjection, path); //change the parameter to path. 
                mediaRecord.start();
               }else {
                screenRecord = new ScreenRecordService(width, height, bitRate, dpi,
                mediaProjection, path);//change the parameter to path. 
                screenRecord.start();
               }
               
               Log.d(TAG, "screenrecord service is running");
               this.callbackContext.success("screenrecord service is running");
               cordova.getActivity(); //removed movetasktoback, so if when plugin is invoked in home page, it closes the app to previous screen (exit). 
           }catch (Exception e){
              e.printStackTrace();
          }
      }
  }

}
