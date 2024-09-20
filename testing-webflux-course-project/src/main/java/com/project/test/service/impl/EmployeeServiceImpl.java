package com.project.test.service.impl;

import com.project.test.dto.EmployeeDto;
import com.project.test.entity.Employee;
import com.project.test.mapper.EmployeeMapper;
import com.project.test.repository.EmployeeRepository;
import com.project.test.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public Mono<EmployeeDto> createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Mono<Employee> savedEmployee = employeeRepository.save(employee);
        return savedEmployee.map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Mono<EmployeeDto> getEmployeeById(String employeeId) {
        Mono<Employee> employee = employeeRepository.findById(employeeId);
        return employee.map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        Flux<Employee> employees = employeeRepository.findAll();
        return employees.map(EmployeeMapper::mapToEmployeeDto)
            .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(String employeeId, EmployeeDto employeeDto) {
        Mono<Employee> savedEmployee = employeeRepository.findById(employeeId);
        Mono<Employee> updatedEmployee = savedEmployee.flatMap((employee) -> {
            employee.setFirstname(employeeDto.getFirstName());
            employee.setLastname(employeeDto.getLastName());
            employee.setEmail(employeeDto.getEmail());
            return employeeRepository.save(employee);
        });
        return updatedEmployee.map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Mono<Void> deleteEmployee(String employeeId) {
        return employeeRepository.deleteById(employeeId);
    }

}
