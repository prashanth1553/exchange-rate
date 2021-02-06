package com.exchangerate.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CurrencyNotSupported.class)
	public final ResponseEntity<String> handleAllExceptions(Exception ex, WebRequest request) {
		return ResponseEntity.badRequest().body("Currency Not Supported!");
	}
}
