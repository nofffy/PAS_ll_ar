package pasrest.managers;

import org.springframework.stereotype.Service;
import pasrest.Exeptions.ResourceConflictException;
import pasrest.api.DTOmappers.PlantDTOMapper;
import pasrest.Exeptions.ResourceNotFoundException;
import pasrest.api.DTOs.CreateUpdatePlantDTO;
import pasrest.api.DTOs.PlantResponseDTO;
import pasrest.model.Plant;
import pasrest.model.repositories.PlantRepository;

import java.util.List;

@Service
public class PlantManager {
    private final PlantRepository plantRepository;

    public PlantManager(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public PlantResponseDTO createPlant(CreateUpdatePlantDTO plant) {
        Plant newPlant = PlantDTOMapper.DTOToModel(plant);
        Plant plantWithId = plantRepository.addPlant(newPlant);
        return PlantDTOMapper.ModelToDTO(plantWithId);
    }

    public PlantResponseDTO getPlant(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        Plant plant = plantRepository.getPlant(id);
        if (plant == null) {
            throw new ResourceNotFoundException("Plant with id " + id + " not found");
        }
        return PlantDTOMapper.ModelToDTO(plant);
    }

    public void updatePlant(String id, CreateUpdatePlantDTO plant) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        Plant existingPlant = plantRepository.getPlant(id);
        if (existingPlant == null) {
            throw new ResourceNotFoundException("Plant with id " + id + " does not exist");
        }
        Plant updatedPlant = PlantDTOMapper.DTOToModel(plant);
        updatedPlant.setId(id);
        plantRepository.updatePlant(updatedPlant);
    }

    public void removePlant(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        try {
            plantRepository.removePlant(id);
        }  catch (ResourceConflictException e) {
            throw new ResourceConflictException("Can't remove plant " + id + " because it's rented or has history of rents.");
        }
    }

    public List<PlantResponseDTO> getPlants() {
        return plantRepository.getPlants().stream()
            .map(PlantDTOMapper::ModelToDTO)
            .toList();
    }
}
