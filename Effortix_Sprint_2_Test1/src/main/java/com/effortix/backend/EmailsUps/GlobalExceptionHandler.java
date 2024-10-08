package com.effortix.backend.EmailsUps;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, Model model) {
        // Log the exception if needed
        ex.printStackTrace();
        
        // Create a ModelAndView object to redirect to the error page
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");  // View name "error" should match your error page template
        modelAndView.addObject("errorMessage", ex.getMessage());  // You can pass additional info to the error page

        return modelAndView;
    }
}

