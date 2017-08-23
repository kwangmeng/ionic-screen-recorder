var exec = require('cordova/exec');

var ScreenRecord = function(){
}

ScreenRecord.startRecord = function(options, filePath, success, error) {
  exec(success, error, "ScreenRecord", "startRecord", [options, filePath]);
};

ScreenRecord.stopRecord = function(success, error) {
  exec(success, error, "ScreenRecord", "stopRecord", []);
}

module.exports = ScreenRecord;
