package com.project.test.controller;

import com.project.test.dto.EmployeeDto;
import com.project.test.service.EmployeeService;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService employeeService;

    private List<EmployeeDto> employeeDtos;

    @PostConstruct
    public void setup() {
        employeeDtos = new ArrayList<>();
        employeeDtos.add(EmployeeDto.builder().id(UUID.randomUUID().toString())
            .firstName("Mohamed").lastName("Ahmed").email("mahmed@gmail.com").build());
        employeeDtos.add(EmployeeDto.builder().id(UUID.randomUUID().toString())
            .firstName("Ahmed").lastName("Samir").email("asamir@gmail.com").build());
    }

    // Junit test for creating a new employee
    @Test
    public void givenEmployeeObject_whenSavingEmployee_thenReturnSavedEmployee() {
        // Given
        EmployeeDto employeeDto = employeeDtos.get(0);
        given(employeeService.createEmployee(ArgumentMatchers.any(EmployeeDto.class)))
            .willReturn(Mono.just(employeeDto));

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
        String employeeId = employeeDto.getId();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Mono.just(employeeDto));

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
        given(employeeService.getAllEmployees()).willReturn(Flux.fromIterable(employeeDtos));

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
        String employeeId = employeeDtos.get(0).getId();
        EmployeeDto employeeDto = employeeDtos.get(1);
        given(employeeService.updateEmployee(ArgumentMatchers.eq(employeeId),
            ArgumentMatchers.any(EmployeeDto.class))).willReturn(Mono.just(employeeDto));

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
        String employeeId = employeeDtos.get(0).getId();
        given(employeeService.deleteEmployee(ArgumentMatchers.eq(employeeId))).willReturn(Mono.empty());

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
