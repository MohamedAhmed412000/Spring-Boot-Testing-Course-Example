package com.project.test.repository;

import com.project.test.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// We don't add here the repository annotation as JpaRepository will be attached
// to Bean of type SimpleJpaRepository which has @Repository annotation and @Transaction annotation also
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.email = :email")
    Optional<Employee> findByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 AND e.lastName = ?2")
    Optional<Employee> findByJpql(String firstName, String lastName);

}
