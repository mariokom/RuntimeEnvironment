/*
 Copyright: Access Technologies Group, Germany, 2007-2009.  
 This software is licensed under the CC-GNU GPL.  See http://creativecommons.org/licenses/GPL/2.0/ for 
 human-readable Commons Deed, lawyer-readable legal code, and machine-readable digital code.

 ***********************************************************************************

 JavaScript library for generic webclients.  Defines constructor function and
   methods for Socket class.  
 
 Imports: 
   org_myurc_lib.js
   org_myurc_urchttp.js
   
 List of exported functions:
   org_myurc_socket_Socket: Constructor function for org_myurc_socket_Socket object.
   getEltType: Get tag name of an element with a given id
   getType: Get the type of an element with a given id
   getParentId: Get the id of the parent of a given id
   hasClosedSelection: Find out whether a socket element has a closed selection
   getSelectionSets: Get selection sets of a socket element.
   getEnumsForType: Get allowed static values for a type
   getIdForType: Get the id for a type definition
   getRootId: Get the id of the uiSocket element
   getDimTypes: Get the dimension types for a given element
   getPathForId: Get the absolute path string for an id in a socket.
   getLocalParameterIds: Get the ids the local parameters for a given command.
   getNode: Get the node for a given id.

 Author: Gottfried Zimmermann, ATG
 Last modified: 2009-08-07

 **********************************************************************************/


/********************************************************
org_myurc_socket_Socket: Constructor function for org_myurc_socket_Socket object.
   socketName: socket name (URI)
	socketDescriptionUrl: URL to retrieve socket description (if known).
	targetDescriptionUrl: URL to retrieve target description (if known).
	return: new created Socket object.
	Note: At least one of socketName or socketDescriptionUrl must be given.
 ********************************************************/
org_myurc_socket_Socket = function(socketName, socketDescriptionUrl, targetDescriptionUrl) {
   this.socketName = socketName;
   this.socketDescriptionUrl = socketDescriptionUrl;   
   this.targetDescriptionUrl = targetDescriptionUrl;
   this.doc = null;
   this.uiSocketId = null;
   this.idTable = null;
   this.selectionNodeTable = {};    // Cache for selection nodes.
   this.selectionSetsTable = {};    // Cache for selection sets.
   this.enumsTable = {};            // Cache for enumerations for types, including those externally defined.
   this.nsUriTable = {};            // Cache for namespace URIs needed to resolve external types.
   this.schemaTable = {};        // Cache for external schema objects needed to resolve external types.
   // No return statement since constructor function.
};


/********************************************************
Method getEltType: Get tag name of an element with a given id
   id: id of socket element, not including indices.
	return: Tag name of specified element (not including any namespace prefixes)
 ********************************************************/
org_myurc_socket_Socket.prototype.getEltType = function(id) {
   var element = this.getNode(id);
   switch (element.tagName) {
      case "uiSocket":
         return "uiSocket";
      case "set":
         return "set";
      case "variable": 
         return "variable";
      case "constant": 
         return "constant";
      case "command": 
         return "command";
      case "param":
         var dir = element.getAttribute("dir");
         if (dir == "in")
            return "inpar";
         else if (dir == "inout")
            return "inoutpar";
         else if (dir == "out")
            return "outpar";
      case "notify":
         return "notify";
      default:
         throw new Error("Unknown tag name " + element.tagName + " for id " + id);
   }
};


/********************************************************
Method getType: Get the type of an element with a given id
   id: id of socket element, not including indices.
	return: value of 'type' attribute (including any namespace prefix)
 ********************************************************/
org_myurc_socket_Socket.prototype.getType = function(id) {
   var element = this.getNode(id);
   var type;
   
   switch (this.getEltType(id)) {
      case "command":
         type = element.getAttribute("type");
         return type ? type : "uis:voidCommand";    // Default for command type is uis:voidCommand.
      case "notify":
         type = element.getAttribute("type");
         return type ? type : "show";
        // return type ? type : "info"; // Parikshit : 20111005 . changed from info to show.     
      default:
         type = element.getAttribute("type");
         if (type)
            return type;
         else
            throw new Error("Cannot determine type for " + id);
   }
};



/********************************************************
Method getNotifyCategory: Get the Category of a notification with a given id
   id: id of socket element, not including indices.
	return: value of 'Category' attribute 
 ********************************************************/
org_myurc_socket_Socket.prototype.getNotifyCategory = function(id) {
	var element = this.getNode(id);
	var category;
	if(this.getEltType(id) == "notify"){
		category = element.getAttribute("category");
		return category ? category : "info"; // Parikshit : 20111005. changed from show to info.
	}
	else{
		return null;
	}
};


/**
 * Method getCustomNotifVariable: Get the variables in 'custom' type notification. 
 * id: id of element.
 * return: array containing id and type of variable.
 */
org_myurc_socket_Socket.prototype.getCustomNotifVariable = function(id) {
	var element = this.getNode(id);
	var variableNodes = element.getElementsByTagName("variable");
	var customVariables = [];
	
	for(var i=0; i<variableNodes.length; i++){
		
		customVariables.push({ "type": variableNodes[i].getAttribute("type"), "id": variableNodes[i].getAttribute("id") });
	}
	
	return customVariables;
};

/**
 * Method getCustomNotifCommands: Get the commands in 'custom' type notification. 
 * id: id of element.
 * return: array containing id and type of command.
 */
org_myurc_socket_Socket.prototype.getCustomNotifCommands = function(id){
	var element = this.getNode(id);
	var commandNodes = element.getElementsByTagName("command");
	var customCommands = [];
	
	for(var i=0; i<commandNodes.length; i++){
		
		customCommands.push({ "type": commandNodes[i].getAttribute("type"), "id": commandNodes[i].getAttribute("id") });
	}
	
	return customCommands;
};
/********************************************************
Method getParentId: Get the id of the parent of a given id
   id: id of socket element, not including indices.
	return: id of parent
 ********************************************************/
org_myurc_socket_Socket.prototype.getParentId = function(id) {
   var element = this.getNode(id);
   var parent = element.parentNode;
   var parentId = parent.getAttribute("id");
   if (parentId === null)
      throw new Error("Parent element of " + id + " does not have an id attribute");   
   return parentId;
};


/********************************************************
Method hasClosedSelection: Find out whether a socket element has a closed selection
   elementId: id of an element
	return: true if the element has a closed selection specified / false otherwise
 ********************************************************/
org_myurc_socket_Socket.prototype.hasClosedSelection = function(elementId) {
   var selectionNode = this.getSelectionNode(elementId);
   return (selectionNode !== null &&
      selectionNode.getAttribute("closed") != "false");     // Default attribute value is "true".
};


/********************************************************
Method getSelectionSets: Get selection sets of a socket element.
   elementId: id of an element
	return: Array with selection sets / null if there is no selection defined for elementId.
	   The array consists of objects of the following forms:
         static selection - {typeRef: name of pertinent type, id: id of selection set} 
         dynamic selection - {varRef: id of pertinent element, id: id of selection set}
 ********************************************************/
org_myurc_socket_Socket.prototype.getSelectionSets = function(elementId) {
   if (this.selectionSetsTable[elementId] === undefined) {      // Not yet cached?
      var selectionNode = this.getSelectionNode(elementId);
      if (selectionNode === null)      // No selection node available?
         return null;
         
      var selectionSets = [];
      for (var i = 0; i < selectionNode.childNodes.length; i++) {
         var node = selectionNode.childNodes[i];
         if (node.nodeType == 1) {    // Element node?
            if (node.nodeName == "selectionSetStatic")
               selectionSets.push({ "typeRef": node.getAttribute("typeRef"), "id": node.getAttribute("id") }); 
            else if (node.nodeName == "selectionSetDynamic")          
               selectionSets.push({ "varRef": node.getAttribute("varRef"), "id": node.getAttribute("id") }); 
         }
      }
      this.selectionSetsTable[elementId] = selectionSets;      // Cache for later use.
   }   
   
   return this.selectionSetsTable[elementId]; 
};


/********************************************************
Method getEnumsForType: Get allowed static values for a type name in the CURIE format.
   typeName: name of a type in the CURIE format, as specified in the socket description
	return: Array with enumerated values of the specified type / 
	   null if there is no type definition / empty array if the type does not specify enumerated values
 ********************************************************/
org_myurc_socket_Socket.prototype.getEnumsForType = function(typeName) {

   var curie = org_myurc_lib_splitCurie(typeName);
   if (curie.prefix == "xsd" || curie.prefix == "uis")
      return null;      // Cannot retrieve enumeration for xsd data type.
      
   var nsUri = this.getNsUriForPrefix(curie.prefix);     // curie.prefix may be "", then nsUri will be "" too.
   var schema = this.getSchemaForNsUri(nsUri);           // May be null if not available.
   
   return schema ? schema.getEnumsForType(curie.local) : null;
};


/********************************************************
Method getEltRefForType: Get the eltRef for a local or external type definition.  This is needed for retrieving
      resources pertaining to the type.
   typeName: type name in the CURIE format, as specified in the socket description
	return: eltRef of the type definition / 
	   null if there is no matching type definition or it has no 'id' attribute
 ********************************************************/
org_myurc_socket_Socket.prototype.getEltRefForType = function(typeName) {

   var curie = org_myurc_lib_splitCurie(typeName);
   if (curie.prefix == "xsd" || curie.prefix == "uis")
      return null;      // Cannot retrieve eltRef for xsd data type.
      
   var nsUri = this.getNsUriForPrefix(curie.prefix);     // curie.prefix may be "", then nsUri will be "" too.
   var schema = this.getSchemaForNsUri(nsUri);           // May be null if not available.
   
   return schema ? schema.getEltRefForType(curie.local) : null;
};


/********************************************************
Method getRootId: Get the id of the uiSocket element
	return: id of uiSocket element
 ********************************************************/
org_myurc_socket_Socket.prototype.getRootId = function() {
   if (this.uiSocketId)       // Cached?
      return this.uiSocketId;
      
   var uiSocketElements = this.getDoc().getElementsByTagName("uiSocket");
   if (uiSocketElements.length !== 1)
      throw new Error("Socket Description does not have exactly one &lt;uiSocket&gt; element");
   var id = uiSocketElements[0].getAttribute("id");
   if (id)
      return id;
   else 
      throw new Error("&lt;uiSocket&gt; element does not have an id attribute");   
};


/********************************************************
Method getDimTypes: Get the dimension types for a given element
   id: id of socket element, not including indices.
	return: dimension types as array (empty if non-dimensional)
 ********************************************************/
org_myurc_socket_Socket.prototype.getDimTypes = function(id) {
   var element = this.getNode(id);
   var dimString = element.getAttribute("dim");
   return dimString ? dimString.split(" ") : [];      
      // TODO: Test with other whitespaces.
};


/********************************************************
Method getPathForId: Get the absolute path string for an id in a socket.
   id: id of socket element, not including indices.
   Note: If element has a dimensional ancestor, the resulting path will not include
      any indices for that ancestor.  Relevant indices should be inserted later.
      However, the indices of the leaf element will be contained in the result.
	return: path string
	exception: Throws exception if invalid id syntax.
 ********************************************************/
org_myurc_socket_Socket.prototype.getPathForId = function(id) {
   if (id.indexOf("/") != -1)    // id contains slash?
      throw new Error("Invalid id " + id + " (contains slash)");
   if (id.indexOf("[") != -1)    // id contains bracket?
      throw new Error("Invalid id " + id + " (contains index)");
      
   var path = "";
   for (var node = this.getNode(id); node !== null && node.tagName != "uiSocket"; node = node.parentNode) {
      var nodeId = node.getAttribute("id");
      path = "/" + nodeId + path;
   }
   return path;
};


/********************************************************
Method getLocalParameterIds: Get the ids the local parameters for a given command.
   id: id of a command.
	return: New object with the following properties:
	   in: Array with ids of input parameters
	   inout: Array with ids of input-output parameters
	   out: Array with ids of output parameters
 ********************************************************/
org_myurc_socket_Socket.prototype.getLocalParameterIds = function(id) {
   var node = this.getNode(id);
   var params = {};
   params["in"] = [];            // Cannot write params.in since in is a keyword.
   params.inout = [];
   params.out = [];
   if (node === null || this.getEltType(id) != "command")
      throw new Error("Element " + id + " is not a command");
   for (var i = 0; i < node.childNodes.length; i++) {
      var childNode = node.childNodes[i];
      if (childNode.nodeType == 1 &&  // Element?
         childNode.tagName == "param" &&
         childNode.getAttribute("dir") &&
         childNode.getAttribute("id"))
         params[childNode.getAttribute("dir")].push(childNode.getAttribute("id"));
   }
   return params;
};


/********************************************************
Method getNode: Get the node for a given id.
   id: id of an element in the socket description.
	return: Node with the given id.
 ********************************************************/
org_myurc_socket_Socket.prototype.getNode = function(id) {
   var node = this.getIdTable()[id];     // Look up in cache.
   if (!node)
      throw new Error("Socket Description does not have an element with id=" + id);
   return node;
};


/* ------------------------- Convenience functions below ----------------------- */


/********************************************************
Method getIdTable: Get the cache table for ids, create if not existing yet.
	return: id table
 ********************************************************/
org_myurc_socket_Socket.prototype.getIdTable = function() {
   if (this.idTable === null) {
      this.idTable = {};
      this.collectIds(this.getDoc().documentElement, this.idTable);      // Start with root element
   }
   return this.idTable;
};

 
/********************************************************
Method collectIds: Convenience function for caching ids in an id table.
   node: node in XML document - collect ids from it and its children
   idTable: Object for storing ids.
	return: id table
 ********************************************************/
org_myurc_socket_Socket.prototype.collectIds = function(node, idTable) {  
   if (node.nodeType === 1) {          // Node is an element?
      if (node.getAttribute("id"))     // Node has id attribute?
         idTable[node.getAttribute("id")] = node;
      for (var i = 0; i < node.childNodes.length; i++)
         this.collectIds(node.childNodes[i], idTable);
   }
   return idTable;
};


/********************************************************
Method getDoc: Get the socket description document.
	return: SD document
 ********************************************************/
org_myurc_socket_Socket.prototype.getDoc = function() {
   if (!this.doc) {     // Not cached yet?      
      if (this.socketDescriptionUrl) {    // URL provided?
         this.doc = org_myurc_urchttp_getDocument(this.socketDescriptionUrl);  // May be null.
         if (this.doc === null)
            throw new Error("Could not retrieve socket description from " + this.socketDescriptionUrl);
      }
      else
         throw new Error("Unknown URL for socket description " + this.socketName);      
   }

   return this.doc;
};


/********************************************************
Method getSelectionNode: Get the <selection> subelement of a socket element
   elementId: id of an element
	return: <selection> node if existing / null otherwise
 ********************************************************/
org_myurc_socket_Socket.prototype.getSelectionNode = function(elementId) {
   if (this.selectionNodeTable[elementId] === undefined) {      // Not yet cached?
      var element = this.getNode(elementId);
      var selectionNodes = element.getElementsByTagName("selection");
      if (selectionNodes.length === 0) {    // Element has no <selection> subelement?
         this.selectionNodeTable[elementId] = null;    // Cache it for later.
         return null;
      }
      if (selectionNodes.length > 1)
         throw new Error("Invalid socket description - element " + elementId + " has more than one &lt;selection&gt; subelement: " + 
            this.socketDescriptionUrl);
      this.selectionNodeTable[elementId] = selectionNodes[0];    // Cache it for later.
   }
   
   return this.selectionNodeTable[elementId];
};


/********************************************************
Method getNsUriForPrefix: Get the namespace URI for a given prefix.
   prefix: namespace prefix, as used in a QName / "" for local name
	return: namespace URI for the prefix / "" for local URI.
	Exception: if no namespace URI specified for the prefix.
 ********************************************************/
org_myurc_socket_Socket.prototype.getNsUriForPrefix = function(prefix) {

   if (this.nsUriTable[prefix] === undefined) {    // Not already cached?
      if (prefix === "")     // Local name?
         this.nsUriTable[prefix] = "";
      else {
         var rootNode = this.getDoc().documentElement;
         // TODO: Look for namespace declarations along the ancestor path, not only in <uiSocket> root element.  Warning: Caching will be wrong then!
         if (rootNode.tagName != "uiSocket")
            throw new Error("Root element is not &lt;uiSocket&gt; in SD " + socketName);
         
         this.nsUriTable[prefix] = rootNode.getAttribute("xmlns:" + prefix);
         if (!this.nsUriTable[prefix])
            throw new Error("Root element &lt;uiSocket&gt; in SD " + socketName + " has no namespace declaration for prefix " + prefix);
      }
   }
   
   return this.nsUriTable[prefix];
};


/********************************************************
Method getSchemaForNsUri: Get the schema object for a given namespace URI.
   nsUri: namespace URI
	return: New Schema object / null if schema is not available.
	Exception: if no schema location given or schema file cannot be retrieved.
 ********************************************************/
org_myurc_socket_Socket.prototype.getSchemaForNsUri = function(nsUri) {
   
   if (this.schemaTable[nsUri] !== undefined)     // Cached already?
      return this.schemaTable[nsUri];
      
   else if (nsUri === "") {    // Local namespace, internal schema?
      this.schemaTable[""] = new org_myurc_schema_Schema("",         // Empty targetNamespace for internal schema.
         this.socketName, this.socketDescriptionUrl, this.getDoc());
      return this.schemaTable[nsUri];
   }

   else {         // External schema
      var rootNode = this.getDoc().documentElement;
      // TODO: Look for schema locations on elements other than root element.
      var schemaLocationString = rootNode.getAttribute("xsi:schemaLocation");
      if (!schemaLocationString)        // No attribute 'xsi:schemaLocation'.
         return null;
//         throw new Error("Root element &lt;uiSocket&gt; in SD " + socketName + " has no attribute 'xsi:schemaLocation'");
      var schemaLocations = schemaLocationString.split(" ");
      for (var i=0; i<schemaLocations.length; i+=2) {
         if (schemaLocations[i] == nsUri) {     // NS URI found?
            var schemaDoc = org_myurc_urchttp_getDocument(schemaLocations[i+1]); // May be null.
            if (schemaDoc === null) 
               return null;      // Could not retrieve schema file. Give up.
            this.schemaTable[nsUri] = new org_myurc_schema_Schema(nsUri, null,   // No socketName for external schema.
               schemaLocations[i+1],  // The next element has the schema location.
               schemaDoc);      
            return this.schemaTable[nsUri];
         }
      }
   }
   
   // If execution arrives here, no matching NS URI has been found.
   return null;
//   throw new Error("Schema location for namespace '" + nsUri + "' not specified on &lt;uiSocket&gt; element in SD " + this.socketName);
};