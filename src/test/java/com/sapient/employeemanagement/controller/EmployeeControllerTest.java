package com.sapient.employeemanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sapient.employeemanagement.model.Employee;
import com.sapient.employeemanagement.service.EmployeeService;

@WebAppConfiguration
public class EmployeeControllerTest {

	public MockMvc mockMvc;

	@Mock
	EmployeeService employeeService;

	@InjectMocks
	EmployeeController employeeController;
	
	Employee emp;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
		
		emp = new Employee();
		emp.setEmployeeId(1);
		emp.setEmployeeName("Sarthak");
		emp.setPlace("Bangalore");
		emp.setCompetencies("Java");
		emp.setBusinessUnit("ISE");
		emp.setSalary(100.0);
		emp.setSupervisorId("Vinay");
		emp.setTitle("SE");
	}
	
	@Test
    public void getEmployeesByPlaceTest1() throws Exception 
    {
		mockMvc.perform(get("/employees/place/Bangalore"))
		.andExpect(status().isOk());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath("/employees/place/{place}");
        request.setParameter("place", "Bangalore");
       // request.addParameter("percentage", "10.0");
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
         
        when(employeeService.getListOfEmployeeByPlace("Bangalore")).thenReturn(Arrays.asList(emp));
         
        List<Object> emp=employeeController.getEmployeesByPlace("Bangalore");
         
        assertThat(emp.size()).isEqualTo(1);
    }

	@Test
	public void updateSalaryPercentageOkTest() throws Exception {
		mockMvc.perform(put("/employees/place/Bangalore/salary/10"))
				// .param("planId", "plan123")
				.andExpect(status().isOk());
		// .andExpect(content().contentType("application/json;-8)) //line D

	}
	
	@Test
	public void updateSalaryPercentageUrlNotFoundTest() throws Exception {
		mockMvc.perform(put("/employees/place/Bangalore/alary/10"))
				.andExpect(status().isNotFound());

	}
	
	@Test
	public void getEmployeesByPlaceTest() throws Exception {
		 mockMvc.perform(get("/employees/place/Bangalore"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getTotalSalaryOfEmployeeByColumnNameTest() throws Exception {
		 mockMvc.perform(get("/employees/totalsalary"))
				.andExpect(status().isOk());
	}
	
	
	

}
