/*
 Copyright: Access Technologies Group, Germany, 2007-2009.  
 This software is licensed under the CC-GNU GPL.  See http://creativecommons.org/licenses/GPL/2.0/ for 
 human-readable Commons Deed, lawyer-readable legal code, and machine-readable digital code.

 ***********************************************************************************

 JavaScript library for client-side communication over URC-HTTP protocol.  

 Imports:
   org_myurc_lib.js
 
 List of exported functions:
   org_myurc_urchttp_getAvailableSockets: Retrieve all socket names from uiList, but only if protocolName="URC-HTTP".
   org_myurc_urchttp_openSession: Open a new session with UCH.
   org_myurc_urchttp_closeSession: Close a session with UCH.
   org_myurc_urchttp_getValues: Get values from UCH.
   org_myurc_urchttp_getIndex: Get current index values for a dimensional element or set.
   org_myurc_urchttp_setValue: send new value request to UCH.
   org_myurc_urchttp_setValues: send new values request to UCH.
   org_myurc_urchttp_invokeCommand: send command invocation request to UCH.
   org_myurc_urchttp_acknowledgeNotification: Signal that the controller has received a notification from the server.
   org_myurc_urchttp_getUpdates: Get updates from the UCH.
   org_myurc_urchttp_getResources: Get resources from the UCH.
   org_myurc_urchttp_getDocument: Get document from the UCH (via HTTP proxy function).   
	
 Author: Gottfried Zimmermann, ATG
 Last modified: 2009-08-07

 **********************************************************************************/

/**
  Variables and defaults
 */

var org_myurc_urchttp_getUiListUrl = location.protocol + "//" + location.host + "/UCH/GetCompatibleUIs";
var org_myurc_urchttp_getResourcesUrl = location.protocol + "//" + location.host + "/UCH/GetResources";
var org_myurc_urchttp_getDocumentUrl = location.protocol + "//" + location.host + "/UCH/GetDocument";
var org_myurc_urchttp_test = false;

/******************************************************
org_myurc_urchttp_getAvailableSockets: Retrieve all socket names from uiList, but only if protocolName="URC-HTTP".
	return: Array with objects with the following properties:
	   targetId: target identifer
	   targetLabel: Label for target
	   protocolUrl: URL for session activities (e.g. open session)
	   targetName: target name (URI)
	   socketName: socket name (URI)
	   socketFriendlyName: socket friendly name / NULL if not available
	   socketDescriptionAt: URL for socket description
	   targetDescriptionAt: URL for target description
 */
org_myurc_urchttp_getAvailableSockets = function() {
   
	var uiList = org_myurc_urchttp_getUiList();
	var protocolNodes = uiList.getElementsByTagName("protocol");
	var sockets = [];
   
	for (var i=0; i<protocolNodes.length; i++) {
	
	   var protocolNode = protocolNodes[i];
	 
      if (protocolNode.getAttribute("shortName") == "URC-HTTP") {
         var uiidNodes = org_myurc_lib_selectChildren(protocolNode.parentNode, "uiID");
         var nameNodes = org_myurc_lib_selectChildren(protocolNode.parentNode, "name");
         var uriNodes = org_myurc_lib_selectChildren(protocolNode, "uri");
         var protocolInfoNodes = org_myurc_lib_selectChildren(protocolNode, "protocolInfo");
         if (protocolInfoNodes.length != 1)
            throw new Error("Invalid number of &lt;protocolInfo&gt; elements in UIList");
         var protocolInfoNode = protocolInfoNodes[0];
         var targetNameNodes = org_myurc_lib_selectChildren(protocolInfoNode, "targetName");
         var socketNameNodes = org_myurc_lib_selectChildren(protocolInfoNode, "socketName");
         var socketDescriptionAtNodes = org_myurc_lib_selectChildren(protocolInfoNode, "socketDescriptionAt");
         var targetDescriptionAtNodes = org_myurc_lib_selectChildren(protocolInfoNode, "targetDescriptionAt");
         if (uiidNodes.length != 1 || nameNodes.length != 1 ||
            uriNodes.length != 1 || targetNameNodes.length != 1 || socketNameNodes.length != 1 || 
            socketDescriptionAtNodes.length != 1 || targetDescriptionAtNodes.length != 1)    	 
//2012-08-28 - Yuvaraj - below line commented.        	 
        	 ;
        	 //throw new Error("Invalid format of &lt;protocol&gt; element in UIList");

         var uiIdNode = org_myurc_lib_getTextContent(uiidNodes[0]);
         var tarId = uiIdNode.substring(uiIdNode.lastIndexOf(" ") + 1);
         var sockName = uiIdNode.substring(uiIdNode.indexOf(" ") + 1, uiIdNode.lastIndexOf(" "));
         var tarName = org_myurc_lib_getTextContent(uiidNodes[0]).substring( 0, org_myurc_lib_getTextContent(uiidNodes[0]).indexOf(" "));

         
         sockets.push( {
            "targetId": tarId,
            "targetLabel": org_myurc_lib_getTextContent(nameNodes[0]),
            "protocolUrl": org_myurc_lib_getTextContent(uriNodes[0]),
//2012-08-28 - Yuvaraj - below line commented.            
            "targetName": tarName,//org_myurc_lib_getTextContent(targetNameNodes[0]),
//2012-08-28 - Yuvaraj - below line commented.
            "socketName": sockName,//org_myurc_lib_getTextContent(socketNameNodes[0]),
//2012-08-28 - Yuvaraj - below line commented.            
            "socketFriendlyName": sockName,//socketNameNodes[0].getAttribute("friendlyName"),      // May be NULL if attribute not present
            "socketDescriptionAt": org_myurc_lib_getTextContent(socketDescriptionAtNodes[0]),
            "targetDescriptionAt": org_myurc_lib_getTextContent(targetDescriptionAtNodes[0])
         } );
      }
else if(protocolNode.getAttribute("shortName") == "HTTP/HTML"){
           
      	 var uiidNodes = org_myurc_lib_selectChildren(protocolNode.parentNode, "uiID");
                var nameNodes = org_myurc_lib_selectChildren(protocolNode.parentNode, "name");
                var uriNodes = org_myurc_lib_selectChildren(protocolNode, "uri");
                var protocolInfoNodes = org_myurc_lib_selectChildren(protocolNode, "protocolInfo");
                 if (protocolInfoNodes.length != 1)
                     throw new Error("Invalid number of &lt;protocolInfo&gt; elements in UIList");
         var protocolInfoNode = protocolInfoNodes[0];
         var targetNameNodes = org_myurc_lib_selectChildren(protocolInfoNode, "targetName");         
         var socketNameNodes = org_myurc_lib_selectChildren(protocolInfoNode, "socketName");
         
         var uipmClientNameNodes = org_myurc_lib_selectChildren(protocolInfoNode,"uipmClientName");
           if (uiidNodes.length != 1 || nameNodes.length != 1 ||
            uriNodes.length != 1 || targetNameNodes.length != 1 || socketNameNodes.length != 1)
//2012-08-28 - Yuvaraj - below line commented.           
        	  ; 
           //throw new Error("Invalid format of &lt;protocol&gt; element in UIList");
            
           sockets.push({
            "targetId": org_myurc_lib_getTextContent(uiidNodes[0]),
            "targetLabel": org_myurc_lib_getTextContent(nameNodes[0]),
            "protocolUrl": org_myurc_lib_getTextContent(uriNodes[0]),
            "targetName": "Target Name",//org_myurc_lib_getTextContent(targetNameNodes[0]),
            "socketName": "Socket Name",//org_myurc_lib_getTextContent(socketNameNodes[0]),
            "uipmClientName": "Uipm Client Name"//org_myurc_lib_getTextContent(uipmClientNameNodes[0])            
           });
      }
   }
	
   return sockets;
};


/******************************************************
org_myurc_urchttp_openSession: Open a new session with UCH.
   protocolUrl: URL of socket protocol
   authorizationCode: string for authorization / null if not provided
   resources: Array of objects (with properties) representing resource queries.
   return: sessionId
 */
org_myurc_urchttp_openSession = function(protocolUrl, authorizationCode, resources) {
   var resourceQuery = null;
   if (resources !== null && resources.length > 0) {
      resourceQuery = '<includeResources>\n';
      for (var i = 0; i < resources.length; i++) {
         var resource = resources[i];
         resourceQuery += '<resource';
         for (prop in resource)
            resourceQuery += ' ' + prop + '="' + resource[prop] + '"';
         resourceQuery += '/>\n';
      }
      resourceQuery += '</includeResources>\n';
   }

   var postString = '<openSessionRequest' +
      (authorizationCode ? ' authorizationCode="' + authorizationCode + '"' : "") +
      '>\n' +
      (resourceQuery ? resourceQuery : "") +
      '</openSessionRequest>';

   var sessionDoc = 
      org_myurc_urchttp_serverRequest(
         protocolUrl + "?openSessionRequest", 
         postString,    // Post data string (POST request) 
         true);
         
   if (sessionDoc === null)
      throw new Error("Empty response document on open session request to server.");
	var sessionNodes = sessionDoc.getElementsByTagName("session");
	var sessionId = org_myurc_lib_getTextContent(sessionNodes[0]);   // Take content of first <session> node.

	/*
	 * Code added to create web socket
	 * For Web socket, IP address and port of client is set from response document.
	 */
	var ipAddressNodes = sessionDoc.getElementsByTagName("ipAddress"); //Added for web socket ip address.
	var portNodes = sessionDoc.getElementsByTagName("portNo"); //Added for web socket port number.
	var webSocketIPAndPort = null;
	if(ipAddressNodes.length > 0 && portNodes.length > 0) {
		webSocketIPAndPort = org_myurc_lib_getTextContent(ipAddressNodes[0]);
		if(webSocketIPAndPort.indexOf(":") > 0)
			webSocketIPAndPort = webSocketIPAndPort.substr(0,webSocketIPAndPort.indexOf(":"));
		webSocketIPAndPort = webSocketIPAndPort + ":" + org_myurc_lib_getTextContent(portNodes[0]);
	}
	var timer = null;
	if(webSocketIPAndPort != null) {
		if (window.WebSocket) {
			connectWebSocket = function() {
				org_myurc_urchttp_webSocket = new WebSocket("ws://"+webSocketIPAndPort);
				org_myurc_urchttp_webSocket.onopen = function(evt) {
					org_myurc_urchttp_webSocket.send("<session>"+sessionId+"</session>");
					if(timer!=null) {
						var session = org_myurc_webclient_sessions[sessionId];
					  	var pathValues = org_myurc_urchttp_getValues(session.protocolUrl, session.sessionId, "/");
						org_myurc_webclient_postPathValues(session, pathValues);
					}
				};
				org_myurc_urchttp_webSocket.onmessage = function(evt) {
					var responseDoc = org_myurc_lib_toXmlDoc(evt.data);
					if(responseDoc != null) {
						var updateNodes = responseDoc.getElementsByTagName("updates");
						var errorNodes = responseDoc.getElementsByTagName("error");
						if(errorNodes.length == 0) {
							if(updateNodes.length > 0) {
								var session = org_myurc_webclient_sessions[sessionId];
								org_myurc_webclient_postPathValues(session, org_myurc_urchttp_toPathValues(org_myurc_lib_toXmlDoc(responseDoc)));
							}
						}
					}
				};
				org_myurc_urchttp_webSocket.onclose = function(evt) {
					timer = setTimeout("connectWebSocket();",1000);
				};
				org_myurc_urchttp_webSocket.onerror = function(evt) {
				};

				if(org_myurc_urchttp_webSocket != null) {
					if(org_myurc_urchttp_webSocket.readyState == 1 || org_myurc_urchttp_webSocket.readyState == 0) {
						if(timer!=null)
							clearTimeout(timer);
						updateInterval = -1;
					}
				}
				window.addEventListener("pagehide", closeWebSocket, false);
			};
			closeWebSocket = function() {
				org_myurc_urchttp_webSocket.close(1000,"closed successfully");
			};
			connectWebSocket();
		} 
	}
	return sessionId;
};


/******************************************************
org_myurc_urchttp_closeSession: Close a session with UCH.
   protocolUrl: URL of socket protocol
   sessionId: session identifier (obtained from openSession)
   return: -
   exception: may occur
 */
org_myurc_urchttp_closeSession = function(protocolUrl, sessionId) {
   org_myurc_urchttp_serverRequest(protocolUrl + "?closeSessionRequest&session=" + sessionId, null, true);  // GET request
};


/******************************************************
org_myurc_urchttp_getValues: Get values from UCH.
   protocolUrl: URL of socket protocol
   sessionId: session identifier (obtained from openSession)
   path: path string for requested value.  Use "/" for retrieving all values of the session.
   return: Array of pathValue objects.
   exception: may occur
 */
org_myurc_urchttp_getValues = function(protocolUrl, sessionId, path) {
	
	var valuesDoc;
	
	if( path && path instanceof Array ){
		var postDoc = org_myurc_lib_createEmptyDoc("getValues");
		for( var i=0;i<path.length;i++){
			var getElement = postDoc.createElement("get");
			getElement.setAttribute("ref", org_myurc_urchttp_encodeValue(path[i]));	
			postDoc.documentElement.appendChild(getElement);
		}
		
	  valuesDoc = org_myurc_urchttp_serverRequest(protocolUrl + '?getValues&session=' + sessionId,
      postDoc,   // POST request
      true);
	}else{
 	  valuesDoc = org_myurc_urchttp_serverRequest(protocolUrl + '?getValues&session=' + sessionId,
      '<getValues><get ref="' + path + '" /></getValues>',   // POST request
      true);
	}
	return org_myurc_urchttp_toPathValues(valuesDoc);
};


/******************************************************
org_myurc_urchttp_getIndex: Get current index values for a dimensional element or set.
   protocolUrl: URL of socket protocol
   sessionId: session identifier (obtained from openSession)
   elementPath: Path of a dimensional Socket element or set.
   return: Array of index values (as strings).
   exception: may occur
 */
//Parikshit Thakur : 20110929. changed method for new spec, removed index no and changed elementId to elementPath
org_myurc_urchttp_getIndex = function(protocolUrl, sessionId, elementPath) {
   var indexDoc = org_myurc_urchttp_serverRequest(protocolUrl + '?getValues&session=' + sessionId,
      '<getValues><getIndex ref="' + elementPath + '" /></getValues>',   // POST request
      true);
	return org_myurc_urchttp_toIndexValues(indexDoc);
};


/******************************************************
org_myurc_urchttp_setValue: send new value request to UCH.
   protocolUrl: URL of socket protocol
   sessionId: session identifier (obtained from openSession)
   path: absolute path of socket element
   value: new requested value for socket element
   return: Array of pathValue objects with updates.
   exception: may occur
 */
org_myurc_urchttp_setValue = function(protocolUrl, sessionId, path, value) {
	
	var postDoc = org_myurc_lib_createEmptyDoc("setValues");
	var setElement = postDoc.createElement("set");
	setElement.setAttribute("ref", org_myurc_urchttp_encodeValue(path));	
	org_myurc_lib_setTextContent(setElement, org_myurc_urchttp_encodeValue(value));
	postDoc.documentElement.appendChild(setElement);
	var updateDoc = org_myurc_urchttp_serverRequest(protocolUrl + "?setValues&session=" + sessionId, postDoc, true);    // POST request
	return org_myurc_urchttp_toPathValues(updateDoc);
};



/******************************************************
org_myurc_urchttp_setValues: send new value request to UCH.
   protocolUrl: URL of socket protocol
   sessionId: session identifier (obtained from openSession)
   elements : {elementPath, operation, value} 
		Map for elements
	/* Each item (object) in elements has the following properties:
   	* elementPath: Path of the socket element.
   	* operation: Operation to be perform on socket element like ['S' - setValue, 'I' - InvokeCommand, 'K' - Acknoledgment].
	* value : New value to be set for the specified socket element.
   return: Array of pathValue objects with updates.
   exception: may occur
 */
org_myurc_urchttp_setValues = function(protocolUrl, sessionId, elements) {
	
	if ( !elements || (elements.length <= 0) )
		return null;
	var postDoc = org_myurc_lib_createEmptyDoc("setValues");
	for( var i=0;i<elements.length; i++){
		var element = elements[i];
		
		var path = element.elementPath;
		var operation = element.operation;
		var value = element.value;
		
		switch(operation){
			case "S":
				var setElement = postDoc.createElement("set");
				setElement.setAttribute("ref", org_myurc_urchttp_encodeValue(path));	
				org_myurc_lib_setTextContent(setElement, org_myurc_urchttp_encodeValue(value));
				postDoc.documentElement.appendChild(setElement);
				break;
			case "I":
				var cmdElement = postDoc.createElement("invoke");
				cmdElement.setAttribute("ref", org_myurc_urchttp_encodeValue(path));		
				org_myurc_lib_setTextContent(cmdElement, org_myurc_urchttp_encodeValue("syn"));    
				postDoc.documentElement.appendChild(cmdElement);
				break;
			case "K":
				var ackElement = postDoc.createElement("ack");
				ackElement.setAttribute("ref", org_myurc_urchttp_encodeValue(path));		
				postDoc.documentElement.appendChild(ackElement);
				break;
			case "A":
				var addElement = postDoc.createElement("add");
				addElement.setAttribute("ref", org_myurc_urchttp_encodeValue(path));
				org_myurc_lib_setTextContent(addElement, org_myurc_urchttp_encodeValue(value));
				postDoc.documentElement.appendChild(addElement);
				break;
			case "R":
				var removeElement = postDoc.createElement("remove");
				removeElement.setAttribute("ref", org_myurc_urchttp_encodeValue(path));		
				postDoc.documentElement.appendChild(removeElement);
				break;
			
		}
	}
	
	
	var updateDoc = org_myurc_urchttp_serverRequest(protocolUrl + "?setValues&session=" + sessionId, postDoc, true);    // POST request
	return org_myurc_urchttp_toPathValues(updateDoc);
	
};

/******************************************************
org_myurc_urchttp_invokeCommand: send command invocation request to UCH.
   protocolUrl: URL of socket protocol
   sessionId: session identifier (obtained from openSession)
   path: absolute path of command element
   paramPathValues: Array with path-value objects for input and input-output parameter
   return: Array of pathValue objects with updates.
   exception: may occur
*/

org_myurc_urchttp_invokeCommand = function(protocolUrl, sessionId, path, paramPathValues) {
	var postDoc = org_myurc_lib_createEmptyDoc("setValues");
	
	// First append values of in and inout parameters.
	for (var i = 0; i < paramPathValues.length; i++) {
	   var paramElement = postDoc.createElement("set");
	   paramElement.setAttribute("ref", org_myurc_urchttp_encodeValue(paramPathValues[i].path));		
	   org_myurc_lib_setTextContent(paramElement, org_myurc_urchttp_encodeValue(paramPathValues[i].value));
   	postDoc.documentElement.appendChild(paramElement);
	}
	// Then append "invoke" element for command invocation.
	var cmdElement = postDoc.createElement("invoke");
	cmdElement.setAttribute("ref", org_myurc_urchttp_encodeValue(path));		
	org_myurc_lib_setTextContent(cmdElement, org_myurc_urchttp_encodeValue("syn"));     // Synchronous call for now.
	postDoc.documentElement.appendChild(cmdElement);
	
	var updateDoc = org_myurc_urchttp_serverRequest(protocolUrl + "?setValues&session=" + sessionId, postDoc, true);    // POST request
	return org_myurc_urchttp_toPathValues(updateDoc);
};



/******************************************************
org_myurc_urchttp_acknowledgeNotification: Signal that the controller has received a notification from the server.
      If the notification requires explicit user acknowledgment, this functions should only be called upon
      user acknowledgment.  Otherwise it can be called as soon as the notification has been received.
   protocolUrl: URL of socket protocol
   sessionId: session identifier (obtained from openSession)
   path: absolute path of notify element
   return: Array of pathValue objects with updates.
   exception: may occur
 */
org_myurc_urchttp_acknowledgeNotification = function(protocolUrl, sessionId, path, value) { // changed signature. Parikshit Thakur : 20110730
	var postDoc = org_myurc_lib_createEmptyDoc("setValues");
	var setElement = postDoc.createElement("ack");
	setElement.setAttribute("ref", org_myurc_urchttp_encodeValue(path)); 
	org_myurc_lib_setTextContent(setElement, org_myurc_urchttp_encodeValue(value)); // added to append value to ack. Parikshit Thakur : 20110730
	postDoc.documentElement.appendChild(setElement);
	var updateDoc = org_myurc_urchttp_serverRequest(protocolUrl + "?setValues&session=" + sessionId, postDoc, true);    // POST request
	return org_myurc_urchttp_toPathValues(updateDoc);
};


/******************************************************
org_myurc_urchttp_getUpdates: Get updates from the UCH.
   protocolUrl: URL of socket protocol
   sessionId: session identifier (obtained from openSession)
   refPaths: Array with update paths
   return: -
   exception: may occur
 */
org_myurc_urchttp_getUpdates = function(protocolUrl, sessionId, refPaths) {
		org_myurc_urchttp_setUpdateSignal(true);         // Make update signal visible.
		setTimeout("org_myurc_urchttp_setUpdateSignal(false)", 500);    // Make signal invisible again after 0.5 seconds; otherwise too fast to see.
		var postDoc = org_myurc_lib_createEmptyDoc("getUpdates");
	
		if (refPaths) {
			 for (var i = 0; i < refPaths.length; i++) {
				 var getElement = postDoc.createElement("get");
				 getElement.setAttribute("ref", org_myurc_urchttp_encodeValue(refPaths[i])); 
				 postDoc.documentElement.appendChild(getElement);
			 }
		}
		else {      // refPaths not specified
			 var getElement = postDoc.createElement("get");
			 getElement.setAttribute("ref", org_myurc_urchttp_encodeValue("/"));		// Get all updates
			 postDoc.documentElement.appendChild(getElement);
		}
		var updateDoc = org_myurc_urchttp_serverRequest(protocolUrl + "?getUpdates&session=" + sessionId, postDoc, false);    // POST request
	 	return org_myurc_urchttp_toPathValues(updateDoc);
};

/******************************************************
org_myurc_urchttp_getResources: Get resources from the UCH.
   resourceQueries: Array of objects (with properties) representing resource queries.
   return: Array of resource objects (or null) with any of the following properties:
      - string: text resource
      - at: URL resource
   exception: may occur
 */
org_myurc_urchttp_getResources = function(resourceQueries) {
   if (resourceQueries !== null && resourceQueries.length > 0) {
      var resourceQueryString = '<getResources>';
      for (var i = 0; i < resourceQueries.length; i++) {
         var resourceQuery = resourceQueries[i];
         resourceQueryString += '<resource';
         for (prop in resourceQuery)
            resourceQueryString += ' ' + prop + '="' + org_myurc_urchttp_encodeValue(resourceQuery[prop]) + '"';
         resourceQueryString += '/>';
      }
      resourceQueryString += '</getResources>';
   }
   
   var resourceDoc = org_myurc_urchttp_serverRequest(org_myurc_urchttp_getResourcesUrl, resourceQueryString, true);
   if (resourceDoc === null)
      throw new Error("Empty response document on getResources request to server.");
	var resourceNodes = resourceDoc.getElementsByTagName("resource");
	var resources = [];
	for (var i = 0; i < resourceNodes.length; i++) {
	   var resourceNode = resourceNodes[i];
	   var textContent = org_myurc_lib_getTextContent(resourceNode);
	   if (textContent)
	      resources.push( {"string": textContent} );
	   else if (resourceNode.getAttribute("at"))
	      resources.push( {"at": resourceNode.getAttribute("at")} );
	   else
	      resources.push(null);
	}

	return resources;
};


/******************************************************
org_myurc_urchttp_getDocument: Get document from the UCH (via HTTP proxy function).
   url: URL of document to retrieve. Null if retrieval failed.
   return: retrieved document as document object
   exception: may occur
 */
org_myurc_urchttp_getDocument = function(docUrl) {
   // TODO: Make this work for non-XML documents as well.
   var url = docUrl;
   if(!org_myurc_urchttp_test)
	   url = org_myurc_urchttp_getDocumentUrl +"?url="+ docUrl;		
   
   var doc = org_myurc_urchttp_serverRequest(url, null, true);
   return doc;    // May be null.   
//   if (doc !== null)
//      return doc;
//   else
//      throw new Error("Cannot retrieve document at " + docUrl);
};


/* -------------------- Internal functions below ----------------------- */


/******************************************************
org_myurc_urchttp_getUiList: Get the list of available UIs from the server.
   return: uiList as document
   exceptions: may be thrown by serverRequest
 */
org_myurc_urchttp_getUiList = function() {
	
   var uiList = org_myurc_urchttp_serverRequest(org_myurc_urchttp_getUiListUrl, null, true);

   if (uiList === null)
      throw new Error("Server returned empty ui list");
   return uiList;
};


/********************************************************
org_myurc_urchttp_serverRequest: Send an HTTP request to the UCH.
   url: URL for retrieving the uiList
   postData: POST data as string or document for method "POST" / null for method "GET"
   logEmptyMessage: whether to log message even if if response is empty.
	return: server response as document, null if empty response
	exceptions: May throw exception as result of using XMLHttpRequest.  Must be caught be caller.
 */
 
org_myurc_urchttp_serverRequest = function(url, postData, logEmptyMessage) {
	
   var responseText;
   var method = postData ? "POST" : "GET";
   var postString = org_myurc_lib_toXmlString(postData);
   if (org_myurc_urchttp_test) { 
      // --- Feed test cases instead of real data ---
      responseText = org_myurc_urchttp_test_getData(url, postData);
   }
   else{
  
   var request = null;
    
      // --- Get real data from UCH ---
  			try {
				
				request = new XMLHttpRequest();
			} catch(e) {
				try {
					request = new ActiveXObject("Msxml2.XMLHTTP");
						
				} catch(e) {
					try {
						
						request = new ActiveXObject("Microsoft.XMLHTTP");
					} catch(e) {
						return null;
						
					}
				}
			  }
			
			if ( request == null ) {
				return null;
			}
	  
	  request.open(method, url, false);		// synchronous for now
     request.setRequestHeader("User-Agent", "org_myurc_urchttp");
     request.setRequestHeader("Accept-Language", "en");
     request.send(postString);
	
	  responseText = request.responseText;
 
   }

  var responseXml = (responseText) ? org_myurc_lib_toXmlDoc(responseText) : null;
  
  if (logEmptyMessage || (responseXml && responseXml.documentElement && responseXml.documentElement.childNodes.length > 0)) {  
  	 org_myurc_urchttp_log(
	      method + " to " + url + "\n" + (postString ? postString + "\n" : "") +
	   	"------------------------------------\n" +
	   	"RESPONSE" +(org_myurc_urchttp_test ? 
            " (test case from org_myurc_urchttp_test):\n" : 
            " (HTTP return code " + request.status + "):\n") +
	   	responseText);
  }
   return responseXml;
};


/******************************************************
org_myurc_urchttp_toPathValues: convert a values doc from the server into an array of path values.  Preserve the order of elements.
   valuesDoc: Document with paths and values as in a getValues, setValues or getUpdates response from the server.
   return: Array of pathValue objects with the following properties:
      operation: "add", "remove" or "value"
      path: path of element
      value: value of element / null or "" for "remove" operation.
      resources: Resource array with either one of: 
         null (empty resource), 
         object with "string" property (text resource), 
         object with "localAt" property (URL resource).
 */
org_myurc_urchttp_toPathValues = function(valuesDoc) {
   var pathValues = [];
   if (!valuesDoc)
      return pathValues;
      
   var opNodes;
   if (valuesDoc.documentElement) {
      opNodes = valuesDoc.documentElement.childNodes;   
   }
   else
      throw new Error("Invalid document received from UCH");
	for (var i = 0; i < opNodes.length; i++) {
	   var opNode = opNodes[i];
	   if (opNode.nodeType != "1")      // Not an element node?
	      continue;
	   
	   var pathValue = {};
	   pathValue.operation = opNode.tagName;
	   pathValue.path = org_myurc_urchttp_decodeValue(opNode.getAttribute("ref"));
	   pathValue.value = org_myurc_urchttp_decodeValue(org_myurc_lib_getTextContent(opNode));
	   var resourceNodes = opNode.getElementsByTagName("resource");
	   var resources = [];
	   for (var j = 0; j < resourceNodes.length; j++) {
	      var resourceNode = resourceNodes[i];
	      var localAt = org_myurc_urchttp_decodeValue(resourceNode.getAttribute("localAt"));
	      var textContent = org_myurc_urchttp_decodeValue(org_myurc_lib_getTextContent(resourceNode));
	      if (localAt !== null)
	         resources.push( { "localAt": localAt } );    // Create an object with a "localAt" property.
	      else if (textContent)
	         resources.push( { "string": textContent } );
	      else     // empty resource
	         resources.push(null);
	   }
 	   pathValue.resources = resources;
	   pathValues.push(pathValue);
	}

	return pathValues;
};


/******************************************************
org_myurc_urchttp_toIndexValues: convert an index values doc from the server into an array of index values.  Preserve the order of elements.
   indexDoc: Document with index values as in a getValues (with <getIndex> element) response from the server.
   return: Array of index value objects with the following properties:
      value: index value 
      resources: Resource array with either one of: 
         null (empty resource), 
         object with "string" property (text resource), 
         object with "localAt" property (URL resource).
 */
org_myurc_urchttp_toIndexValues = function(indexDoc) {
   if (!indexDoc)
      return [];

   var indices = [];
   if (!indexDoc.documentElement || 
         indexDoc.documentElement.childNodes.length != 1 ||       // One <index> node expected as response.
         indexDoc.documentElement.firstChild.nodeType != 1 ||     // ... with one subelement
         indexDoc.documentElement.firstChild.tagName != "index")  // ... which is <index>
      throw new Error("Invalid response received from UCH for GetValues with index request");
      
   var indexValueNodes = indexDoc.documentElement.firstChild.childNodes;    // All <indexValue> nodes under <index>
	for (var i = 0; i < indexValueNodes.length; i++) {
	   var indexValueNode = indexValueNodes[i];
	   if (indexValueNode.nodeType != "1" || indexValueNode.tagName != "indexValue")      
	      // Not an element node with name "indexValue"?
	      continue;
	   
	   var index = {};
	   index.value = org_myurc_urchttp_decodeValue(org_myurc_lib_getTextContent(indexValueNode));
	   var resourceNodes = indexValueNode.getElementsByTagName("resource");
	   var resources = [];
	   for (var j = 0; j < resourceNodes.length; j++) {
	      var resourceNode = resourceNodes[i];
	      var localAt = org_myurc_urchttp_decodeValue(resourceNode.getAttribute("localAt"));
	      var textContent = org_myurc_urchttp_decodeValue(org_myurc_lib_getTextContent(resourceNode));
	      if (localAt !== null)
	         resources.push( { "localAt": localAt } );    // Create an object with a "localAt" property.
	      else if (textContent)
	         resources.push( { "string": textContent } );
	      else     // empty resource
	         resources.push(null);
	   }
	   index.resources = resources;
	   indices.push(index);
	}

	return indices;
};


/********************************************************
org_myurc_urchttp_encodeValue: Encode a string to be sent as value via URC-HTTP.
   string: string to be encoded.
	return: encoded string
 */
org_myurc_urchttp_encodeValue = function(string) {
   if (string === null)
      throw new Error("Attempt to send null value to server");

   if (string === undefined)
      return "~";
   
// Do nothing else - the JavaScript XML parser does all this automatically.
//   string = string.replace(/\&/g, "&amp;");     // Must be first.
//   string = string.replace(/\</g, "&lt;");
//   string = string.replace(/\>/g, "&gt;");
//   string = string.replace(/\"/g, "&quot;");
//   string = string.replace(/\'/g, "&apos;");
   
   return string;
};


/********************************************************
org_myurc_urchttp_decodeValue: Decode a string that was received as value from URC-HTTP.
   string: encoded string
	return: decoded string
 */
org_myurc_urchttp_decodeValue = function(string) {
   if (string === undefined || string === null || string === "~")
      return undefined;

// Do nothing else - the JavaScript XML parser does all this automatically.
//   string = string.replace(/\&lt;/g, "<");
//   string = string.replace(/\&gt;/g, ">");
//   string = string.replace(/\&quot;/g, "\"");
//   string = string.replace(/\&apos;/g, "'");
//   string = string.replace(/\&amp;/g, "&");
   
   return string;
};


/********************************************************
org_myurc_urchttp_log: Add a log message to the urc-http logging area (marked by id="log").
   text: log message as string.
	return: -
 */
org_myurc_urchttp_log = function(text) {
	
   var node = document.getElementById("log_");
   if (node)
      node.innerHTML += "<pre>\n" + text.replace(/</g, "&lt;") + "\n</pre>\n";
};


/********************************************************
org_myurc_urchttp_setUpdateSignal: Make update signal visible or invisible.
   visible: true=update signal visible, false=update signal invisible.
	return: -
 */
org_myurc_urchttp_setUpdateSignal = function(visible) {	
	var node = document.getElementById("updateSignal_");
    if (node)
      node.setAttribute("class", visible ? "signalvisible" : "signalinvisible");
};

