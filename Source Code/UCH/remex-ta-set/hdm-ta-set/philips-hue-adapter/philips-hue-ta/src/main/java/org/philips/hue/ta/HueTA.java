package org.philips.hue.ta;
/** 
 *@author Marcel Reuss
 *@author Marcel Heisler
 **/
import java.util.Map;
import java.util.logging.Logger;

import org.openurc.taframework.SuperTA;
import org.openurc.taframework.SuperTAFacade;
import org.philips.hue.api.HueConstants;
import org.philips.hue.tdm.Hue;

import com.philips.lighting.model.PHLight;

import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.Session;

public class HueTA extends SuperTA<HueHandler> {
	private String lightId;
	private PHLight light;
	private Logger logger = LoggerUtil.getSdkLogger();

	public HueTA(SuperTAFacade<HueTA> facade) {
		super(facade);
	}

	/**
	 * incoming requests are mapped to specific functions that are then executed
	 * by the handler to control the target
	 */
	@Override
	public void executeCommand(String commandName, Session session, String value) {
		switch (commandName) {
		case "/HueDegree":
			int hueDegree = Integer.parseInt(value);
			handler.setHueDegree(hueDegree);
			handler.changeLightValue();
			session.setValue("/HueDegree", value);
			break;

		case "/Brightness":
			float brightness = Float.parseFloat(value);
			handler.setBrightness(brightness);
			handler.changeLightValue();
			session.setValue("/Brightness", value);
			break;

		case "/Saturation":
			float saturation = Float.parseFloat(value);
			handler.setSaturation(saturation);
			handler.changeLightValue();
			session.setValue("/Saturation", value);
			break;

		case "/LightSwitch":
			boolean lightSwitch = Boolean.parseBoolean(value);
			handler.setLightSwitch(lightSwitch);
			handler.changeLightValue();
			session.setValue("/LightSwitch", value);
			break;

		case "/HueMinutes":
			logger.info("The use of HueMinutes is not implemented yet.");
			break;

		default:
			logger.info("Unvalid Command: " +commandName);
			break;
		}
	}

	@Override
	public void initSessionValues(Session session) {
		setCurrentLight();
		try {
			handler.loadInitValues();
		} catch (NullPointerException e) {
			logger.info("No Controller Values to load. \n" + e.getMessage());
		}
		session.setValue("/HueDegree", Integer.toString(handler.getHueDegree()));
		session.setValue("/Brightness", Float.toString(handler.getBrightness()));
		session.setValue("/Saturation", Float.toString(handler.getSaturation()));
		session.setValue("/LightSwitch", Boolean.toString(handler.getLightSwitch()));
	}

	/**
	 * use target Props to get access to the id of the target light and to the
	 * Hue instance needed in the handler
	 */
	@Override
	public void useTargetProps(Map<String, Object> targetProps) {
		Hue hue = (Hue) targetProps.get(HueConstants.HUE_OBJECT);
		handler.setHueObject(hue);
		this.lightId = (String) targetProps.get(HueConstants.LIGHT_ID);
		handler.setLightId(lightId);
		setCurrentLight();
	}

	/**
	 * This function is automatically, regularly called in order to update and
	 * sync the TA with the target.
	 */
	@Override
	public void update(Session session) {
		updateVariable(session, "/HueDegree", "" + Integer.toString(handler.getHueDegree()));
		updateVariable(session, "/Brightness", "" + Float.toString(handler.getBrightness()));
		updateVariable(session, "/Saturation", "" + Float.toString(handler.getSaturation()));
		updateVariable(session, "/LightSwitch", "" + Boolean.toString(handler.getLightSwitch()));
	}

	/**
	 * Set and Update the Light (For new Sessions)
	 */
	private void setCurrentLight() {
		this.light = handler.getLightFromHue(lightId);
		if (light != null) {
			handler.setLight(light);
		} else {
			logger.warning("Could not find a Light for the LightID: " + lightId);
		}

	}
}
