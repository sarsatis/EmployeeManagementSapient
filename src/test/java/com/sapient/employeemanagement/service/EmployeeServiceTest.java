package com.sapient.employeemanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sapient.employeemanagement.model.Employee;
import com.sapient.employeemanagement.redis.EmployeeRedis;
import com.sapient.employeemanagement.repository.EmployeeRepository;

public class EmployeeServiceTest {

	@InjectMocks
	public EmployeeService empService;

	@Mock
	EmployeeRepository employeeRepository;

	@Mock
	EmployeeRedis employeeRedis;

	Employee emp;

	Employee empSupervisor;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		emp = new Employee();
		emp.setEmployeeId(1);
		emp.setEmployeeName("Sarthak");
		emp.setPlace("Bangalore");
		emp.setCompetencies("Java");
		emp.setBusinessUnit("ISE");
		emp.setSalary(100.0);
		emp.setSupervisorId("Vinay");
		emp.setTitle("SE");

		empSupervisor = new Employee();
		empSupervisor.setEmployeeId(2);
		empSupervisor.setEmployeeName("Vinay");
		empSupervisor.setPlace("Bangalore");
		empSupervisor.setCompetencies("Java");
		empSupervisor.setBusinessUnit("ISE");
		empSupervisor.setSalary(1000.0);
		empSupervisor.setSupervisorId("Vikas");
		empSupervisor.setTitle("Manager");

	}

	@Test
	public void updateEmployeeSalaryByPlaceSizeTest() {
		when(employeeRepository.findByPlace("Bangalore")).thenReturn(Arrays.asList(emp));
		List<Employee> result = empService.updateEmployeeSalaryByPlace("Bangalore", 10.0);
		assertEquals(1, result.size());

		assertThat(result.get(0).getSalary()).isEqualTo(110);
	}

	@Test
	public void getListOfEmployeeByPlaceRedisTest() {
		when(employeeRedis.checkLocationExists("Bangalore")).thenReturn(true);
		when(employeeRedis.getEmployeeBasedOnLocation("Bangalore")).thenReturn(Arrays.asList(emp));
		assertEquals(1, empService.getListOfEmployeeByPlace("Bangalore").size());
	}

	@Test
	public void getListOfEmployeeByPlaceDatabaseTest() {
		when(employeeRedis.checkLocationExists("Bangalore")).thenReturn(false);
		when(employeeRepository.findByPlace("Bangalore")).thenReturn(Arrays.asList(emp));
		when(employeeRedis.saveEmployeesBasedOnLocation("Bangalore", Arrays.asList(emp))).thenReturn(true);
		when(employeeRedis.getEmployeeBasedOnLocation("Bangalore")).thenReturn(Arrays.asList(emp));
		assertEquals(1, empService.getListOfEmployeeByPlace("Bangalore").size());
	}

	@Test
	public void getTotalSalaryOfEmployeeTest() {
		when(employeeRepository.findByTitle("SE")).thenReturn(Arrays.asList(emp));
		assertArrayEquals(new int[] { 0, 100 }, empService.getRangeOfSalariesGivenTitle("SE").getSalaryRange());
	}

	@Test
	public void getNestedListOfEmployeeGivenSupervisorTest() {
		when(employeeRepository.findAll()).thenReturn(Arrays.asList(emp, empSupervisor));
		assertEquals(1, empService.getNestedListOfEmployeeGivenSupervisor("Vinay").size());
	}

}
