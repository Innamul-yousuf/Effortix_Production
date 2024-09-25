/*
 * package project;
 * 
 * import java.util.List;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.ModelAttribute; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.ModelAttribute; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestMapping;
 * 
 * import java.util.List;
 * 
 * @Controller
 * 
 * @RequestMapping("/employees") public class EmployeeController {
 * 
 * @Autowired private EmployeeService employeeService;
 * 
 * @GetMapping public String listEmployees(Model model) { List<Employee>
 * employees = employeeService.getAllEmployees();
 * model.addAttribute("employees", employees); return "employee_list"; //
 * Thymeleaf template name (employee_list.html) }
 * 
 * @GetMapping("/new") public String showEmployeeForm(Model model) {
 * model.addAttribute("employee", new Employee()); return "employee_form"; //
 * Thymeleaf template name (employee_form.html) }
 * 
 * @PostMapping("/save") public String saveEmployee(@ModelAttribute("employee")
 * Employee employee) { employeeService.saveOrUpdateEmployee(employee); return
 * "redirect:/employees"; } }
 * 
 */
package com.effortix.backend.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.effortix.backend.models.Project;
import com.effortix.backend.services.ProjectService;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // GET all projects and display them on a UI page
    @GetMapping("/allProject")
    public String getAllProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "project_list"; // Returns the Thymeleaf template 'project_list.html'
    }

    // GET a single project by its ID
    @GetMapping("/project/{id}")
    public String getProjectById(@PathVariable("id") Long id, Model model) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project.isPresent()) {
            model.addAttribute("project", project.get());
            return "project_detail"; // Returns the 'project_detail.html' page
        } else {
            model.addAttribute("errorMessage", "Project not found");
            return "error"; // Returns an error page if the project isn't found
        }
    }

    // GET the form to create a new project
    @GetMapping("/project/new")
    public String showCreateProjectForm(Model model) {
        model.addAttribute("project", new Project()); // Empty Project object for form binding
        return "create_project"; // Returns the 'create_project.html' form
    }

    // POST method to create a new project
    @PostMapping
    public String createProject(@ModelAttribute("project") Project project) {
        projectService.saveProject(project);
        return "redirect:/projects"; // Redirect to the list of projects after saving
    }

    // GET the form to edit an existing project
    @GetMapping("/{id}/edit")
    public String showEditProjectForm(@PathVariable("id") Long id, Model model) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project.isPresent()) {
            model.addAttribute("project", project.get());
            return "edit_project"; // Returns the 'edit_project.html' form
        } else {
            model.addAttribute("errorMessage", "Project not found");
            return "error"; // Returns an error page if the project isn't found
        }
    }

    // POST method to update an existing project
    @PostMapping("/{id}/update")
    public String updateProject(@PathVariable("id") Long id, @ModelAttribute("project") Project project) {
        projectService.updateProject(id, project);
        return "redirect:/projects"; // Redirect to the project list after updating
    }

    // DELETE a project by its ID
    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable("id") Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects"; // Redirect to the project list after deleting
    }
}