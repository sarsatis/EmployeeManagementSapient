package com.sapient.employeemanagement.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sapient.employeemanagement.model.Employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ApiModel(value="EmploteeDTO",description="Employee Data Transfer Object")
public class EmployeeDTO {
	@ApiModelProperty("Total salary for the employees at the give location")
	private Double totalSalary;
	@ApiModelProperty("Salary Range for the given search criteria")
	private int[] salaryRange;
	@ApiModelProperty("Stores the list of Employees ")
	private List<Employee> employeeList;

}
