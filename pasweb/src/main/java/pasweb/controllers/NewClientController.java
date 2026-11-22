package pasweb.controllers;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import pasweb.rest.ClientApi;
import pasweb.DTOs.ClientDTO;
import pasweb.DTOs.NewClientDTO;

import java.util.Objects;

@Controller
@RequestMapping("/newClient")
@SessionAttributes("newClient")
public class NewClientController {
    private final ClientApi clientApi;
    private final PasswordEncoder passwordEncoder;

    public NewClientController(ClientApi clientApi, PasswordEncoder passwordEncoder) {
        this.clientApi = clientApi;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping()
    public String startNewClient(Model model) {
        model.addAttribute("newClient", new NewClientDTO(new ClientDTO(),""));
        return "/newClient";
    }

    @PostMapping("/confirm")
    public String confirmNewClient(@ModelAttribute("newClient") @Valid NewClientDTO newClient, BindingResult bindingResult) {
        if (!Objects.equals(newClient.getClientData().getPassword(), newClient.getPasswordRepeat())) {
            bindingResult.rejectValue("passwordRepeat", "error.passwordRepeat", "Passwords do not match");
        }
        if(bindingResult.hasErrors()) {
            return "/newClient";
        }
        return "/newClientConfirm";
    }

    @PostMapping("/save")
    public String saveNewClient(@ModelAttribute("newClient") NewClientDTO newClient, BindingResult bindingResult, SessionStatus sessionStatus) {
        ClientDTO source = newClient.getClientData();
        ClientDTO payload = new ClientDTO(
                source.getLogin(),
                passwordEncoder.encode(source.getPassword()),
                source.getEmail(),
                source.getAge(),
                true
        );
        try {
            clientApi.registerClient(payload);
            sessionStatus.setComplete();
            return "redirect:/success.html";
        } catch (HttpClientErrorException.Conflict e) {
            bindingResult.rejectValue("clientData.login", "error.login", "Login is already taken");
            return "newClient";
        }
    }
}
