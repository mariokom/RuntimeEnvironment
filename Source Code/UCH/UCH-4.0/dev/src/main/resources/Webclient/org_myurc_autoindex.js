/*
 Copyright: Access Technologies Group, Germany, 2007-2009.  
 This software is licensed under the CC-GNU GPL.  See http://creativecommons.org/licenses/GPL/2.0/ for 
 human-readable Commons Deed, lawyer-readable legal code, and machine-readable digital code.

 ***********************************************************************************

 JavaScript library for webpages building a automatic index of available sockets, based on org_myurc_urchttp lib.
 
 The following restrictions apply:
 
 Current limitations:
 
 Imports (make sure these files are read before execution of any code in org_myurc_autoindex.js): 
   org_myurc_urchttp.js
      
 List of exported functions:
   org_myurc_autoindex_buildSocketList
   org_myurc_autoindex_getSocketLabel: Get the label for a socket.
   
 Author: Gottfried Zimmermann, ATG
 Last modified: 2009-08-07

 **********************************************************************************/

/** TODO
*/

/**
  Declaration of variables
 */  
org_myurc_autoindex_forLang = document.documentElement.lang;      // Get language from <html> tag.


/********************************************************
   org_myurc_autoindex_buildSocketList: Build the list of sockets (controllers).
      anchorNode: node in the HTML document to append the <ul> node with list of socket names.
	   return: -
 ********************************************************/
org_myurc_autoindex_buildSocketList = function(anchorNode) {
	var ulNode = document.createElement("ul");
   var sockets = org_myurc_urchttp_getAvailableSockets();
   for (var i=0; i<sockets.length; i++) {     
      var liNode = document.createElement("li");
      var aNode = document.createElement("a");
         
      if( sockets[i].uipmClientName == undefined) {
     		var socketLabel = sockets[i].socketFriendlyName ? sockets[i].socketFriendlyName : org_myurc_autoindex_getSocketLabel(sockets[i].socketName);
         var href = "controller.html?socket=" + encodeURIComponent(sockets[i].socketName) + 
               (sockets[i].targetId ? "&targetId=" + encodeURIComponent(sockets[i].targetId) : "");
  		   aNode.setAttribute("href", href);
         }
      else {
	      var socketLabel = sockets[i].socketFriendlyName ? sockets[i].socketFriendlyName : org_myurc_autoindex_getSocketLabel(sockets[i].uipmClientName);
         var href=sockets[i].protocolUrl;
	      aNode.setAttribute("href", href);
         }
         
      aNode.innerHTML = sockets[i].targetLabel + ": " + socketLabel;
  
      liNode.appendChild(aNode);
      ulNode.appendChild(liNode);
      anchorNode.appendChild(ulNode);

      // Use socket friendly name if available in UIList, otherwise label resource for socket.
     
   }
};


/********************************************************
   org_myurc_autoindex_getSocketLabel: Get the label for a socket.
      socketName: Name (URI) of the socket.
	   return: label / "{socketName}" if no resource available.
 ********************************************************/
org_myurc_autoindex_getSocketLabel = function(socketName) {

   var rq = {
      "eltRef": socketName,
      "role": "http://openurc.org/ns/res#label",
      "type": "Text"
   };
   if (org_myurc_autoindex_forLang)    // 'lang' attribute defined on HTML page?
      rq.forLang = org_myurc_autoindex_forLang;
      
   var resources = org_myurc_urchttp_getResources([ rq ]);
   if (resources.length !== 1)
      throw new Error("Invalid number of resources in getResources response: " + resources.length);      
   var resource = resources[0];
   return (resource && resource.string) ?
      resource.string : "{" + socketName + "}";
};


