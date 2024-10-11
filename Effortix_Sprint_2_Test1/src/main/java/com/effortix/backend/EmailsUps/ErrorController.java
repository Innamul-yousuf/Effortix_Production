package com.effortix.backend.EmailsUps;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(org.springframework.ui.Model model, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "An unexpected error occurred";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == 404) {
                errorMessage = "Page not found";
            } else if (statusCode == 500) {
                errorMessage = "Internal server error";
            }
        }
        
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
    
    @RequestMapping("favicon.ico")
    @ResponseBody
    void handleFaviconRequest() {
        // Do nothing, prevents 404 errors for favicon
    }
}
