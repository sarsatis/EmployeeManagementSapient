package com.sapient.employeemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sapient.employeemanagement.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>,JpaSpecificationExecutor<Employee>{
	
	List<Employee> findByPlace(String place);

	List<Employee> findByTitle(String title);
}
