package com.sapient.weather.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sapient.weather.error.WeatherInfoNotFoundException;
import com.sapient.weather.response.City;
import com.sapient.weather.response.WeatherResponse;

@Service
public class WeatherServiceImpl implements WeatherService{

    @Value("${weather.app.hotincelcius}")
    private String hotincelcius;

    @Value("${weather.app.windinmph}")
    private String windinmph;

    @Value("weather.app.endpoint.api")
    private String endpoint;

    @Value("${weather.app.key}")
    private String apiKey;
    
    private RestTemplate restTemplate = null;
    
    @Autowired
    public WeatherServiceImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }
    
    @Override
    public Object getWeather(String city) throws WeatherInfoNotFoundException{
    	System.out.println("WeatherServiceImpl.getWeather() City "+city);
        System.out.println("hotincelcius-->>" + hotincelcius);
        System.out.println("windinmph-->>" + windinmph);
        System.out.println("WeatherServiceImpl.getWeather()"+apiKey);
        String template = new String("https://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&cnt=10");
        String uri  =  template.format(template, city,apiKey);
        System.out.println("WeatherServiceImpl.getWeather() Uri "+uri);
        URI uri2 = null; 
        try {
        	uri2 = new URI(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        WeatherResponse value = restTemplate.getForObject(uri2, WeatherResponse.class);
        System.out.println("WeatherServiceImpl.getWeather() "+value.getCity());
        City city1 = value.getCity();
        System.out.println("WeatherServiceImpl.getWeather() Name "+city1.getName());
        System.out.println("WeatherServiceImpl.getWeather() "+value.toString());
       
     //  WeatherResponse response = getParsedResponse(value);
		/*
		 * java.util.List<com.sapient.weather.service.List> list = value.getList();
		 * list.forEach((n)->
		 * System.out.println("WeatherServiceImpl.getWeather() "+n.getVisibility()));
		 * list.forEach((n)-> { if((null!=n ) && (n.getRain() != null)) {
		 * System.out.println("WeatherServiceImpl.getWeather() Rain ="+n.getRain().get3h
		 * ());
		 * 
		 * 
		 * } else { System.out.println("WeatherServiceImpl.getWeather()");
		 * 
		 * 
		 * } }); if(list == null) { throw new
		 * WeatherInfoNotFoundException("Infor not available"); } list.forEach((n)-> {
		 * if(n.getMain()!=null) {
		 * System.out.println("WeatherServiceImpl.getWeather() Max Temperature "+n.
		 * getMain().getTempMax()+"Min Temperature "+n.getMain().getTempMin()); } });
		 */
        
        throw new WeatherInfoNotFoundException("Service Not Available");
      //  return null;
    }


	/*
	 * private WeatherResponse
	 * getParsedResponse(com.sapient.weather.response.WeatherResponse weather) {
	 * java.util.List<com.sapient.weather.response.List> list = weather.getList();
	 * WeatherResponse response = new WeatherResponse(); for(int i=0;
	 * i<list.size();i++) { com.sapient.weather.response.List obj = list.get(i);
	 * Double speed = obj.getWind().getSpeed();
	 * System.out.println("WeatherServiceImpl.getParsedResponse() Speed = "+speed);
	 * if(speed > 10) { response.setTemp("It's too Windy ..Watch Out "); }
	 * System.out.println("WeatherServiceImpl.getParsedResponse()"+obj.getMain().
	 * toString()); }
	 * 
	 * return response;
	 * 
	 * 
	 * }
	 */
}
