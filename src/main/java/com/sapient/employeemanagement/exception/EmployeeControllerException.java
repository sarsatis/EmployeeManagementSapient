package com.sapient.employeemanagement.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Sarthak Satish
 * 
 * @class EmployeeControllerException
 * 
 * @description Controller advice to manage all the exceptions
 * 
 */

@ControllerAdvice
public class EmployeeControllerException extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = EmployeeException.class)
	public ResponseEntity<Object> exception(EmployeeException exception, HttpServletRequest servletRequest) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", exception.getMessage());
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(EmployeeException exception, HttpServletRequest servletRequest) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
