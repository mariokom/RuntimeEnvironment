/*
 Copyright: Access Technologies Group, Germany, 2007-2009.  
 This software is licensed under the CC-GNU GPL.  See http://creativecommons.org/licenses/GPL/2.0/ for 
 human-readable Commons Deed, lawyer-readable legal code, and machine-readable digital code.

 ***********************************************************************************

 JavaScript library for generic webclients.  Defines constructor function and
   methods for Schema class.  
 
 Imports: 
   org_myurc_lib.js
   org_myurc_urchttp.js
   
 List of exported functions:
   org_myurc_schema_Schema: Constructor function for org_myurc_schema_Schema object.
   getEnumsForType: Get allowed static values for a local type
   getEltRefForType: Get the eltRef for a local type definition.  This is needed for retrieving
   getIdForType: Get the id for a local type definition

 Author: Gottfried Zimmermann, ATG
 Last modified: 2009-08-07

 **********************************************************************************/


/********************************************************
org_myurc_schema_Schema: Constructor function for Schema object.
   targetNs: URI of the schema's target namespace / "" if SD-internal schema.
   socketName: URI of socket / null if external schema
   docLocation: Retrievable URI for the SD/schema file.
   doc: Document object for schema file or SD file / null if not retrieved yet
	return: new created Schema object, with the following properties:
	   targetNs: URI of target namespace / "" for local namespace
	   docLocation: URL for retrieving the document
	   doc: Document object for SD or XSD file containing <schema> element
	   schemaNode: <schema> node / null if no <schema> node available.
	   typeNodeTable: Map object: 'name' attribute -> <simpleType> or <complexType> node.
	   enumsTable: Cache object of enums for types.
 ********************************************************/
org_myurc_schema_Schema = function(targetNs, socketName, docLocation, doc) {
   this.targetNs = targetNs;           // May be empty.
   this.socketName = socketName;       // May be null.
   this.docLocation = docLocation;   
   this.doc = doc;

   var schemaNodes = doc.getElementsByTagName("schema");    
      // Note: There must be no namespace prefix attached to <schema> for IE to work correctly.  (IE does not
      // support the getElementsByTagNameNS() method.)
   if (schemaNodes.length === 0) 
      this.schemaNode = null;         // Must be an SD - no <schema> element contained.
   else if (schemaNodes.length > 1)
      throw new Error("More than one &lt;schema&gt; element found in " + this.docLocation);
   else
      this.schemaNode = schemaNodes[0];
   
   this.typeNodeTable = this.getTypeNodeTable();
   this.enumsTable = {};      // Create empty cache for enums.

   // No return statement since constructor function.
};


/********************************************************
Method getEnumsForType: Get allowed static values for a local type
   name: local name for a type, as defined by 'name' attribute on <simpleType> element.
	return: Array with enumerated values of the specified type / 
	   null if there is no type definition / empty array if the type does not specify enumerated values
 ********************************************************/
org_myurc_schema_Schema.prototype.getEnumsForType = function(name) {

   if (this.enumsTable[name] === undefined) {     // Not yet cached?
      var typeNode = this.typeNodeTable[name];    // May be undefined.
      
      if (!typeNode || typeNode.tagName != "simpleType")    // No such type node or not <simpleType>?
         this.enumsTable[name] = null;                      // Sorry, no enums available.
      else {   
         var enums = [];
         // TODO: Do a better job of parsing the SD (catch invalid syntax, etc.).
         var enumNodes = typeNode.getElementsByTagName("enumeration");     // Just look for <enumeration> descendants.
         for (var i = 0; i < enumNodes.length; i++) {
            var value = enumNodes[i].getAttribute("value");
            if (value !== null)
               enums.push(value);
         }
         this.enumsTable[name] = enums;
      }
   }
   
   return this.enumsTable[name];
};


/********************************************************
Method getEltRefForType: Get the eltRef for a local type definition.  This is needed for retrieving
      resources pertaining to the type.
   name: local type name, as specified in the 'name' attribute
	return: eltRef of the type definition / 
	   null if there is no matching type definition or it has no 'name' is undefined.
 ********************************************************/
org_myurc_schema_Schema.prototype.getEltRefForType = function(name) {
   if (this.socketName) {       // This is for an SD-internal schema.
      //var id = this.getIdForType(name);
      return name ?          
         this.socketName + "#" + name :     // Format as defined in ISO/IEC 24752-5.
         null;                            // No id available.
   }
   else                         // This is for an external type.  
      return this.targetNamespace + name;    
         // Format: namespaceUri + typeName.
         // This is similar to QName, as defined at http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#QName.
         // Note: This is an extension of ISO/IEC 24752-5 in the spirit of XSD.
};


/********************************************************
Method getIdForType: Get the id for a local type definition
   name: name of a type, as defined in the socket description
	return: id of the type definition / 
	   null if there is no matching type definition or it has no 'id' attribute
 ********************************************************/
org_myurc_schema_Schema.prototype.getIdForType = function(name) {
   var node = this.typeNodeTable[name];
   if (!node)      // No type definition available?
      return null;
   var id = node.getAttribute("id");
   if (id === null)     // No id attribute defined?
      throw new Error("Document " + this.docLocation + " does not have an id attribute for type " + name);
      
   return id;
};


/* ------------------------- Convenience functions below ----------------------- */


/********************************************************
Method getTypeNodeTable: Parse the document and record all type nodes with a name in a map object.
	return: Map object with:
	   the indexes being the names of the types, and
	   the values being the corresponding type nodes.
 ********************************************************/
org_myurc_schema_Schema.prototype.getTypeNodeTable = function() {

   var table = {};      // Start with empty map object.
   
   if (this.schemaNode) {     // Is there any <schema> node?
      var simpleTypeNodes = this.schemaNode.getElementsByTagName("simpleType");    // Namespace neglected.
      for (var i = 0; i < simpleTypeNodes.length; i++) {
         var name = simpleTypeNodes[i].getAttribute("name");
         if (name !== null)
            table[name] = simpleTypeNodes[i];
      }

      var complexTypeNodes = this.schemaNode.getElementsByTagName("complexType");    // Namespace neglected.
      for (var i = 0; i < complexTypeNodes.length; i++) {
         var name = complexTypeNodes[i].getAttribute("name");
         if (name !== null)
            table[name] = complexTypeNodes[i];
      }
   }
   
   return table;
};

