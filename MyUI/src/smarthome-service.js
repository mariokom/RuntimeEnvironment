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
}

// Register in namespace
smarthome.service = new SmarthomeService();

})();