package org.open.weather.map.ta;


import java.io.IOException;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.json.JSONException;

public class OpenWeatherMapHandler extends Thread {
	OwmClient owmClient;
	WeatherStatusResponse currentWeather;
	private String countryCode;
	private String cityName;
	
	
	public OpenWeatherMapHandler(){
		owmClient = new OwmClient();
	}

public void run(){
	try {
		this.currentWeather = owmClient.currentWeatherAtCity(cityName, countryCode);
		if (currentWeather.hasWeatherStatus ()) {
			WeatherData weather = currentWeather.getWeatherStatus ().get (0);
		
		}
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}