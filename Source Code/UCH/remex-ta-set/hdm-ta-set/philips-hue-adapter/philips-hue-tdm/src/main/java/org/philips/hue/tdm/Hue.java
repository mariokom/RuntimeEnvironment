package org.philips.hue.tdm;

/**
 *  @author Marcel Reuss
 *  @author Marcel Heisler 
 */

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.philips.hue.api.HueConstants;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.hue.sdk.exception.PHHueException;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import edu.wisc.trace.uch.util.LoggerUtil;

public class Hue {
	final PHHueSDK phHueSDK;
	private HueDiscovery<HueTDM> discovery;
	private Logger logger = LoggerUtil.getSdkLogger();

	public Hue() {
		/**
		 * get an PHHueSDK-Instance and register the PHSDKListener to receive
		 * callbacks from the bridge.
		 */
		phHueSDK = PHHueSDK.getInstance();
		phHueSDK.getNotificationManager().registerSDKListener(getListener());
		/** Load last used IP-Address and Username saved as Java Properties */
		HueProperties.loadProperties();
	}

	public PHSDKListener getListener() {

		return new PHSDKListener() {

			/**
			 * Handle your bridge search results here. Typically if multiple
			 * results are returned you will want to display them in a list and
			 * let the user select their bridge. If one is found you may opt to
			 * connect automatically to that bridge. Due to an Interface might
			 * not be able to show a list of all Bridges found they all get a
			 * try to connect. The risk to connect to a wrong bridge is low
			 * because authentication is still needed after this point
			 */
			public void onAccessPointsFound(List<PHAccessPoint> accessPointsList) {
				logger.info("this is onAccessPointFound Listener... discoverdBridges: " + discovery.discoverdBridges);
				for (PHAccessPoint phAccessPoint : accessPointsList) {
					if (!discovery.discoverdBridges.contains(phAccessPoint.getBridgeId())) {
						try {
							phHueSDK.connect(phAccessPoint);
						} catch (PHHueException e) {
							logger.warning(e.getMessage());
						}
					} else {
						logger.info("not connected again to bridge id: " + phAccessPoint.getBridgeId());
					}
				}

			}

			/**
			 * Arriving here indicates that Pushlinking is required (to prove
			 * the User has physical access to the bridge). Typically here you
			 * will display a pushlink image (with a timer) indicating to the
			 * user they need to push the button on their bridge within 30
			 * seconds.
			 */
			public void onAuthenticationRequired(PHAccessPoint accessPoint) {
				phHueSDK.startPushlinkAuthentication(accessPoint);
				logger.info("PushlinkAuthentication started. Push the button on your Bridge within 30 seconds.");
			}

			/**
			 * Here the connected bridge is set in the sdk object and the
			 * heartbeat is started. HB_INTERVAL is set to 10 seconds. At this
			 * point you are connected to a bridge so control is passed back to
			 * the discovery class. The username is generated randomly by the
			 * bridge. Also the connected IPAddress and Username are stored
			 * here. This will allow easy automatic connection on subsequent
			 * use. Also a ShutdownHook is registered to the runtime to disable
			 * the Heartbeat before exiting the Application.
			 */
			public void onBridgeConnected(PHBridge bridge, String username) {
				phHueSDK.setSelectedBridge(bridge);
				phHueSDK.enableHeartbeat(bridge, PHHueSDK.HB_INTERVAL);

				String lastIpAddress = bridge.getResourceCache().getBridgeConfiguration().getIpAddress();
				logger.info("Connetion to Bridge succeful. IP: " + lastIpAddress + " Username: " + username);
				HueProperties.storeUsername(username);
				HueProperties.storeLastIPAddress(lastIpAddress);
				HueProperties.saveProperties();

				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						PHBridge bridge = phHueSDK.getSelectedBridge();
						phHueSDK.disableHeartbeat(bridge);
						logger.info("shutdownHook disabled the Heartbeat before exiting the application");
					}
				});

				discovery.discoverBridge();
				discovery.discoverLamps();
			}

			/**
			 * Here you receive notifications that the BridgeResource Cache was
			 * updated. Use the PHMessageType to check which cache was updated.
			 * Here LIGHTS_CACHE_UPDATED is used to check if reachability of a
			 * lamp has changed. New reachable lamps get registered to the UCH,
			 * unreachable ones get discarded.
			 */
			public void onCacheUpdated(List<Integer> cacheNotificationsList, PHBridge bridge) {
				if (cacheNotificationsList.contains(PHMessageType.LIGHTS_CACHE_UPDATED)) {
					for (PHLight light : getAllLights()) {
						if (light.getLastKnownLightState().isReachable()
								&& !discovery.discoverdLamps.contains(light.getIdentifier())) {
							discovery.discoverSingleLamp(light);
						} else if (!light.getLastKnownLightState().isReachable()
								&& discovery.discoverdLamps.contains(light.getIdentifier())) {
							discovery.discardLamp(light);
						}
					}
				}
			}

			/** Here you handle the loss of connection to your bridge. */
			public void onConnectionLost(PHAccessPoint accessPoint) {
				logger.info("Connection lost. AccessPoint: " + accessPoint);
			}

			public void onConnectionResumed(PHBridge bridge) {
				logger.info("Connection resumed. Bridge: " + bridge);
			}

			/**
			 * Here you can handle events such as Bridge Not Responding,
			 * Authentication Failed and Bridge Not Found
			 * 
			 * @see <a href=
			 *      "http://www.developers.meethue.com/documentation/error-messages">
			 *      http://www.developers.meethue.com/documentation/error-messages</a>
			 */
			public void onError(int code, String message) {
				logger.warning("A Connectionerror occoured. Errorcode: " + code + " Message: " + message
						+ " Find more details to errors here: http://www.developers.meethue.com/documentation/error-messages");
			}

			/**
			 * Any JSON parsing errors are returned here. Typically your program
			 * should never return these.
			 */
			public void onParsingErrors(List<PHHueParsingError> errs) {
				logger.warning("Parsing errors:" + errs);
			}
		};
	}

	/**
	 * This starts a UPNP/Portal Search and takes around 10 seconds. The
	 * PHSDKListener (onAccessPointsFound) will be notified with the bridges
	 * found.
	 */
	public void searchBridge() {
		PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
		sm.search(true, true);
	}

	/**
	 * read stored IP Address/Username and if set try to connect automatically.
	 * When connected the onBridgeConnected will be called again, and if your
	 * users Bridge IP has changed for example, onError will be called and can
	 * be handled programatically.
	 * 
	 * @return false if no username or IP could be read, true if there was a try
	 *         to connect to the last known accessPoint
	 */
	public boolean connectToLastBridge() {
		String usr = HueProperties.getUsername();
		String ip = HueProperties.getLastConnectedIP();
		if (usr == null || ip == null || usr.toString().isEmpty() || ip.toString().isEmpty()) {
			logger.info("Missing Last Username or Last IP.  Last known connection not found.");
			return false;
		}

		PHAccessPoint accessPoint = new PHAccessPoint();
		accessPoint.setIpAddress(ip);
		accessPoint.setUsername(usr);
		phHueSDK.connect(accessPoint);
		return true;
	}

	/** generates random light changes for all lamps */
	public void randomLights() {
		PHBridge bridge = phHueSDK.getSelectedBridge();
		PHBridgeResourcesCache cache = bridge.getResourceCache();

		List<PHLight> allLights = cache.getAllLights();
		Random rand = new Random();

		for (PHLight light : allLights) {
			PHLightState lightState = new PHLightState();
			lightState.setHue(rand.nextInt(HueConstants.MAX_HUE));
			bridge.updateLightState(light, lightState);
		}
	}

	/**
	 * @param light
	 *            the light which shall get the new state
	 * @param hueValue
	 *            the new state's color value as hueValue from 0 to 65535 both
	 *            red
	 * @param brightness
	 *            the new state's brightness value can range from 1 to 254
	 * @param saturation
	 *            the new state's saturation value can range from 0 to 254
	 * @param on
	 *            use true to turn the light on or use false to turn the light
	 *            off
	 */
	public void changeLightState(PHLight light, int hueValue, int brightness, int saturation, boolean on) {
		PHBridge bridge = phHueSDK.getSelectedBridge();
		PHLightState lightState = new PHLightState();
		lightState.setHue(hueValue);
		lightState.setOn(on);
		lightState.setBrightness(brightness);
		lightState.setSaturation(saturation);
		bridge.updateLightState(light, lightState, new PHLightListener() {

			@Override
			public void onSuccess() {
			}

			@Override
			public void onStateUpdate(Map<String, String> success, List<PHHueError> error) {
				// onStateUpdate method indicates your command was successful.
			}

			@Override
			public void onError(int code, String message) {
				logger.info("An Error occoured during a Lightstate change. Errorcode: " + code + " Message: " + message
						+ " Find more details to errors here: http://www.developers.meethue.com/documentation/error-messages");
			}

			@Override
			public void onSearchComplete() {
			}

			@Override
			public void onReceivingLights(List<PHBridgeResource> lights) {
			}

			@Override
			public void onReceivingLightDetails(PHLight light) {
			}
		});
	}

	/**
	 * @param id
	 *            of a specific light object you want to get returned
	 * @return null if there is no light matching to the id. otherwise the light
	 *         object is returned
	 */
	public PHLight getLightById(String lightId) {
		PHLight actLight = null;

		List<PHLight> allLights = getAllLights();
		for (PHLight light : allLights) {
			if (light.getIdentifier().equals(lightId)) {
				actLight = light;
			}
		}
		return actLight;
	}

	public int getHueDegree(PHLight light) {
		PHLightState lightState = getLightState(light);
		return lightState.getHue();
	}

	public int getBrightness(PHLight light) {
		PHLightState lightState = getLightState(light);
		return lightState.getBrightness();
	}

	public int getSaturation(PHLight light) {
		PHLightState lightState = getLightState(light);
		return lightState.getSaturation();
	}

	public boolean getLightSwitch(PHLight light) {
		PHLightState lightState = getLightState(light);
		return lightState.isOn();
	}

	public PHLightState getLightState(PHLight light) {
		return light.getLastKnownLightState();
	}

	public PHBridge getBridge() {
		return phHueSDK.getSelectedBridge();
	}

	public List<PHLight> getAllLights() {
		return getBridge().getResourceCache().getAllLights();
	}

	public PHLight getLight(int number) {
		return getAllLights().get(number);
	}

	public void setDiscovery(HueDiscovery<HueTDM> discovery) {
		this.discovery = discovery;
	}
}
