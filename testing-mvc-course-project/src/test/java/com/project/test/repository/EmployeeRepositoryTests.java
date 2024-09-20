package com.project.test.repository;

import com.project.test.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;
    private List<Employee> employees;

    @BeforeEach
    public void setUp() {
        employees = new ArrayList<>();
        employees.add(Employee.builder()
                .firstName("Mohamed")
                .lastName("Ahmed")
                .email("mahmed@example.com")
                .build());
        employees.add(Employee.builder()
                .firstName("Ahmed")
                .lastName("Samir")
                .email("asamir@example.com")
                .build());
    }

    // Junit test for seve employee operations
    @DisplayName("Junit Test for saving employee")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // Given
        Employee employee = employees.get(0);
        // When
        Employee savedEmployee = employeeRepository.save(employee);
        // Then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    // Junit test for get all employees operations
    @DisplayName("Junit Test for getting all employees")
    @Test
    public void givenManyEmployeeObjectsInDB_whenGetAllEmployees_thenReturnListOfEmployees() {
        // Given
        employeeRepository.saveAll(employees);
        // When
        List<Employee> employees = employeeRepository.findAll();
        // Then
        assertThat(employees).isNotNull();
        assertThat(employees).size().isEqualTo(2);
    }

    // Junit test for getting employee by id
    @DisplayName("Junit Test for getting employee by id")
    @Test
    public void givenEmployeeObjectInDB_whenGettingEmployeeById_thenRetuenEmployeeObject() {
        // Given
        Employee employee = employeeRepository.save(employees.get(0));
        // When
        Employee retrievedEmployee = employeeRepository.findById(employee.getId()).get();
        // Then
        assertThat(retrievedEmployee).isNotNull();
        assertThat(retrievedEmployee.getFirstName()).isEqualTo("Mohamed");
    }

    // Junit test for getting employee by email
    @DisplayName("Junit Test for getting employee by email")
    @Test
    public void givenEmployeeObjectInDB_whenGettingEmployeeByEmail_thenReturnEmployeeObject() {
        // Given
        Employee employee = employeeRepository.save(employees.get(0));
        // When
        Employee retrievedEmployee = employeeRepository.findByEmail(employee.getEmail()).get();
        // Then
        assertThat(retrievedEmployee).isNotNull();
        assertThat(retrievedEmployee.getFirstName()).isEqualTo("Mohamed");
    }

    // Junit test for updating employee from database
    @DisplayName("Junit Test for updating employee from database")
    @Test
    public void givenEmployeeObjectInDB_whenUpdatingEmployeeObject_thenReturnUpdatedEmail() {
        // Given
        Employee employee = employeeRepository.save(employees.get(0));
        // When
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        String updatedEmail = "mahmed123@example.com";
        savedEmployee.setEmail(updatedEmail);
        Employee updatedEmployee = employeeRepository.save(savedEmployee);
        // Then
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo(updatedEmail);
    }

    // Junit test for deleting employee from database
    @DisplayName("Junit Test for deleting employee from database")
    @Test
    public void givenEmployeeObjectInDB_whenDeletingEmployee_thenCheckIfDeleted() {
        // Given
        Employee employee = employeeRepository.save(employees.get(0));
        // When
        employeeRepository.delete(employee);
        Optional<Employee> retrievedEmployee = employeeRepository.findById(employee.getId());
        // Then
        assertThat(retrievedEmployee).isEmpty();
        assertThat(employeeRepository.count()).isZero();
    }

    // Junit test for getting employee using first name and last name
    @DisplayName("Junit Test for getting employee by firstname & lastname")
    @Test
    public void givenEmployeeObjectInDB_whenGettingEmployeeByFirstNameAndLastName_thenReturnEmployeeObject() {
        // Given
        Employee employee = employeeRepository.save(employees.get(0));
        // When
        Employee retrievedEmployee = employeeRepository.findByJpql("Mohamed", "Ahmed").get();
        // Then
        assertThat(retrievedEmployee).isNotNull();
        assertThat(retrievedEmployee.getFirstName()).isEqualTo("Mohamed");
        assertThat(retrievedEmployee.getLastName()).isEqualTo("Ahmed");
    }

}
