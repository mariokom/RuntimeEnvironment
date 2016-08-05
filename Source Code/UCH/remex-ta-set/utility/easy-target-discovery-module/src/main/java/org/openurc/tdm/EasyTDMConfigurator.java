package org.openurc.tdm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.openurc.util.NewConstants;

import edu.wisc.trace.uch.util.Constants;
import edu.wisc.trace.uch.util.LoggerUtil;

public class EasyTDMConfigurator {

	public static Logger logger = LoggerUtil.getSdkLogger();
	
	public static File configFile 	= new File("targetList.xml");
public static void main(String[] args) {


	TargetList targetList = null;

	if (!configFile.exists()){
		try {
			configFile.createNewFile();
			logger.info("new config file " + configFile.getAbsolutePath() + " was created.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		targetList = new TargetList();
	} else {
	targetList = readTargetList();
	
	}
	Target target = new Target();
	
	Prop propType = new Prop(Constants.PROPERTY_RES_TYPE, Constants.PROPERTY_RES_TYPE_VALUE_TA);
	target.addTaProp(propType);
	
	String conformsTo = getConformsTo();
	
	if ( conformsTo != null ){
	Prop propConformsTo = new Prop(Constants.PROPERTY_CONFORMS_TO, conformsTo);
	target.addTaProp(propConformsTo);
	}
	
	target.addTargetProp(new Prop(Constants.PROPERTY_RES_TYPE  , Constants.PROPERTY_RES_TYPE_VALUE_TARGET ));
	
	System.out.println("Add your properties:");
	String[ ] targetPropNames = new String[5];
	targetPropNames[0] = Constants.PROPERTY_DEVICE_TYPE;
			targetPropNames[1] = Constants.PROPERTY_DEVICE_PLATFORM;  
					targetPropNames[2] = Constants.PROPERTY_RES_TARGET_FRIENDLY_NAME;
							targetPropNames[3] = Constants.PROPERTY_RES_INSTANCE_ID; 
									targetPropNames[4] = NewConstants.TARGET_PROPS_TD_URI; 
String[] values = new String[5];
											
											
											
									for (int i = 0; i< targetPropNames.length;i++){
											String value = "";
											value = readLine(targetPropNames[i]);
											target.addTargetProp(new Prop(targetPropNames[i], value));
											values[i] = value;
											if ( i < 2){
												target.addTaProp(new Prop(targetPropNames[i], value));
											}
	
									}
									targetList.addTarget(target);
									writeToFile(targetList);
									

									String value = readLine("Do you want to create an .rprop file for a related Target adapter for this target? [y,n]");
									if (value.equals("y") ){
										createTaRprop(values);
									}
									
									
}

private static String readLine(String str) {
	BufferedReader	reader = new BufferedReader(new InputStreamReader(System.in));
	System.out.println(str);
	try {
		return reader.readLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}

private static void createTaRprop(String[] values) {
	String deviceType = values[0];
	String devicePlatform = values[1];
	createTaRprop(deviceType, devicePlatform);
}

private static void createTaRprop(String deviceType, String devicePlatform) {
	String rpropFileName = readLine("rprop file name:");
			File rpropFile = new File(rpropFileName);
	Props props = new Props();
	
	String[] names = new String[9];
	String[] values = new String[9];
	
	names[0] = "http://openurc.org/ns/res#name";
	values[0] = readLine(names[0]);
			
			names[1] = "http://openurc.org/ns/res#type";
	values[1] = "http://openurc.org/restypes#ta";
	
	names[2] = "http://openurc.org/ns/res#resourceUri";
	values[2] = readLine(names[2]);
	
	names[3] = "http://openurc.org/ns/res#dynamicLibClass";
	values[3] = readLine(names[3]);
	
	names[4] = "http://openurc.org/ns/res#deviceType";
	values[4] = deviceType;
	
	names[5] = "http://openurc.org/ns/res#devicePlatform";
	values[5] = devicePlatform;
	
	names[6] = "http://openurc.org/ns/res#runtimePlatform";
	values[6] = "java";
	
	names[7] = "http://openurc.org/ns/res#mimeType";
	values[7] = "application/java-archive";
	
	names[8] = "http://purl.org/dc/terms/conformsTo";
	values[8] = "http://openurc.org/TR/uch1.0-20121022/";

	for (int i = 0; i < names.length;i++){
	Prop p = new Prop(names[i], values[i]);
props.addProp(p);		
	}
writeToFile(props, rpropFile);	
}

public static TargetList readTargetList() {
	logger.info("Reading target list from file: " + configFile.getAbsolutePath());
	TargetList targetList = null;
	try {
	JAXBContext context = JAXBContext.newInstance(TargetList.class);

	
	Unmarshaller um = context.createUnmarshaller();

			targetList = (TargetList) um.unmarshal(configFile);
	} catch (JAXBException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return targetList;
}

private static void writeToFile(TargetList targetList) {
	try {
	JAXBContext jaxbContext = JAXBContext.newInstance(TargetList.class);  
	Marshaller jaxbMarshaller = jaxbContext.createMarshaller();  
	
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	
	jaxbMarshaller.marshal(targetList, configFile);     
	} catch (PropertyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JAXBException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
	

	
}

private static void writeToFile(Props props,File rpropFile) {
	try {
	JAXBContext jaxbContext = JAXBContext.newInstance(Props.class);  
	Marshaller jaxbMarshaller = jaxbContext.createMarshaller();  
	
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	
	jaxbMarshaller.marshal(props, rpropFile);     
	} catch (PropertyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JAXBException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
	

	
}


/**
 * Get the value of the property 'http://purl.org/dc/terms/conformsTo' from UCH Properties.
 * 
 * @return a String specifies the value of the property 'http://purl.org/dc/terms/conformsTo'.
 */
protected static String getConformsTo() {
return "http://openurc.org/TR/uch1.0-20121022/";
}

public static TargetList readTargetList(String pathName) {
	configFile = new File(pathName);
	return readTargetList();
}
}
