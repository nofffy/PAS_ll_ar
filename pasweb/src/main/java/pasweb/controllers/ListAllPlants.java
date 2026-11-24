package pasweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pasweb.rest.PlantApi;

@Controller
@RequestMapping("/listAllPlants")
public class ListAllPlants {
    private final PlantApi plantApi;

    public ListAllPlants(PlantApi plantApi) {
        this.plantApi = plantApi;
    }

    @GetMapping
    public String listPlants(Model model) {
        model.addAttribute("plants", plantApi.getAllPlants());
        return "listAllPlants";
    }
}
