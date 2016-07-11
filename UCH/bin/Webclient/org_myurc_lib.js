/*
 Copyright: Access Technologies Group, Germany, 2007-2009.  
 This software is licensed under the CC-GNU GPL.  See http://creativecommons.org/licenses/GPL/2.0/ for 
 human-readable Commons Deed, lawyer-readable legal code, and machine-readable digital code.

 ***********************************************************************************

 JavaScript library for utility functions.
 
 The following restrictions apply:
 
 Current limitations:
 
 Imports (make sure these files are read before execution of any code in org_myurc_autoindex.js): 
      
 List of exported functions:
   org_myurc_lib_toXmlString
   org_myurc_lib_createEmptyDoc
   org_myurc_lib_toXmlDoc
   org_myurc_lib_selectChildren
   org_myurc_lib_getTextContent
   org_myurc_lib_setTextContent
   org_myurc_lib_getArgs
   org_myurc_lib_getUrlComponents
   org_myurc_lib_splitCurie
   
 Author: Gottfried Zimmermann, ATG
 Last modified: 2009-08-07

 **********************************************************************************/

/** TODO
*/


org_myurc_lib_toXmlString = function(doc) {
	var xmlString;
   
    if (!doc || typeof doc == "string")   
	   return doc;                         // doc is already a string
	else if (doc.documentElement.xml)
		return doc.documentElement.xml;		// IE
	else
		return (new XMLSerializer()).serializeToString(doc);
}

org_myurc_lib_createEmptyDoc = function(rootNodeName) {
   if (document.implementation && document.implementation.createDocument) {    // Mozilla, Firefox, and related browsers
      return document.implementation.createDocument(null, rootNodeName, null);
   }
   else if (typeof ActiveXObject != "undefined") {    // Internet Explorer
   	var doc = new ActiveXObject("MSXML2.DOMDocument");
   	doc.documentElement = doc.createElement(rootNodeName);
   	return doc;
   }
   else {
      new Error("Cannot create a document object on this platform");
      return null;
   }
};

org_myurc_lib_toXmlDoc = function(text) {
   if (typeof text !== "string")     // Already a document.
      return text;

   if (typeof DOMParser != "undefined") {    // Mozilla, Firefox, and related browsers
      return (new DOMParser()).parseFromString(text, "text/xml");
   }
   else if (typeof ActiveXObject != "undefined") {    // Internet Explorer
      var doc = new ActiveXObject("MSXML2.DOMDocument");
      doc.loadXML(text);
      return doc;
   }
   else {
      new Error("Cannot parse document on this platform");
      return null;
   }
};


org_myurc_lib_selectChildren = function(node, name) {
   var set = [];
   for (var i = 0; i < node.childNodes.length; i++) {
      var child = node.childNodes[i];
      if (child.nodeType === 1 &&   // Element
         child.nodeName == name)
         set.push(child);
   }
   return set;
};


org_myurc_lib_getTextContent = function(node) {
   if (node.innerHTML !== undefined)
      return node.innerHTML;
   else if (node.text)  // IE
      return node.text === undefined ? "" : node.text; 
   else 
      return node.textContent === undefined ? "" : node.textContent;    // FFs
};

org_myurc_lib_setTextContent = function(node, value) {
   if (typeof ActiveXObject != "undefined") { // IE
      if (node.className != undefined)     // HTMLElement
	      node.innerHTML = value;            
	   else
	      node.text = value;
	}
	else
	   node.textContent = value;   // FF
};


org_myurc_lib_getArgs = function(url) {
   var firstQuestionMark = url.indexOf("?");
   if (firstQuestionMark === -1)    // No '?' included in url?
      return {};

   var url_args = new Object();
   var query = url.substring(firstQuestionMark + 1);    // Take string after '?'
   var pairs = query.split("&");
   
   for (var i=0; i<pairs.length; i++) {
      var pos = pairs[i].indexOf("=");
      if (pos == -1) continue;      // if not found, skip.
      var argname = pairs[i].substring(0, pos);    // Extract name
      var value = decodeURIComponent(pairs[i].substring(pos+1));    // Extract value
      url_args[argname] = value;
   }
   return url_args;
};


org_myurc_lib_getUrlComponents = function(url) {
   var urlComps = url.match( /(\w+:)\/\/([^\/]+)\/(\S*)/ );
   return urlComps ?     // Matched?
      { "protocol": urlComps[1], "host": urlComps[2], "path": urlComps[3] } :
      null;
};


org_myurc_lib_splitCurie = function(curie) {
   var firstColon = curie.indexOf(":");
   return (firstColon < 0) ?
      { "prefix": "", "local": curie } :      // No prefix included.
      { "prefix": curie.slice(0, firstColon), "local": curie.slice(firstColon + 1) };     // Prefix was included.
};



function org_myurc_lib_RTrimString(value)
{
 for(var i=value.length-1 ; i>0 ; i--){
  if( !(value.charAt(i) == ' '))
   break;
 }
 value = value.substr(0,i+1); 
 return value;
}


//to left trim the string value
function org_myurc_lib_LTrimString(value)
{
 for(var i=0 ; i<value.length-1 ; i++){
  if( !(value.charAt(i) == ' '))
   break;
 }
 value = value.substr(i); 
 return value;
}


//to trim the string value

function org_myurc_lib_trimString(value)
{ 
 return org_myurc_lib_LTrimString(org_myurc_lib_RTrimString(value));
}
