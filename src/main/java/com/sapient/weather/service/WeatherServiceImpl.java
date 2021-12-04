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
		System.out.println("WeatherServiceImpl.getWeather() endpoint " + endpoint);
		String template = endpoint;
		String uri = template.format(template, city, apiKey, cnt);
		URI uri2 = null;
		try {
			uri2 = new URI(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			System.out.println("WeatherServiceImpl.getWeather() Inside Exception");
			throw new WeatherInfoNotFoundException("Service Not Available");
		}
		WeatherResponse value = null;
		try {
			value = restTemplate.getForObject(uri2, WeatherResponse.class);
		} catch (Exception e) {
			if (e instanceof NoRouteToHostException)
				throw new WeatherInfoNotFoundException("Weather Service Not Available");
		}
		System.out.println("WeatherServiceImpl.getWeather() " + value.toString());
		com.sapient.weather.response.List response = getWeatherDetails(value);

		return getWeatherResponseSapient(response);
	}

	private com.sapient.weather.response.List getWeatherDetails(WeatherResponse value) {
		java.util.List<com.sapient.weather.response.List> list = value.getList();
		com.sapient.weather.response.List response = null;
		for (int i = 0; i < list.size(); i++) {
			response = list.get(i);
			long timeDiff = getCurrentUtcTimeJoda(response.getDt());
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
			long diff = date1.getTime() - date.getTime();// as given
			hrs = TimeUnit.MILLISECONDS.toHours(diff);
			System.out.println("WeatherServiceImpl.getCurrentUtcTimeJoda() Hrs  " + hrs);
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
		for (int i = 0; i < weatherList.size(); i++) {
			if (weatherList.get(i).getDescription().contains("rain")) {
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
}
