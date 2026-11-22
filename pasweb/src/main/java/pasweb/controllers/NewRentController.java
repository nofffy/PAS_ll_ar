package pasweb.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import pasweb.DTOs.ClientDTO;
import pasweb.DTOs.PlantDTO;
import pasweb.DTOs.RentDTO;
import pasweb.rest.ClientApi;
import pasweb.rest.PlantApi;
import pasweb.rest.RentApi;

@Controller
@RequestMapping("/newRent")
@SessionAttributes("newRent")
public class NewRentController {

    private final RentApi rentApi;
    private final PlantApi plantApi;
    private final ClientApi clientApi;

    public NewRentController(RentApi rentApi, PlantApi plantApi, ClientApi clientApi) {
        this.rentApi = rentApi;
        this.plantApi = plantApi;
        this.clientApi = clientApi;
    }

    @GetMapping
    public String startNewRent(Model model, Authentication authentication) {
        ClientDTO currentClient = clientApi.getClientByLogin(authentication.getName());
        RentDTO rentDTO = new RentDTO(currentClient, new PlantDTO());

        model.addAttribute("newRent", rentDTO);
        model.addAttribute("plants", plantApi.getAvailablePlants());
        return "newRent";
    }

    @PostMapping("/confirm")
    public String confirmNewRent(@ModelAttribute("newRent") RentDTO newRent, BindingResult bindingResult, Model model) {
        if (newRent.getPlant() == null || newRent.getPlant().getId() == null || newRent.getPlant().getId().isBlank()) {
            bindingResult.rejectValue("plant.id", "error.plant", "Choose a plant from the list");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("plants", plantApi.getAllPlants());
            return "newRent";
        }
        PlantDTO selectedPlant = plantApi.getPlant(newRent.getPlant().getId());
        newRent.setPlant(selectedPlant);

        return "newRentConfirm";
    }

    @PostMapping("/save")
    public String saveNewRent(@ModelAttribute("newRent") RentDTO newRent, BindingResult bindingResult, Model model, SessionStatus sessionStatus) {
        try {
            rentApi.createRent(newRent);
            sessionStatus.setComplete();
            return "redirect:/success.html";
        } catch (HttpClientErrorException.Conflict e) {
            bindingResult.rejectValue("plant.id", "error.plant", "This plant is already rented");
            model.addAttribute("plants", plantApi.getAllPlants());
            return "newRent";
        }
    }
}
