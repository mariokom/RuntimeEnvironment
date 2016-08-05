<?xml version="1.0" encoding="UTF-8"?>
<target about="http://res.openurc.org/devices/thermostat/targetdesc/template/lightWeightThermostat.td" 
    id="thermostattarget" 
    hidden="false" 
    xmlns="http://openurc.org/ns/targetdesc-2" 
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:dcterms="http://purl.org/dc/terms/" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xsi:schemaLocation="http://openurc.org/ns/targetdesc http://openurc.org/ns/targetdesc 
    http://purl.org/dc/elements/1.1/ http://dublincore.org/schemas/xmls/qdc/2003/04/02/dc.xsd 
    http://purl.org/dc/terms/ http://dublincore.org/schemas/xmls/qdc/2003/04/02/dcterms.xsd">
    
    <dc:description xml:lang="en">
        Target Description for Thermostat CT-80, connected via WI-FI.
    </dc:description>
    <dcterms:conformsTo>http://openurc.org/ns/targetdesc-2/isoiec24752-4-2013</dcterms:conformsTo>
    <dcterms:created>2010-09-17</dcterms:created>
    <dcterms:modified>2010-09-17</dcterms:modified>
    <dc:creator>Vishal Bhuva and Sandip Patel</dc:creator>
    <dc:contributor>Parikshit Thakur</dc:contributor>
    
    <socket id="thermostatSocket" name="http://res.openurc.org/devices/thermostat/uisocketdesc/template/lightWeightThermostat.uis" hidden="false"/>
    
	<resSheet about="http://res.openurc.org/devices/thermostat/ressheet/template/lightWeightThermostat.rsheet">
		<dcterms:conformsTo>http://openurc.org/ns/rsheet-2/isoiec24752-5-2013</dcterms:conformsTo>
		<retrieveFrom>http://res.openurc.org/devices/thermostat/ressheet/template/lightWeightThermostat.rsheet</retrieveFrom>
		<scents>
			<forLang>en</forLang>
			<dc:type>Text</dc:type>
			<dc:type>Image</dc:type>
			<forDomain>http://res.openurc.org/devices/thermostat/uisocketdesc/template/lightWeightThermostat.uis</forDomain> 
		</scents>
		
	</resSheet>    
</target>