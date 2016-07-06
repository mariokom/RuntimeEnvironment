var smarthome = smarthome || {};

(function() {
'use strict';

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
        { on: "SelectService('models')", goto: "Models" }
      ]
    },
    {
      name: "Lamps",
      do: {
        situation: "SelectLamp",
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
        { on: "SelectLamp", goto: "Adjust" },
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
            "${SelectLamp.socketName}",
            "${SelectLamp.targetId}"
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
              "${SelectLamp.socketName}",
              "${SelectLamp.targetId}",
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
        situation: "SelectModel",
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
          on: "SelectModel", 
          goto: "Models",
          do: {
            service: "asterics",
            name: "startModel",
            parameters: [
              "${SelectModel.modelPath}"
            ]
          }
        },
        { on: "MetaGoto('back')", goto: "Services" }
      ]
    }
  ]
};

let behavior = new myui.AaimBehavior(smarthome.factory, smarthome.service);
behavior.registerService("urc", new myui.urc.UchService());
behavior.registerService("asterics", new myui.asterics.AreService("http://localhost:8081/rest/"));

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
  
  // Just for testing purpose
  window.aaiminterpreter = interpreter;
};

}());