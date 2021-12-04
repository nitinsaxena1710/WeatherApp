package com.sapient.weather.error;

public class WeatherInfoNotFoundException extends Exception {

	/**
	 * 
	 * 
	 * 
	 */
	private static final long serialVersionUID = -8278992596722349731L;
	
	public WeatherInfoNotFoundException() {
		
	}
	
	public WeatherInfoNotFoundException(String Message) {
		super(Message);
	}
	
	public WeatherInfoNotFoundException(String Message,Throwable cause) {
		super(Message,cause);
	}

}
