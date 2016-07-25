var smarthome = smarthome || {};

(function() {
'use strict';

// The URI of the AsTeRICS Runtime Environment's REST interface
let areURI = "http://localhost:8085/rest/";

// Define the smarthome AAIM
let aaim = {
  initial: "Services",
  states: [
    {
      name: "Services",
      do: {
        situation: "SelectService",
        parameters: {
          name: "getInstalledApps",
          parameters: []
        }
      },
      events: [
        { on: "SelectService('light')", goto: "Lamps" },
        { on: "SelectService('models')", goto: "Models" },
        { on: "SelectService('outlets')", goto: "Outlets" }
      ]
    },
    {
      name: "Lamps",
      do: {
        situation: "SelectOneOfMany",
        parameters: {
          service: "urc",
          name: "queryTargets",
          parameters: [
            "http://openurc.org/TPL/coloured-light-bulb-1.0/ColouredLightBulbSocket"
          ],
          mapping: {
            name: "mapTargetsToMenu"
          }
        }
      },
      events: [
        { on: "SelectOneOfMany", goto: "Adjust" },
        { on: "MetaGoto('back')", goto: "Services" }
      ]
    },
    {
      name: "Adjust",
      do: {
        situation: "AdjustLight",
        parameters: {
          service: "urc",
          name: "currentState",
          parameters: [
            "${SelectOneOfMany.socketName}",
            "${SelectOneOfMany.targetId}"
          ],
          mapping: {
            name: "mapUrcHSBtoColor"
          }
        }
      },
      events: [
        {
          on: "AdjustLight", 
          goto: "Adjust", 
          do: {
            service: "urc",
            name: "setState",
            parameters: [
              "${SelectOneOfMany.socketName}",
              "${SelectOneOfMany.targetId}",
              "${AdjustLight}"
            ],
            parameterMapping: [
              undefined,
              undefined,
              { name: "mapColorToUrcHSB" }
            ]
          }
        },
        { on: "MetaGoto('back')", goto: "Lamps" }
      ]
    },
    {
      name: "Models",
      do: {
        situation: "SelectOneOfMany",
        parameters: {
          service: "asterics",
          name: "listStoredModels",
          parameters: [],
          mapping: {
            name: "mapModelsToMenu"
          }
        },
      },
      events: [
        { 
          on: "SelectOneOfMany", 
          goto: "Models",
          do: {
            service: "asterics",
            name: "startModel",
            parameters: [
              "${SelectOneOfMany.modelPath}"
            ]
          }
        },
        { on: "MetaGoto('back')", goto: "Services" }
      ]
    },
    {
      name: "Outlets",
      do: {
        situation: "SelectOneOfMany",
        parameters: {
          service: "urc",
          name: "queryTargets",
          parameters: [
            "http://hdm-stuttgart.de/WoehlkeWebsteckdose/WoehlkeWebsteckdoseSocket"
          ],
          mapping: {
            name: "mapTargetsToMenu"
          }
        }
      },
      events: [
        { on: "SelectOneOfMany", goto: "Switch" },
        { on: "MetaGoto('back')", goto: "Services" }
      ]
    },
    {
      name: "Switch",
      do: {
        situation: "SwitchOutlets",
        parameters: {
          service: "urc",
          name: "currentState",
          parameters: [
            "${SelectOneOfMany.socketName}",
            "${SelectOneOfMany.targetId}"
          ],
          mapping: {
            name: "mapUrcSwitches"
          }
        }
      },
      events: [
        {
          on: "SwitchOutlets",
          goto: "Switch",
          do: {
            service: "urc",
            name: "setState",
            parameters: [
              "${SelectOneOfMany.socketName}",
              "${SelectOneOfMany.targetId}",
              "${SwitchOutlets}"
            ],
            parameterMapping: [
              undefined,
              undefined,
              { name: "mapSwitchToUrc" }
            ]
          }
        },
        { on: "MetaGoto('back')", goto: "Outlets" }
      ]
    }
  ]
};

// Create the SituationFactory specific to the application domain
smarthome.factory = new smarthome.SmarthomeSituationFactory(new myui.AdaptationEngine());

// Initialize the AaimBehavior and register aditional services
let behavior = new myui.AaimBehavior(smarthome.factory, smarthome.service);
behavior.registerService("urc", new myui.urc.UchService());
behavior.registerService("asterics", new myui.asterics.AreService(areURI));

// Create the AaimInterpreter and load the applications AAIM
let interpreter = new myui.AaimInterpreter(behavior);
interpreter.load(aaim);
smarthome.factory.situationEventHandler = interpreter;

// Start the interpreter when document loading is completed
window.onload = function() {
  // Configure factory
  smarthome.factory.header = document.querySelector("header");
  smarthome.factory.main = document.querySelector("main");
  
  // Run the AAIM
  interpreter.running = true;
  
  // Load profile
  new awc.AjaxProfileStore("profiles/currentuser.json");
};

}());