package com.effortix.backend.controllers;

import com.effortix.backend.AIServices.GeneratePerfectSkillsAndDetails;
import com.effortix.backend.models.EmployeeSkills;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.services.EmployeeSkillsService;
import com.effortix.backend.services.TicketService;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeSkillsController {
	 @Autowired
	    private EmployeeSkillsService employeeSkillsService;

   
    // Controller method to display all EmployeeSkills entries
    @GetMapping("/employeeSkills")
    public String getAllEmployeeSkills(Model model) {
        List<EmployeeSkills> skillsList = employeeSkillsService.getAllEmployeeSkills();
        model.addAttribute("skillsList", skillsList);
        return "skills/employeeSkills"; // Return Thymeleaf template name
    }
    
}

class EmployeeSuperSkills{
	List<String> Skills= new ArrayList<String>();
	List<String> Sub_Skills= new ArrayList<String>();
	public List<String> getSkills() {
		return Skills;
	}
	public void setSkills(List<String> skills) {
		Skills = skills;
	}
	public List<String> getSub_Skills() {
		return Sub_Skills;
	}
	public void setSub_Skills(List<String> sub_Skills) {
		Sub_Skills = sub_Skills;
	}
	
}
@Controller
@RequestMapping("/employeeSkillsREST")
class EmployeeSkillsREST { 
	
	 @Autowired
	    private EmployeeSkillsService employeeSkillsService;

	 @Autowired
	 private GeneratePerfectSkillsAndDetails generatePerfectSkillsAndDetailsAI;
    // Endpoint to fetch tickets for the selected employee and flag = 0
    @GetMapping("/get")
    @ResponseBody  // Returns the response as JSON
    public Map<String, List<String>> getSkillsAndSkillDetails(@RequestParam("employeeId") Long employeeId) {
    	List<EmployeeSkills> EmployeeSkills=employeeSkillsService.getEmployeeSkillsByEId(employeeId);
    	 Gson gson = new Gson();
    	String EmployeeSkillsJson = gson.toJson(EmployeeSkills);
    	System.out.println("EmployeeSkillsJson: "+EmployeeSkillsJson);
    	
    	return generatePerfectSkillsAndDetailsAI.GeneratePerfectSkillsAndDetailsAI(EmployeeSkillsJson);  // Flag = 0 for filtering tickets
    }
    
    @GetMapping("/getAll")
    @ResponseBody  // Returns the response as JSON
    public ResponseEntity<List<EmployeeSkills>> getAllSkillsAndSkillDetails() {
    	List<EmployeeSkills> EmployeeSkills=employeeSkillsService.getAllEmployeeSkills();
    return ResponseEntity.ok(EmployeeSkills);
    }
    
}