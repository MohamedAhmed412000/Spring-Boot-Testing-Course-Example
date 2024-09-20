package com.project.test.integration;

import com.project.test.dto.EmployeeDto;
import com.project.test.entity.Employee;
import com.project.test.mapper.EmployeeMapper;
import com.project.test.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerITests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private WebTestClient webTestClient;

    private List<EmployeeDto> employeeDtos;

    @PostConstruct
    public void setup() {
        employeeDtos = new ArrayList<>();
        employeeDtos.add(EmployeeDto.builder().id(UUID.randomUUID().toString())
                .firstName("Mohamed").lastName("Ahmed").email("mahmed@gmail.com").build());
        employeeDtos.add(EmployeeDto.builder().id(UUID.randomUUID().toString())
                .firstName("Ahmed").lastName("Samir").email("asamir@gmail.com").build());
    }

    @BeforeEach
    public void cleanup() {
        employeeRepository.deleteAll().block();
    }

    // Junit test for creating a new employee
    @Test
    public void givenEmployeeObject_whenSavingEmployee_thenReturnSavedEmployee() {
        // Given
        EmployeeDto employeeDto = employeeDtos.get(0);

        // When
        WebTestClient.ResponseSpec response =webTestClient.post()
            .uri("/api/v1/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(employeeDto)
            .exchange();

        // Then
        response.expectStatus().isCreated()
            .expectBody()
            .consumeWith(System.out::println)
            .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
            .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
            .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    // Junit test for getting employee by id
    @Test
    public void givenEmployeeId_whenGettingEmployeeById_thenReturnEmployeeObject() {
        // Given
        EmployeeDto employeeDto = employeeDtos.get(0);
        Mono<Employee> savedEmployee = employeeRepository.save(
            EmployeeMapper.mapToEmployee(employeeDto));
        String employeeId = Objects.requireNonNull(savedEmployee.block()).getId();

        // When
        WebTestClient.ResponseSpec response = webTestClient.get()
            .uri("/api/v1/employees/" + employeeId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange();

        // Then
        response.expectStatus().isOk()
            .expectBody()
            .consumeWith(System.out::println)
            .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
            .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
            .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    // Junit test for getting all employees
    @Test
    public void givenListOfEmployeesObjects_whenGettingAllEmployees_thenReturnListOfEmployees() {
        // Given
        employeeDtos.forEach(employeeDto -> {
            employeeRepository.save(EmployeeMapper.mapToEmployee(employeeDto)).block();
        });

        // When
        WebTestClient.ResponseSpec response = webTestClient.get()
            .uri("/api/v1/employees")
            .accept(MediaType.APPLICATION_JSON)
            .exchange();

        // Then
        response.expectStatus().isOk()
            .expectBodyList(EmployeeDto.class).hasSize(employeeDtos.size())
            .consumeWith(System.out::println);
    }

    // Junit test for updating employee
    @Test
    public void givenEmployeeObjectFromDB_whenUpdatingEmployee_thenReturnUpdatedEmployee() {
        // Given
        Mono<Employee> savedEmployee = employeeRepository.save(
            EmployeeMapper.mapToEmployee(employeeDtos.get(0)));
        String employeeId = Objects.requireNonNull(savedEmployee.block()).getId();
        EmployeeDto employeeDto = employeeDtos.get(1);

        // When
        WebTestClient.ResponseSpec response = webTestClient.put()
            .uri("/api/v1/employees/" + employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(employeeDto)
            .exchange();

        // Then
        response.expectStatus().isOk()
            .expectBody()
            .consumeWith(System.out::println)
            .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
            .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
            .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    // Junit test for deleting employee
    @Test
    public void givenEmployeeId_whenDeletingEmployee_thenReturnEmptyResponse() {
        // Given
        Mono<Employee> savedEmployee = employeeRepository.save(
            EmployeeMapper.mapToEmployee(employeeDtos.get(0)));
        String employeeId = Objects.requireNonNull(savedEmployee.block()).getId();

        // When
        WebTestClient.ResponseSpec response = webTestClient.delete()
            .uri("/api/v1/employees/" + employeeId)
            .exchange();

        // Then
        response.expectStatus().isNoContent()
            .expectBody()
            .consumeWith(System.out::println);
    }

}
