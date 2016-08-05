package org.open.weather.map.ta;

import org.openurc.taframework.SuperTA;
import org.openurc.taframework.SuperTAFacade;

public class OpenWeatherMapTAFacade extends SuperTAFacade<OpenWeatherMapTA> {

	public OpenWeatherMapTAFacade(){
		super("OpenWeatherMapTAFacade");
	}

	@Override
	public OpenWeatherMapTA createSpezificTA(SuperTAFacade<OpenWeatherMapTA> superTAFacade) {
		
		return new OpenWeatherMapTA(superTAFacade);
	}
	
}
