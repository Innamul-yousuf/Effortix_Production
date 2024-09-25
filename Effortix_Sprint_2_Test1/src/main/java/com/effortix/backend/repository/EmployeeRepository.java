package com.effortix.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.effortix.backend.models.Employee;

@Repository
public
interface EmployeeRepository extends JpaRepository<Employee, Long> {
	 // Additional query methods can be defined here if needed
}