package com.sapient.weather.response.cache;

import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.weather.response.WeatherResponse;
import com.sapient.weather.service.WeatherServiceImpl;

/**
 * @author nsaxena
 *
 */
public class WeatherResponseCache implements IWeatherResponse {

	private static Hashtable<String, WeatherResponse> cache = new Hashtable<>();
	private static WeatherResponseCache obj = null;

	private static final Logger logger = LoggerFactory.getLogger(WeatherResponseCache.class);
	private WeatherResponseCache() {

	}
	
	
	public static WeatherResponseCache getInstance() {
		if (null == obj) {
			obj = new WeatherResponseCache();
		}
		return obj;
	}

	
	public WeatherResponse getWeatherResponseList(String city) {
		WeatherResponse response = null;
		if (null != city) {
			response = (cache.get(city));
		}
		logger.info("Response from Cache "+response);
		return response;
	}

	@Override
	public void cacheWeatherInfoList(String city, WeatherResponse response) {
		if ((null != city) && (null != response)) {
			cache.put(city, response);
		}

	}

	
	public void purgeData(String city) {
		if (null != city && cache != null) {
			cache.remove(city);
		}
	}

}
