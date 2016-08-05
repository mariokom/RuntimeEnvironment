/*
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

*/


package org.woehlke.tdm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.openurc.tdmframework.SuperDiscovery;
import org.openurc.tdmframework.SuperTDM;
import org.openurc.uch.ITDMListener;

import de.hdm.woehlke.api.WoehlkeConstants;
import edu.wisc.trace.uch.util.Constants;
import edu.wisc.trace.uch.util.LoggerUtil;
/**
 * This class handels the real discovery process
 * @author Lukas Smirek
 *
 * @param <T>
 *
 */
public class Discovery<T extends SuperTDM> extends SuperDiscovery{
	public Logger logger = LoggerUtil.getSdkLogger();
int i =1;	
	private String PROPERTY_DEVICE_PLATFORM_VALUE_LAN = "LAN";
	private File file;
	
	public static final String TARGET_PROPS_TD_URI = "http://openurc.org/ns/res#tdUri";
	
		public Discovery(T woehlkeTDM, ITDMListener tdmListener) {
super(woehlkeTDM, tdmListener);
		}

	public void run(){

		discoverConfigurationManager(); 
		file = getConfigFile();
		readConfigFile();
					}

	private File getConfigFile() {
		String filePath = System.getProperty("user.dir")+ "/resources/Woehlke/woehlke.config";
		filePath.replace("\\", "/");
File file = new File(filePath);

		return file;
	}
/*
 *reads the configfile and registers there configured targets  
 */
	private void readConfigFile() {
		BufferedReader br = null; 
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		try {
			while ( (line = br.readLine() ) != null ) {
				if (!line.equals("")){
								discoverWebSteckdose(line,false);
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/*
	 * register a configurationManager that is able to manually discover new targets by using a callback mechanism
	 */
	public void discoverConfigurationManager() {
		

HashMap<String,String> taProps = getTaProps(WoehlkeConstants.PROPERTY_DEVICE_TYPE_VALUE_WOEHLKE_CONFIGURATION_MANAGER, WoehlkeConstants.DEVICE_PLATFORM);
		
				Map<String, Object> targetProps = getTargetProps(WoehlkeConstants.wcm_friendly_name ,						
						WoehlkeConstants.PROPERTY_DEVICE_TYPE_VALUE_WOEHLKE_CONFIGURATION_MANAGER,
						WoehlkeConstants.DEVICE_PLATFORM,
						"WoehlkeConfigurationManagerId",
getTdUri("Woehlke/URC-INF/WoehlkeConfigurationManager/WoehlkeConfigurationManager.td")						
						);
						
		targetProps.put(Constants.PROPERTY_RES_TARGET_MODEL_NAME , "Model1");
		targetProps.put(WoehlkeConstants.WOEHLKE_DISCOVERY_MODULE, this.tdm );
		
				tdmListener.targetDiscovered(this.tdm, targetProps, taProps, null);	

		
	}

	protected static String getTdUri(String path) {
		
				return  "file:///" + System.getProperty("user.dir").replace("\\", "/") + "/resources/" + path;
	}

	public void discoverWebSteckdose(String ipadr, boolean checkConfigFile) {

		boolean alreadyAvailable = alreadyAvailable(ipadr);
		if (checkConfigFile ^ alreadyAvailable) {
			if (alreadyAvailable){
				writeToConfigFile(ipadr);
			}
			
			HashMap<String, String> taProps = getTaProps(WoehlkeConstants.PROPERTY_DEVICE_TYPE_VALUE_WOEHLKE_Web_Steckdose, WoehlkeConstants.DEVICE_PLATFORM );
		
				Map<String, Object> targetProps =  getTargetProps("Woehlke Websteckdose", WoehlkeConstants.PROPERTY_DEVICE_TYPE_VALUE_WOEHLKE_Web_Steckdose	, WoehlkeConstants.DEVICE_PLATFORM, "woehlke" + i++, getTdUri( "Woehlke/URC-INF/WoehlkeWebsteckdose/WoehlkeWebsteckdose.td"));
		targetProps.put(WoehlkeConstants.IpAddress   , ipadr);



						tdmListener.targetDiscovered(this.tdm, targetProps, taProps, null);	
		}
			}

	private boolean alreadyAvailable(String ipadr) {
		BufferedReader br = null; 
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
		
		try {
			
			while ( (line = br.readLine() ) != null ) {
				
				if (line.trim().equals(ipadr.trim() ) ){
					logger.info("Ip address " + ipadr + "already exists in config file");
					return true;
				}
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
private void writeToConfigFile(String ipadr){
	boolean hasMoreThanZeroLines = false;
	BufferedReader br = null; 
	try {
		br = new BufferedReader(new FileReader(file));
hasMoreThanZeroLines =	!(br.readLine() == null);
br.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
			e.printStackTrace();
	}
	

			FileWriter fw = null;
			try {
				fw = new FileWriter(file);
				if (hasMoreThanZeroLines){
					fw.append("\n");
				}
				fw.append(ipadr.trim());
				logger.info("Ip Address " + ipadr + " successfuly written to config file");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}