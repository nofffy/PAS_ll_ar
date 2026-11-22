package pasweb.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pasweb.DTOs.ClientDTO;
import pasweb.rest.ClientApi;
import pasweb.rest.RentApi;

@Controller
@RequestMapping("/listRents")
public class ListRents {
    private final RentApi rentApi;
    private final ClientApi clientApi;

    public ListRents(RentApi rentApi, ClientApi clientApi) {
        this.rentApi = rentApi;
        this.clientApi = clientApi;
    }

    @GetMapping
    public String listRents(Model model, Authentication authentication) {
        ClientDTO currentClient = clientApi.getClientByLogin(authentication.getName());
        model.addAttribute("rents", rentApi.getUserRents(currentClient.getId()));
        return "listRents";
    }

    @PostMapping("/{id}/finish")
    public String finishRent(@PathVariable("id") String id) {
        rentApi.finishRent(id);
        return "redirect:/listRents";
    }
}
