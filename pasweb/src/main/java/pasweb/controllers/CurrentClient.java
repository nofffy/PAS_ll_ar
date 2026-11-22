package pasweb.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pasweb.DTOs.ClientDTO;
import pasweb.rest.ClientApi;

@Controller
@RequestMapping("/current")
public class CurrentClient {
    private final ClientApi clientApi;

    public CurrentClient(ClientApi clientApi) {
        this.clientApi = clientApi;
    }

    @GetMapping
    public String getCurrentClient(Model model, Authentication authentication) {
        ClientDTO client = clientApi.getClientByLogin(authentication.getName());
        model.addAttribute("client", client);
        return "currentClient";
    }
}
