
  package com.effortix.backend.services;
  
  import org.springframework.beans.factory.annotation.Autowired; import
  org.springframework.stereotype.Service;

import com.effortix.backend.models.Project;
import com.effortix.backend.models.ProjectEmployee;
import com.effortix.backend.repository.ProjectEmployeeRepository;

import java.util.List; import java.util.Optional;
  
  @Service public class ProjectEmployeeService {
  
  @Autowired private ProjectEmployeeRepository projectEmployeeRepository;
  
  // Save or Update ProjectEmployee
  public ProjectEmployee saveOrUpdateProjectEmployee(ProjectEmployee projectEmployee) { 
	  return projectEmployeeRepository.save(projectEmployee);
}
  
  // Get ProjectEmployee by ID 
  public Optional<ProjectEmployee> getProjectEmployeeById(Long peID) { return
  projectEmployeeRepository.findById(peID); }
  
  // Get All ProjectEmployees 
  public List<ProjectEmployee> getAllProjectEmployees() { return projectEmployeeRepository.findAll(); }
  
  // Delete ProjectEmployee by ID 
  public void deleteProjectEmployee(Long peID){ 
	  projectEmployeeRepository.deleteById(peID); } 
  
  public List<Project> getProjectsByEmployeeId(Long employeeId) {
      return projectEmployeeRepository.findProjectsByEmployeeId(employeeId);
  }
  
  
  }
 


