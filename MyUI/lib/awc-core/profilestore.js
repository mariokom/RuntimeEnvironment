(function() {
'use strict';

const PROFILE_CHANGED_EVENT = 'awcProfileChanged';

/**
 * Implementations based on this (abstract) base class represent different sources, storage locations or fetch methods
 * for user interface profiles.
 */
class ProfileStore {
	constructor() {
		/** The current profile */
		this._profile = undefined;
	}
	
	/**
	 * Returns a promise to the current user interface profile.
	 */
	get profile() {
    let profile = this._profile;
		return new Promise(function(resolve, reject) {
			if (profile) {
				resolve(Object.assign({}, profile));
			} else {
				reject('No profile available.');
			}
		});
	}
	
	/**
	 * Allows to request a value change of profile properties. It returns a promise to
	 * 
	 * @param {JSON} newValues - A map of the requested property changes. JSON structured like
	 * 		<code>{ propertyName: newValue , ... }</code>.
	 */
	changeProfile(newValues) {
		return Promise.reject('Changing the profile is not supported.');
	}
	
	/**
	 * Creates and dispatches a profile changed event containing information on changed values of profile properties.
	 * 
	 * @param {JSON} diff - A data structure describing the last changes to the profile properties. JSON is expected to
	 *      be structured like <code>{ propertyName: { from: oldValue, to: newValue }, ... }</code>.
	 */
	_profileChanged(diff) {
		if (true) {	// TODO: Check for valid diff
			// Create profile changed event
      var changeEvent = new CustomEvent(PROFILE_CHANGED_EVENT, { detail: {
				store: this,
				changes: diff
			}});
			
			// Dispatch event
			window.dispatchEvent(changeEvent);
		}
	}
}

// Register in namespace
awc.ProfileStore = ProfileStore;
awc.PROFILE_CHANGED_EVENT = PROFILE_CHANGED_EVENT;

}());