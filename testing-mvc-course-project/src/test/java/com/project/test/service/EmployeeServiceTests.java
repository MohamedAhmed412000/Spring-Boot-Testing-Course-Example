package com.project.test.service;

import com.project.test.exception.ResourceAlreadyExists;
import com.project.test.model.Employee;
import com.project.test.repository.EmployeeRepository;
import com.project.test.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;


import java.util.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;
    private List<Employee> employeeList;

    @BeforeEach
    public void setup() {
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);
        employeeList = new ArrayList<>();
        employeeList.add(Employee.builder()
                .id(1L)
                .firstName("Mohamed")
                .lastName("Ahmed")
                .email("mahmed@gmail.com")
                .build());
        employeeList.add(Employee.builder()
                .id(2L)
                .firstName("Ahmed")
                .lastName("Samir")
                .email("asamir@gmail.com")
                .build());
    }

    // Junit test for saving employee method
    @DisplayName("Junit test for saving employee method")
    @Test
    public void givenEmployeeObject_whenCreatingNewEmployee_thenReturnEmployeeObject() {
        // Given
        Employee employee = employeeList.get(0);
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);
        // When
        Employee createdEmployee = employeeServiceImpl.createEmployee(employee);
        // Then
        assertThat(createdEmployee).isNotNull();
    }

    // Junit test for saving employee method when throwing an exception
    @DisplayName("Junit test for saving employee method when throw exception")
    @Test
    public void givenEmployeeObject_whenCreatingNewEmployee_thenThrowException() {
        // Given
        Employee employee = employeeList.get(0);
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));
        // When
        assertThrows(ResourceAlreadyExists.class, () -> employeeServiceImpl.createEmployee(employee));
        // Then verify that saving is never called
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // Junit test for getting all employees
    @DisplayName("Junit test for getting all employees")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        // Given
        given(employeeRepository.findAll()).willReturn(employeeList);
        // When
        List<Employee> employees = employeeServiceImpl.getEmployees();
        // Then
        assertThat(employees).isNotNull();
        assertThat(employees).hasSize(2);
    }

    // Junit test for getting all employees (Empty list)
    @DisplayName("Junit test for getting all employees (Negative Scenario)")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmptyList() {
        // Given
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        // When
        List<Employee> employees = employeeServiceImpl.getEmployees();
        // Then
        assertThat(employees).isNotNull();
        assertThat(employees).hasSize(0);
    }

    // Junit test for getting employee by id
    @DisplayName("Junit test for getting employee by id")
    @Test
    public void givenEmployeeId_whenGettingEmployeeId_thenReturnEmployeeObject() {
        // Given
        Employee employee = employeeList.get(0);
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        // When
        Employee returnedEmployee = employeeServiceImpl.getEmployeeById(employee.getId()).get();
        // Then
        assertThat(returnedEmployee).isNotNull();
    }

    // Junit test for updating employee
    @DisplayName("Junit test for updating employee")
    @Test
    public void givenEmployeeObject_whenUpdatingEmployee_thenReturnUpdatedEmployee() {
        // Given
        Employee employee = employeeList.get(0);
        given(employeeRepository.save(employee)).willReturn(employee);
        String updatedEmail = "mohamed@gmail.com";
        employee.setEmail(updatedEmail);
        // When
        Employee returnedEmployee = employeeServiceImpl.updateEmployee(employee);
        // Then
        assertThat(returnedEmployee).isNotNull();
        assertThat(returnedEmployee.getEmail()).isEqualTo(updatedEmail);
    }

    // Junit test for deleting employee
    @DisplayName("Junit test for deleting employee")
    @Test
    public void givenEmployeeId_whenDeletingEmployee_thenNoThing() {
        // Given
        willDoNothing().given(employeeRepository).deleteById(1L);
        // When
        employeeServiceImpl.deleteEmployee(1L);
        // Then
        verify(employeeRepository, times(1)).deleteById(1L);
    }

}
