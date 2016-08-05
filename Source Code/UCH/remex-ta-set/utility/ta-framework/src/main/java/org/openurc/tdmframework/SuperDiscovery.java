/*
Copyright 2006-2013  Board of Regents of the University of Wisconsin System (Univ. of Wisconsin-Madison, Trace R&D Center).  
Copyright 2015 Hochschule der Medien (HDM) / Stuttgart Media University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

"Parts of this program were based on software developed by the Trace Center, University of Wisconsin-Madison under funding from NIDRR / US Dept of Education."

*/

package org.openurc.tdmframework;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.openurc.uch.ITDMListener;
import org.openurc.util.NewConstants;

import edu.wisc.trace.uch.util.Constants;
import edu.wisc.trace.uch.util.LoggerUtil;

public abstract class SuperDiscovery <T extends SuperTDM> extends Thread {
	
	public Logger logger = LoggerUtil.getSdkLogger();
	protected ITDMListener tdmListener;
	protected T tdm;
	public SuperDiscovery(T tdm, ITDMListener tdmListener) {
		this.tdm = tdm;
		this.tdmListener = tdmListener;
		}
	public HashMap<String, String> getTaProps(String deviceType, String taDevicePlatform) {
		 
		HashMap<String,String> taProps= new HashMap<String, String>();
		taProps.put(Constants.PROPERTY_RES_TYPE, Constants.PROPERTY_RES_TYPE_VALUE_TA);
		taProps.put(Constants.PROPERTY_DEVICE_PLATFORM, taDevicePlatform);
		taProps.put(Constants.PROPERTY_DEVICE_TYPE, deviceType);
		
		String conformsTo = getConformsTo();
		
		if ( conformsTo != null ){
			taProps.put(Constants.PROPERTY_CONFORMS_TO, conformsTo);
		}

		return taProps;
	}
	/**
	 * Get the value of the property 'http://purl.org/dc/terms/conformsTo' from UCH Properties.
	 * 
	 * @return a String specifies the value of the property 'http://purl.org/dc/terms/conformsTo'.
	 */
	protected String getConformsTo() {
	
	if ( tdm.uchProps == null ) {
		logger.warning("UCH Properties is null.");
		return null;
	}
	
	return tdm.uchProps.get(Constants.PROPERTY_CONFORMS_TO);
	}
	
	public Map<String, Object> getTargetProps(String targetFriendlyName, 
			String deviceType,
			String devicePlatform,
			String instanceId, 
			String tdUri){
		Map<String,Object> targetProps = new HashMap<String, Object>();
		targetProps.put(Constants.PROPERTY_RES_TYPE  , Constants.PROPERTY_RES_TYPE_VALUE_TARGET );
		targetProps.put(Constants.PROPERTY_RES_INSTANCE_ID , instanceId);
		targetProps.put(Constants.PROPERTY_RES_TARGET_FRIENDLY_NAME, targetFriendlyName ); 
		targetProps.put(Constants.PROPERTY_DEVICE_PLATFORM  , devicePlatform );
		targetProps.put(Constants.PROPERTY_DEVICE_TYPE, deviceType);
		targetProps.put(NewConstants.TARGET_PROPS_TD_URI , tdUri);
		return targetProps;
	}
	
	}
