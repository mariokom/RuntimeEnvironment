(function() {
'use strict';

/**
 * 
 */
class AreService extends myui.AaimService {
  constructor(baseURI) {
    super();
    
    setBaseURI(baseURI);
    
    this._functions.set("listStoredModels", this.listStoredModels);
    this._functions.set("startModel", this.startModel);
    this._functions.set("stopModel", this.stopModel);
  }
  
  listStoredModels() {
    return new Promise(function(resolve, reject) {
      listStoredModels(function(data, httpStatus) {
        resolve(data);
      }, function(httpStatus, message) {
        reject(new Error(message));
      });
    });
  }
  
  startModel(modelPath) {
    return new Promise(function(resolve, reject) {
      autorun(function(data, httpStatus) {
        resolve(data);
      }, function(httpStatus, message) {
        reject(new Error(message));
      }, modelPath);
    });
  }
  
  stopModel() {
    return new Promise(function(resolve, reject) {
      stopModel(function(data, httpStatus) {
        resolve(data);
      }, function(httpStatus, message) {
        reject(message);
      });
    });
  }
}

// Register in namespace
myui.asterics.AreService = AreService;

})();