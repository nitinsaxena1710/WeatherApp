package com.sapient.weather.service;

import com.sapient.weather.error.WeatherInfoNotFoundException;

public interface WeatherService {
    Object getWeather(String city) throws WeatherInfoNotFoundException;
}
