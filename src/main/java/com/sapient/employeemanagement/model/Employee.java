package com.sapient.employeemanagement.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="employees")
@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(value="Employee",description="Employee Entity/Table Structure")
public class Employee implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty("Employee Id of an Employee")
	private int employeeId;
	@Column(name="employeename")
	@ApiModelProperty("Employee name")
	private String employeeName;
	@Column(name="title")
	@ApiModelProperty("Employee Designation")
	private String title;
	@Column(name="businessunit")
	@ApiModelProperty("Business Unit information of Employee ")
	private String businessUnit;
	@Column(name="place")
	@ApiModelProperty("Employee Location")
	private String place;
	@Column(name="supervisorid")
	@ApiModelProperty("Employee Supervisor/Manager Name")
	private String supervisorId;
	@Column(name="competencies")
	@ApiModelProperty("Tech stack which Employee has..")
	private String competencies;
	@ApiModelProperty("Total Compensation of Employee ")
	@Column(name="salary")
	private Double salary;

}
