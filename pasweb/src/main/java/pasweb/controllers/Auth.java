package pasweb.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class Auth {
    @GetMapping("/login")
    public String doLogin() {
        return "/loginPage";
    }

    @GetMapping("/logout")
    public String doLogout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/login?logout";
    }
}
