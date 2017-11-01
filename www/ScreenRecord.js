var exec = require('cordova/exec'),
  argscheck = require('cordova/argscheck');

var ScreenRecord = function(){
}

ScreenRecord.startRecord = function(options, filePath, success, error) {
  var getValue = argscheck.getValue;
  options = {
    isAudio: getValue(options.isAudio, false),
    width: getValue(options.width, 720),
    height: getValue(options.height, 1280),
    bitRate: getValue(options.bitRate, 6 * 1000000),
    dpi: getValue(options.dpi, 1)
  };
  exec(success, error, "ScreenRecord", "startRecord", [options, filePath]);
};

ScreenRecord.stopRecord = function(success, error) {
  exec(success, error, "ScreenRecord", "stopRecord", []);
}

// modify by Kenny Ng Kwang Meng, added pause and resume capability for mediarecord only, screen recording have not implemented, ***NOTE: ONLY ANDROID NOUGAT AND ABOVE
ScreenRecord.pauseRecord = function(success, error) {
  exec(success, error, "ScreenRecord", "pauseRecord", []);
}

ScreenRecord.resumeRecord = function(success, error) {
  exec(success, error, "ScreenRecord", "resumeRecord", []);
}

module.exports = ScreenRecord;
