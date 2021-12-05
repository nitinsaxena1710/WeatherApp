package com.sapient.weather.response.cache;

import com.sapient.weather.response.WeatherResponse;

public interface IWeatherResponse {
	
	public WeatherResponse getWeatherResponseList(String city);
	
	public void cacheWeatherInfoList(String city,WeatherResponse response);
	
	public void purgeData(String city);
}
