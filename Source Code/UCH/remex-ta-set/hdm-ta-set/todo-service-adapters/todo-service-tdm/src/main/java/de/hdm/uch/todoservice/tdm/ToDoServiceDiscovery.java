package de.hdm.uch.todoservice.tdm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.openurc.tdmframework.SuperTDM;
import org.openurc.uch.ITDMListener;

import de.hdm.todoservice.api.TodoServiceConstants;
import edu.wisc.trace.uch.util.Constants;
import edu.wisc.trace.uch.util.LoggerUtil;

public class ToDoServiceDiscovery<T extends SuperTDM> extends ZeroConfDiscovery{
	private Logger logger = LoggerUtil.getSdkLogger();
	
	protected String PROPERTY_DEVICE_PLATFORM_VALUE_GENERIC = "todo-service";
	protected String PROPERTY_DEVICE_TYPE_VALUE_GENERIC = "lusm.deviceType";

	
	public  ToDoServiceDiscovery(T todoServiceTDM, ITDMListener tdmListener) {
		super(todoServiceTDM, tdmListener); 
	}
	
	/**
	 * Get the value of the property 'http://purl.org/dc/terms/conformsTo' from UCH Properties.
	 * 
	 * @return a String specifies the value of the property 'http://purl.org/dc/terms/conformsTo'.
	 */
	public String getConformsTo() {
		 
		if ( tdm.uchProps == null ) {
			logger.warning("UCH Properties is null.");
			return null;
		}
		
		return tdm.uchProps.get(Constants.PROPERTY_CONFORMS_TO);
	
	}
	
	public void close(){
	mdnsService.removeServiceListener("my-service-type", mdnsServiceListener);
	try {
		mdnsService.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


	
}

public void registerTarget(String hostName){
HashMap<String, String> taProps = new HashMap<String, String>();
	
	taProps.put(Constants.PROPERTY_RES_TYPE, Constants.PROPERTY_RES_TYPE_VALUE_TA);
	taProps.put(Constants.PROPERTY_DEVICE_PLATFORM, TodoServiceConstants.DEVICE_PLATFORM);
	taProps.put(Constants.PROPERTY_DEVICE_TYPE, TodoServiceConstants.DEVICE_TYPE);
	
	String conformsTo = getConformsTo();
	
	if ( conformsTo != null ){
		taProps.put(Constants.PROPERTY_CONFORMS_TO, conformsTo);
}
	Map<String, Object> targetProps = new HashMap<String, Object>(); 
	targetProps.put(Constants.PROPERTY_RES_TYPE  , Constants.PROPERTY_RES_TYPE_VALUE_TARGET );
	targetProps.put(Constants.PROPERTY_RES_INSTANCE_ID , "TodoService1");
	targetProps.put(Constants.PROPERTY_RES_TARGET_FRIENDLY_NAME, TodoServiceConstants.SERVICE_FRIENDLY_NAME);
	targetProps.put(Constants.PROPERTY_DEVICE_PLATFORM  , TodoServiceConstants.DEVICE_PLATFORM);
	targetProps.put(Constants.PROPERTY_DEVICE_TYPE, TodoServiceConstants.DEVICE_TYPE);
	targetProps.put(Constants.PROPERTY_RES_TARGET_MODEL_NAME, TodoServiceConstants.MODEL_1);

//	targetProps.put(NewConstants.NETWORK_LOCATION, hostName);
	
	 		tdmListener.targetDiscovered(tdm, targetProps, taProps, null);
}

public void run(){
	logger.info("ToDoDiscovery is running");
	registerTarget("http://localhost:8080");
}


}
