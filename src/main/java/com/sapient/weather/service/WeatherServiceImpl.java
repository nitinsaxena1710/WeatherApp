package com.sapient.weather.service;

import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sapient.weather.error.WeatherInfoNotFoundException;
import com.sapient.weather.response.Weather;
import com.sapient.weather.response.WeatherResponse;
import com.sapient.weather.response.cache.WeatherResponseCache;

/**
 * @author nsaxena
 *
 */
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

	@Value("${weather.app.toggle}")
	private boolean toggle;

	private RestTemplate restTemplate = null;

	@Autowired
	public WeatherServiceImpl(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

	@Override
	public Object getWeather(String city) throws WeatherInfoNotFoundException {
		String template = endpoint;
		String uri = template.format(template, city, apiKey, cnt);
		URI uri2 = null;
		WeatherResponse response = null;
		WeatherResponseCache cache = WeatherResponseCache.getInstance();
		logger.info("Weather Information for  " + city);
		if (toggle) {
			response = readAndValidateCache(city);
		}
		if (null == response) {
			try {
				uri2 = new URI(uri);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				throw new WeatherInfoNotFoundException("Service Not Available");
			}
			try {
				response = restTemplate.getForObject(uri2, WeatherResponse.class);
				cache.cacheWeatherInfoList(city, response);
			} catch (Exception e) {
				response = readAndValidateCache(city);
				logger.info("response " + response);
				if (null == response) {
					if (e instanceof SocketException)
						throw new WeatherInfoNotFoundException("Weather Service Not Available");
					else {
						logger.info("Exception " + e.getMessage());
						throw new WeatherInfoNotFoundException("Please try again later ");
					}
				}
			}

		}
		WeatherResponseSapient weatherResponse = null;
		if (null != response) {
			com.sapient.weather.response.List responseList = getWeatherDetails(response);
			weatherResponse = getWeatherResponseSapient(responseList);

		}
		return weatherResponse ;
	}

	private WeatherResponse readAndValidateCache(String city) {
		WeatherResponseCache cache = WeatherResponseCache.getInstance();
		WeatherResponse response = cache.getWeatherResponseList(city);
		if (null != response) {
			if (!isCacheValid(response)) {
				cache.purgeData(city);
				response = null;
			}
		}
		return response;
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
			logger.info("Time Difference in Hrs   " + hrs);
		}
		return hrs;
	}

	private WeatherResponseSapient getWeatherResponseSapient(com.sapient.weather.response.List list) {
		Double tempK = list.getMain().getTempMax();
		Double tempC = tempK - 273;
		WeatherResponseSapient response = new WeatherResponseSapient();
		Double tempComparison 	=	Double.parseDouble(hotincelcius);
		if (tempC > tempComparison) {
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
		if (-1 == getCurrentUtcTimeJoda(date)) {
			isCacheValid = false;
		} else {
			isCacheValid = true;
		}
		logger.info("isCacheValid   " + isCacheValid);
		return isCacheValid;

	}
}
