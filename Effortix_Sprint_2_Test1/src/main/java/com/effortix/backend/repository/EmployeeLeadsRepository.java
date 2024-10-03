package com.effortix.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeLeads;
import com.effortix.backend.models.EmployeeSkills;
import com.effortix.backend.models.ProjectEmployee;

@Repository
public interface EmployeeLeadsRepository extends JpaRepository<EmployeeLeads, Long> {
	 Optional<EmployeeLeads> findByEmployee(Employee employee);
	 
}