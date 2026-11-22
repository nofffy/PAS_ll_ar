package pasrest.api.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pasrest.api.DTOs.CreateUpdatePlantDTO;
import pasrest.api.DTOs.PlantResponseDTO;
import pasrest.managers.PlantManager;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {
    private final PlantManager plantManager;

    public PlantController(PlantManager plantManager) {
        this.plantManager = plantManager;
    }

    @GetMapping
    public ResponseEntity<List<PlantResponseDTO>> getPlants() {
        return ResponseEntity.ok(plantManager.getPlants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantResponseDTO> getPlantById(@PathVariable("id") String id) {
        PlantResponseDTO plant = plantManager.getPlant(id);
        return ResponseEntity.ok(plant);
    }

    @PostMapping
    public ResponseEntity<Void> createPlant(@Valid @RequestBody CreateUpdatePlantDTO plant) {
        PlantResponseDTO createdPlant = plantManager.createPlant(plant);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPlant.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlant(@PathVariable("id") String id, @Valid @RequestBody CreateUpdatePlantDTO plantDTO) {
        plantManager.updatePlant(id, plantDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable("id") String id) {
        plantManager.removePlant(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<PlantResponseDTO>> getAvailablePlants() {
        return ResponseEntity.ok(plantManager.getAvailablePlants());
    }
}
