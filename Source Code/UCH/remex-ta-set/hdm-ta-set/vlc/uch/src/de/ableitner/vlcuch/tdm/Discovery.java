package de.ableitner.vlcuch.tdm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openurc.tdmframework.SuperDiscovery;
import org.openurc.tdmframework.SuperTDM;
import org.openurc.uch.ITDMListener;

import de.ableitner.vlcuch.api.VlcConstants;
import edu.wisc.trace.uch.util.Constants;
import edu.wisc.trace.uch.util.LoggerUtil;

public class Discovery<T extends SuperTDM> extends SuperDiscovery {
	
	
	
	
	public Logger logger = LoggerUtil.getSdkLogger();
	int i =1;	
	private String PROPERTY_DEVICE_PLATFORM_VALUE_LAN = "LAN";
	private File file;
		
	public static final String TARGET_PROPS_TD_URI = "http://openurc.org/ns/res#tdUri";
	
	
	
	
	public Discovery(SuperTDM tdm, ITDMListener tdmListener) {
		super(tdm, tdmListener);
	}
	
	
	
	public void run() {
		discoverConfigurationManager();
		file = getConfigFile();
		readConfigFile();
	}

	private File getConfigFile() {
		String filePath = System.getProperty("user.dir") + "/resources/Vlc/vlc.config";
		filePath.replace("\\", "/");
		File file = new File(filePath);
		return file;
	}

	/*
	 * reads the configfile and registers there configured targets
	 */
	private void readConfigFile() {
		this.logger.log(Level.INFO, "Log Tobias: in readConfigFile");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				if (!line.equals("")) {
					this.logger.log(Level.INFO, "Log Tobias: call discoverVLC from readConfigFile with line=" + line + " and false");
					discoverVLC(line, false);
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * register a configurationManager that is able to manually discover new
	 * targets by using a callback mechanism
	 */
	public void discoverConfigurationManager() {

		HashMap<String, String> taProps = getTaProps(VlcConstants.PROPERTY_DEVICE_TYPE_VALUE_VLC_CONFIGURATION_MANAGER, VlcConstants.DEVICE_PLATFORM);

		Map<String, Object> targetProps = getTargetProps(de.ableitner.vlcuch.api.VlcConstants.WCM_FRIENDLY_NAME,
				VlcConstants.PROPERTY_DEVICE_TYPE_VALUE_VLC_CONFIGURATION_MANAGER, VlcConstants.DEVICE_PLATFORM,
				"VlcConfigurationManagerId", getTdUri("vlc/URC-INF/VlcConfigurationManager/WoehlkeConfigurationManager.td"));

		targetProps.put(Constants.PROPERTY_RES_TARGET_MODEL_NAME, "Model1");
		targetProps.put(VlcConstants.VLC_DISCOVERY_MODULE, this.tdm);
		
		// added on Lukas' advice
		targetProps.put("http://openurc.org/ns/res#tdUri", getTdUri("vlc/URC-INF/VlcConfigurationManager/WoehlkeConfigurationManager.td"));
		
		
		this.logger.log(Level.INFO, "Log Tobias: test");
		if(tdmListener == null){
			this.logger.log(Level.INFO, "Log Tobias: tdmListener is null!");	
		}
		if(this.tdm == null){
			this.logger.log(Level.INFO, "Log Tobias: tdm is null!");	
		}
		
		tdmListener.targetDiscovered(this.tdm, targetProps, taProps, null);

	}

	protected static String getTdUri(String path) {

		return "file:///" + System.getProperty("user.dir").replace("\\", "/") + "/resources/" + path;
	}

	public void discoverVLC(String ipadr, boolean checkConfigFile) {
		this.logger.log(Level.INFO, "Log Tobias: in discoverVLC ipadr=" + ipadr + " checkConfigFile=" + checkConfigFile);
		boolean alreadyAvailable = alreadyAvailable(ipadr);
		if (checkConfigFile ^ alreadyAvailable) {
			this.logger.log(Level.INFO, "Log Tobias: in discoverVLC in if(checkConfigFile ^ alreadyAvailable)");
			this.logger.log(Level.INFO, "Log Tobias: in discoverVLC alreadyAvailable=" + alreadyAvailable);
			//if (alreadyAvailable) {
			if (!alreadyAvailable) {
				this.logger.log(Level.INFO, "Log Tobias: in discoverVLC alreadyAvailable=" + alreadyAvailable);
				writeToConfigFile(ipadr);
			}

			HashMap<String, String> taProps = getTaProps(VlcConstants.PROPERTY_DEVICE_TYPE_VALUE_VLC, VlcConstants.DEVICE_PLATFORM);
			
			Map<String, Object> targetProps = getTargetProps("Vlc", VlcConstants.PROPERTY_DEVICE_TYPE_VALUE_VLC, VlcConstants.DEVICE_PLATFORM, "vlc" + i++, getTdUri("vlc/URC-INF/Vlc/tv-set-basic.td"));
			targetProps.put(VlcConstants.IpAddress, ipadr);
			
			// added on Lukas' advice
			targetProps.put("http://openurc.org/ns/res#tdUri", getTdUri("vlc/URC-INF/Vlc/tv-set-basic.td"));
			

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

			while ((line = br.readLine()) != null) {

				if (line.trim().equals(ipadr.trim())) {
					logger.info("Ip address " + ipadr + "already exists in config file");
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void writeToConfigFile(String ipadr) {
		this.logger.log(Level.INFO, "Log Tobias: in writeToConfigFile ipadr=" + ipadr);
		boolean hasMoreThanZeroLines = false;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			hasMoreThanZeroLines = !(br.readLine() == null);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			this.logger.log(Level.INFO, "Log Tobias: in writeToConfigFile " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			this.logger.log(Level.INFO, "Log Tobias: in writeToConfigFile " + e.getMessage());
		}

		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			if (hasMoreThanZeroLines) {
				fw.append("\n");
			}
			fw.append(ipadr.trim());
			logger.info("Ip Address " + ipadr + " successfuly written to config file");
		} catch (IOException e) {
			e.printStackTrace();
			this.logger.log(Level.INFO, "Log Tobias: in writeToConfigFile " + e.getMessage());
		}
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			this.logger.log(Level.INFO, "Log Tobias: in writeToConfigFile " + e.getMessage());
		}

	}

}
