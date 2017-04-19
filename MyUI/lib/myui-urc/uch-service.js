(function() {
'use strict';

/**
 * 
 */
class UchService extends myui.AaimService {
  constructor() {
    super();
    
    this._functions.set("queryTargets", this.queryTargets);
    this._functions.set("currentState", this.currentState);
    this._functions.set("setState", this.setState);
  }
  
  queryTargets(socketName) {
    let allTargets = org_myurc_urchttp_getAvailableSockets();
    
    if (socketName) {
      return allTargets.filter(function(target) {
        return target.socketName === socketName;
      });
    } else {
      return allTargets;
    }
  }
  
  currentState(socketName, targetId) {
	org_myurc_webclient_openSession(socketName, targetId);
    org_myurc_webclient_init([socketName], 0);

    let values = {};
    for (let elem of org_myurc_webclient_getValues(socketName, "/", targetId)) {
      if (elem.operation === "value") {
        values[elem.path.substr(1)] = elem.value;
      }
    }
    
    return values;
  }
  
  setState(socketName, targetId, values) {
	org_myurc_webclient_openSession(socketName, targetId);
    org_myurc_webclient_init([socketName], 0);
    
    let elements = [];
    for (let val in values) {
      elements.push({
        elementPath: "/" + val,
        operation: "S",
        value: values[val]
      });
    }
    
    org_myurc_webclient_setValues(socketName, elements, targetId);
  }
}

// Register in namespace
myui.urc.UchService = UchService;

})();