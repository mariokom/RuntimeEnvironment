(function() {
'use strict';

// Import
let ProfileStore = awc.ProfileStore;

/**
 * An ProfileStore implementation fetching profiles from a URL.
 */
class AjaxProfileStore extends ProfileStore {
  constructor(url) {
    super();
    
    /** The URL to load the profile from */
    this._url = url;
    this._loadProfile();
  }
  
  _loadProfile() {
    if (this._profile) return;
    
    let _this = this;
    
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
      if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
        _this._profile = JSON.parse(xhr.responseText); 
        
        let diff = {};
        for (let p in _this._profile) {
          diff[p] = { from: undefined, to: _this._profile[p] };
        }
        
        // Dispatch profile changed event
        _this._profileChanged(diff);
      }
    };
    xhr.open("GET", this._url, true);
    xhr.send(null);
  }
}

// Register in namespace
awc.AjaxProfileStore = AjaxProfileStore;

}());