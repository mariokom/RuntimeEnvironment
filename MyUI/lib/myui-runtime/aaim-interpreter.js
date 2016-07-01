(function() {
'use strict';

/**
 * 
 */
class AaimInterpreter {
  
	/**
	 * Create a new AaimInterpreter
	 * 
	 * @param {AaimBehavior} behavior
	 *    The AaimBehavior implementation the execution of the application states 
	 *    is delegated to.
	 */
	constructor(behavior) {
	  /** The behavior instance executing state configurations */
	  this._behavior = behavior;
	  
    /** The running state of the interpreter */
    this._running = false;
    
    /** The currently loaded AAIM */
    this._currentAaim = undefined;
    
    /** The current state */
    this._currentState = undefined;
    
    /** The states of the current AAIM mapped by their names */
    this._loadedStates = undefined;
	}

  /**
   * Returns if the interpreter is currently running.
   * 
   * @return {Boolean} true, if the interpreter is running, false otherwise
   */
  get running() {
    return this._running;
  }
  
  /**
   * Sets the interpreter to run or to pause. The interpreter can only be set
   * to be running, if an AAIM is loaded.
   * 
   * @param {Boolean} run
   *    true to run the interpreter, false to pause
   */
  set running(run) {
    if (run && !this._running && this._currentAaim !== undefined) {
      this._running = true;
      
      // Initial startup?
      if (this._currentState === undefined) { 
        if (typeof this._currentAaim.initial === "string" 
              && this._loadedStates.has(this._currentAaim.initial)) {
          this._performTransition(this._loadedStates.get(this._currentAaim.initial));
        } else {
          // Initial state is not defined or does not exist
          this._running = false;
        }
      }
    } else {
      this._running = false;
    }
  }
  
  /**
   * Returns the currently loaded AAIM.
   * 
   * @return {Object} the currently loaded AAIM or undefined
   */
  get aaim() {
    return this._currentAaim;
  }
  
  /**
   * Returns the current state of the AAIM.
   * 
   * @return {Object} the current state or undefined
   */
  get state() {
    return this._currentState;
  }
  
  /**
   * Loads an AAIM represented by a JSON datastructure.
   * 
   * @param {Object} aaim
   *    
   */
  load(aaim) {
    if (!this._running) { // AAIMs can only be replaced while not running
      if (typeof aaim === "object" && aaim !== null // AAIM objects but not null
          && Array.isArray(aaim.states) && aaim.states.length > 0) { // with an non-empty states array
        this._currentAaim = aaim;
        
        // Create states map
        this._loadedStates = new Map();
        for (let s of this._currentAaim.states) {
          this._loadedStates.set(s.name, s);
        }
        
        return true;
      }
    }
    return false;
  }
  
  /**
   * Resets a paused interpreter. After the reset the interpreter will be in the
   * same state as directly after loading an AAIM.
   * Calls to this method will have no effect if the interpreter is currently
   * running.
   */
  reset() {
    if (!this._running) {
      this._currentState = undefined;
    }
  }
  
  /**
   * Executes an event on the current state of the interpreter by its name. An
   * event name not defined for the current state will have no effect.
   * Calls to this method will have no effect if the interpreter is currently 
   * paused.
   */
  executeEvent(name) {
    if (this._running) {
      for (let e of this._currentState.events) {
        if (e.on == name && this._loadedStates.has(e.goto)) {
          this._performTransition(this._loadedStates.get(e.goto), e.do);
          break;
        }
      }
    }
  }
  
  /**
   * 
   * 
   * @private
   * 
   * @param {Object} target
   *    The target state to transition to
   */
  _performTransition(target, config) {
    this._currentState = target;
    
    if (config && this._behavior) {
      this._behavior.executeTransition(config);
    }
    
    if (this._behavior) {
      this._behavior.executeState(target.do);
    }
  }
}

// Register in namespace
myui.AaimInterpreter = AaimInterpreter;

}());