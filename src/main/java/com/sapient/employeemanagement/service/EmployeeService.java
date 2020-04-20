package com.sapient.employeemanagement.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sapient.employeemanagement.dto.EmployeeDTO;
import com.sapient.employeemanagement.exception.EmployeeException;
import com.sapient.employeemanagement.model.Employee;
import com.sapient.employeemanagement.redis.EmployeeRedis;
import com.sapient.employeemanagement.repository.EmployeeRepository;


/**
 * @author Sarthak Satish
 * 
 * @class EmployeeService
 * 
 * @description Business layer to perform business operation on the obtained resultset/dataset
 * 
 */
@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	EmployeeRedis employeeRedis;

	private Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	
	/**
	 * 
	 * @method updateEmployeeSalaryByPlace
	 * 
	 * @input place and percentage
	 * 
	 * @description This method fetches all the employees based on place and updates their salary by given percentage
	 * 
	 * @return list of Employees whose salary has been updated
	 */
	public List<Employee> updateEmployeeSalaryByPlace(String place, Double percentage) {

		List<Employee> empList = new ArrayList<>();
		try {
			empList = employeeRepository.findByPlace(place).stream()
					.peek(e -> e.setSalary(e.getSalary() + ((e.getSalary() * percentage) / 100)))
					.peek(e -> employeeRepository.save(e)).collect(Collectors.toList());

			if (empList.isEmpty())
				throw new EmployeeException("Place cannot be found");

			// employeeRedis.saveEmployeesBasedOnLocation(place, empList);
		} catch (Exception ex) {
			throw new EmployeeException(ex.getMessage());
		}

		return empList;
	}

	/**
	 * 
	 * @method getListOfEmployeeByPlace
	 * 
	 * @input place
	 * 
	 * @description This method fetches all the employees based on place , It 1st checks weather this place is already exists in Redis 
	 * 				if exits it fetches it from redis else queries database saves it into redis and return's the response
	 * 
	 * @return list of Employees for a given place
	 */
	public List<Object> getListOfEmployeeByPlace(String place) {
		if (employeeRedis.checkLocationExists(place)) {
			logger.info("in redis");
			logger.info("Retrived Employee Details from redis" + new Timestamp(System.currentTimeMillis()));
			return employeeRedis.getEmployeeBasedOnLocation(place);
		} else {
			logger.info("from db");
			List<Employee> employee = employeeRepository.findByPlace(place);
			if (employee.isEmpty())
				throw new EmployeeException("Place cannot be found");
			employeeRedis.saveEmployeesBasedOnLocation(place, employee);
			logger.info("Saved Employee Details to redis and retrived from redis"
					+ new Timestamp(System.currentTimeMillis()));
			return employeeRedis.getEmployeeBasedOnLocation(place);
		}

	}
	
	/**
	 * 
	 * @method getTotalSalaryOfEmployee
	 * 
	 * @input businessunit, supervisor , place
	 * 
	 * @description This methods returns the total salary of the employee for a given search criteria.
	 * 				all the 3 input parameters are optional 
	 * 				1) So if all the 3 input is null it returns total salary of all the employee in database
	 * 				2) if only business unit is give fetches all the employees belonging to that business unit and sum's their salary
	 * 				3) If Business unit and Supervisor are given ,Fetches all the employees belonging to that business unit and reporting to that supervisor and sum's their salary
	 * 
	 * @return Employee Data transfer object whhich contains all the employees and total salary
	 */
	public EmployeeDTO getTotalSalaryOfEmployee(String businessunit, String supervisor, String place) {

		List<Employee> employeeList = employeeRepository.findAll(new Specification<Employee>() {

			@Override
			public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (businessunit != null) {
					predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("businessUnit"), businessunit)));
				}
				if (supervisor != null) {
					predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("supervisorId"), supervisor)));
				}
				if (place != null) {
					predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("place"), place)));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

			}
		});

		if (employeeList.isEmpty())
			throw new EmployeeException("Employees Cannot be found for the given search criteria");

		Double sumSal = employeeList.stream().map(Employee::getSalary).reduce(0.0, Double::sum);

		EmployeeDTO result = new EmployeeDTO();
		result.setEmployeeList(employeeList);
		result.setTotalSalary(sumSal);

		return result;
	}

	/**
	 * 
	 * @method getRangeOfSalariesGivenTitle
	 * 
	 * @input title
	 * 
	 * @description Get's the range of salaries for given title
	 * 
	 * @return Integer array
	 */
	public EmployeeDTO getRangeOfSalariesGivenTitle(String title) {
		EmployeeDTO result = new EmployeeDTO();
		List<Employee> employeeList = employeeRepository.findByTitle(title);
		if (employeeList.isEmpty())
			throw new EmployeeException("Title cannot be found");
		DoubleSummaryStatistics stats = employeeList.stream()
				.collect(Collectors.summarizingDouble(Employee::getSalary));
		result.setSalaryRange(stats.getMax() == stats.getMin() ? new int[] { 0, (int) stats.getMax() }
				: new int[] { (int) stats.getMin(), (int) stats.getMax() });

		return result;
	}

	/**
	 * 
	 * @method getNestedListOfEmployeeGivenSupervisor
	 * 
	 * @input supervisor
	 * 
	 * @description Get's all the nested list of employees for a given supervisor
	 * 
	 * @return Map<Employee, List<Employee>>
	 */
	public Map<Employee, List<Employee>> getNestedListOfEmployeeGivenSupervisor(String supervisor) {
		List<Employee> employeeList = employeeRepository.findAll();
		
		List<Employee> initialSupervisor = getSupervisors(employeeList, supervisor);
		List<Employee> supervisorsToExplore = new ArrayList<>();
		Map<Employee, List<Employee>> res=new LinkedHashMap<>();
		for (Employee employee : employeeList) {
			if(employee.getEmployeeName().equals(supervisor)) {
				res.put(employee, initialSupervisor);
			}
		}
		for (Employee employee : initialSupervisor) {
			supervisorsToExplore.add(employee);
		}

		while (!supervisorsToExplore.isEmpty()) {
			Employee currentEmployee = supervisorsToExplore.get(supervisorsToExplore.size() - 1);
			supervisorsToExplore.remove(supervisorsToExplore.size() - 1);

			List<Employee> unvisitedSupervisors = getSupervisors(employeeList, currentEmployee.getEmployeeName());
			
			if(!unvisitedSupervisors.isEmpty()) {
				res.put(currentEmployee, unvisitedSupervisors);
			}
			
			for (Employee emp : unvisitedSupervisors) {
				supervisorsToExplore.add(emp);
			}
		}
		if(res.isEmpty()) {
			throw new EmployeeException("Supervisor not found");
		}
		return res;

	}

	/**
	 * 
	 * @method getSupervisors
	 * 
	 * @parentmethod getNestedListOfEmployeeGivenSupervisor
	 * 
	 * @input employeelist and supervisor
	 * 
	 * @description Helps to filter all the employees belonging to that supervisor id
	 * 
	 * @return list of employees
	 */
	private List<Employee> getSupervisors(List<Employee> employeelist, String supervisor) {
		return employeelist.stream().filter(e -> e != null).filter(e -> e.getSupervisorId().equals(supervisor))
				.collect(Collectors.toList());
	}

}
