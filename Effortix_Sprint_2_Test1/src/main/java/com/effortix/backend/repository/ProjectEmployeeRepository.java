package com.effortix.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.effortix.backend.models.ProjectEmployee;

@Repository
public interface ProjectEmployeeRepository extends JpaRepository<ProjectEmployee, Long> { // Custom queries can be added
																							// here if needed

}
