# cordova-plugin-screenrecord

**cordova plugin, support Android ONLY**

using MediaRecorder or MediaMuxer to record Android device's screen

This plugin is not made by me but I did some modification in the code such as adding functionalities like pause and resume in mediarecord. 

## Installation

`cordova plugin add cordova-plugin-screenrecord --save`

The modification made is suit for my use. You can modify the options in ScreenRecord.java.
 

## Api Reference
### [ScreenRecord.startRecord(options, success, error)]()
* options, Object

| Name | Type | Default | Description |
| ---  | :---:  | :---:     | :---: |
| isAudio | boolean | false | If true, the plugin uses MediaRecorder to record audio & video |
| width  | number | 720 |width in pixels to record screen |
| height | number | 1280 |height in pixels to record screen |
| bitRate| number | 6000000 | the bit rate of video |
| dpi | number| 1 | the dpi of video  |

* success,  Function

Callback function that provides a message when the screen-record process started

After the screen-record process started, the java will call `cordova.getActivity()(true)`

* error,  Function

Callback function that provides a error message

### [ScreenRecord.stopRecord(success, error)]()
stop a running screen-record process 
* success,  Function

Callback function that provide a message when stop a running process

* error,  Function

Callback function that provide a error message.

### [ScreenRecord.pauseRecord(success, error)]()

**NOTE: ONLY WORKS FOR ANDROID NOUGAT AND ABOVE**

pause a running screen-record process 
* success,  Function

Callback function that provide a message when stop a running process

* error,  Function

Callback function that provide a error message.

### [ScreenRecord.resumeRecord(success, error)]()
**NOTE: ONLY WORKS FOR ANDROID NOUGAT AND ABOVE**

resume a running screen-record process 
* success,  Function

Callback function that provide a message when stop a running process

* error,  Function

Callback function that provide a error message.
## Ionic2/3 Example

use this plugin in ionic2 project

1. `ionic plugin add cordova-plugin-screenrecord`
2. `declare var ScreenRecord` in `declarations.d.ts`
3.  using `ScreenRecord` in your .ts file
