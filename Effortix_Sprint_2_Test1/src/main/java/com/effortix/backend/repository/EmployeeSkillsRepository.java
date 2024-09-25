package com.effortix.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.effortix.backend.models.EmployeeSkills;
import com.effortix.backend.models.ProjectEmployee;

@Repository
public interface EmployeeSkillsRepository extends JpaRepository<EmployeeSkills, Long> { // Custom queries can be added
	 List<EmployeeSkills> findByEId(Long EId);													// here if needed

}
