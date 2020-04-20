package com.sapient.employeemanagement.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.employeemanagement.dto.EmployeeDTO;
import com.sapient.employeemanagement.model.Employee;
import com.sapient.employeemanagement.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Sarthak Satish
 * 
 * @class EmployeeController
 * 
 * @description Rest controller interface for which contains API endpoints to perform operation of employee records
 * 
 */
@RestController
@Api(value = "EmployeeController", description = "Employee Controller APIs")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	private Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@ApiOperation(value = "update each record by increasing their salary by percentage for employees that have place matching with place path param", response = List.class)
	@PutMapping("/employees/place/{place}/salary/{percentage}")
	public List<Employee> updateSalaryPercentage(
			@ApiParam(value = "location where employee salary need to be updated e.g Bangalore,Chennai", required = true) @PathVariable("place") String place,
			@PathVariable("percentage") Double percentage) {
		logger.info("In Controller ,Updating salary by " + percentage + "For people belonging to location " + place);
		return employeeService.updateEmployeeSalaryByPlace(place, percentage);
	}

	@ApiOperation(value = "Retrives the List of employees searched by place. Searches Redis Cache 1st if not availble retrives it from db", response = List.class)
	@GetMapping("/employees/place/{place}")
	public List<Object> getEmployeesByPlace(
			@ApiParam(value = "location for which employees needs to be fetched e.g Bangalore,Chennai", required = true) @PathVariable("place") String place) {
		logger.info("In Controller ,Retriving employee by " + place);
		return employeeService.getListOfEmployeeByPlace(place);
	}

	@ApiOperation(value = "Return the total salary for a)business unit b)supervisor c)place", response = List.class)
	@GetMapping("/employees/totalsalary")
	public EmployeeDTO getTotalSalaryOfEmployeeByColumnName(
			@ApiParam(value = "business unit e.g FIAM,ISE,VINYASA,PROD", required = false) @RequestParam(value = "businessunit", required = false) String businessunit,
			@ApiParam(value = "supervisor e.g Sarthak,Vikas,Vinay", required = false) @RequestParam(value = "supervisor", required = false) String supervisor,
			@ApiParam(value = "place e.g Bangalore,Chennai,Mumbai", required = false) @RequestParam(value = "place", required = false) String place) {
		logger.info("In Controller ,Fetching total salary details for the given search criteria" + businessunit +" , "+ supervisor + " , "+place);
		return employeeService.getTotalSalaryOfEmployee(businessunit, supervisor, place);
	}

	@ApiOperation(value = "Return the range of salaries for a given title", response = List.class)
	@GetMapping("/employees/rangeofsalary/{title}")
	public EmployeeDTO getRangeOfSalariesGivenTitle(
			@ApiParam(value = "provide title e.g SSE,SE,Manager,VP", required = true) @PathVariable(value = "title") String title) {
		logger.info("In Controller ,Fetching range of salaries for given Title " + title);
		return employeeService.getRangeOfSalariesGivenTitle(title);
	}

	@ApiOperation(value = "Retrives the nested list of supervisors for a given supervisor", response = List.class)
	@GetMapping("/employees/nestedlist/{supervisor}")
	public Map<Employee, List<Employee>> getNestedListOfEmployeeGivenSupervisor(
			@ApiParam(value = "Provide Supervisor e.g Vikas,Vinay,Siva,Rajani", required = true) @PathVariable String supervisor) {
		logger.info("In Controller ,Retriving nested list of employees for given  supervisor" + supervisor);
		return employeeService.getNestedListOfEmployeeGivenSupervisor(supervisor);
	}

}
