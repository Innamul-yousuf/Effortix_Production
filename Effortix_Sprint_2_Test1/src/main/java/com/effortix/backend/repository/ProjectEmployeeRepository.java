package com.effortix.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.effortix.backend.models.Project;
import com.effortix.backend.models.ProjectEmployee;

@Repository
public interface ProjectEmployeeRepository extends JpaRepository<ProjectEmployee, Long> { // Custom queries can be added
																							// here if needed
	@Query("SELECT pe.project FROM ProjectEmployee pe WHERE pe.employee.eId = :employeeId")
    List<Project> findProjectsByEmployeeId(@Param("employeeId") Long employeeId);
 
}
