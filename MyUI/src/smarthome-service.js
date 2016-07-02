var smarthome = smarthome || {};

(function() {
'use strict';
/**
 * 
 */
class SmarthomeService extends myui.AaimService {
  constructor() {
    super();
    
    this._functions.set("getInstalledApps", this.getInstalledServices);
    this._functions.set("mapTargetsToMenu", this.mapTargetsToMenu);
    this._functions.set("mapUrcHSBtoColor", this.mapUrcHSBtoColor);
    this._functions.set("mapColorToUrcHSB", this.mapColorToUrcHSB);
  }
  
  getInstalledServices() {
    return [
      { id: "tv", label: "Television" },
      { id: "weather", label: "Weather Forecast" },
      { id: "mail", label: "E-Mails" },
      { id: "light", label: "Lights Control" },
      { id: "some", label: "Thing" },
      { id: "some", label: "Thing" },
      { id: "some", label: "Thing" },
      { id: "some", label: "Thing" },
      { id: "some", label: "Thing" },
      { id: "some", label: "Thing" },
      { id: "some", label: "Thing" }
    ];
  }
  
  mapTargetsToMenu(targets) {
    return targets.map(
      function(target, index) {
        return {
          id: index,
          label: target.targetLabel,
          targetId: target.targetId,
          socketName: target.socketName
        };
      }
    );
  }
  
  mapUrcHSBtoColor(hsbValues) {
    return { 
      hue: hsbValues.HueDegree, 
      sat: hsbValues.Saturation, 
      bright: hsbValues.Brightness
    };
  }
  
  mapColorToUrcHSB(color) {
    return {
      HueDegree: color.hue,
      Saturation: color.sat,
      Brightness: color.bright,
      LightSwitch: color.bright > 0
    };
  }
}

// Register in namespace
smarthome.service = new SmarthomeService();

})();