package com.sapient.weather.service;

public class WeatherResponseSapient {
	
	String temperature ; 
	String rain ; 
	String wind;
	
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getRain() {
		return rain;
	}
	public void setRain(String rain) {
		this.rain = rain;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	@Override
	public String toString() {
		return "WeatherResponseSapient [temperature=" + temperature + ", rain=" + rain + ", wind=" + wind + "]";
	} 
}
