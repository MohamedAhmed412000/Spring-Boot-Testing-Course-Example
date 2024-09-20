package com.project.test.service.impl;

import com.project.test.exception.ResourceAlreadyExists;
import com.project.test.model.Employee;
import com.project.test.repository.EmployeeRepository;
import com.project.test.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        Optional<Employee> searchedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (searchedEmployee.isPresent()) {
            throw new ResourceAlreadyExists("Employee with email " +
                employee.getEmail() + " already exists");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
