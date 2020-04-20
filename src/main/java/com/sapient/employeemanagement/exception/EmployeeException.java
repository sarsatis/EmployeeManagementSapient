package com.sapient.employeemanagement.exception;


/**
 * @author Sarthak Satish
 * 
 * @class EmployeeException
 * 
 * @description Custom Exception class to manage employee not found etc
 * 
 */
public class EmployeeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeException(String msg) {

        super(msg);
    }
	
	

}
