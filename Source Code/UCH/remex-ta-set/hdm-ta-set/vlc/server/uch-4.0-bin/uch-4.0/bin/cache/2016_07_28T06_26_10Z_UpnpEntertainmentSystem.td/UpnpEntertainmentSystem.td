<?xml version="1.0" encoding="UTF-8"?>
<!-- 
Target Description for UPnP Entertainment System 
Gottfried Zimmermann
-->
<target about="http://res.openurc.org/upnp/av" 
	id="target" 
	hidden="false" 
	xmlns="http://openurc.org/ns/targetdesc-2" 
	xmlns:dc="http://purl.org/dc/elements/1.1/" 
	xmlns:dcterms="http://purl.org/dc/terms/" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://openurc.org/ns/targetdesc http://openurc.org/ns/targetdesc 
		http://purl.org/dc/elements/1.1/ http://dublincore.org/schemas/xmls/qdc/2003/04/02/dc.xsd 
		http://purl.org/dc/terms/ http://dublincore.org/schemas/xmls/qdc/2003/04/02/dcterms.xsd">
	<dcterms:conformsTo>http://openurc.org/ns/targetdesc-2/isoiec24752-4-2013</dcterms:conformsTo>
	<dcterms:created>2007-08-14</dcterms:created>
	<dcterms:modified>2008-10-23</dcterms:modified>
	<dc:creator>Gottfried Zimmermann, Trace Center</dc:creator>
	
	<socket id="level1socket" name="http://res.openurc.org/upnp/av/play-1.uis" hidden="false">
		<retrieveFrom>play-1.uis</retrieveFrom>
	</socket>
	
	<socket id="level2socket" name="http://res.openurc.org/upnp/av/play-2.uis" hidden="false">
		<retrieveFrom>play-2.uis</retrieveFrom>
	</socket>
</target>
