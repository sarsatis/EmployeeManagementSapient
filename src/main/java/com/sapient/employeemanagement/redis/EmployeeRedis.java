package com.sapient.employeemanagement.redis;

import java.util.List;

import com.sapient.employeemanagement.model.Employee;



public interface EmployeeRedis {
	
	/* Methods To perform redis operation on a Employee Object */

	public boolean saveEmployeesBasedOnLocation(String key, List<Employee> employee);

	public List<Object> getEmployeeBasedOnLocation(String key);

	public boolean checkLocationExists(String key);

}
