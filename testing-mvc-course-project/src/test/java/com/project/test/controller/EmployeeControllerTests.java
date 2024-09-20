package com.project.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.test.model.Employee;
import com.project.test.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    // Junit test for creating a new employee
    @Test
    public void givenEmployeeObject_whenCreatingEmployee_thenReturnSavedEmployee() throws Exception {
        // Given
        Employee employee = Employee.builder()
            .firstName("Mohamed")
            .lastName("Ahmed")
            .email("mahmed@gmail.com")
            .build();
        // Mock create employee method in the service layer
        given(employeeService.createEmployee(ArgumentMatchers.any(Employee.class)))
            .willAnswer(invocation -> invocation.getArgument(0));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employee)));

        // Then
        resultActions.andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
            .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
            .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    // Junit test for listing all employees
    @Test
    public void givenListOfEmployees_whenGettingAllEmployees_thenReturnListOfEmployees() throws Exception {
        // Given
        List<Employee> employees = new ArrayList<>();
        employees.add(Employee.builder().firstName("Mohamed").lastName("Ahmed").email("mahmed@gmail.com").build());
        employees.add(Employee.builder().firstName("Ahmed").lastName("Samir").email("asamir@gmail.com").build());
        given(employeeService.getEmployees()).willReturn(employees);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employees"));

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(employees.size())));
    }

    // Positive scenario
    // Junit test for getting employee by id
    @Test
    public void givenEmployeeId_whenGettingEmployeeById_thenReturnEmployeeObject() throws Exception {
        // Given
        long employeeId = 1L;
        Employee employee = Employee.builder()
           .firstName("Mohamed")
           .lastName("Ahmed")
           .email("mahmed@gmail.com")
           .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employees/{id}",
            employeeId));

        // Then
        resultActions.andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
           .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
           .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    // Negative scenario
    // Junit test for getting employee by id
    @Test
    public void givenInvalidEmployeeId_whenGettingEmployeeById_thenReturnEmptyResponse() throws Exception {
        // Given
        long employeeId = 5L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employees/{id}",
            employeeId));

        // Then
        resultActions.andDo(print()).andExpect(status().isNotFound());
    }

    // Positive scenario
    // Junit test for updating employee
    @Test
    public void givenUpdatedEmployee_whenUpdatingEmployee_thenReturnTheUpdatedEmployee() throws Exception {
        // Given
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
            .firstName("Ahmed")
            .lastName("Samir")
            .email("asamir@gmail.com")
            .build();
        Employee updatedEmployee = Employee.builder()
            .firstName("Mohamed")
            .lastName("Ahmed")
            .email("mahmed@gmail.com")
            .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
            .willAnswer(invocation -> invocation.getArgument(0));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/v1/employees/{id}", employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedEmployee)));

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
            .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
            .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    // Negative scenario
    // Junit test for updating employee
    @Test
    public void givenInvalidEmployeeId_whenUpdatingEmployee_thenReturnEmptyResponse() throws Exception {
        // Given
        long employeeId = 5L;
        Employee updatedEmployee = Employee.builder()
                .firstName("Mohamed")
                .lastName("Ahmed")
                .email("mahmed@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/v1/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // Then
        resultActions.andDo(print()).andExpect(status().isNotFound());
    }

    // Junit test for deleting employee
    @Test
    public void givenEmployeeId_whenDeletingEmployee_thenReturn200() throws Exception {
        // Given
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/employees/{id}",
            employeeId));

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isString());
    }

}
