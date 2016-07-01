(function() {
'use strict';

/**
 * 
 */
class AaimService {
  
  /**
   * Create a new AaimService
   */
  constructor() {
    /** Provided functions by name*/
    this._functions = new Map();
  }
  
  /**
   * Calls a function by its name applying the provided parameters.
   * 
   * @param {String} name
   *    The name of the function to call.
   * @param {Array} params
   *    The parameters to call the function with.
   * 
   * @returns {Promise} A promise to the result of the function call
   * @throws an error if no function with thre requested name is provided by this service.
   */
  execute(name, ...params) {
    // Check if function exists
    if (!this._functions.has(name)) {
      throw new Error(`Function '${name}' is not provided by this service.`);
    }
    
    // Execute function with given params
    let val;
    try {
      // Actualy execute function
      val = this._functions.get(name).apply(this, params);
      
      // Wrap non-promise return values into promise
      if (val instanceof Promise) {
        return val;
      } else {
        return new Promise(function(resolve, reject) {
          resolve(val);
        });
      }
    } catch(e) {
      // On error, return immediatly rejecting promise
      return new Promise(function(resolve, reject) {
        reject(e);
      });
    }
  }
  
  /**
   * Checks if a function is provided by this service.
   * 
   * @param {String} name
   *    The name of the function to check.
   * 
   * @returns {Boolean} true if a functions with the given name is provided by this service, false otherwise.
   */
  provides(name) {
    return this._functions.has(name);
  }
}

// Register in namespace
myui.AaimService = AaimService;

}());