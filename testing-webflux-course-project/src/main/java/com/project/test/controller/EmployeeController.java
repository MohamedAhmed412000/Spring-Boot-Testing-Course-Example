package com.project.test.controller;

import com.project.test.dto.EmployeeDto;
import com.project.test.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        return employeeService.createEmployee(employeeDto);
    }

    @GetMapping("/{employeeId}")
    public Mono<EmployeeDto> getEmployeeById(@PathVariable("employeeId") String employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    @GetMapping
    public Flux<EmployeeDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PutMapping("/{employeeId}")
    public Mono<EmployeeDto> updateEmployee(
        @PathVariable("employeeId") String employeeId,
        @RequestBody EmployeeDto employeeDto
    ) {
        return employeeService.updateEmployee(employeeId, employeeDto);
    }

    @DeleteMapping("/{employeeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployee(@PathVariable("employeeId") String employeeId) {
        return employeeService.deleteEmployee(employeeId);
    }

}
