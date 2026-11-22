package pasweb.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import pasweb.DTOs.PlantDTO;
import pasweb.rest.PlantApi;

@Controller
@RequestMapping("/newPlant")
@SessionAttributes("newPlant")
public class NewPlantController {
    private final PlantApi plantApi;

    public NewPlantController(PlantApi plantApi) {
        this.plantApi = plantApi;
    }

    @GetMapping()
    public String startNewPlant(Model model) {
        PlantDTO plantDTO = new PlantDTO();
        plantDTO.setType("BASIC");
        model.addAttribute("newPlant", plantDTO);
        return "/newPlant";
    }

    @PostMapping("/confirm")
    public String confirmNewPlant(@ModelAttribute("newPlant") @Valid PlantDTO plantDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "newPlant";
        }
        return "/newPlantConfirm";
    }

    @PostMapping("/save")
    public String saveNewPlant(@ModelAttribute("newPlant") PlantDTO newPlant, BindingResult bindingResult, SessionStatus sessionStatus) {
        try {
            plantApi.createPlant(newPlant);
            sessionStatus.setComplete();
            return "redirect:/success.html";
        } catch (HttpClientErrorException.Conflict e) {
            return "newPlant";
        }
    }
}
