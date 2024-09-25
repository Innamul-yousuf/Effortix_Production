package com.effortix.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effortix.backend.models.Project;
import com.effortix.backend.repository.ProjectRepository;

	@Service
	public class ProjectService {

	    @Autowired
	    private ProjectRepository projectRepository;

	    // Method to save a project
	    public Project saveProject(Project project) {
	        return projectRepository.save(project);
	    }

	    // Method to get all projects
	    public List<Project> getAllProjects() {
	        return projectRepository.findAll();
	    }

	    // Method to get a project by its ID
	    public Optional<Project> getProjectById(Long id) {
	        return projectRepository.findById(id);
	    }

	    // Method to update a project
	    public Project updateProject(Long id, Project updatedProject) {
	        Optional<Project> existingProject = projectRepository.findById(id);

	        if (existingProject.isPresent()) {
	            Project project = existingProject.get();
	            project.setpName(updatedProject.getpName());
	            project.setpDetails(updatedProject.getpDetails());
	            project.setpFlag(updatedProject.getpFlag());
	            project.setpPriority(updatedProject.getpPriority());
	            project.setpStartDate(updatedProject.getpStartDate());
	            project.setpEndDate(updatedProject.getpEndDate());
	            project.setpSkills(updatedProject.getpSkills());
	            project.setpConcepts(updatedProject.getpConcepts());
	            return projectRepository.save(project);
	        } else {
	            return null; // or throw an exception, e.g. ProjectNotFoundException
	        }
	    }

	    // Method to delete a project
	    public void deleteProject(Long id) {
	        projectRepository.deleteById(id);
	    }
	}
	

