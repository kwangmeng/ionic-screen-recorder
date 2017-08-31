# cordova-plugin-screenrecord

**cordova plugin, support Android**

using MediaCodec and MediaMuxer to record Android device's screen

## Installation

`cordova plugin add cordova-plugin-screenrecord --save`

## Api Reference
### [ScreenRecord.startRecord(options, filePath, success, error)]()
* options, Object

| Name | Type | Default | Description |
| ---  | :---:  | :---:     | :---: |
| width  | number | 720 |width in pixels to record screen |
| height | number | 1280 |height in pixels to record screen |
| bitRate| number | 6000000 | the bit rate of video |
| dpi | number| 1 | the dpi of video  |

* filePath,  String

	the full absolute path of the video file

	**example:** `/sdcard/yourapp/test.mp4`

* success,  Function

	Callback function that provides a message when the screen-record process started

	After the screen-record process started, the java will call `cordova.getActivity().moveTaskToBack(true)`

* error,  Function

	Callback function that provides a error message

### [ScreenRecord.stopRecord(success, error)]()
	stop a running screen-record process 
* success,  Function

	Callback function that provide a message when stop a running process

* error,  Function

	Callback function that provide a error message.

## Ionic2 Example

use this plugin in ionic2 project

1. `ionic plugin add cordova-plugin-screenrecord`
2. `declare var ScreenRecord` in `declarations.d.ts`
3.  using `ScreenRecord` in your .ts file
