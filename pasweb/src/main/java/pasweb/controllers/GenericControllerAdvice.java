package pasweb.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GenericControllerAdvice {
    @ExceptionHandler
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "/error";
    }
}
