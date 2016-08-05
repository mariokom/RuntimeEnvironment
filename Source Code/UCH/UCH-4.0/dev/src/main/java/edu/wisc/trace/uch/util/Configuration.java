package edu.wisc.trace.uch.util;


import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;





/**
 * 
 * @author Lukas Smirek
 * This file holds all Data needed to configure the UCH.
 * Attention: This class is only used in the launcher class at the moment. The UCH class reads the config file on its own
 */

@XmlRootElement
public class Configuration {
@XmlTransient
	private static Logger logger = LoggerUtil.getSdkLogger();
	
private String resServerAppPath  ="http://res.openurc.org";

	private String resServerIpAdress = "";

	private String resServerPwd = "GuestUser";

		private String resServerUserName = "GuestUser";
	
		private String uchPortNo = "8080";

		private String uchDebuggLevel = "3";


	@XmlElement(name="resserver.appPath")
	public String getAppPath() {
	return resServerAppPath;
}
		
	@XmlElement(name="resserver.ipAdress")
public String getIpAdress() {
	return resServerIpAdress;
}

	public void setAppPath(String resServerAppPath) {
		this.resServerAppPath = resServerAppPath;	
		}

	public void setIpAdress(String resServerIpAdress) {
		this.resServerIpAdress  = resServerIpAdress;
		
	}
	
	@XmlElement(name="resserver.password")
public String getPwd() {
	return resServerPwd;
}

	@XmlElement(name="resserver.userName")
public String getUserName() {
	return resServerUserName;
}
	
	public void setUserName(String resServerUserName) {
	this.resServerUserName = resServerUserName;
	
}

public void setPwd(String resServerPwd) {
	this.resServerPwd	= resServerPwd;
}


  @XmlElement(name="UCH.portNo")
public String getPort(){
	return this.uchPortNo;
}

public void setPort(String uchPortNo) {
	this.uchPortNo = uchPortNo;	
}

@XmlElement(name="UCH.DebuggLevel")
public String getUCHDebuggLevel() {
	// 
	return uchDebuggLevel;
}

public void setUCHDebuggLevel(String uchDebuggLevel){
	this.uchDebuggLevel = uchDebuggLevel;
}


public static Configuration readConfigFile(File file) throws Exception {
	/*	
	JAXBContext context = JAXBContext.newInstance(Configuration.class);
	Unmarshaller um = context.createUnmarshaller();
	System.out.println("sml " + xMLfile);
	Configuration config = (Configuration) um.unmarshal(xMLfile);
	*/
	Configuration config = new Configuration();
	
//	File file = new File( path + "config.xml");
	try{
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(file.getAbsoluteFile());
	
	doc.getDocumentElement().normalize();
	NodeList nList = doc.getElementsByTagName("configuration");
	
	for (int temp = 0; temp < nList.getLength(); temp++) {
		Node nNode = nList.item(temp);
		Element eElement = (Element) nNode;
		config.resServerAppPath = eElement.getElementsByTagName("resserver.appPath").item(0).getTextContent();
		config.resServerUserName = eElement.getElementsByTagName("resserver.userName").item(0).getTextContent();
		config.resServerPwd = eElement.getElementsByTagName("resserver.password").item(0).getTextContent();
	config.uchPortNo = eElement.getElementsByTagName("UCH.portNo").item(0).getTextContent();
		config.resServerIpAdress = eElement.getElementsByTagName("resserver.ipAdress").item(0).getTextContent();
		config.uchDebuggLevel = eElement.getElementsByTagName("UCH.DebuggLevel").item(0).getTextContent();
		
		System.out.println("Configuration was successfuly read.");
		return config;
}	
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (org.xml.sax.SAXException e) {
		logger.severe("An error occured when parsing config file" + file.getAbsolutePath());
		System.out.println("parsing error");
	} catch (IOException e) {
		 
		System.out.println("io");
	}
	throw new Exception();
	
}

public void writeToFile(File XMLfile) throws JAXBException {
	JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);  
	Marshaller jaxbMarshaller = jaxbContext.createMarshaller();  
	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);  
	
	jaxbMarshaller.marshal(this, XMLfile);     

	logger.info("Configuration was successfully written to " + XMLfile.getAbsolutePath()  );
	
 }

public static Configuration getDefaultConfiguration() {
return new Configuration();	


}



}