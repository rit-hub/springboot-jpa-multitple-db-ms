package com.rithub.jpa.mysql.repository;

import com.rithub.jpa.mysql.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
