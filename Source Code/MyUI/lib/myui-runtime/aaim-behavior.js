(function() {
'use strict';

/**
 * 
 */
class AaimBehavior {
  
  /**
   * Create a new AaimBehavior
   */
  constructor(situationFactory, defaultService) {
    /** The SituationFactory */
    this._factory = situationFactory;
    
    /** Registered services */
    this._services = new Map();
    if (defaultService) {
      this._services.set("", defaultService);
    }
    
    /** The data context */
    this._data = new Map();
    
    /** The last executed state configuration */
    this._currentConfig = undefined;
  }
  
  /**
   * 
   */
  executeState(config) {
    let situationParameters;
    
    if (Array.isArray(config.parameters)) {
      // List of parameter values
      situationParameters = this._resolveParameters(config.parameters);
      
    } else if (typeof config.parameters === "object") {
      // Service call configuration
      let service = this._services.get(config.parameters.service ? config.parameters.service : "");
      situationParameters = service.execute(config.parameters.name, ...this._resolveParameters(config.parameters.parameters));
      // Perform result mapping if configured
      if (config.parameters.mapping) {
        let mapService = this._services.get(config.parameters.mapping.service ? config.parameters.mapping.service : "");
        situationParameters = situationParameters.then(function(result) {
          return mapService.execute(config.parameters.mapping.name, result);
        });
      }
    } else {
      situationParameters = [];
    }
    
    if (this._currentConfig === config) {
      // Refresh current situation with identical configuration
      this._factory.refresh(config.situation, situationParameters, this._data);
      
    } else {
      // Create new situation for new configuration
      this._factory.create(config.situation, situationParameters, this._data);
    
      // Store config
      this._currentConfig = config;
    }
  }

  /**
   * 
   */
  executeTransition(config) {
    if (typeof config === "object") {
      // Service call configuration
      let result;
      let service = this._services.get(config.service ? config.service : "");
      
      if (config.parameterMapping) {
        let parameters = this._resolveParameters(config.parameters);
        result = Promise.all(parameters.map(function(param, index) {
          if (config.parameterMapping[index]) {
            let mapService = this._services.get(config.parameterMapping[index].service ? config.parameterMapping[index].service : "");
            return mapService.execute(config.parameterMapping[index].name, param);
          } else {
            return param;
          }
        }, this)).then(function(mapped) {
          return service.execute(config.name, ...mapped);
        });
        
      } else {
        result = service.execute(config.name, ...this._resolveParameters(config.parameters));
      }
      
      return Promise.resolve(result);
    }
	}
	
  /**
   * Registers a service with a given name.
   * 
   * @param {String} name
   *    The non-empty name under which the service should be registered.
   * @param {AaimService} service
   *    The actual service implementation to register.
   */
  registerService(name, service) {
    if(!name || name.length < 1) {
      throw new Error("A service cannot be registered without a name.");
    } else if (!service || typeof service.execute !== "function") {
      throw new Error("Services are required to provide an 'execute' function.");
    } else if (this._services.has(name)) {
      throw new Error(`There is already a service named '${name}'.`);
    }
    
    this._services.set(name, service);
  }
	
  /**
   * Resolves the given array of parameter values and parameter references 
   * into an array of values.
   * 
   * @private
   * 
   * @param {Array} parameters
   *    The parameter array to resolve.
   * 
   * @returns {Array} The array of resolved values.
   */
  _resolveParameters(parameters) {
    let varPattern = /\${(?:(\w+)|([\w\.]+))}/;
    let resolved = parameters.map(function(param) {
      let match = varPattern.exec(param);
      
      if (match !== null && match[1] !== undefined) {
        return this._data.get(match[1]);
        
      } else if (match !== null && match[2] !== undefined) {
        let steps = match[2].split(".");
        let val = this._data.get(steps[0]);
        for (let i=1; typeof val == "object" && i<steps.length; i++) {
          val = val[steps[i]];
        }
        return val;
        
      } else {
        return param;
      }
    }, this);
    return resolved;
  }
}

// Register in namespace
myui.AaimBehavior = AaimBehavior;

}());