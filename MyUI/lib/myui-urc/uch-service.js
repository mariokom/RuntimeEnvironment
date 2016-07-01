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
  
  queryTargets(targetType) {
    if (targetType === "ColouredLightBulb") {
      return [
        {id: 1, label: "Desk Lamp", targetID: "ColouredLightBulb", socket: "http://openurc.org/TPL/coloured-light-bulb-1.0/ColouredLightBulbSocket"},
        {id: 2, label: "Lamp at Cupboard", targetID: "ColouredLightBulb", socket: ""},
        {id: 3, label: "Lamp besides TV", targetID: "ColouredLightBulb", socket: ""}
      ];
    }
  }
  
  currentState(socket) {
    org_myurc_webclient_init([socket], 0);
    
    let b = org_myurc_webclient_getValue(socket + "#/Brightness");
    let s = org_myurc_webclient_getValue(socket + "#/Saturation");
    let hd = org_myurc_webclient_getValue(socket + "#/HueDegree");
    
    return  { bright: b, sat: s, hue: hd };
  }
  
  setState(socket, value) {
    org_myurc_webclient_init([socket], 0);
    
    org_myurc_webclient_setValue(socket + "#/LightSwitch", value.bright > 0);
    org_myurc_webclient_setValue(socket + "#/Brightness", value.bright);
    org_myurc_webclient_setValue(socket + "#/Saturation", value.sat);
    org_myurc_webclient_setValue(socket + "#/HueDegree", value.hue);
  }
}

// Register in namespace
myui.urc.UchService = UchService;

})();