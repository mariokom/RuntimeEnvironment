package edu.wisc.trace.uch.util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class ConfigurationTest {
	File XMLfile = new File("config.xml");
@Test
	public void testConfigurationXML() throws Exception{
	Configuration configurationToWrite = new Configuration();

	configurationToWrite.setAppPath("http:");
	configurationToWrite.setIpAdress("127.127.127.1");
	configurationToWrite.setPort("8080");
	configurationToWrite.setPwd("secret");
	configurationToWrite.setUserName("user");
configurationToWrite.setUCHDebuggLevel("2");
	configurationToWrite.writeToFile(XMLfile);
		
	Configuration configurationToRead = Configuration.readConfigFile(XMLfile);

	JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);  
	Marshaller jaxbMarshaller = jaxbContext.createMarshaller();  
	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);  
	
	jaxbMarshaller.marshal(configurationToRead , System.out);     
	jaxbMarshaller.marshal(configurationToWrite , System.out);
	
	

	XMLfile.delete();
	
	
	
	assertEquals(configurationToWrite.getAppPath()	, configurationToRead.getAppPath());
	assertEquals(configurationToWrite.getIpAdress(), configurationToRead.getIpAdress())	;
	assertEquals(configurationToWrite.getPort(), configurationToRead.getPort());
	assertEquals(configurationToWrite.getPwd(), configurationToRead.getPwd());
	assertEquals(configurationToWrite.getUserName(), configurationToRead.getUserName()	);
assertEquals(configurationToWrite.getUCHDebuggLevel() ,configurationToRead.getUCHDebuggLevel());
}
}
