package managers;

import Exeptions.ResourceConflictException;
import api.DTOmappers.PlantDTOMapper;
import Exeptions.ResourceNotFoundException;
import api.DTOs.CreateUpdatePlantDTO;
import api.DTOs.PlantResponseDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Plant;
import model.repositories.PlantRepository;
import model.repositories.RentRepository;

import java.util.List;

@RequestScoped
public class PlantManager {

    @Inject
    private PlantRepository plantRepository;

    @Inject
    private RentRepository rentRepository;

    public PlantResponseDTO createPlant(CreateUpdatePlantDTO plant) {
        Plant newPlant = PlantDTOMapper.DTOToModel(plant);
        plantRepository.addPlant(newPlant);
        return PlantDTOMapper.ModelToDTO(newPlant);
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
        if (!rentRepository.getCurrentRentsByPlantId(id).isEmpty() || !rentRepository.getPastRentsByPlantId(id).isEmpty()) {
            throw new ResourceConflictException("Can't remove this plant because it's rented or has history of rents");
        }
        plantRepository.removePlant(id);
    }

    public List<PlantResponseDTO> getPlants() {
        return plantRepository.getPlants().stream()
            .map(PlantDTOMapper::ModelToDTO)
            .toList();
    }

    public List<PlantResponseDTO> findAllAvailable() {
        return plantRepository.findAllAvailable().stream()
                .map(PlantDTOMapper::ModelToDTO)
                .toList();
    }

    public List<PlantResponseDTO> findAllUnavailable() {
        return plantRepository.findAllUnavailable().stream()
                .map(PlantDTOMapper::ModelToDTO)
                .toList();
    }
}
