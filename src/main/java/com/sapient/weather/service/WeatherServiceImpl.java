package com.sapient.weather.service;

import java.net.NoRouteToHostException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sapient.weather.error.WeatherInfoNotFoundException;
import com.sapient.weather.response.Weather;
import com.sapient.weather.response.WeatherResponse;
import com.sapient.weather.response.cache.WeatherResponseCache;

@Service
public class WeatherServiceImpl implements WeatherService {

	@Value("${weather.app.hotincelcius}")
	private String hotincelcius;

	@Value("${weather.app.windinmph}")
	private String windinmph;

	@Value("${weather.app.endpoint.api}")
	private String endpoint;

	@Value("${weather.app.key}")
	private String apiKey;

	@Value("${weather.app.count}")
	private String cnt;
	private RestTemplate restTemplate = null;

	@Autowired
	public WeatherServiceImpl(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	@Override
	public Object getWeather(String city) throws WeatherInfoNotFoundException {
		String template = endpoint;
		String uri = template.format(template, city, apiKey, cnt);
		URI uri2 = null;
		WeatherResponseCache cache = WeatherResponseCache.getInstance();
		WeatherResponse response = cache.getWeatherResponseList(city);
		boolean isFetchReq = false;
		if (null != response) {
			if (!isCacheValid(response)) {
				isFetchReq = true;
				cache.purgeData(city);
			}
		}
		if (null == response || isFetchReq) {
			try {
				uri2 = new URI(uri);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				throw new WeatherInfoNotFoundException("Service Not Available");
			}
			try {
				response = restTemplate.getForObject(uri2, WeatherResponse.class);
			} catch (Exception e) {
				if (e instanceof NoRouteToHostException)
					throw new WeatherInfoNotFoundException("Weather Service Not Available");
			}
			cache.cacheWeatherInfoList(city, response);
		}

		com.sapient.weather.response.List responseList = getWeatherDetails(response);

		return getWeatherResponseSapient(responseList);
	}

	private com.sapient.weather.response.List getWeatherDetails(WeatherResponse value) {
		java.util.List<com.sapient.weather.response.List> list = value.getList();
		com.sapient.weather.response.List response = null;
		for (com.sapient.weather.response.List responseTemp : list) {
			response = responseTemp;
			long timeDiff = getCurrentUtcTimeJoda(responseTemp.getDt());
			if (timeDiff > 0 && timeDiff <= 3) {
				break;
			}
		}
		return response;
	}

	private long getCurrentUtcTimeJoda(Integer WeatherTime) {
		long CurrentUnixTime = System.currentTimeMillis() / 1000L;
		long unixSeconds = CurrentUnixTime;
		long hrs = -1;
		// convert seconds to milliseconds
		Date date = new java.util.Date(unixSeconds * 1000L);
		long unixSeconds1 = WeatherTime;
		// convert seconds to milliseconds
		Date date1 = new java.util.Date(unixSeconds1 * 1000L);
		if (WeatherTime > CurrentUnixTime) {
			long diff = date1.getTime() - date.getTime();
			hrs = TimeUnit.MILLISECONDS.toHours(diff);
			System.out.println("WeatherServiceImpl.getCurrentUtcTimeJoda() Time Difference in hrs  " + hrs);
		}
		return hrs;
	}

	private WeatherResponseSapient getWeatherResponseSapient(com.sapient.weather.response.List list) {
		Double tempK = list.getMain().getTempMax();
		Double tempC = tempK - 273;
		WeatherResponseSapient response = new WeatherResponseSapient();
		if (tempC > 40) {
			response.setTemperature("Use Lotion");
		}
		java.util.List<Weather> weatherList = list.getWeather();
		for (Weather weather : weatherList) {
			if (weather.getDescription().contains("rain")) {
				response.setRain("Carry umbrella");
			} else {
				response.setRain("Normal Weather");
			}
		}
		if (list.getWind().getSpeed() > 10) {
			response.setWind("Its too windy !! Watch Out!");
		} else {
			response.setWind("Normal Wind ");
		}
		return response;

	}

	private boolean isCacheValid(WeatherResponse response) {
		java.util.List<com.sapient.weather.response.List> list = response.getList();
		Integer date = list.get(list.size() - 1).getDt();
		boolean isCacheValid = false;
		System.out.println("WeatherServiceImpl.isCacheValid() date " + date);
		if (-1 == getCurrentUtcTimeJoda(date)) {
			isCacheValid = false;
		} else {
			isCacheValid = true;
		}
		System.out.println("WeatherServiceImpl.isCacheValid() isCacheValid " + isCacheValid);
		return isCacheValid;

	}
}
