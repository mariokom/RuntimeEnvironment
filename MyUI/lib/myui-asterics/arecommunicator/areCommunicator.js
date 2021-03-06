
//The base URI that ARE runs at
var _baseURI;

//A map holding the opened connection with ARE for SSE
var _eventSourceMap = new areUtils.map();

//delimiter used for encoding
var delimiter = "-";

//enumeration for server event types
var ServerEvents = {
		MODEL_STATE_CHANGED: "model_state_changed",
		MODEL_CHANGED: "model_changed",
		MODEL_EVENT: "model_event"
};

//set the base uri (usually where ARE runs at)
function setBaseURI(uri) {
	_baseURI = uri;
}

//encodes PathParametes
function encodeParam(text) {
	encoded = "";
	for (i=0; i<text.length; i++) {
		encoded += text.charCodeAt(i) + delimiter;
	}
	
	return encoded;
}


//replaces all occurrences of a 'oldString' with 'newString' in 'text'
function replaceAll(text, oldString, newString) {
	return text.split(oldString).join(newString);
}

/**********************
 *	Runtime resources 
 **********************/

function downloadDeployedModel(successCallback, errorCallback) {
	$.ajax({
		type: "GET",
		url: _baseURI + "runtime/model",
		datatype: "text/xml",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function uploadModel(successCallback, errorCallback, modelInXML) {
	
	if (modelInXML == "") return;
	
	$.ajax({
		type: "PUT",
		url: _baseURI + "runtime/model",
		contentType: "text/xml",									//content-type of the request
		data: modelInXML,	
		datatype: "text",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function autorun(successCallback, errorCallback, filepath) {
	
	if (filepath == "") return;
	
	$.ajax({
		type: "PUT",
		url: _baseURI + "runtime/model/autorun/" + encodeParam(filepath),
		datatype: "text",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function pauseModel(successCallback, errorCallback) {
	$.ajax({
		type: "PUT",
		url: _baseURI + "runtime/model/state/pause",
		datatype: "text",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function startModel(successCallback, errorCallback) {
	$.ajax({
		type: "PUT",
		url: _baseURI + "runtime/model/state/start",
		datatype: "text",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function stopModel(successCallback, errorCallback) {
	$.ajax({
		type: "PUT",
		url: _baseURI + "runtime/model/state/stop",
		datatype: "text",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function getModelState(successCallback, errorCallback) {
	$.ajax({
		type: "GET",
		url: _baseURI + "runtime/model/state",	
		datatype: "text",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function deployModelFromFile(successCallback, errorCallback, filepath) {
	
	if ( filepath == "") return;
	
	$.ajax({
		type: "PUT",
		url: _baseURI + "runtime/model/" + encodeParam(filepath),
		datatype: "text",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function getRuntimeComponentIds(successCallback, errorCallback) {
	$.ajax({
		type: "GET",
		url: _baseURI + "runtime/model/components/ids",
		datatype: "application/json",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					jsonString = jqXHR.responseText;
					successCallback(JSON.parse(jsonString), textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function getRuntimeComponentPropertyKeys(successCallback, errorCallback, componentId) {
	
	if ( componentId == "" ) return;
	
	$.ajax({
		type: "GET",
		url: _baseURI + "runtime/model/components/"+ encodeParam(componentId),
		datatype: "application/json",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					jsonString = jqXHR.responseText;
					successCallback(JSON.parse(jsonString), textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function getRuntimeComponentProperty(successCallback, errorCallback, componentId, componentKey) {
	
	if ( (componentId == "") || (componentKey == "")) return;
	
	$.ajax({
		type: "GET",
		url: _baseURI + "runtime/model/components/"+encodeParam(componentId)+"/"+encodeParam(componentKey),
		datatype: "text",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function setRuntimeComponentProperty(successCallback, errorCallback, componentId, componentKey, componentValue) {
	
	if ( (componentId == "") || (componentKey == "") || (componentValue == "") ) return;
	
	$.ajax({
		type: "PUT",
		url: _baseURI + "runtime/model/components/"+encodeParam(componentId)+"/"+encodeParam(componentKey),
		contentType: "text/plain",
		data: componentValue,
		datatype: "text",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}




/*************************************
 *	Storage/ARE-repository resources
 *************************************/

function downloadModelFromFile(successCallback, errorCallback, filepath) {
	
	if (filepath == "") return;
	
	$.ajax({
		type: "GET",
		url: _baseURI + "storage/models/" + encodeParam(filepath),
		datatype: "text/xml",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function storeModel(successCallback, errorCallback, filepath, modelInXML) {
	
	if ( (filepath == "") || (modelInXML == "")) return;
	
	$.ajax({
		type: "POST",
		url: _baseURI + "storage/models/" + encodeParam(filepath),
		contentType: "text/xml",									//content-type of the request
		data: modelInXML,	
		datatype: "text",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function deleteModelFromFile(successCallback, errorCallback, filepath) {
	
	if ( filepath == "" ) return;
	
	$.ajax({
		type: "DELETE",
		url: _baseURI + "storage/models/" + encodeParam(filepath),
		datatype: "text",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(jqXHR.responseText, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function listStoredModels(successCallback, errorCallback) {
	$.ajax({
		type: "GET",
		url: _baseURI + "storage/models/names",
		datatype: "application/json",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					jsonString = jqXHR.responseText;
					successCallback(JSON.parse(jsonString), textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function getComponentDescriptorsAsXml(successCallback, errorCallback) {
	$.ajax({
		type: "GET",
		url: _baseURI + "storage/components/descriptors/xml",
		datatype: "text/xml",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					successCallback(data, textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


function getComponentDescriptorsAsJSON(successCallback, errorCallback) {
	$.ajax({
		type: "GET",
		url: _baseURI + "storage/components/descriptors/json",
		datatype: "application/json",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					jsonObject = JSON.parse(jqXHR.responseText);
					successCallback(jsonObject[0], textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}


/**********************
 *	Other Functions
 **********************/

function getRestFunctions(successCallback, errorCallback) {
	$.ajax({
		type: "GET",
		url: _baseURI + "restfunctions",
		datatype: "application/json",
		crossDomain: true,
		success:
				function (data, textStatus, jqXHR){
					jsonString = jqXHR.responseText;
					successCallback(JSON.parse(jsonString), textStatus);
				},
		error: 
				function (jqXHR, textStatus, errorThrown) {
					errorCallback(errorThrown,jqXHR.responseText);
				}
	});
}




/**********************************
 *	Subscription to SSE events
 **********************************/

function subscribe(successCallback, errorCallback, eventsType) {
	
	// Browser does not support SSE
	if((typeof EventSource)==="undefined") { 
	   alert("SSE not supported by browser");
	   return;
	}

	var eventSource = _eventSourceMap.get(eventsType);
	if (eventSource != null) {
		eventSource.close();
	}
	eventSource = new EventSource(_baseURI + "events/subscribe"); // Connecting to SSE service
	_eventSourceMap.add(eventsType, eventSource);
	
	//adding listener for specific events
	eventSource.addEventListener(eventsType, function(e) {
		successCallback(e.data, 200);
	}, false);
	
	// After SSE handshake constructed
	eventSource.onopen = function (e) {
		console.log("Waiting message...");
	};

	// Error handler	
	eventSource.onerror = function (e) {
		switch(e.target.readyState) {
			case EventSource.CONNECTING:	
				errorCallback(400, 'reconnecting');
				break;
			case EventSource.CLOSED:		
				errorCallback(400, 'connectionLost');
				break;
			default:
				errorCallback(400, 'someErrorOccurred');
				console.log("Error occured");
		}
	};
}


function unsubscribe(eventsType) {
	var eventSource = _eventSourceMap.remove(eventsType);
	
	if (eventSource == null) {
		return false;
	}
	else {
		eventSource.close();
		return true;
	}
}

