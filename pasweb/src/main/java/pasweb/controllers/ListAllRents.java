package pasweb.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pasweb.rest.RentApi;

@Controller
@RequestMapping("/listAllRents")
public class ListAllRents {
    private final RentApi rentApi;

    public ListAllRents(RentApi rentApi) {
        this.rentApi = rentApi;
    }

    @GetMapping
    public String listRents(Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("rents", rentApi.getAllRents());
        model.addAttribute("isAdmin", isAdmin);
        return "listAllRents";
    }

    @PostMapping("/{id}/finish")
    public String finishRent(@PathVariable("id") String id) {
        rentApi.finishRent(id);
        return "redirect:/listAllRents";
    }

    @PostMapping("/{id}/delete")
    public String deleteRent(@PathVariable("id") String id) {
        rentApi.deleteRent(id);
        return "redirect:/listAllRents";
    }
}
