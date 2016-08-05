package org.philips.hue.tdm;

/** 
 * @author Marcel Reuss
 * @author Marcel Heisler
 * */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openurc.tdmframework.SuperDiscovery;
import org.openurc.tdmframework.SuperTDM;
import org.openurc.uch.ITDMListener;

import org.philips.hue.api.HueConstants;

import com.philips.lighting.model.PHLight;

import edu.wisc.trace.uch.util.Constants;
import edu.wisc.trace.uch.util.LoggerUtil;

/** @param <T>  should be a HueTDM*/
public class HueDiscovery<T extends SuperTDM> extends SuperDiscovery<SuperTDM> {

	private Hue hue;
	private Logger logger = LoggerUtil.getSdkLogger();
	public List<String> discoverdLamps;
	public List<String> discoverdBridges;

	public HueDiscovery(T hueTDM, ITDMListener tdmListener) {
		super(hueTDM, tdmListener);
		this.discoverdLamps = new ArrayList<String>();
		this.discoverdBridges = new ArrayList<String>();
	}

	/** use this constructor in TDM to get the same hue object */
	public HueDiscovery(T hueTDM, ITDMListener tdmListener, Hue hue) {
		super(hueTDM, tdmListener);
		this.hue = hue;
		this.discoverdLamps = new ArrayList<String>();
		this.discoverdBridges = new ArrayList<String>();
	}

	/**
	 * always discover the Configuration Manager in case the user wants to
	 * connect to an other bridge as last time also try to connect to the last
	 * bridge again automatically
	 */
	@Override
	public void run() {
		discoverConfigurationManager();
		hue.connectToLastBridge();
	}

	/**
	 * register a configurationManager that is able to manually discover new
	 * targets by using a callback mechanism
	 */
	private void discoverConfigurationManager() {
		HashMap<String, String> taProps = getTaProps(HueConstants.PROPERTY_DEVICE_TYPE_VALUE_HUE_CONFIGURATION_MANAGER,
				HueConstants.DEVICE_PLATFORM);

		Map<String, Object> targetProps = getTargetProps(HueConstants.wcm_friendly_name,
				HueConstants.PROPERTY_DEVICE_TYPE_VALUE_HUE_CONFIGURATION_MANAGER, HueConstants.DEVICE_PLATFORM,
				"HueConfigurationManagerId",
				getTdUri("philips-hue-adapter/philips-hue/ConfigurationManager/PhilipsHueConfigurationManager.td"));

		targetProps.put(Constants.PROPERTY_RES_TARGET_MODEL_NAME, "Model1");
		targetProps.put(HueConstants.HUE_DISCOVERY_MODULE, this.tdm);

		tdmListener.targetDiscovered(this.tdm, targetProps, taProps, null);
		logger.info("Configuration Manager for Philips Hue registered as Target.");
	}

	/**
	 * register the philips hue bridge to the uch and add it to the
	 * discoverdBridges list to not connect to the same one again
	 */
	public void discoverBridge() {
		HashMap<String, String> taProps = getTaProps(HueConstants.PROPERTY_DEVICE_TYPE_VALUE_PHILIPS_HUE,
				HueConstants.DEVICE_PLATFORM);

		Map<String, Object> targetProps = getTargetProps("Philips Hue Bridge",
				HueConstants.PROPERTY_DEVICE_TYPE_VALUE_PHILIPS_HUE, HueConstants.DEVICE_PLATFORM, "bridge",
				getTdUri("philips-hue-adapter/philips-hue/ColouredLightBulb/ColouredLightBulb.td"));
		targetProps.put(HueConstants.IpAddress, HueProperties.getLastConnectedIP());
		targetProps.put(HueConstants.HUE_OBJECT, hue);
		discoverdBridges.add(hue.getBridge().getResourceCache().getBridgeConfiguration().getBridgeID());
		tdmListener.targetDiscovered(this.tdm, targetProps, taProps, null);
		logger.info("Philips Hue Bridge registered as Target. IP Address: " + HueProperties.getLastConnectedIP());

	}

	/**
	 * register lamps found in the cache of the philips hue bridge you are
	 * connected to. only lamps that are reachable at the moment are registered
	 * and also added to the discoverdLamps list.
	 * Use this method to discover multiple Lamps after connecting to a bridge
	 */
	public void discoverLamps() {
		List<PHLight> allLights = hue.getAllLights();
		for (PHLight light : allLights) {
			if (light.getLastKnownLightState().isReachable()) {
				String id = light.getIdentifier();

				HashMap<String, String> taProps = getTaProps(HueConstants.PROPERTY_DEVICE_TYPE_VALUE_PHILIPS_HUE,
						HueConstants.DEVICE_PLATFORM);

				Map<String, Object> targetProps = getTargetProps("Philips Hue Lamp " + id,
						HueConstants.PROPERTY_DEVICE_TYPE_VALUE_PHILIPS_HUE, HueConstants.DEVICE_PLATFORM, "Hue" + id,
						getTdUri("philips-hue-adapter/philips-hue/ColouredLightBulb/ColouredLightBulb.td"));
				targetProps.put(HueConstants.IpAddress, HueProperties.getLastConnectedIP());
				targetProps.put(HueConstants.HUE_OBJECT, hue);
				targetProps.put(HueConstants.LIGHT_ID, id);
				tdmListener.targetDiscovered(this.tdm, targetProps, taProps, null);
				discoverdLamps.add(id);
				logger.info("Philips Hue Lamp registered as Target: " + light.toString() );
			}
		}
	}
	
	/** register a single lamp from the cache of the philips hue bridge you are connected to.
	 * use this method when a lamp changed its state to reachable to add it as a target to the uch.  */
	public void discoverSingleLamp(PHLight light) {
		if (light.getLastKnownLightState().isReachable()) {
			String id = light.getIdentifier();
			HashMap<String, String> taProps = getTaProps(HueConstants.PROPERTY_DEVICE_TYPE_VALUE_PHILIPS_HUE,
					HueConstants.DEVICE_PLATFORM);

			Map<String, Object> targetProps = getTargetProps("Philips Hue Lamp " + id,
					HueConstants.PROPERTY_DEVICE_TYPE_VALUE_PHILIPS_HUE, HueConstants.DEVICE_PLATFORM, "Hue" + id,
					getTdUri("philips-hue-adapter/philips-hue/ColouredLightBulb/ColouredLightBulb.td"));
			targetProps.put(HueConstants.IpAddress, HueProperties.getLastConnectedIP());
			targetProps.put(HueConstants.HUE_OBJECT, hue);
			targetProps.put(HueConstants.LIGHT_ID, id);
			tdmListener.targetDiscovered(this.tdm, targetProps, taProps, null);
			discoverdLamps.add(id);
			logger.info("Philips Hue Lamp registered as Target: " + light.toString() );
		}
	}
	
	/** discard a lamp as a uch target when it changed its state to not reachable anymore */
	public void discardLamp(PHLight light) {
		String id = light.getIdentifier();
		tdmListener.targetDiscarded("Hue" + id);
		discoverdLamps.remove(id);
		logger.info("Philips Hue lamp target discarded: " + light);
	}

	protected static String getTdUri(String path) {
		return "file:///" + System.getProperty("user.dir").replace("\\", "/") + "/resources/" + path;
	}

}
