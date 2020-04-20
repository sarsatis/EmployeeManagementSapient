package com.sapient.employeemanagement.batch.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.sapient.employeemanagement.model.Employee;

public class EmployeeProcessor implements ItemProcessor<Employee,Employee> {

	private Logger logger = LoggerFactory.getLogger(EmployeeProcessor.class);
	@Override
	public Employee process(Employee employee) throws Exception {
		logger.info("Writing records to DB");
		return employee;
	}
	
	

}
