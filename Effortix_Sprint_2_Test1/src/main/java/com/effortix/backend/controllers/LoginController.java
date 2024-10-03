package com.effortix.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.effortix.backend.models.Employee;
import com.effortix.backend.services.EmployeeService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LoginController {
	 @Autowired
	    private EmployeeService employeeService;

	    // Display login page
	    @GetMapping("/login")
	    public String showLoginPage() {
	        return "login/login";
	    }

	    // Handle login form submission
	    @PostMapping("/login")
	    public String login(@RequestParam("eId") Long eId,
	                        @RequestParam("ePassword") String ePassword,
	                        HttpSession session, Model model) {

	        // Check if the employee exists and password matches
	        Employee employee = employeeService.authenticateEmployee(eId, ePassword);
	        if (employee != null) {
	            // Store employee in session
	            session.setAttribute("employee", employee);
	            
	            return "redirect:/dashboard"; // Redirect to dashboard or homepage after successful login
	        } else {
	            // Add an error message and reload the login page
	            model.addAttribute("error", "Invalid eId or password. Please try again.");
	            return "login/login";
	        }
	    }

	    // Handle logout
	    @GetMapping("/logout")
	    public String logout(HttpSession session) {
	        session.invalidate(); // Clear session on logout
	        return "redirect:/login"; // Redirect to login page
	    }
	    
	    @GetMapping("/dashboard1")
	    public String showDashboard() {
	        return "pages/dashboard"; // Thymeleaf will resolve this to 'pages/dashboard.html'
	    }
	    
	    @GetMapping("/dashboard")
	    public ModelAndView  showDashboard2(HttpSession session) {
	    	   ModelAndView modelAndView = new ModelAndView();
	           
	    	if (session.getAttribute("employee") == null) {
	            // Redirect to login page if not logged in
	            modelAndView.setViewName("redirect:/login");
	        } else {
	            // Load dashboard if logged in
	            modelAndView.setViewName("pages/dashboard");
	        }
			return modelAndView;
	    	
	        
	    }
}
