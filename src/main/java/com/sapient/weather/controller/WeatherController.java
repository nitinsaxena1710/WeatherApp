package com.sapient.weather.controller;

import com.sapient.weather.error.WeatherInfoNotFoundException;
import com.sapient.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/weather")
public class WeatherController {
    @Autowired
    public WeatherService weatherService;

    /**
     * @param city
     * @return
     * @throws WeatherInfoNotFoundException
     */
    @GetMapping("/{city}")
    public Object getWeatherInfo(@PathVariable("city") String city) throws WeatherInfoNotFoundException {
    	  return weatherService.getWeather(city);
    }
    
	
}
