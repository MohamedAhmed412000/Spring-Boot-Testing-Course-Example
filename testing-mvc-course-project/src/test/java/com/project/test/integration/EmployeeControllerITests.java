package com.project.test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.test.model.Employee;
import com.project.test.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    // Junit test for creating a new employee
    @Test
    public void givenEmployeeObject_whenCreatingEmployee_thenReturnSavedEmployee() throws Exception {
        // Given
        Employee employee = Employee.builder()
                .firstName("Mohamed")
                .lastName("Ahmed")
                .email("mahmed@gmail.com")
                .build();

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

    @Test
    public void givenListOfEmployees_whenGettingAllEmployees_thenReturnListOfEmployees() throws Exception {
        // Given
        List<Employee> employees = new ArrayList<>();
        employees.add(Employee.builder().firstName("Mohamed").lastName("Ahmed").email("mahmed@gmail.com").build());
        employees.add(Employee.builder().firstName("Ahmed").lastName("Samir").email("asamir@gmail.com").build());
        employeeRepository.saveAll(employees);

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
        Employee employee = Employee.builder()
                .firstName("Mohamed")
                .lastName("Ahmed")
                .email("mahmed@gmail.com")
                .build();
        employeeRepository.save(employee);
        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employees/{id}",
            employee.getId()));

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
        long employeeId = 0L;
        Employee employee = Employee.builder()
                .firstName("Mohamed")
                .lastName("Ahmed")
                .email("mahmed@gmail.com")
                .build();
        employeeRepository.save(employee);

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
        Employee savedEmployee = Employee.builder()
                .firstName("Ahmed")
                .lastName("Samir")
                .email("asamir@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);
        Employee updatedEmployee = Employee.builder()
                .firstName("Mohamed")
                .lastName("Ahmed")
                .email("mahmed@gmail.com")
                .build();

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/v1/employees/{id}",
            savedEmployee.getId())
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
        long employeeId = 0L;
        Employee updatedEmployee = Employee.builder()
                .firstName("Mohamed")
                .lastName("Ahmed")
                .email("mahmed@gmail.com")
                .build();
        employeeRepository.save(updatedEmployee);

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
        Employee employee = Employee.builder()
                .firstName("Mohamed")
                .lastName("Ahmed")
                .email("mahmed@gmail.com")
                .build();
        employeeRepository.save(employee);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/employees/{id}",
            employee.getId()));

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString());
    }

}
