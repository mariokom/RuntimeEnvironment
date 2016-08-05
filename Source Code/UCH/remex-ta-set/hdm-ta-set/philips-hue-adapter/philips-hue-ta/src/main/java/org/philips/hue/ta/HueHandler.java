package org.philips.hue.ta;
/**
 * @author Marcel Reuss
 * @author Marcel Heisler
 * */
import org.philips.hue.api.HueConstants;
import org.philips.hue.tdm.Hue;

import com.philips.lighting.model.PHLight;


public class HueHandler {
	private int hueDegree;
	private float brightness;
	private float saturation;
	private boolean lightSwitch;
	private Hue hue;
	private String lightId;
	private PHLight light;
	
	public HueHandler() {}
	
	/**
	 * Load the Values of the Lamp for the Controller on Session-Init
	 */
	public void loadInitValues() {
		setHueDegree(mapToHueDegree(hue.getHueDegree(light)));
		setBrightness(mapBackBrightness(hue.getBrightness(light)));
		setSaturation(mapBackSaturation(hue.getSaturation(light)));
		setLightSwitch(hue.getLightSwitch(light));
	}
	
	/**
	 * Update the Lightstate of the Lamp with the new Values
	 */
	public void changeLightValue() {
		hue.changeLightState(light, mapToHueValue(hueDegree), mapBrightness(brightness), mapSaturation(saturation), lightSwitch);
	}
	
	/**
	 * Map the HueDegree Value from the Controller 
	 * to the Hue Value for the Color
	 * HueDegree is between -180 and 180
	 * Hue Value is between 1 and 65535
	 */
	public int mapToHueValue(int degree) {
		if (degree == -180) {
			return 0;
		} else if (degree < 0){
			degree = Math.abs(degree);
			degree = 180 - degree;
		}else if (degree == 0) {
			return HueConstants.MAX_HUE / 2;
		} else if (degree == 180) {
			return HueConstants.MAX_HUE;
		} else {
			degree += 180;
		}
		return degree * 181;
	}
	
	/**
	 * Map the Hue Value from the Light
	 * to the HueDegree Value for the Controller
	 * HueDegree is between -180 and 180
	 * Hue Value is between 1 and 65535
	 */
	public int mapToHueDegree(int degree) {
		if (degree == 0) {
			return -180;
		} else if (degree < HueConstants.MAX_HUE / 2) {
			degree = degree / 181;
			degree = degree - 180;
		} else if (degree == HueConstants.MAX_HUE / 2) {
			return 0;
		} else if (degree == HueConstants.MAX_HUE) {
			return 180;
		} else {
			degree = degree / 181;
		}
		return degree;
	}
	
	/**
	 * Map the Saturation Value from the Controller for the Light
	 * Hue uses a Saturation Value between 0 and 254
	 */
	public int mapSaturation(float value) {
		int val;
		if (value == 0) {
			val = HueConstants.MIN_SAT;
		} else if (value == 100 || value == 100.0 || value == 100.00) {
			val = HueConstants.MAX_SAT;
		} else {
			val = (int) ((int) value * 2.54F);
		}
		return val;
	}
	
	/**
	 * Map the Saturation Value from the Light for the Controller
	 * Controller uses a Saturation Value between 0 and 100
	 */
	public float mapBackSaturation(int value) {
		float val;
		if (value == HueConstants.MIN_SAT) {
			val = 0;
		} else if (value == HueConstants.MAX_SAT) {
			val = 100;
		} else {
			val = (float) Math.floor(value / 2.54F);
		}
		return val;
	}
	
	/**
	 * Map the Brightness Value from the Controller for the Light
	 * Hue uses a Brightness Value between 1 and 254
	 */
	public int mapBrightness(float value) {
		int intVal;
		if (value == 1 || value == 1.0 || value == 1.00) {
			intVal = HueConstants.MIN_BRIGHT;
		} else if (value == 100 || value == 100.0 || value == 100.00) {
			intVal = HueConstants.MAX_BRIGHT;
		} else {
			intVal = (int) ((int) value * 2.54F);
		}
		return intVal;
	}
	/**
	 * Map the Brightness Value from the Light for the Controller
	 * Controller uses a Brightness Value between 1 and 100
	 * In the Socket the lowest possible Value is 0, but we use 1 as the Hue
	 * because the Lamp will not switch off
	 */
	public float mapBackBrightness(int value) {
		float val;
		if (value == HueConstants.MIN_BRIGHT) {
			val = 1;
		} else if (value == HueConstants.MAX_BRIGHT) {
			val = 100;
		} else {
			val = (float) Math.floor(value / 2.54F);
		}
		return val;
	}
	
	public int getHueDegree() {
		return hueDegree;
	}

	public float getBrightness() {
		return brightness;
	}

	public float getSaturation() {
		return saturation;
	}
	
	public boolean getLightSwitch() {
		return lightSwitch;
	}

	public void setHueDegree(int hueDegree) {
		if (hueDegree >= -180 && hueDegree <= 180) {
			this.hueDegree = hueDegree;
		}
	}

	public void setBrightness(float brightness) {
		if (brightness >= 0 && brightness <= 100)
			this.brightness = brightness;
	}

	public void setSaturation(float saturation) {
		if (saturation >= 0 && saturation <= 100)
			this.saturation = saturation;
	}
	
	public void setLightSwitch(boolean lightSwitch) {
		this.lightSwitch = lightSwitch;
	}
	
	public void setHueObject(Hue hue) {
		this.hue = hue;
	}
	
	public Hue getHueObject() {
		return hue;
	}
	
	public PHLight getLightFromHue(String id) {
		return hue.getLightById(id);
	}
	
	public PHLight getLight() {
		return light;
	}

	public void setLight(PHLight light) {
		this.light = light;
	}
	
	public String getLightId() {
		return lightId;
	}

	public void setLightId(String lightId) {
		this.lightId = lightId;
	}
}
