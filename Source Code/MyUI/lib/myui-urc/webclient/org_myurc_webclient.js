/*
 Copyright: Access Technologies Group, Germany, 2007-2009.  
 This software is licensed under the CC-GNU GPL.  See http://creativecommons.org/licenses/GPL/2.0/ for 
 human-readable Commons Deed, lawyer-readable legal code, and machine-readable digital code.

 ***********************************************************************************

 JavaScript library for webclients based on org_myurc_urchttp lib.  A DHTML user interface
   is automatically and dynamically generated based on the socket description and updates
   from a target.  
   
 See readme.txt for instructions of how to use the webclient library.
 
 Imports (make sure these files are read before execution of any code in org_myurc_webclient.js): 
   org_myurc_socket.js
   org_myurc_urchttp.js
   org_myurc_lib.js
      
 List of exported functions:
   org_myurc_webclient_init: Do all necessary initialization so that the module can be used:
   org_myurc_webclient_finalize: Do all necessary clean-up before the browser window is closed:
   org_myurc_webclient_setUpdateInterval: Set the refresh time for updates polling.
   org_myurc_webclient_getUpdateInterval: Get the current refresh time for updates polling.
   org_myurc_webclient_getSession: Get the session object for a given socket name and targetId.  Open the session if not existing yet.
   org_myurc_webclient_openSession: Open a session with a socket.
   org_myurc_webclient_closeSession: Close a session with the UCH.
   org_myurc_webclient_setValue: Signal that the user has changed a value on the user interface.
   org_myurc_webclient_setValues: Signal that the user has changed values on the user interface.
   org_myurc_webclient_getValue: Get the current value of a socket element.
   org_myurc_webclient_getValues: Get the values for the specified socket elements.
   org_myurc_webclient_getIndex: Get the available index values for a dimensional socket set or element.
   org_myurc_webclient_invoke: Signal that the user has requested to invoke a command.
   org_myurc_webclient_acknowledge: Signal that the controller has received a notification from the server, and - if required -
   org_myurc_webclient_postUpdates: Request and post server updates for all open sessions.
   org_myurc_webclient_postUpdatesForSession: Request and post server updates for a specific session.
   org_myurc_webclient_getUpdates: Request server updates for a specific session.
   
 Author: Gottfried Zimmermann, ATG
 Last modified: 2009-08-07
 
 **********************************************************************************/

/**
  Declaration of variables
 */ 

org_myurc_webclient_autogenerate = false;
 // Clients that want to use autogenerate mode have to set this manually before calling init (hidden feature).
org_myurc_webclient_updateInterval = 0;
org_myurc_webclient_forLang = document.documentElement.lang;
org_myurc_webclient_timer = null;
org_myurc_webclient_sessions = {};   // Map for open session objects, with sessionId as key.  
/* Each session (object) in org_myurc_webclient_sessions has the following properties:
   * socketName: name (URI) of the socket.
   * sessionId: Session identifier, given by UCH.
   * protocolUrl: Session-specific URL for the URC-HTTP protocol.
   * socketDescriptionAt: URL for socket description.
   * targetDescriptionAt: URL for target description.
   * socket: Socket object (as created by org_myurc_socket.js)
   * targetId: target identifier
 */
 
/********************************************************
org_myurc_webclient_init: Do all necessary initialization so that the module can be used:
      * Insert missing socket names for ids.
      * Add event handlers for elements with ids.
      * Open sessions for involved socket names.
      * Post all values for the open sessions.
      * Set update interval.
   sockets: array of sockets to open a session with.
   updateInterval: polling interval (in milliseconds) / 0 for no polling
	return: -
	Exception: May throw Error ex, with text message in ex.message.
 ********************************************************/
 
org_myurc_webclient_init = function(sockets, updateInterval) {
   
   if (sockets) {       // Parameter 'sockets' provided?
      for (var i=0; i < sockets.length; i++)
         org_myurc_webclient_openSession(sockets[i], null, null);    // targetId and authorizationCode not known.
   }
   
   if (org_myurc_webclient_autogenerate) {
      org_myurc_webclient_processNodeAndChildren(document.getElementsByTagName("BODY")[0], null);    
   }
         // Recursively walk through all nodes under the <body> node.
         // As a result, all regular ids are now consisting of socketName#path, have event handlers, and sessions for 
         // the used socket names have been opened.
   
   // Now display the values of the open sessions.
	for (var sessionId in org_myurc_webclient_sessions) {	
      var session = org_myurc_webclient_sessions[sessionId];
  	   var pathValues = org_myurc_urchttp_getValues(session.protocolUrl, session.sessionId, "/");
	   org_myurc_webclient_postPathValues(session, pathValues);
   	}
	org_myurc_webclient_setUpdateInterval(updateInterval);      // Only start updates when initial rendering of values done and web socket is not supported.
	
};


/********************************************************
org_myurc_webclient_finalize: Do all necessary clean-up before the browser window is closed:
      * Close all open sessions.
	return: -
 ********************************************************/
org_myurc_webclient_finalize = function() {
   for (var sessionId in org_myurc_webclient_sessions) {
      org_myurc_webclient_closeSession(null, null, org_myurc_webclient_sessions[sessionId]);
   }
};

/********************************************************
org_myurc_webclient_setUpdateInterval: Set the refresh time for updates polling.
   interval: interval (in milliseconds) / 0 for no polling
	return: -
 ********************************************************/
org_myurc_webclient_setUpdateInterval = function(interval) {
	org_myurc_webclient_updateInterval = interval;
    
   if (org_myurc_webclient_timer) {
	   window.clearInterval(org_myurc_webclient_timer);
	   org_myurc_webclient_timer = null;
   }

   if (interval > 0) {
	   org_myurc_webclient_timer = setInterval(org_myurc_webclient_postUpdates, interval); 
   }
   
};

/********************************************************
org_myurc_webclient_getUpdateInterval: Get the current refresh time for updates polling.
   return: interval (in milliseconds) / 0 for no polling
 ********************************************************/
org_myurc_webclient_getUpdateInterval = function() {
   return org_myurc_webclient_updateInterval;
};


/********************************************************
org_myurc_webclient_getSession: Get the session object for a given socket name and targetId.
	socketName: Name (URI) of the socket.
	targetId: Target identifier (to disambiguate multiple sockets with the same name) - may be omitted or null/undefined.
	return: session object / null if session doesn't exist
 ********************************************************/

org_myurc_webclient_getSession = function(socketName, targetId) {

   for (var sessionId in org_myurc_webclient_sessions) {
      var session = org_myurc_webclient_sessions[sessionId];
      if (session &&      // Session active?
         session.socketName == socketName &&
         (!targetId || session.targetId == targetId))
         return session;
   }
	
	// Session object not found.
   return null;      
};



/********************************************************
org_myurc_webclient_openSession: Open a session with a socket, or return existing session.
	socketName: Name (URI) of the socket.
	targetId: Target identifier (to disambiguate multiple sockets with the same name) - may be omitted or null/undefined.
	authorizationCode: string for authorization / null if not available.
	return: session object
 ********************************************************/
org_myurc_webclient_openSession = function(socketName, targetId, authorizationCode) {

   var session = org_myurc_webclient_getSession(socketName, targetId);
   if (session)            // Session already open?
      return session;      // Return open session
      
	var sockets = org_myurc_urchttp_getAvailableSockets(); 
	   // Items in sockets have the following properties: protocolUrl, targetId, socketName, socketDescriptionAt, targetDescriptionAt.
    
	for (var i=0; i<sockets.length; i++) {
	   var socket = sockets[i];
		
	   if (socket.socketName == socketName &&
	         (!targetId || socket.targetId == targetId)) {     // Matching socket found?
	      // Now open the session, reuse the socket object.
	      var session = socket;
		  session.socket = new org_myurc_socket_Socket(socketName, session.socketDescriptionAt, session.targetDescriptionAt);
	      session.sessionId = org_myurc_urchttp_openSession(session.protocolUrl, authorizationCode, null);        
	         // No authorization code, no resources requested with values.
	      org_myurc_webclient_sessions[session.sessionId] = session;      // Store session object in org_myurc_webclient_sessions.
      	 
	      return session;
	   }
	}

   // No matching socket found.
   throw new Error("No match found in UIList for socket=" + socketName + ", targetId=" + targetId); 
};


/********************************************************
org_myurc_webclient_closeSession: Close a session with the UCH.
   socketName: name (URI) of socket of the session to be closed.
   targetId: target identifier of the session to be closed.  May be null if socketName sufficient for identification of session.
   session: session object (if given, socketName and targetId are ignored)
	return: -
   Note: There are 3 options for identifying the session:
      (1) By socketName only (targetId and session are null or undefined).  Okay if there are no two target instances of the same type.
      (2) By socketName and targetId (session is null or undefined).  
      (3) By session (socketName and targetId are null or undefined).
 ********************************************************/
org_myurc_webclient_closeSession = function(socketName, targetId, session) {

   if (!session)     // session object not specified?
      session = org_myurc_webclient_getSession(socketName, targetId);
	if (session && session.sessionId)
		org_myurc_urchttp_closeSession(session.protocolUrl, session.sessionId);
	org_myurc_webclient_sessions[session.sessionId] = null;
	   // Set to null because cannot remove from map.
};


/********************************************************
org_myurc_webclient_setValue: Signal that the user has changed a value on the user interface.
 	socketPath: path (in the form of socketName#path or path) of value changed.
   value: new value
   targetId: target identifier of the session to be closed.  May be null if socketName sufficient for identification of session.
   session: session object (if given, socketName and targetId are ignored)
	return: -
   Note: There are 3 options for identifying the session:
      (1) By socketName only (targetId and session are null or undefined).  Okay if there are no two target instances of the same type.
      (2) By socketName and targetId (session is null or undefined).  
      (3) By session (socketName and targetId are null or undefined).
 ********************************************************/
org_myurc_webclient_setValue = function(socketPath, value, targetId, session) {

   if (!session) {    // session object not specified?
      var socketName = org_myurc_webclient_getSocketName(socketPath);
      session = org_myurc_webclient_getSession(socketName, targetId);
   }
   if (!session)
      throw new Error("No open session found for socketPath " + socketPath);
   var path = org_myurc_webclient_getPath(socketPath);

   var pathValues = org_myurc_urchttp_setValue(session.protocolUrl, session.sessionId, path, value);
   if (pathValues === null || pathValues.length === 0)    // Change request seems to be rejected by server
      pathValues = org_myurc_urchttp_getValues(session.protocolUrl, session.sessionId, path);    
         // Ask for the current value now since old is lost.
   
   // Now display new value.
	org_myurc_webclient_postPathValues(session, pathValues);
   
};

/********************************************************
org_myurc_webclient_setValues: Signal that the user has changed a value on the user interface.
	socketName : name (URI) of the socket
 	elements : {elementPath, operation, value} 
		Map for elements
	   Each item (object) in elements has the following properties:
   	   * elementPath: Path of the socket element.
   	   * operation: Operation to be perform on socket element like ['S' - setValue, 'I' - InvokeCommand, 'K' - Acknoledgment].
   	   * value: New value to be set for the specified socket element.
   targetId: target identifier of the session.  May be null if socketName sufficient for identification of session.
   session: session object (if given, socketName and targetId are ignored)
   return: Current Values
   Note: There are 3 options for identifying the session:
      (1) By socketName only (targetId and session are null or undefined).  Okay if there are no two target instances of the same type.
      (2) By socketName and targetId (session is null or undefined).  
      (3) By session (socketName and targetId are null or undefined).
 ********************************************************/
org_myurc_webclient_setValues = function(socketName, elements, targetId, session) {
	
	if ( !elements || (elements.length <= 0) ) 
		return null;
		
   if (!session)     // session object not specified?
      session = org_myurc_webclient_getSession(socketName, targetId);
	if (!session)
       throw new Error("No open session found for socket name " + socketName);
	
	return org_myurc_urchttp_setValues(session.protocolUrl, session.sessionId, elements);
};


/********************************************************
org_myurc_webclient_getValue: Get the current value of a socket element.
 	socketPath: path (in the form of socketName#path or path) of value requested.
   targetId: target identifier of the session to be closed.  May be null if socketName sufficient for identification of session.
   session: session object (if given, socketName and targetId are ignored)
	return: Current value (as string)
   Note: There are 3 options for identifying the session:
      (1) By socketName only (targetId and session are null or undefined).  Okay if there are no two target instances of the same type.
      (2) By socketName and targetId (session is null or undefined).  
      (3) By session (socketName and targetId are null or undefined).
 ********************************************************/
org_myurc_webclient_getValue = function(socketPath, targetId, session) {

   if (!session) {    // session object not specified?
      var socketName = org_myurc_webclient_getSocketName(socketPath);
      session = org_myurc_webclient_getSession(socketName, targetId);
   }
   if (!session)
      throw new Error("No open session found for socketPath " + socketPath);
   var path = org_myurc_webclient_getPath(socketPath);

   var pathValues = org_myurc_urchttp_getValues(session.protocolUrl, session.sessionId, path);
   if (pathValues && pathValues[0])
      return pathValues[0].value;
   else
      throw new Error("UCH returned no value for " + socketPath);
};


/********************************************************
org_myurc_webclient_getValues: Get the current value of a socket elements.
 	socketName: Name of the socket of value requested.
	elements : List of socket element path 
		[in the form of elementId#xmlPath(nodeId) 
	    	where xmlPath : xmlPath for partial XML content which could be root, parentOf, childrenOf or id.
				  nodeId  : id of the node.
		]
   targetId: target identifier of the session.  May be null if socketName sufficient for identification of session.
   session: session object (if given, socketName and targetId are ignored)
	return: Current value (as string)
   Note: There are 3 options for identifying the session:
      (1) By socketName only (targetId and session are null or undefined).  Okay if there are no two target instances of the same type.
      (2) By socketName and targetId (session is null or undefined).  
      (3) By session (socketName and targetId are null or undefined).
 ********************************************************/
org_myurc_webclient_getValues = function ( socketName, elements, targetId, session ){
	
	if ( !elements || (elements.length <= 0) ) 
		return null;

   if (!session)     // session object not specified?
      session = org_myurc_webclient_getSession(socketName, targetId);
	if (!session)
       throw new Error("No open session found for socket name " + socketName);
	
	return org_myurc_urchttp_getValues( session.protocolUrl, session.sessionId, elements);
	
	
};


/********************************************************
org_myurc_webclient_getIndex: Get the available index values for a dimensional socket set or element.
 	socketName: Name of the socket 
 	elementPath: element path of dimensional socket set or element (not the whole path)
   targetId: target identifier of the session.  May be null if socketName sufficient for identification of session.
   session: session object (if given, socketName and targetId are ignored)
	return: Current index values (as array)
   Note: There are 3 options for identifying the session:
      (1) By socketName only (targetId and session are null or undefined).  Okay if there are no two target instances of the same type.
      (2) By socketName and targetId (session is null or undefined).  
      (3) By session (socketName and targetId are null or undefined).
 ********************************************************/
//Parikshit Thakur : 20110929. changed method for new spec, removed index no and changed elementId to elementPath
org_myurc_webclient_getIndex = function(socketName, elementPath, targetId, session) {
   
   if (!session)     // session object not specified?
      session = org_myurc_webclient_getSession(socketName, targetId);
	if (!session)
       throw new Error("No open session found for socket name " + socketName);

   return org_myurc_urchttp_getIndex(session.protocolUrl, session.sessionId, elementPath);
};


/********************************************************
org_myurc_webclient_invoke: Signal that the user has requested to invoke a command.
 	socketPath: path (in the form of socketName#path or path) for command element to invoke.
 	params: 
 	   Array of parameter-value objects, each with the following properties:
 	      "id": elementId of parameter
 	      "value": Value for the parameter
 	   null okay for void commands and in auto-generate mode.
 	   Note: In auto-generation mode the parameters are read from the HTML user interface instead.
   targetId: target identifier of the session to be closed.  May be null if socketName sufficient for identification of session.
   session: session object (if given, socketName and targetId are ignored)
	return: -
   Note: There are 3 options for identifying the session:
      (1) By socketName only (targetId and session are null or undefined).  Okay if there are no two target instances of the same type.
      (2) By socketName and targetId (session is null or undefined).  
      (3) By session (socketName and targetId are null or undefined).
 ********************************************************/
org_myurc_webclient_invoke = function(socketPath, params, targetId, session) {

   if (!session) {    // session object not specified?
      var socketName = org_myurc_webclient_getSocketName(socketPath);
      session = org_myurc_webclient_getSession(socketName, targetId);
   }
   if (!session)
      throw new Error("No open session found for socketPath " + socketPath);
   var path = org_myurc_webclient_getPath(socketPath);
   var cmdId = org_myurc_webclient_getId(session, socketPath);
   var paramPathValues = [];
	
   if (org_myurc_webclient_autogenerate) {
      var localParams = session.socket.getLocalParameterIds(cmdId);
      for (var i=0; i < localParams["in"].length; i++) {      // Have to write as localParams["in"], not localParams.in, since in is a keyword
         var paramPath = path + "/" + localParams["in"][i];
         var paramValue = org_myurc_webclient_readValue(socketName + '#' + paramPath);
         if (paramValue === null || paramValue === undefined)
            throw new Error("Command parameter missing in HTML template: " + paramPath);
         var pathValue = { "path": paramPath, "value": paramValue };
         paramPathValues.push(pathValue);
      }
      for (var i=0; i < localParams.inout.length; i++) {      // Have to write as localParams["in"], not localParams.in, since in is a keyword
         var paramPath = path + "/" + localParams.inout[i];
         var paramValue = org_myurc_webclient_readValue(socketName + '#' + paramPath);
         if (paramValue === null || paramValue === undefined)
            throw new Error("Command parameter missing in HTML template: " + paramPath);
         var pathValue = { "path": paramPath, "value": paramValue };
         paramPathValues.push(pathValue);
      }
	  
   }
   else if (params) {      // HTML template mode: parameters provided with function call.
      for (var i = 0; i < params.length; i++) {
         var param = params[i];
         paramPathValues.push( { "path": path + "/" + param.id, "value": param.value } );
      }
   }
   var pathValues = org_myurc_urchttp_invokeCommand(session.protocolUrl, session.sessionId, path, paramPathValues);
  	
  	// Change in command state included in pathValues, or will be sent later via updates.
   org_myurc_webclient_postPathValues(session, pathValues);
	
};

/********************************************************
org_myurc_webclient_acknowledge: Signal that the controller has received a notification from the server, and - if required -
      the user has acknowledged the notification.
 	socketPath: path (in the form of socketName#path) of notify element.
   targetId: target identifier of the session to be closed.  May be null if socketName sufficient for identification of session.
   session: session object (if given, socketName and targetId are ignored)
	return: -
   Note: There are 3 options for identifying the session:
      (1) By socketName only (targetId and session are null or undefined).  Okay if there are no two target instances of the same type.
      (2) By socketName and targetId (session is null or undefined).  
      (3) By session (socketName and targetId are null or undefined).
 ********************************************************/
org_myurc_webclient_acknowledge = function(socketPath, value, targetId, session) { // changed signature to pass value of notification. Parikshit Thakur : 20110730

   if (!session) {    // session object not specified?
      var socketName = org_myurc_webclient_getSocketName(socketPath);
      session = org_myurc_webclient_getSession(socketName, targetId);
   }
   if (!session)
      throw new Error("No open session found for socketPath " + socketPath);
   var path = org_myurc_webclient_getPath(socketPath);

   var pathValues = org_myurc_urchttp_acknowledgeNotification(session.protocolUrl, session.sessionId, path, value);
   
   if (pathValues === null || pathValues.length === 0)    // Change request seems to be rejected by server
      pathValues = org_myurc_urchttp_getValues(session.protocolUrl, session.sessionId, path);    // Ask for the real value now
	org_myurc_webclient_postPathValues(session, pathValues);	
};


/********************************************************
org_myurc_webclient_postUpdates: Request and post server updates for all open sessions.
	return: -
	Note: Cannot use "this" since this function will be called out of context.
 ********************************************************/
org_myurc_webclient_postUpdates = function() {

	for (var sessionId in org_myurc_webclient_sessions) {
	   org_myurc_webclient_postUpdatesForSession(org_myurc_webclient_sessions[sessionId], null);
	      // root path assumed.
	}
};

/********************************************************
org_myurc_webclient_postUpdatesForSession: Request and post server updates for a specific session.
	session: session object
	paths: Array of paths (without socket name).  null if root path assumed.
	return: -
 ********************************************************/
org_myurc_webclient_postUpdatesForSession = function(session, paths) {

   var pathValues = org_myurc_urchttp_getUpdates(session.protocolUrl, session.sessionId, paths);  
   org_myurc_webclient_postPathValues(session, pathValues);
};

/********************************************************
org_myurc_webclient_getUpdates: Request server updates for a specific session.
	socketName: Name of the socket
	paths: Array of paths (no socket names included, only path)
   targetId: target identifier of the session to be closed.  May be null if socketName sufficient for identification of session.
   session: session object (if given, socketName and targetId are ignored)
	return: Array of pathValue
   Note: There are 3 options for identifying the session:
      (1) By socketName only (targetId and session are null or undefined).  Okay if there are no two target instances of the same type.
      (2) By socketName and targetId (session is null or undefined).  
      (3) By session (socketName and targetId are null or undefined).
	Note: Cannot use "this" since this function will be called out of context.
 ********************************************************/
org_myurc_webclient_getUpdates = function(socketName, paths, targetId, session) {

   if (!session)     // session object not specified?
      session = org_myurc_webclient_getSession(socketName, targetId);
	if (!session)
       throw new Error("No open session found for socket name " + socketName);

   return org_myurc_urchttp_getUpdates(session.protocolUrl, session.sessionId, paths ? paths : ["/"]);  
      // Default is root path
};


/* ------------------- Internal functions below --------------------------- */


/********************************************************
org_myurc_webclient_processNodeAndChildren: Recursively process the given node and its children:
   * Change id value to be a full socketPath, if needed
   * Add event handlers
   * Open session for new socket name if encountered
   node: HTML node to process
   defaultSocketName: inherited socket name
	return: -
 ********************************************************/
org_myurc_webclient_processNodeAndChildren = function(node, defaultSocketName) {
   if (node.nodeType !== 1)   // No element node?
      return;
   
   var socketNameOnNode = null;
   
   if (node.id && node.id.charAt(node.id.length-1) != "_") {    // Node has id, no trailing "_"?
      
      // Complete socket path if necessary.
      var socketNameOnNode = org_myurc_webclient_getSocketName(node.id);
      if (socketNameOnNode === null) {
         if (defaultSocketName !== null)
            node.id = defaultSocketName + "#" + node.id;
         else
            throw new Error("Don't know what to do with &lt;" + node.tagName + " id=" + node.id + "&gt;: no socket name specified and none to inherit.");
      }
      
      // Get session - open a new one if not existing.
      var socketName = socketNameOnNode ? socketNameOnNode : defaultSocketName;
      var session = org_myurc_webclient_getSession(socketName, null);
      if (!session)
         session = org_myurc_webclient_openSession(socketName, null, null);
         // targetId and authorization code not known from HTML code.
         
      // Add label or event handler if appropriate.
      var trailer = (node.id.length > 4) ? node.id.slice(-4) : null;
      if (trailer == ".lab") 
         node.innerHTML = org_myurc_webclient_getLabelForElement(session, org_myurc_webclient_getId(session, node.id.slice(0, -4)));
      else if (trailer == ".val" || trailer == ".inv")
         org_myurc_webclient_addEventHandler(node, session.socket.getEltType(org_myurc_webclient_getId(session, node.id.slice(0, -4))));
   }
   
   // Process the node's children.
   for (var i = 0; i < node.childNodes.length; i++)
      org_myurc_webclient_processNodeAndChildren(node.childNodes[i], socketNameOnNode ? socketNameOnNode : defaultSocketName);
};

/*******************************************************
org_myurc_webclient_readValue: Get the current value for a specified path.  
 	socketPath: path for a variable or parameter.
	return: value / undefined if no value node existing for path.
 ********************************************************/
org_myurc_webclient_readValue = function(socketPath) {

   var node = document.getElementById(socketPath + ".val");
   if (!node)              // Value node not existing?
      return undefined;
   
	switch (node.tagName) {
		case "SELECT":
		   return node.value;

		case "INPUT":
	      switch (node.type) {
	         case "checkbox":
	         case "CHECKBOX":
	            return node.checked;
	         default:
      			return node.value;
			}
			
		case "BUTTON":
		   throw new Error("Internal error: readValue called for BUTTON, socketPath = " + socketPath);

		default:
		   return org_myurc_lib_getTextContent(node);
	}
};

/*******************************************************
org_myurc_webclient_postPathValues: Display values and resources.
   session: session object.
 	pathValues: Path value objects to be displayed.  Either "add", "remove" or "value" operation.
 	Note: The first resource is treated as label for the element.  The second as label for the value.
	return: -
 ********************************************************/
org_myurc_webclient_postPathValues = function(session, pathValues) {
   for (var i = 0; i < pathValues.length; i++) {
      var pathValue = pathValues[i];

      switch (pathValue.operation) {
      
         case "add":       
         case "value":
            org_myurc_webclient_processValue(session, session.socketName + '#' + pathValue.path, pathValue.value); 
            break;
   		
         case "remove":
		 	if( org_myurc_webclient_autogenerate ){
            	org_myurc_webclient_removeNode(session, session.socketName + '#' + pathValue.path);
			}
            break;  
         
         default:
            throw new Error("Invalid operation received from server: " + pathValue.operation);
      }
   }
};


/*******************************************************
org_myurc_webclient_processValue: Process socketPath and value for rendering.
   session: session object.
 	socketPath: path (in the form socketName#path) for value.
	value: value to be displayed.
	return: -
 ********************************************************/
org_myurc_webclient_processValue = function(session, socketPath, value) {

   // First special treatment for notifications.
   // Note: Notifications should always be rendered, independent of org_myurc_webclient_autogenerate.
   var eltType = session.socket.getEltType(org_myurc_webclient_getId(session, socketPath));

   
   if (eltType == "notify") {
      // TODO: Distinguish between explicit user acknowledgment and not.
      
      var notifyNode = document.getElementById(socketPath);
      if (org_myurc_webclient_autogenerate && !notifyNode) {
         var containerNode = document.getElementById("notify_");
         if (!containerNode) {      // No element with id="notify_" in the HTML template.
            containerNode = document.createElement("div");     
            containerNode.id = "notify_";
            document.body.appendChild(containerNode);    // Add as last element in the HTML document.
         }
         if(socketPath.indexOf('[state]') < 0){ //Parikshit Thakur : 20111015. SocketPath in updates contains [state] but not in getValues. Added for homogeneity.
        	 socketPath += '[state]';
         }
         var id = org_myurc_webclient_getId(session, socketPath);
         var type = session.socket.getType(id);
         var message = org_myurc_webclient_getLabelForElement(session, id);
         notifyNode = org_myurc_webclient_createNotification(session, containerNode, socketPath, type, message);
      }
      
      if (notifyNode) {    // Node with notify id existing
         notifyNode.className = (value == "active" || value == "Active" || value == "ACTIVE") ? 
            "notifyactive" :    // Make notification node visible.
            "notifyinactive";   // Make notification node invisible.
    	 
         //Parikshit Thakur : 20111014. Changes to display newly added elements(variables and commands) in custom notification.
         if((type == "custom" || type == "customCancel") && (value == "active" || value == "Active" || value == "ACTIVE"))
         {
        	document.getElementById(socketPath.substring(0, socketPath.indexOf('[state]')) + '.tab').style.display = "table";
        	notifyNode.appendChild( document.getElementById(socketPath.substring(0, socketPath.indexOf('[state]')) + '.tab'));
            document.getElementById(socketPath.substring(0, socketPath.indexOf('[state]')) + '.tab').setAttribute("align", "center"); 
            document.getElementById(socketPath.substring(0, socketPath.indexOf('[state]')) + '.tab').setAttribute("bgcolor", "green");
     
            if(type == "custom"){
            	var OKButton = document.createElement("div");
            	var elementTable = document.getElementById(socketPath.substring(0, socketPath.indexOf('[state]')) + '.tab');
            	OKButton.setAttribute("id", "OKCustomButton");
            	OKButton.innerHTML = " <p><button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "' , 'OK')\"" +
            	" onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "' , 'OK')\">OK</button></p>";
            	notifyNode.insertBefore(OKButton, elementTable.nextSibling);
            }
            if(type == "customCancel"){
            	var cancelButton = document.createElement("div");
            	var elementTable = document.getElementById(socketPath.substring(0, socketPath.indexOf('[state]')) + '.tab');
            	cancelButton.setAttribute("id", "cancelCustomButton");
            	cancelButton.innerHTML = " <p><button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "')\"" +
            			" onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "')\">OK</button>" +
            			" <button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Cancel')\"" +
            			" onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Cancel')\">Cancel</button></p>";
            	notifyNode.insertBefore(cancelButton, elementTable.nextSibling);
            }
        } 
        else if((type == "custom" || type == "customCancel") && value == "inactive") {
        	document.getElementById(socketPath.substring(0, socketPath.indexOf('[state]')) + '.tab').style.display = "none";
        }
       //Parikshit Thakur : 20111014. Change ends.
                
      return;
      }
   }
   
   // Special treatment for command state.
   if (eltType == "command") {
	   
	 //Parikshit Thakur : 20111013. If command is in a notification, no need to display in main div.
	   var parentWithIndices = org_myurc_webclient_getParent(session, socketPath);
	   var parent = org_myurc_webclient_stripIndices(session, parentWithIndices);
	   var parentId = org_myurc_webclient_getId(session, parent); 
	   var parentElt = session.socket.getEltType(parentId);
	   if(parentElt == "notify")
		   return;
	   //change ends.
   
      if (socketPath.length > 7 && socketPath.slice(-7) == "[state]") {
         var cmdPath = socketPath.slice(0, -7);
         var valueNode = document.getElementById(cmdPath + ".val") ||   // Try pattern for simple command first...
            document.getElementById(cmdPath + "/invoke.val");   // then complex command pattern...         
         if (!valueNode && org_myurc_webclient_autogenerate) {   // Try harder...
            var node = org_myurc_webclient_getNode(session, cmdPath);      // Will create command table if not existing yet.
            org_myurc_webclient_createCommandStructure(session, node, cmdPath);      
               // Creates right side of command table, for simple or complex command.
            valueNode = document.getElementById(cmdPath + ".val") ||   // Try pattern for simple command first...
               document.getElementById(cmdPath + "/invoke.val");      // Then pattern for complex command...  
            if (!valueNode)
               throw new Error("Internal error: cmd table has no element with id=" + cmdPath + "/invoke.val");
         }
         if (!value)             // value an empty string - as for voidCommand commands.
            value = "invoke";    // Take a default value to make the button bigger.
         if (valueNode)
            org_myurc_webclient_postValue(valueNode, value, session);
         else if (window.onpostvalue)      // Global onpostvalue event handler defined? - Use window object to not cause an exception if undefined.
            window.onpostvalue(socketPath, value, session);
         return;
      }
      
      // Special treatment for command ttc (time-to-complete).
      else if (socketPath.length > 5 && socketPath.slice(-5) == "[ttc]") {
         var cmdPath = socketPath.slice(0, -5);
         var valueNode = document.getElementById(cmdPath + ".val") ||   // Try pattern for simple command first...
            document.getElementById(cmdPath + "/invoke.val");   // then complex command pattern...         
         if (!valueNode && org_myurc_webclient_autogenerate) {   // Try harder...
            var node = org_myurc_webclient_getNode(session, cmdPath);      // Will create command table if not existing yet.
            org_myurc_webclient_createCommandStructure(session, node, cmdPath);      
               // Creates right side of command table, for simple or complex command.
            valueNode = document.getElementById(cmdPath + ".val") ||   // Try pattern for simple command first...
               document.getElementById(cmdPath + "/invoke.val");      // Then pattern for complex command...  
            if (!valueNode)
               throw new Error("Internal error: cmd table has no element with id=" + cmdPath + "/invoke.val");
         }
         if (valueNode) {
            var oldValue = org_myurc_lib_getTextContent(valueNode);
            var lastBracket = oldValue.lastIndexOf("[");
            var newValue = (lastBracket == -1) ?      // No ttc found?
               oldValue + " [" + value + "]" :        // append ttc
               oldValue.slice(0, lastBracket) + " [" + value + "]";   // replace ttc
            org_myurc_webclient_postValue(valueNode, newValue, session);
         }
         else if (window.onpostvalue)      // Global onpostvalue event handler defined? - Use window object to not cause an exception if undefined.
            org_myurc_webclient_onpostvalue(socketPath, value, session);
         return;
      }
      
   else
      throw new Error("Missing [state] or [ttc] trailer for path from UCH: " + socketPath);
   }
   
   else if (eltType == "set" && !value)     // Path to set and empty value?
      return;     // Nothing to do - was an <add> for set.

   else {
	   //Parikshit Thakur : 20111013. If variable is in a notification, no need to display it in main div.
	   var parentWithIndices = org_myurc_webclient_getParent(session, socketPath);
	   var parent = org_myurc_webclient_stripIndices(session, parentWithIndices);
	   var parentId = org_myurc_webclient_getId(session, parent); 
	   var parentElt = session.socket.getEltType(parentId);
	   if(parentElt == "notify")
		   return;
	   //change ends.
	   
      var valueNode = document.getElementById(socketPath + ".val");
      if (!valueNode && org_myurc_webclient_autogenerate) {
         var node = org_myurc_webclient_getNode(session, socketPath);
         // Note: In some cases (parameters) this may already create the value node.
         valueNode = document.getElementById(socketPath + ".val") ||
            org_myurc_webclient_createValueNode(session, node, session.socket.getEltType(org_myurc_webclient_getId(session, socketPath)));  
      }
   }
      
   if (valueNode)
      org_myurc_webclient_postValue(valueNode, value, session);
   else if (window.onpostvalue)      // Global onpostvalue event handler defined? - Use window object to not cause an exception if undefined.
      onpostvalue(socketPath, value, session);
   // Ignore non-existent nodes.
      
   return;
};
	


/**
 * org_myurc_webclient_processNotificationElements : process elements of notification.
 *  session: session object.
 *	socketPath: path (in the form socketName#path) for value.
 *	value: value to be displayed.
 *	return: -
 */
org_myurc_webclient_processNotificationElements = function(session, socketPath, value) {
	   
	   var eltType = session.socket.getEltType(org_myurc_webclient_getId(session, socketPath));

	   if (eltType == "command") {
		 
	      if (socketPath.length > 7 && socketPath.slice(-7) == "[state]") {
	         var cmdPath = socketPath.slice(0, -7);
	         var valueNode = document.getElementById(cmdPath + ".val") ||   
	            document.getElementById(cmdPath + "/invoke.val");          
	         if (!valueNode && org_myurc_webclient_autogenerate) {  
	            var node = org_myurc_webclient_getNode(session, cmdPath);     
	            org_myurc_webclient_createCommandStructure(session, node, cmdPath);      
	               // Creates right side of command table, for simple or complex command.
	            valueNode = document.getElementById(cmdPath + ".val") ||  
	               document.getElementById(cmdPath + "/invoke.val");       
	            if (!valueNode)
	               throw new Error("Internal error: cmd table has no element with id=" + cmdPath + "/invoke.val");
	         }
	         if (!value)             
	            value = "invoke";   
	         if (valueNode)
	            org_myurc_webclient_postValue(valueNode, value, session);
	         else if (window.onpostvalue)     
	            window.onpostvalue(socketPath, value, session);
	         return;
	      }
	      
	      // Special treatment for command ttc (time-to-complete).
	      else if (socketPath.length > 5 && socketPath.slice(-5) == "[ttc]") {
	         var cmdPath = socketPath.slice(0, -5);
	         var valueNode = document.getElementById(cmdPath + ".val") ||   
	            document.getElementById(cmdPath + "/invoke.val");           
	         if (!valueNode && org_myurc_webclient_autogenerate) {   
	            var node = org_myurc_webclient_getNode(session, cmdPath);      // Will create command table if not existing yet.
	            org_myurc_webclient_createCommandStructure(session, node, cmdPath);      
	               // Creates right side of command table, for simple or complex command.
	            valueNode = document.getElementById(cmdPath + ".val") ||   // Try pattern for simple command first...
	               document.getElementById(cmdPath + "/invoke.val");      // Then pattern for complex command...  
	            if (!valueNode)
	               throw new Error("Internal error: cmd table has no element with id=" + cmdPath + "/invoke.val");
	         }
	         if (valueNode) {
	            var oldValue = org_myurc_lib_getTextContent(valueNode);
	            var lastBracket = oldValue.lastIndexOf("[");
	            var newValue = (lastBracket == -1) ?      // No ttc found?
	               oldValue + " [" + value + "]" :        // append ttc
	               oldValue.slice(0, lastBracket) + " [" + value + "]";   // replace ttc
	            org_myurc_webclient_postValue(valueNode, newValue, session);
	         }
	         else if (window.onpostvalue)      // Global onpostvalue event handler defined? - Use window object to not cause an exception if undefined.
	            org_myurc_webclient_onpostvalue(socketPath, value, session);
	         return;
	      }
	      
	   else
	      throw new Error("Missing [state] or [ttc] trailer for path from UCH: " + socketPath);
	   }
	   	  
	   else {
		  		   
	      var valueNode = document.getElementById(socketPath + ".val");
	      if (!valueNode) {
	         var node = org_myurc_webclient_getNode(session, socketPath);
	         // Note: In some cases (parameters) this may already create the value node.
	         valueNode = document.getElementById(socketPath + ".val") ||
	            org_myurc_webclient_createValueNode(session, node, session.socket.getEltType(org_myurc_webclient_getId(session, socketPath)));  
	      }
	   }
	      
	   if (valueNode)
	      org_myurc_webclient_postValue(valueNode, value, session);
	   else if (window.onpostvalue)      // Global onpostvalue event handler defined? - Use window object to not cause an exception if undefined.
	      onpostvalue(socketPath, value, session);
	   // Ignore non-existent nodes.
	      
	   return;
	};







/*******************************************************
org_myurc_webclient_postValue: Render a value on a given node.
 	thisNode: node to render the value
	thisValue: value to be rendered
	session: pertaining session object
	return: -
 ********************************************************/
org_myurc_webclient_postValue = function(thisNode, thisValue, session) {
   // Don't change name of parameters thisNode and thisValue.
   var onpostvalueOnNode = thisNode.getAttribute("onpostvalue");
   if (onpostvalueOnNode) {      // Node has a special postValue handler?
      eval(onpostvalueOnNode);       // May reference: node via "thisNode"; value via "thisValue", session object via "session".
      return;
   }
   
   switch (thisNode.tagName) {
	   case "SELECT":
		   for (var i=0; i<thisNode.options.length; i++) {
			   var optionNode = thisNode.options[i];
			   if (optionNode.getAttribute("value") == thisValue) {
				   thisNode.selectedIndex = optionNode.index;
				   break;      // No need to look further.
			   }
		   }
		   break;
			
	   case "INPUT":
	      if (thisNode.type == "checkbox" || thisNode.type == "CHECKBOX")
	         thisNode.checked = (thisValue == "true" || thisValue == "True" || thisValue == "TRUE");      // Value comes in as string.
         else
		      thisNode.value = thisValue;
		   break;
			
	   default:
	      org_myurc_lib_setTextContent(thisNode, thisValue);
	      break;
   }
};

/*******************************************************
org_myurc_webclient_postLabel: Replace the currently displayed label of a node.
   session: session object.
 	socketPath: path in the form of socketName#path for value.
	label: label to be displayed.
	return: -
 ********************************************************/
org_myurc_webclient_postLabel = function(session, socketPath, label) {
   var node = document.getElementById(socketPath + ".lab");

   if (org_myurc_webclient_autogenerate) {      // Try again.
      if (!node)
         org_myurc_webclient_getNode(session, socketPath);     // This will create the label node.
      node = document.getElementById(socketPath + ".lab");
   }
   
   if (node)
      org_myurc_lib_setTextContent(node, label);
   // Do nothing if no label node is available.
};


/*******************************************************
org_myurc_webclient_removeNode: Remove the node for a socketPath.  This is triggered by a removal of indices.
   session: session object
 	socketPath: path in the form of socketName#path to be removed.
	return: -
 ********************************************************/
org_myurc_webclient_removeNode = function(session, socketPath) {

   if (socketPath.slice(-2) == "#/")     // Is it the root path?
      throw new Error("Server tried to remove root path, by &lt;remove ref=\"/\" /&gt;");
      
   var id = org_myurc_webclient_getId(session, socketPath);
   var elt = session.socket.getEltType(id);

   switch (elt) {
   
      case "set":                   // --- set element ---
         var indices = org_myurc_webclient_getIndices(socketPath); 
         if (indices.length > 0) {      // Ignore non-dimensional set.
            var thNode = document.getElementById(socketPath + ".col");
            if (thNode)    // Ignore if node not found.
               org_myurc_webclient_removeTableCol(thNode);     // remove the whole column of thNode
         }
         break;
         
      case "variable":                   // --- variable element ---
         var indices = org_myurc_webclient_getIndices(socketPath); 
         
         // Ignore non-dimensional variable.
         if (indices.length == 1) {
            var thNode = document.getElementById(socketPath + ".col");
            if (thNode)
               org_myurc_webclient_removeTableCol(thNode);     // remove the whole column of thNode.
         }
         else if (indices.length >= 2) {   
            var tdNode = document.getElementById(socketPath);
            if (tdNode)
               tdNode.innerHTML = "";     // Just wipe the cell, but leave the id on it.
         }
         break;

      case "command":                  // --- command element ---
         var indices = org_myurc_webclient_getIndices(socketPath); 
         
         // Ignore non-dimensional command.
         if (indices.length > 0) {
            var thNode = document.getElementById(socketPath + ".col");
            if (thNode)
               org_myurc_webclient_removeTableCol(thNode);     // remove the whole column of thNode.
         }
         break;

      default:
         throw new Error("Internal error in removeTableCol: Unexpected element type " + elt + " for " + socketPath);
   }
};
	

/********************************************************
org_myurc_webclient_createValueNode: Create an HTML node where an actual value is rendered, with id="socketName#path.val"
   session: session object.
   container: HTML container, with id="socketName#path".  No paths allowed corresponding to notify elements.
   eltType: element type of socket element socketName#path.
	return: value node (may be identical to container)
 ********************************************************/
org_myurc_webclient_createValueNode = function(session, container, eltType) {   

   switch (eltType) {
      case "variable":
      case "inpar":  
      case "inoutpar":
         // TODO: If an element has a closed selection AND an enumeration type, take the intersection of both.
         var id = org_myurc_webclient_getId(session, container.id);
         if (session.socket.hasClosedSelection(id))
            return org_myurc_webclient_createComboBox(session, container, eltType, session.socket.getSelectionSets(id));
         else {
            var typeName = session.socket.getType(id);
            if (typeName == "xsd:boolean")
               return org_myurc_webclient_createCheckBox(session, container, eltType);
            else if (typeName !== null) {
               var enums = session.socket.getEnumsForType(typeName);
               if (enums !== null && enums.length > 0)      // Enumerated values available?
                  return org_myurc_webclient_createComboBox(session, container, eltType,
                     [ { "typeRef": typeName, "id": null } ]);
                     // Treat enumeration type as static selection set, id is not used.
            }

            // Otherwise create a simple input box.
            return org_myurc_webclient_createInputBox(session, container, eltType);
         }
         
      case "outpar":
         var div = document.createElement("DIV");
         div.id = container.id + ".val";
         container.appendChild(div);
         // No event handler since output parameter.
         return div;
         
      case "command":
         var button = document.createElement("BUTTON");
         button.id = container.id + ".inv";
         org_myurc_webclient_addEventHandler(button, "command");
         var div = document.createElement("DIV");
         div.id = container.id + ".val";
         div.innerHTML = "no state";
         button.appendChild(div);
         container.appendChild(button);
         return button;   

      default:
         throw new Error("createValueNode cannot deal with eltType " + eltType + ", container id=" + container.id);
   }
};

/*******************************************************
org_myurc_webclient_getNode: Get the document node with the given path.  If not existent, create it.
   session: session object.
 	socketPath: long path (in the form of socketName#path) for the node.  
 	   socketPath shall not correspond to a notify element.
	return: node as part of the current HTML document.
	exception: Throws exception if invalid syntax in path
	
   The following sketch illustrates the various structures of nested HTML tables.
   Abbreviations:
      "rec." means that the entry may be recursive
      "sn" is socket name, i.e. URI of the pertinent socket
   Note: Paths without socket names are allowed as id values, if there is an ancestor of the
      HTML node that has a socket name specified as part of its id.  
      Example (allowed): 
         <table id="http://res.myurc.org/device/socket#/">
            <tbody>
               <tr>
                  ...
                  <td id="/power">
               </tr>
            </tbody>
         </table>
   
   Root pattern:   
      <body id="sn#/">
      <h1 id="sn#/.lab">label for uiSocket</h1>
      <table id="sn#/.tab">
      <tbody>
      -------------------------------------------------------------
      | <th>uiSocket</th>        | <th id="sn#/.col">             |
      |                          | Value</th>                     |
      -------------------------------------------------------------
      | <th id="sn#/elt.lab">    | <td id="sn#/elt">              |
      | label for elt</th>       | value/node for elt (rec.)</td> |
      -------------------------------------------------------------
      </tbody>
      </table>
      </body>
   	
   	
   Pattern for non-dimensional set:
	   <th id="sn#path.lab">label for set</th>
	   <td id="sn#path">
	   <table id="sn#path.tab">
	   <tbody>
      --------------------------------------------------------------
      | <th>Set (0-dim)</th>      | <th id="sn#path.col">          |
      |                           | Value</th>                     |
      --------------------------------------------------------------
      | <th id="sn#path/elt.lab"> | <td id="sn#path/elt">          |
      | label for elt</th>        | value/node for elt (rec.)</td> |
      --------------------------------------------------------------
      </tbody>
      </table>
      </td>


   Pattern for dimensional set:
	   <th id="sn#path.lab">label for set</th>
	   <td id="sn#path">	
	   <table id="sn#path.tab">
	   <tbody>
      ---------------------------------------------------------------
      | <th>Set (n-dim)</th>      | <th id="sn#path[i1]..[in].col"> |
      |                           | labels for indices</th>         | 
      ---------------------------------------------------------------
      | <th id="sn#path/elt.lab"> | <th id="sn#path[i1]..[i2]/elt"> |
      | label for elt</th>        | value/node for elt (rec.)</td>  |
      ---------------------------------------------------------------
      </tbody>
      </table>
      </td>


   Pattern for non-dimensional variable:
	   <th id="sn#path.lab">label for variable (0-dim)</th>
	   <td id="sn#path"><input id="sn#path.val">value for var</input></td>	

   	
   Pattern for 1-dimensional variable:
	   <th id="sn#path.lab">label for variable</th>
	   <td id="sn#path">		
	   <table id="sn#path.tab">
	   <tbody>
      ------------------------------------------------------------
      | <th>Variable (1-dim)</th> | <th id="sn#path[i1].col">    |
      |                           | label for i1</th>            |
      ------------------------------------------------------------
      | <th id="sn#path.row">     | <td id="sn#path[i1]">        |
      | Value</th>                | value/node for var</td>      |
      ------------------------------------------------------------
      </tbody>
      </table>
      </td>

   	
   Pattern for n-dimensional variable:
	   <th id="sn#path.lab">label for variable</th>
	   <td id="sn#path">		
	   <table id="sn#path.tab">
	   <tbody>
      ---------------------------------------------------------------
      | <th>Variable (n-dim)</th> | <th id="sn#path[i2]..[in].col"> |
      |                           | labels for indices i2..in</th>  |
      ---------------------------------------------------------------
      | <th id="sn#path[i1].lab"> | <td id="sn#path[i1][i2]..[in]"> |
      | label for i1</th>         | value/node for var</td>         |
      ---------------------------------------------------------------
      </tbody>
      </table>
      </td>


   Possible options for value/node for var:
	   <td id="sn#path"><input id="sn#path.val">value for var</input></td>	
	   <td id="sn#path"><select id="sn#path.val"><option>...</option></select></td>	


   Pattern for simple command (non-dimensional, no local parameters):
	   <th id="sn#path.lab">label for command</th>
	   <td id="sn#path"><button id="sn#path.inv"><div id="sn#path.val">value for state/ttc</div></button></td>	

   
   Pattern for non-dimensional command:
	   <th id="sn#path.lab">label for command</th>
	   <td id="sn#path">	
	   <table id="sn#path.tab">	
	   <tbody>
      --------------------------------------------------------------------
      | <th>Command (0-dim)</th>     | <th id="sn#path.col">             |
      |                              | Value</th>                        |
      --------------------------------------------------------------------
      | <th id="sn#path/invoke.lab"> | <td id="sn#path/invoke">          |
      | State</th>                   | <button id="sn#path/invoke.inv">  |
      |                              | <div id="sn#path/invoke.val">     |
      |                              | state/ttc</div></button></td>     |
      --------------------------------------------------------------------
      | <th id="sn#path/in.lab">     | <td id="sn#path/in">              |
      | label for input par</th>     | <anytag id="sn#path/in.val">      |
      |                              | value for par in</anytag></td>    |
      --------------------------------------------------------------------
      | <th id="sn#path/inout.lab">  | <td id="sn#path/inout">           |
      | label for inout par</th>     | <anytag id="sn#path/inout.val">   |
      |                              | value for par inout</anytag></td> |
      --------------------------------------------------------------------
      | <th id="sn#path/out.lab">    | <td id="sn#path/out">             |
      | label for output par</th>    | <div id="sn#path/out.val">        |
      |                              | value for par out</div></td>      |
      --------------------------------------------------------------------
	   </tbody>
	   </table>	
      </td>


   Pattern for n-dimensional command:
	   <th id="sn#path.lab">label for command</th>
	   <td id="sn#path">
	   <table id="sn#path.tab">	
	   <tbody>
      -----------------------------------------------------------------------------
      | <th>Command (n-dim)</th>     | <th id="sn#path[i1]..[in].col">            |
      |                              | labels for [i1]..[in]</th>                 |
      -----------------------------------------------------------------------------
      | <th id="sn#path/invoke.lab"> | <td id="sn#path[i1]..[in]/invoke">         |
      | State</th>                   | <button id="sn#path[i1]..[in]/invoke.inv"> |
      |                              | <div id="sn#path[i1]..[in]/invoke.val">    |
      |                              | state/ttc</div></button></td>              |
      -----------------------------------------------------------------------------
      | <th id="sn#path/in.lab">     | <td id="sn#path[i1]..[in]/in">             |
      | label for input par</th>     | <anytag id="sn#path[i1]..[in]/in.val">     |
      |                              | value for input par</anytag></td>          |
      -----------------------------------------------------------------------------
      | <th id="sn#path/inout.lab">  | <td id="sn#path[i1]..[in]/inout">          |
      | label for inout par</th>     | <anytag id="sn#path[i1]..[in]/inout.val">  |
      |                              | value for inout par</anytag></td>          |
      -----------------------------------------------------------------------------
      | <th id="sn#path/out.lab">    | <td id="sn#path[i1]..[in]/out">            |
      | label for output par</th>    | <div id="sn#path[i1]..[in]/out.val">       |
      |                              | value for par out</div></td>               |
      -----------------------------------------------------------------------------
	   </tbody>
	   </table>	
      </td>


      Notifications are shown as modal dialogs (JavaScript alerts).  No HTML markup involved.
	
 ********************************************************/
org_myurc_webclient_getNode = function(session, socketPath) {

   // Note: The special treatment of the root path is somewhat sub-optimal since it retrieves the 3 nodes
   //    for root path, label and table every time the root path is consulted.  However, this should not
   //    pose a significant problem since the root path will not be requested when all nodes have been
   //    created in the user interface.
   
   if (socketPath.slice(-2) == "#/")     // Is it the root path?
      {
      var node = document.getElementById(socketPath);
      if (!node) {
         node = document.body;     // Use <body> element as anchor
         node.id = socketPath;
      }

      var table = document.getElementById(socketPath + ".tab");     
      if (table === null) {   // Table not available in document?
         var table = org_myurc_webclient_createTable(node, socketPath + ".tab", "UI Socket");
         node.insertBefore(table, node.firstChild);
      }

      var label = document.getElementById(socketPath + ".lab");
      if (label === null) {
         label = document.createElement("h1");
         label.id = socketPath + ".lab";
         label.innerHTML = org_myurc_webclient_getLabelForElement(session, session.socket.getRootId());
         node.insertBefore(label, node.firstChild);
      }

      return node;
   }
   
   // Find out about element type.
   var id = org_myurc_webclient_getId(session, socketPath);
   var elt = session.socket.getEltType(id);
            
   switch (elt) {

      case "set":                   // --- set element ---
         // Look if node is already existing.
         var node = document.getElementById(socketPath);
         if (node) {       // Node existing?
            if (!document.getElementById(socketPath + ".tab"))   // <table> subelement not yet existing?
               org_myurc_webclient_createTable(node, socketPath + ".tab", "Set");   // Create <table> as subnode.
            return node;      // Now ready.
         }

         var indices = org_myurc_webclient_getIndices(socketPath); 
         if (indices.length !== 0) 
            throw new Error("Internal error: getNode() called with indexed set: " + socketPath);
         var parentWithIndices = org_myurc_webclient_getParent(session, socketPath);
         var parent = org_myurc_webclient_stripIndices(session, parentWithIndices);
         if (org_myurc_webclient_getId(session, parent) != session.socket.getParentId(id))
            throw new Error("Invalid socketPath - variable " + id + " does not belong to set " + org_myurc_webclient_getId(session, parent) + ": " + socketPath);
         var parentNode = org_myurc_webclient_getNode(session, parent);   // Will create table if not existing
         var parentTable = document.getElementById(parent + ".tab");    // May be null.
         if (parentTable === null) {     
            parentTable = org_myurc_webclient_createTable(parentNode, parent + ".tab", "Set");
         }
         var row = parent + (parent.charAt(parent.length - 1) == "/" ? "" : "/") + id + ".lab";
         var col = parentWithIndices + ".col"; 
         var tableCell = org_myurc_webclient_addTableCell(session, parentTable, row, col, null);     // Assigns id="socketPath" to cell.
         var tableNode = org_myurc_webclient_createTable(tableCell, socketPath + ".tab", "Set");
         return tableCell;


      case "variable":                   // --- variable element ---
         var node = document.getElementById(socketPath);
         if (node)       // Node existing?
            return node;      

         var indices = org_myurc_webclient_getIndices(socketPath); 
         if (indices.length != session.socket.getDimTypes(id).length)
            throw new Error("Invalid socketPath - variable must have " + session.socket.getDimTypes(id).length + " indices: " + socketPath);
         
         if (indices.length === 0) {    // var without dimensions
            var parentWithIndices = org_myurc_webclient_getParent(session, socketPath);
            var parent = org_myurc_webclient_stripIndices(session, parentWithIndices);
            if (org_myurc_webclient_getId(session, parent) != session.socket.getParentId(id))
               throw new Error("Invalid socketPath - variable " + id + " does not belong to set " + org_myurc_webclient_getId(session, parent) + ": " + socketPath);
            org_myurc_webclient_getNode(session, parent);      // Creates table node if not existing.
            var parentTable = document.getElementById(parent + ".tab");   
            var row = parent + (parent.charAt(parent.length - 1) == "/" ? "" : "/") + id + ".lab";
            var col = parentWithIndices + ".col"; 
            node = org_myurc_webclient_addTableCell(session, parentTable, row, col, null);     // Assigns id="socketPath" to cell.
            return node;
         }
         
         else if (indices.length > 0) {   // var is dimensional
            var pathWithoutIndices = org_myurc_webclient_stripIndices(session, socketPath);
            var tableNode = document.getElementById(pathWithoutIndices + ".tab");
            if (tableNode === null) {    // Dim var table not existing yet?
               var parentWithIndices = org_myurc_webclient_getParent(session, socketPath);
               var parent = org_myurc_webclient_stripIndices(session, parentWithIndices);
               var parentIndices = org_myurc_webclient_getIndices(parentWithIndices);
               var parentId = org_myurc_webclient_getId(session, parent);
               if (parentId != session.socket.getParentId(id))
                  throw new Error("Invalid socketPath - variable " + id + " does not belong to set " + org_myurc_webclient_getId(session, parent) + ": " + socketPath);
               if (parentIndices.length != session.socket.getDimTypes(parentId).length)
                  throw new Error("Invalid socketPath - set " + parentId + " must have " + session.socket.getDimTypes(parentId).length + " indices: " + socketPath);
               var parentNode = org_myurc_webclient_getNode(session, parent);  
               var parentTable = document.getElementById(parent + ".tab");
               var parentRow = parent + (parent.charAt(parent.length - 1) == "/" ? "" : "/") + id + ".lab";
               var parentCol = parentWithIndices + ".col"; 
               var parentCell = org_myurc_webclient_addTableCell(session, parentTable, parentRow, parentCol, null);     // Assigns id="socketPath" to cell.
               tableNode = org_myurc_webclient_createTable(parentCell, pathWithoutIndices + ".tab", "Variable");    
            }
            
            var tableCell;
            if (indices.length == 1) {    // var is 1-dimensional
               var row = pathWithoutIndices + ".row";    
                  // Note: Using ".row" to prevent conflict with cell for var label which is pathWithoutIndices + ".lab"
               var col = pathWithoutIndices + indices[0] + ".col";
               var tableCell = org_myurc_webclient_addTableCell(session, tableNode, row, col, "Value");
            }
            else {                        // var is multi-dimensional
               var row = pathWithoutIndices + indices[0] + ".row";
                  // Note: Using ".row" to prevent conflict with cell for var label which is pathWithoutIndices + ".lab"
               indices.shift();     // Remove first index.
               var col = pathWithoutIndices + indices.join("") + ".col";
               var tableCell = org_myurc_webclient_addTableCell(session, tableNode, row, col, null);
            }               

            return tableCell;
         }


      case "inpar":                    // --- command parameter ---
      case "inoutpar":
      case "outpar":
         var parentWithIndices = org_myurc_webclient_getParent(session, socketPath);
         var cmdNode = org_myurc_webclient_getNode(session, parentWithIndices);    // Creates command node if not existing yet.
         org_myurc_webclient_createCommandStructure(session, cmdNode, parentWithIndices);      
            // Creates right side of command table, for simple or complex command - if not existing yet.
         return document.getElementById(socketPath);
         
         
      case "command":                  // --- command element ---
         var node = document.getElementById(socketPath);
         if (node)       // Node existing?
            return node;      

         var indices = org_myurc_webclient_getIndices(socketPath); 
         if (indices.length != session.socket.getDimTypes(id).length)
            throw new Error("Invalid socketPath - command must have " + session.socket.getDimTypes(id).length + " indices: " + socketPath);
         var params = session.socket.getLocalParameterIds(id);

         if (indices.length === 0 &&     // non-dimensional command?
            params["in"].length === 0 && params.inout.length === 0 && params.out.length === 0) {  // no local parameters? 

            // Build simple command structure, with value node.
            var parentWithIndices = org_myurc_webclient_getParent(session, socketPath);
            var parent = org_myurc_webclient_stripIndices(session, parentWithIndices);
            if (org_myurc_webclient_getId(session, parent) != session.socket.getParentId(id))
               throw new Error("Invalid socketPath - command " + id + " does not belong to set " + org_myurc_webclient_getId(session, parent) + ": " + socketPath);
            var parentNode = org_myurc_webclient_getNode(session, parent);   // Will create table if not existing
            var parentTable = document.getElementById(parent + ".tab");
            var row = parent + (parent.charAt(parent.length - 1) == "/" ? "" : "/") + id + ".lab";
            var col = parentWithIndices + ".col"; 
            node = org_myurc_webclient_addTableCell(session, parentTable, row, col, null);     // Assigns id="socketPath" to cell.
            valueNode = org_myurc_webclient_createCommandStructure(session, node, socketPath);     // Creates value node for simple command. 
            return node;
         }

         else {
            // Build table for complex command, including all value nodes
            var pathWithoutIndices = org_myurc_webclient_stripIndices(session, socketPath);
            node = document.getElementById(pathWithoutIndices + ".tab");
            if (node === null) {    // Command table not existing yet?
               var parentWithIndices = org_myurc_webclient_getParent(session, socketPath);
               var parent = org_myurc_webclient_stripIndices(session, parentWithIndices);
               var parentId = org_myurc_webclient_getId(session, parent);
               var parentIndices = org_myurc_webclient_getIndices(parentWithIndices);
               if (parentId != session.socket.getParentId(id))
                  throw new Error("Invalid socketPath - command " + id + " does not belong to set " + org_myurc_webclient_getId(session, parent) + ": " + socketPath);
               if (parentIndices.length != session.socket.getDimTypes(parentId).length)
                  throw new Error("Invalid socketPath - set " + parentId + " must have " + session.socket.getDimTypes(parentId).length + " indices: " + socketPath);
               var parentNode = org_myurc_webclient_getNode(session, parent);  
               var parentTable = document.getElementById(parent + ".tab");
               var parentRow = parent + (parent.charAt(parent.length - 1) == "/" ? "" : "/") + id + ".lab";
               var parentCol = parentWithIndices + ".col"; 
               var parentCell = org_myurc_webclient_addTableCell(session, parentTable, parentRow, parentCol, null);     // Assigns id="socketPath" to cell.
               valueNode = org_myurc_webclient_createCommandStructure(session, parentCell, socketPath);     // Creates table structure for complex command. 
            }
            
            return node;      // Return table node.
         }
	//Parikshit Thakur : 20111017. Previously there were no subelements in a notification.
	// Code added for newly added elements(variable and command). 
      case "notify":
    	      	  
    	  var node = document.getElementById("notify_");
    	  var table = document.getElementById(socketPath + ".tab");     
          if (table === null) {
        	   	  table = org_myurc_webclient_createTable(node, socketPath + ".tab", "Notify Element");
           }
          table.style.display = "none";
          return node;
          
      default:
         throw new Error("Internal error in getNode: Unexpected element type " + elt + " for socketPath " + socketPath);
   }
};

/********************************************************
org_myurc_webclient_getTbody: Get the <tbody> element of a table - create one if not existing.
   table: container table for the <tbody> element.
	return: <tbody> element.
 ********************************************************/
org_myurc_webclient_getTbody = function(table) {
   // Go shortcut - <tbody> should be the first element under <table>.
   if (table.childNodes.length === 0) {      // <TBODY> not existing yet.
      var tbody = document.createElement("TBODY");
      table.appendChild(tbody);
      return tbody;
   }
   else {
      var tbody = table.childNodes[0];
      if (tbody.tagName == "TBODY")
         return tbody;
      else
         throw new Error("&lt;TBODY&gt; element expected as first element in table (id=" + table.id + ")"); 
   }
};



/*******************************************************
org_myurc_webclient_appendRow: Append a row to a table node.  The actual <tr> element
   has to be inserted to the <tbody> element under the table node.
 	table: <table> node.
 	row: <tr> node to be inserted.
	return: -
 ********************************************************/
org_myurc_webclient_appendRow = function(table, row) {
   org_myurc_webclient_getTbody(table).appendChild(row);
};


/********************************************************
org_myurc_webclient_createTable: Create a <table> element with just a header row.
   container: HTML element serving as container node for the table
   id: id for table
   title: string to be displayed in the upper left corner of the table
	return: <table> element
 ********************************************************/
org_myurc_webclient_createTable = function(container, id, title) {
   var table = document.createElement("TABLE");
   table.id = id;
   var tr = document.createElement("TR");
   var th = document.createElement("TH");
   th.innerHTML = title;
   tr.appendChild(th);
   org_myurc_webclient_appendRow(table, tr);
   container.appendChild(table);
   return table;
};


/********************************************************
org_myurc_webclient_addTableRow: Add a row with a <tr> element to a <table> element.
   session: session object.
   table: <table> element.
   id: id for row header (<tr> element)
   label: label for the row header
	return: New row element
 ********************************************************/
org_myurc_webclient_addTableRow = function(session, table, id, label) {
   var th = document.createElement("TH");
   th.id = id;
   th.innerHTML = label;
   var tr = document.createElement("TR");
   tr.appendChild(th);

   // Now add any missing <td> elements for the row (to prevent a misformed table).
   var rows = org_myurc_lib_selectChildren(org_myurc_webclient_getTbody(table), "TR");      
   if (rows.length === 0)
      throw new Error("Missing header row in table " + table.id);
   var firstRowCells = org_myurc_lib_selectChildren(rows[0], "TH");
   for (var i=1; i < firstRowCells.length; i++) {
      var cell = document.createElement("TD");
      cell.id = org_myurc_webclient_getCellId(session, id, firstRowCells[i].id);
      tr.appendChild(cell);
   }

   org_myurc_webclient_appendRow(table, tr);
   return tr;
};

/********************************************************
org_myurc_webclient_removeTableCol: Remove a table col with a specified <th> or <td> node.
   node: <th> or <td> node in the column to be removed.
	return: -
 ********************************************************/
org_myurc_webclient_removeTableCol = function(node) {

   var tr = node.parentNode;
   if (tr.tagName != "TR")
      throw new Error("Internal error in removeTableCol: parent node has tag name " + tr.tagName);
      
   var i;
   for (i=0; i<tr.cells.length && tr.cells[i] != node; i++) ;
   if (i >= tr.cells.length)     // Not found?
      throw new Error("Internal error in removeTableCol: Cannot determine cell index");
   
   // i is the index of the column that should be removed.
   var table = tr.parentNode.parentNode;      // <tr> -> <tbody> -> <table>
   for (var j=0; j<table.rows.length; j++)
      table.rows[j].deleteCell(i);
};


/********************************************************
org_myurc_webclient_addTableCell: Create a table cell for given row and col ids, and add to the table (but only if not existing already). 
   session: session object.
   table: <table> element in which to create the table cell
   rowPath: socketPath for row (id with trailing ".row" or ".lab")
   colPath: socketPath for col (id with trailing ".col")
   rowHeaderLabel: Specific row header label / null if default requested
	return: Requested table cell (either new or old)
   Note: The id for the new table cell is created from the given ids for row and col.
 ********************************************************/
org_myurc_webclient_addTableCell = function(session, table, rowPath, colPath, rowHeaderLabel) {

   var rowWithoutTrailing = rowPath.slice(0, -4);    // Strip off trailing ".row" or ".lab"
   var colWithoutTrailing = colPath.slice(0, -4);    // Strip off trailing ".col"

   if (document.getElementById(rowPath) === null) {    
      // Create a new row with cell ids as determined by the col header ids and rowPath.
      var row = document.createElement("TR");
      var headerForRow = document.createElement("TH");
      headerForRow.id = rowPath;
      var rowIndices = org_myurc_webclient_getIndices(rowWithoutTrailing);  
      var rowIndicesString = rowIndices.join("");
      headerForRow.innerHTML = rowHeaderLabel ? 
         rowHeaderLabel : 
         (rowIndicesString ? rowIndicesString : org_myurc_webclient_getLabelForElement(session, org_myurc_webclient_getId(session, rowWithoutTrailing)));
      row.appendChild(headerForRow);
      var rows = org_myurc_lib_selectChildren(org_myurc_webclient_getTbody(table), "TR");      
      if (rows.length === 0)
         throw new Error("Missing header row in table " + table.id);
      var firstRowCells = org_myurc_lib_selectChildren(rows[0], "TH");
      for (var i=1; i < firstRowCells.length; i++) {
         var cell = document.createElement("TD");
         cell.id = org_myurc_webclient_getCellId(session, rowPath, firstRowCells[i].id);
         row.appendChild(cell);
      }
      org_myurc_webclient_appendRow(table, row);
   }
   
   if (document.getElementById(colPath) === null) {
      // Create a new col with cell ids as determined by the row header ids and colPath.
      var headerForCol = document.createElement("TH");
      headerForCol.id = colPath;
      var indices = org_myurc_webclient_getIndices(colWithoutTrailing);
      headerForCol.innerHTML = (indices.length > 0 ? indices.join("") : "Value");
      var rows = org_myurc_lib_selectChildren(org_myurc_webclient_getTbody(table), "TR");
      rows[0].appendChild(headerForCol);           // Append col header in header row.
      for (var i=1; i < rows.length; i++) {        // For each row, append a cell.
         var cell = document.createElement("TD");
         var headerCols = org_myurc_lib_selectChildren(rows[i], "TH");
         cell.id = org_myurc_webclient_getCellId(session, headerCols[0].id, colPath); 
         rows[i].appendChild(cell);         
      }
   }
   
   cell = document.getElementById(org_myurc_webclient_getCellId(session, rowPath, colPath));
   return cell;
};

/********************************************************
org_myurc_webclient_getCellId: Compute the id value for a table cell.
   session: session object.
   rowPath: socketPath string for the row of the table cell.
   colPath: socketPath string for the column of the table cell.
	return: id for the table cell.
 ********************************************************/
org_myurc_webclient_getCellId = function(session, rowPath, colPath) {
   // Both rowPath and colPath contain a trailing ".lab", ".col", etc.
   var rowWithoutTrailing = rowPath.slice(0, -4);
   var colWithoutTrailing = colPath.slice(0, -4);
   var colWithoutIndices = org_myurc_webclient_stripIndices(session, colWithoutTrailing);
   var colIndices = org_myurc_webclient_getIndices(colWithoutTrailing);
   var commonPrefix = rowWithoutTrailing.slice(0, colWithoutIndices.length);  // Take everything before col"s indices.
   if (colWithoutIndices != commonPrefix)     
      throw new Error("Mismatch of row and col paths: row=" + rowPath + ", col=" + colPath);
   var nextSlashInRow = rowWithoutTrailing.indexOf("/", commonPrefix.length);
      // Indices from row that follow immediately
   var mixInIndices = rowWithoutTrailing.slice(commonPrefix.length, 
      (nextSlashInRow >= 0 ? nextSlashInRow : rowWithoutTrailing.length));
   var rowSubElement = (nextSlashInRow >= 0 ? rowWithoutTrailing.slice(nextSlashInRow) : "");
      // Trailing subelement from row
   var cellId = commonPrefix + mixInIndices + colIndices.join("") + rowSubElement;
   return cellId;
};


/********************************************************
org_myurc_webclient_addEventHandler: Add an appropriate event handler - if not existing already.
   node: html element to add event handler, with id of form "socketName#path.val".
   eltType: element type of pertaining socket element.
	return: -
 ********************************************************/
org_myurc_webclient_addEventHandler = function(node, eltType) {
   
   if (eltType == "inpar" || eltType == "inoutpar" || eltType == "outpar")
      return;     // No event handlers on local parameter value nodes.
   
   switch (node.tagName) { 
      case "INPUT":
      case "SELECT":
         if (eltType != "variable")
            throw new Error("&lt;input&gt; not allowed for id=" + node.id);
            
         if (node.tagName == "INPUT" && (node.type == "checkbox" || node.type == "CHECKBOX")) {
            if (!node.onclick)     // No event handler attached?
               node.onclick = function() { org_myurc_webclient_setValue(this.id.slice(0, -4), this.checked ? "true" : "false"); };
            if (!node.onkeypress)     // No event handler attached?
               node.onkeypress = function() { org_myurc_webclient_setValue(this.id.slice(0, -4), this.checked ? "true" : "false"); };
         }
         else {      // Regular input box
            if (!node.onchange)    // No event handler attached?
               node.onchange = function() { org_myurc_webclient_setValue(this.id.slice(0, -4), this.value); };
         }
         break;
         
      case "BUTTON":
         if (eltType != "command")
            throw new Error("&lt;button&gt; not allowed for id=" + node.id);
         var handler = (node.id.slice(-11) == "/invoke.inv") ?
            function() { org_myurc_webclient_invoke(this.id.slice(0, -11)); } :
            function() { org_myurc_webclient_invoke(this.id.slice(0, -4)); };
         if (!node.onclick)     // No event handler attached?
            node.onclick = handler;
         if (!node.onkeypress)     // No event handler attached?
            node.onkeypress = handler;
         break;
      
      default:
         break;      // Ignore unknown nodes.
//         throw new Error("Cannot add event handler for node &lt;" + node.tagName + "&gt;");
   }
};


/********************************************************
org_myurc_webclient_createInputBox: Create an input box - with id="socketName#path.val".
   session: session object.
   container: container node for the input box, with id="socketName#path".
   eltType: element type of pertaining socket element.
	return: input box node.
 ********************************************************/
org_myurc_webclient_createInputBox = function(session, container, eltType) {

   var input = document.createElement("INPUT");
   input.id = container.id + ".val";
   org_myurc_webclient_addEventHandler(input, eltType);
   container.appendChild(input);
   return input;
};


/********************************************************
org_myurc_webclient_createCheckBox: Create a checkbox with id="socketName#path.val".
   session: session object.
   container: container node for the checkbox, with id="socketName#path".
   eltType: element type of pertaining socket element.
	return: checkbox node.
 ********************************************************/
org_myurc_webclient_createCheckBox = function(session, container, eltType) {

   var input = document.createElement("INPUT");
   input.id = container.id + ".val";
   input.type = "checkbox";
   org_myurc_webclient_addEventHandler(input, eltType);
   container.appendChild(input);
   return input;
};


/********************************************************
org_myurc_webclient_createComboBox: Create a combobox with id="elementId.val".
   session: session object.
   container: container node with id="socketName#path".
   eltType: element type of elementId.
   selectionSets: Array of selection objects, each of either of the following forms:
      static selection - {typeRef: qname of pertinent type, id: id of type} 
      dynamic selection - {varRef: id of pertinent element, id: id of selection set}
	return: combobox (SELECT) node.
 ********************************************************/
org_myurc_webclient_createComboBox = function(session, container, eltType, selectionSets) {

   var select = document.createElement("SELECT");
   select.id = container.id + ".val";
   
   // TODO: Create optgroups from selection set labels.
   for (var i = 0; i < selectionSets.length; i++) {
      if (selectionSets[i].typeRef) {     // Static selection set?
         var typeName = selectionSets[i].typeRef;
         var enums = session.socket.getEnumsForType(typeName);
         if (enums === null || enums.length === 0)
            throw new Error("Could not find any enumeration for type " + typeName);
         for (var j = 0; j < enums.length; j++) {
            var option = document.createElement("OPTION");
            option.value = enums[j];
            org_myurc_lib_setTextContent(option, org_myurc_webclient_getLabelForValue(session, org_myurc_webclient_getId(session, container.id), typeName, enums[j]));
            select.appendChild(option);
         }
      }
      else if (selectionSets[i].varRef) {     // Dynamic selection set?
         var varId = selectionSets[i].varRef;
         var eltTypeName = session.socket.getType(org_myurc_webclient_getId(session, container.id));
         var varRefPathWithoutIndices = session.socket.getPathForId(varId);
         var varRefPath = org_myurc_webclient_shareIndices(org_myurc_webclient_getPath(container.id), varRefPathWithoutIndices);
         var pathValues = org_myurc_urchttp_getValues(session.protocolUrl, session.sessionId, varRefPath);
		 
         if (pathValues.length > 1)
            throw new Error("Internal error: Multiple values received for urchttp.getValues() call.");
         if (pathValues.length === 0 ||   // No value provided?
            (pathValues.length == 1 && !pathValues[0].value)) {   // Value is empty or undefined?
            var option = document.createElement("OPTION");
            option.value = "---";
            org_myurc_lib_setTextContent(option, "(not available)");
            select.appendChild(option);
         }
         else {   // exactly one value - good
            var valueList = pathValues[0].value;
            var values = valueList.split((session.socket.getType(varId) == "uis:csvlist") ? "," : " ");
            for (var j = 0; j < values.length; j++) {
               var option = document.createElement("OPTION");
               option.value = values[j];
               org_myurc_lib_setTextContent(option, org_myurc_webclient_getLabelForValue(session, org_myurc_webclient_getId(session, container.id), eltTypeName, values[j]));
               select.appendChild(option);
            }
         }
      }
      else
         throw new Error("Internal error: selectionSet neither static nor dynamic.");
   }
   
   org_myurc_webclient_addEventHandler(select, eltType);
   container.appendChild(select);
   return select;
};



/********************************************************
org_myurc_webclient_createCommandStructure: Create structure for right column of a command table, but only if not existing yet.
      If simple command: Will create value node (button) with id = cmdPath + ".val"
      If complex command: Will create table with fields, including value node (button) with id = cmdPath + "/invoke.val"
   session: session object.
   container: container node for the command structure (with id="socketPath").
   socketPath: socket path (with indices) of command
	return: -
 ********************************************************/
org_myurc_webclient_createCommandStructure = function(session, container, socketPath) {

   var id = org_myurc_webclient_getId(session, socketPath);
   var indices = org_myurc_webclient_getIndices(socketPath); 
   if (indices.length != session.socket.getDimTypes(id).length)
      throw new Error("Invalid socketPath - command must have " + session.socket.getDimTypes(id).length + " indices: " + socketPath);
   var params = session.socket.getLocalParameterIds(id);

   if (indices.length === 0 &&     // non-dimensional command?
      params["in"].length === 0 && params.inout.length === 0 && params.out.length === 0) {  // no local parameters? 
      // Simple command
      if (!document.getElementById(socketPath + ".val"))     // Nothing there yet?
         org_myurc_webclient_createValueNode(session, container, "command");      // Add <button id="socketPath.inv"><div id="socketPath.val"></div></button>
      return;
   }

   else {      // Complex table
      // Now:
      //   * Create command table if not existing yet
      //   * Create rows with their headers within command table, if not existing yet
      //   * Create all cells with value containers, if not existing yet
      // Note that even if the command table exists, we may need to add a column if command is dimensional.

      var pathWithoutIndices = org_myurc_webclient_stripIndices(session, socketPath);
      tableId = pathWithoutIndices + ".tab";
      var tableNode = document.getElementById(tableId) ||   // command table existing?
         org_myurc_webclient_createTable(container, tableId, "Command");   // Create one if not.

      var labelId = pathWithoutIndices + "/invoke.lab";
      if (document.getElementById(labelId) === null)
         org_myurc_webclient_addTableRow(session, tableNode, labelId, "Invoke");
            // Creates row with <th id="socketPath/invoke.lab" /><td id="socketPath/invoke" />
      if (document.getElementById(socketPath + "/invoke") === null) {
         var col = pathWithoutIndices + indices.join("") + ".col";
         var tableCell = org_myurc_webclient_addTableCell(session, tableNode, labelId, col, null);
         org_myurc_webclient_createValueNode(session, tableCell, "command");      // Add <button id="socketPath.inv"><div id="socketPath.val"></div></button>
      }

      for (var i=0; i < params["in"].length; i++) {      // Have to write as params["in"], not params.in, since in is a keyword
         labelId = pathWithoutIndices + "/" + params["in"][i] + ".lab";
         if (document.getElementById(labelId) === null)
            org_myurc_webclient_addTableRow(session, tableNode, labelId, org_myurc_webclient_getLabelForElement(session, params["in"][i]));
               // Creates row with <th id="socketPath/in.lab" /><td id="socketPath/in" />
         var paramPath = socketPath + "/" + params["in"][i];
         if (document.getElementById(paramPath + ".val") === null)
            org_myurc_webclient_createValueNode(session, document.getElementById(paramPath), "inpar");      
               // Add <anytag id="socketPath/in.val"></anytag>
      }

      for (var i=0; i < params.inout.length; i++) {
         labelId = pathWithoutIndices + "/" + params.inout[i] + ".lab";
         if (document.getElementById(labelId) === null)
            org_myurc_webclient_addTableRow(session, tableNode, labelId, org_myurc_webclient_getLabelForElement(session, params.inout[i]));
               // Creates row with <th id="socketPath/inout.lab" /><td id="socketPath/inout" />
         var paramPath = socketPath + "/" + params.inout[i];
         if (document.getElementById(paramPath + ".val") === null)
            org_myurc_webclient_createValueNode(session, document.getElementById(paramPath), "inoutpar");
               // Add <anytag id="socketPath/inout.val"></anytag>
      }

      for (var i=0; i < params.out.length; i++) {
         labelId = pathWithoutIndices + "/" + params.out[i] + ".lab";
         if (document.getElementById(labelId) === null)
            org_myurc_webclient_addTableRow(session, tableNode, labelId, org_myurc_webclient_getLabelForElement(session, params.out[i]));
               // Creates row with <th id="socketPath/out.lab" /><td id="socketPath/out" />
         var paramPath = socketPath + "/" + params.out[i];
         if (document.getElementById(paramPath + ".val") === null)
            org_myurc_webclient_createValueNode(session, document.getElementById(paramPath), "outpar");
               // Add <anytag id="socketPath/out.val"></anytag>
      }
   }
};


/********************************************************
org_myurc_webclient_createNotification: Create a notification element with id="socketName#notifypath".
   session: session object.
   container: container node for the notification element.
   socketPath: path of the notify element, in the form of socketName#notifypath.
   type: Type of message ("info", "alert", or "error")
   message: Text message for notification.
	return: notification element <div>.
 ********************************************************/
org_myurc_webclient_createNotification = function(session, container, socketPath, type, message) {
	
	var div = document.getElementById(socketPath);
	if( !div ){
		var div = document.createElement("DIV");
		div.id = socketPath;
		div.className = "notifyinactive";
	}
   
   container.appendChild(div);
   // changes for implementation of notification types. Parikshit Thakur : 20110730
   var id = org_myurc_webclient_getId(session, socketPath);
   var category = session.socket.getNotifyCategory(id);
   
   // Parikshit Thakur : 20110825. Added to get image path from rsheet.
   var path = org_myurc_webclient_getImagePath(session, id);
   if(path == ""){
	   path = "images/"+category+".png";
   }
   // Change ends : Parikshit Thakur : 20110825
 //  var path = "";
   if(category == "error"){
	   div.innerHTML = "<p><img src=\""+path+"\" alt=\"Error\" align=\"middle\" title=\"Error\"/> </p>" ;
	   div.setAttribute("style", "background:Red"); // Parikshit Thakur : 20110825. added to change background of notification acc to its category.
	      
	 
   }
   else if(category == "alert"){
	   div.innerHTML = "<p><img src=\""+path+"\" alt=\"Alert\" align=\"middle\" title=\"Alert\"/> </p>" ;
	   div.setAttribute("style", "background:Yellow");
   }
   else{
	   div.innerHTML = "<p><img src=\""+path+"\" alt=\"Info\" align=\"middle\" title=\"Info\"/> </p>" ;
	   div.setAttribute("style", "background:Green");
   }
   
   div.innerHTML += "<p>" + message + "</p>";
   
   if(type == "confirmCancel"){
	   div.innerHTML += "<p><button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Confirm')\"" +
	      " onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Confirm')\">Confirm</button> &nbsp;" +
	      "<button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Cancel')\"" +
	      " onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Cancel')\">Cancel</button></p>";
   } 
   else if(type == "show"){
	   div.innerHTML += "<button onclick=\"org_myurc_webclient_discardNotification('" + socketPath + "')\"" +
	      " onkeypress=\"org_myurc_webclient_discardNotification('" + socketPath + "')\">OK</button></p>";
   }
   else if(type == "confirm"){
	   div.innerHTML += "<p><button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Confirm')\"" +
	      " onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Confirm')\">Confirm</button></p>";
   }
   else if(type == "yesNo"){
	   div.innerHTML += "<p><button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Yes')\"" +
	      " onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Yes')\">Yes</button>" +
	      "<button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'No')\"" +
	      " onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'No')\">No</button></p>";
   }
   else if(type == "yesNoCancel"){
	   div.innerHTML += "<p><button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Yes')\"" +
	      " onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Yes')\">Yes</button>" +
	      "<button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'No')\"" +
	      " onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'No')\">No</button>+" +
	      "<button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Cancel')\"" +
	      " onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Cancel')\">Cancel</button></p>";
   }
   //Parikshit Thakur : 20111017. Changes done in option and custom type notifications. Previously implementation was not complete for thewse two types..
   else if(type == "option"){
		var selectionSet = session.socket.getSelectionSets(id);
		for(var i = 0; i < selectionSet.length; i++){
			if(selectionSet[i].typeRef != undefined){
				var typeName = selectionSet[i].typeRef;
			}
			else if( selectionSet[i].varRef != undefined){
				var varId = selectionSet[i].varRef;
				var pathValues = org_myurc_urchttp_getValues(session.protocolUrl, session.sessionId, varId);
				 if (pathValues && pathValues[0]){
					 if(session.socket.getType(varId) == "uis:csvlist"){
						 var userValues = org_myurc_webclient_stringTokenizer(pathValues[0].value, ",", true);
					 }
					 else if(session.socket.getType(varId) == "uis:stringList"){
						 var userValues = org_myurc_webclient_stringTokenizer(pathValues[0].value, " ", false);
					 }
				}
			}
		}
		  
		var simpleType = session.socket.getEnumsForType(typeName);
	   
		var comboBoxStr = "<select id='optionCombo'>"; 
		for(var j=0; j< simpleType.length; j++){
			comboBoxStr = comboBoxStr + "<option value=\""+ simpleType[j]+"\">"+ simpleType[j] +"</option>";
		}
		for(var k=0; k< userValues.length; k++){
			comboBoxStr = comboBoxStr + "<option value=\""+ userValues[k] +"\">"+ userValues[k] +"</option>";
		}
		comboBoxStr = comboBoxStr + "</select>";
		
	   div.innerHTML +=  "<p>" + comboBoxStr + "</p>" +	      
	      "<p><button onclick=\"org_myurc_webclient_optionAcknowledge('" + socketPath + "')\"" +
	      " onkeypress=\"org_myurc_webclient_optionAcknowledge('" + socketPath + "')\">Confirm</button></p>";
   }
   else if(type == "optionCancel"){
		var selectionSet = session.socket.getSelectionSets(id);
		for(var i = 0; i < selectionSet.length; i++){
			if(selectionSet[i].typeRef != undefined){
				var typeName = selectionSet[i].typeRef; 
			}
			else if( selectionSet[i].varRef != undefined){
				var varId = selectionSet[i].varRef;
				var pathValues = org_myurc_urchttp_getValues(session.protocolUrl, session.sessionId, varId);
				 if (pathValues && pathValues[0]){
					 if(session.socket.getType(varId) == "uis:csvlist"){
						 var userValues = org_myurc_webclient_stringTokenizer(pathValues[0].value, ",", true);
					 }
					 else if(session.socket.getType(varId) == "uis:stringList"){
						 var userValues = org_myurc_webclient_stringTokenizer(pathValues[0].value, " ", false);
					 }
				}
			}
		}
		
		var simpleType = session.socket.getEnumsForType(typeName);
	   
		var comboBoxStr = "<select id='optionCombo'>"; 
		for(var j=0; j< simpleType.length; j++){
			comboBoxStr = comboBoxStr + "<option value=\""+ simpleType[j]+"\">"+ simpleType[j] +"</option>";
		}
		for(var k=0; k< userValues.length; k++){
			comboBoxStr = comboBoxStr + "<option value=\""+ userValues[k] +"\">"+ userValues[k] +"</option>";
		}
		comboBoxStr = comboBoxStr + "</select>";
		
	   div.innerHTML += "<p>" + comboBoxStr + "</p>" +	      
	      "<p><button onclick=\"org_myurc_webclient_optionAcknowledge('" + socketPath + "')\"" +
	      " onkeypress=\"org_myurc_webclient_optionAcknowledge('" + socketPath + "')\">Confirm</button>" +
	      " <button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Cancel')\"" +
	      " onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'Cancel')\">Cancel</button></p>";
   }
   else if(type == "custom"){
	  
	   var notifVariables = session.socket.getCustomNotifVariable(id);
	   var notifyCommands = session.socket.getCustomNotifCommands(id);
	   
	   for(var i=0; i< notifVariables.length;i++){
		   var pathValues = org_myurc_urchttp_getValues(session.protocolUrl, session.sessionId, notifVariables[i].id);
		   org_myurc_webclient_processNotificationElements(session, socketPath.substring(0, socketPath.indexOf('[state]')) + '/' + pathValues[0].path, pathValues[0].value);
		 }
	   for(var i=0; i< notifyCommands.length;i++){
		   var pathValues = org_myurc_urchttp_getValues(session.protocolUrl, session.sessionId, notifyCommands[i].id);
		   org_myurc_webclient_processNotificationElements(session, socketPath.substring(0, socketPath.indexOf('[state]')) + '/' + pathValues[0].path, pathValues[0].value);

	   	 }
	   	  
   }
   else if(type == "customCancel"){
		  
	   var notifVariables = session.socket.getCustomNotifVariable(id);
	   var notifyCommands = session.socket.getCustomNotifCommands(id);
	   
	   for(var i=0; i< notifVariables.length;i++){
		   var pathValues = org_myurc_urchttp_getValues(session.protocolUrl, session.sessionId, notifVariables[i].id);
		   org_myurc_webclient_processNotificationElements(session, socketPath.substring(0, socketPath.indexOf('[state]')) + '/' + pathValues[0].path, pathValues[0].value);
		 }
	   for(var i=0; i< notifyCommands.length;i++){
		   var pathValues = org_myurc_urchttp_getValues(session.protocolUrl, session.sessionId, notifyCommands[i].id);
		   org_myurc_webclient_processNotificationElements(session, socketPath.substring(0, socketPath.indexOf('[state]')) + '/' + pathValues[0].path, pathValues[0].value);
	   }
   }
     
   else{
	   div.innerHTML += "<p><button onclick=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'OK')\"" +
	      " onkeypress=\"org_myurc_webclient_acknowledge('" + socketPath + "', 'OK')\">OK</button></p>"; 
   }
   // Change ends.
   //container.appendChild(div);
   return div;
};


/**
 * Tokenizes the given string based on the given seperator.
 * stringValue: String to be parsed.
 * separator: Seperator string for tokenization.
 * trimRequired: Boolean value.
 */
org_myurc_webclient_stringTokenizer = function(stringValue, separator, trimRequired){
	  
	  var options = [];
	  var option="";
	  
	  stringValue = stringValue.split(separator);
	  for(i=0 ; i<stringValue.length ; i++){
		  
	   var temp = stringValue[i];
	   if(temp.indexOf('\\')+1 == temp.length){
		   temp = temp.replace('\\',separator);
		   option += (trimRequired)?org_myurc_lib_trimString(temp):temp;
		   continue; 
	   }
	   option += org_myurc_lib_trimString(temp);
	   options.push(option);
	   option="";
	 }
	  return options;

};



/*******************************************************
org_myurc_webclient_discardNotification: Hides the notification.
socketPath: path of the notify element, in the form of socketName#notifypath.
******************************************************/
org_myurc_webclient_discardNotification = function(socketPath){
	 document.getElementById(socketPath).className = "notifyinactive";
};


/*******************************************************
 org_myurc_webclient_optionAcknowledge: Signals for notification of type 'option'.
 socketPath: path of the notify element, in the form of socketName#notifypath.

 ******************************************************/
 org_myurc_webclient_optionAcknowledge = function(socketPath){
	org_myurc_webclient_acknowledge(socketPath, document.getElementById('optionCombo').value);
	
};


/************************************************************
  Returns path of image from rsheet.
   session: session object.
   id: element id.
   return: path
 ************************************************************/
org_myurc_webclient_getImagePath = function(session, id){
	
	var rqArray = [];  
	var rq = {
	         "eltRef": session.socketName + "#" + id,
	         "type": "image"
	         };
    rqArray.push(rq);
	
	var resources = org_myurc_urchttp_getResources(rqArray);
	return  (resources[0] === null) ? "" : resources[0].at;  
};

/********************************************************
org_myurc_webclient_getLabelForElement: Get the label for an element identified by id.
   session: session object.
   id: element id.
	return: label / "{id}" if label not available.
 ********************************************************/
org_myurc_webclient_getLabelForElement = function(session, id) {
	
   var labels = org_myurc_webclient_getLabels(session, 
      [ { "eltRef": session.socketName + "#" + id, "value": null } ] );
   return (labels[0] === null) ? "{" + id + "}" : labels[0];      // Make up label if not found.
};


/********************************************************
org_myurc_webclient_getLabelForValue: Get the label for an element"s specific value.  
   session: session object.
   elementId: element id.
   typeName: name of the associated type in CURIE format.
   value: value for which the label is requested.
	return: label / "{value}" if label not available.
 ********************************************************/
org_myurc_webclient_getLabelForValue = function(session, elementId, typeName, value) {
   // Build a resource query: label for value on element
   var queryArray = [ { "eltRef": session.socketName + "#" + elementId, "value": value } ];     
   var eltRef = session.socket.getEltRefForType(typeName);
   if (eltRef)    
      queryArray.push( { "eltRef": eltRef, "value": value } );     // Add query: label for value on pertinent type

   var labelArray = org_myurc_webclient_getLabels(session, queryArray);
   
   for (var i = 0; i < labelArray.length; i++) {
      if (labelArray[i] !== null)     // Found one?
         return labelArray[i];
   }
   
   // None of the requests were successful.
   return "{" + value + "}";     // Make up label.
};


/********************************************************
org_myurc_webclient_getLabels: Get labels for elements with or without value.
   session: session object.
   idValueArray: Array of objects, each with the following properties:
      eltRef: element reference.
      value: specific value / null if label for element requested
	return: Array of: { label / null if label not available }.
 ********************************************************/
org_myurc_webclient_getLabels = function(session, idValueArray) {
   var rqArray = [];

   for (var i = 0; i < idValueArray.length; i++) {
	  
      var rq = {
         "eltRef": idValueArray[i].eltRef,
         "role": "http://openurc.org/ns/res#label",
         "type": "Text",
         "forLang": org_myurc_webclient_forLang };
      if (idValueArray[i].value !== null)
         rq.valRef = idValueArray[i].value;
      rqArray.push(rq);
   }

   var resources = org_myurc_urchttp_getResources(rqArray);

   if (resources.length !== idValueArray.length)
      throw new Error("Invalid number of resources in getResources response: " + resources.length);      
   var retArray = [];
   for (var i = 0; i < resources.length; i++) 
      retArray.push((resources[i] && resources[i].string) ? resources[i].string : null);
   return retArray;
};

/* ----------------------- Convenience functions below ------------------------ */


/********************************************************
org_myurc_webclient_getId: Get the element id for a given path or socketPath.
   session: session object.
   pathOrSocketPath: id or path or full path (socketName#path)
	return: id string
 ********************************************************/
org_myurc_webclient_getId = function(session, pathOrSocketPath) {
   var path = org_myurc_webclient_getPath(pathOrSocketPath);
   
   if (path == "/")
      return session.socket.getRootId();
   
   var lastSlash = path.lastIndexOf("/");    // -1 if not found - i.e. already an id.
   var firstBracketOnTail = path.indexOf("[", lastSlash + 1);   // -1 if not found
   var id = path.slice(lastSlash >= 0 ? lastSlash+1 : 0, 
      firstBracketOnTail > 0 ? firstBracketOnTail : path.length);
   return id;
};

/********************************************************
org_myurc_webclient_getParent: Get the parent socketPath for a given socketPath.
   session: session object.
   socketPath: full path in the form socketName#path
	return: socketPath for parent string, including indices
 ********************************************************/
org_myurc_webclient_getParent = function(session, socketPath) {
   var path = org_myurc_webclient_getPath(socketPath);
   var lastSlash = path.lastIndexOf("/");    // -1 if not found
   if (lastSlash < 0)
      throw new Error("Invalid socketPath: " + socketPath);
   return (lastSlash === 0 ? 
      session.socketName + "#/" : 
      session.socketName + "#" + path.slice(0, lastSlash));
};

/********************************************************
org_myurc_webclient_getIndices: Get the trailing indices for a given socketPath.
   socketPath: full path in the form socketName#path
	return: Array with index strings, one by one
 ********************************************************/
org_myurc_webclient_getIndices = function(socketPath) {
   var path = org_myurc_webclient_getPath(socketPath);
   var indices = [];
   var firstSlash = path.lastIndexOf("/");      // -1 if not found
   for (var start = path.indexOf("[", firstSlash+1); start >= 0; start = next) {
      next = path.indexOf("[", start+1);
      indices.push(next > 0 ? path.slice(start, next) : path.slice(start));
   }
   return indices;
};

/********************************************************
org_myurc_webclient_stripIndices: Get the socketPath without trailing indices for a given socketPath.
   session: session object.
   socketPath: full path in the form socketName#path
	return: Path string without trailing indices
 ********************************************************/
org_myurc_webclient_stripIndices = function(session, socketPath) {
   var path = org_myurc_webclient_getPath(socketPath);
   var lastSlash = path.lastIndexOf("/");    // -1 if not found
   if (lastSlash < 0)
      throw new Error("Invalid socketPath: " + socketPath);
   var firstBracketOnTail = path.indexOf("[", lastSlash+1);   // -1 if not found
   return (firstBracketOnTail > 0) ? 
      session.socketName + "#" + path.slice(0, firstBracketOnTail) : 
      socketPath;
};

/********************************************************
org_myurc_webclient_getSocketName: Get the socketName part of a socketPath.
   pathOrSocketPath: socketPath in the form "socketName#path" or "path".
	return: socketName / null if no socketName specified in pathOrSocketPath.
 ********************************************************/
org_myurc_webclient_getSocketName = function(pathOrSocketPath) {
   var firstHash = pathOrSocketPath.indexOf("#");     // -1 if not found
   return (firstHash <= 0) ?
      null :
      pathOrSocketPath.slice(0, firstHash);     // Do not include the "#" sign.
};

/********************************************************
org_myurc_webclient_getPath: Get the path part of a socketPath.
   pathOrSocketPath: socketPath in the form "socketName#path" or "path".
	return: path / "" if missing
 ********************************************************/
org_myurc_webclient_getPath = function(pathOrSocketPath) {
   var firstHash = pathOrSocketPath.indexOf("#");     // -1 if not found
   return (firstHash < 0) ?
      pathOrSocketPath :      // no socketName contained in pathOrSocketPath
      pathOrSocketPath.slice(firstHash + 1);     // Do not include the "#" sign.
};


/********************************************************
org_myurc_webclient_shareIndices: Copy the set indices of path1 to path2, but only for those sets
   that are common to both paths.
   path1: source path with indices (without socket name)
   path2: path without indices (without socket name)
	return: path2 with inserted indices from path1 for common sets
 ********************************************************/
org_myurc_webclient_shareIndices = function(path1, path2) {
   if (path1.charAt(0) !== '/' || path2.charAt(0) !== '/')
      throw new Error("Internal error: One path doesn't start with '/': " + path1 + ", " + path2);
      
   var result = "";
   for (var slash1 = 0, slash2 = 0; slash1 >= 0 && slash2 >= 0; slash1 = nextSlash1, slash2 = nextSlash2) {
      var nextSlash1 = path1.indexOf('/', slash1+1);     // -1 if not found
      var id1WithIndices = (nextSlash1 >= 0) ? path1.slice(slash1+1, nextSlash1) : path1.slice(slash1+1);
      var firstBracket1 = id1WithIndices.indexOf("[");   // -1 if not found
      var id1 = (firstBracket1 >= 0) ? id1WithIndices.slice(0, firstBracket1) : id1WithIndices;
      
      var nextSlash2 = path2.indexOf('/', slash2+1);     // -1 if not found
      var id2WithIndices = (nextSlash2 >= 0) ? path2.slice(slash2+1, nextSlash2) : path2.slice(slash2+1);
      var firstBracket2 = id2WithIndices.indexOf("[");   // -1 if not found
      var id2 = (firstBracket2 >= 0) ? id2WithIndices.slice(0, firstBracket2) : id2WithIndices;
      
      if (id1 == id2)      // Common ancestor ids?
         result += '/' + id1WithIndices;
      else
         break;            // Paths branch here - go out of loop.
   }

   // Come here, slash2 marks the beginning of the branch.
   if (slash2 >= 0)    
      result += path2.slice(slash2);      // Append rest of path2.
   
   return result;
};


/******************************************************
org_myurc_webclient_getResources: Get resources from the UCH.
   resources: Array of objects (with properties) representing resource queries.
   return: Array of resource objects (or null) with any of the following properties:
      - string: text resource
      - at: URL resource
*/
org_myurc_webclient_getResources = function( resourceQueries ){	

	if ( ! resourceQueries )
		throw new Error("Resource Request Invalid.");

	return org_myurc_urchttp_getResources ( resourceQueries );
	
};