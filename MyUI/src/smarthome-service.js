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
    this._functions.set("mapModelsToMenu", this.mapModelsToMenu);
    this._functions.set("mapUrcSwitches", this.mapUrcSwitches);
    this._functions.set("mapSwitchToUrc", this.mapSwitchToUrc);
  }
  
  getInstalledServices() {
    return [
      { id: "tv", label: "Television" },
      { id: "light", label: "Lights Control" },
      { id: "models", label: "AsTeRICS" },
      { id: "outlets", label: "Switched Outlets" },
      { id: "mail", label: "E-Mails" },
      { id: "weather", label: "Weather Forecast" },
      { id: "x1", label: "Something" },
      { id: "x2", label: "Something" },
      { id: "x3", label: "Something" },
      { id: "x4", label: "Something" },
      { id: "x5", label: "Something" }
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
  
  mapModelsToMenu(models) {
    return models.map(
      function(model, index) {
        return {
          id: index,
          label: model,
          modelPath: model
        };
      }
    );
  }
  
  mapUrcSwitches(urcSwitches) {
    let switches = [];
    for (let s in urcSwitches) {
      let val = urcSwitches[s];
      if (typeof val !== Boolean) {
        val = val === "true";
      }
      switches.push({ id: s, label: s, value: val });
    }
    return switches;
  }
  
  mapSwitchToUrc(switchObj) {
    let urcValue = {};
    urcValue[switchObj.id] = !switchObj.value;
    return urcValue;
  }
}

// Register in namespace
smarthome.service = new SmarthomeService();

})();