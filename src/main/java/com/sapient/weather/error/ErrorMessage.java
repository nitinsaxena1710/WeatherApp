package com.sapient.weather.error;

import org.springframework.http.HttpStatus;

public class ErrorMessage {
	
	private HttpStatus status; 
	private String message; 
	public ErrorMessage(HttpStatus notFound, String string) {
		this.status= notFound;
		this.message = string;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
