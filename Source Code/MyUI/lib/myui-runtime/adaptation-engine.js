(function() {
'use strict';

// Import
let PROFILE_CHANGED_EVENT = awc.PROFILE_CHANGED_EVENT;

/**
 * 
 */
class AdaptationEngine {
  constructor() {
    /** The set of widgets managed by this engine */
    this._handlers = new Set();
    
    /** The current profile */
    this._currentProfile = undefined;
    
   	// Register to profile change events
		window.addEventListener(PROFILE_CHANGED_EVENT, this._profileChanged.bind(this));
  }
  
  register(handler) {
		// Add to registered handlers
		this._handlers.add(handler);
		
		// Perform initial adaptation
		handler.performAdaptation(this._currentProfile);
	}
	
	unregister(handler) {
		// Remove from registered handler
		this._handlers.delete(handler);
	}
	
	/**
   * Event handling routine for profile changed events
   */
  _profileChanged(event) {
    let profile = {};
    for (let p in event.detail.changes) {
      profile[p] = event.detail.changes[p].to;
    }
    
    for (let h of this._handlers) {
      h.performAdaptation(profile);
    }
    
    this._currentProfile = profile;
  }
}

// Register in namespace
myui.AdaptationEngine = AdaptationEngine;

}());