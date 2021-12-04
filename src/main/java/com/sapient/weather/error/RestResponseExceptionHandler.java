package com.sapient.weather.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(WeatherInfoNotFoundException.class)
	public ResponseEntity<ErrorMessage> weatherNotFoundException(WeatherInfoNotFoundException exception,
			WebRequest req) {
		System.out.println("RestResponseExceptionHandler.weatherNotFoundException()");
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}
}
