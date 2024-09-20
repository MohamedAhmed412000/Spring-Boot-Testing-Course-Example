package com.project.test.service;

import com.project.test.dto.EmployeeDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {

    Mono<EmployeeDto> createEmployee(EmployeeDto employeeDto);
    Mono<EmployeeDto> getEmployeeById(String employeeId);
    Flux<EmployeeDto> getAllEmployees();
    Mono<EmployeeDto> updateEmployee(String employeeId, EmployeeDto employee);
    Mono<Void> deleteEmployee(String employeeId);

}
