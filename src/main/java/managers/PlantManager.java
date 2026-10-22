package managers;

import Exeptions.ResourceConflictException;
import DTOs.PlantDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Plant;
import model.PlantTypes.Basic;
import model.PlantTypes.Carnivorous;
import model.PlantTypes.Monstera;
import model.PlantTypes.Succulent;
import model.repositories.PlantRepository;
import model.repositories.RentRepository;

import java.util.List;

@RequestScoped
public class PlantManager {

    @Inject
    private PlantRepository plantRepository;

    @Inject
    private RentRepository rentRepository;

    public void createPlant(PlantDTO plant) {
        Plant newPlant;

        boolean isAvailable = true;
        if (plant.isAvailable() != null) {
            isAvailable = plant.isAvailable();
        }

        String type = plant.getType();
        if (type.equals("BASIC")) {
            newPlant = new Basic(plant.getName(), plant.getColor(), plant.getCost(), isAvailable);
        } else if (type.equals("CARNIVOROUS")) {
            if (plant.getFavouriteFood() == null) {
                throw new IllegalArgumentException("Unknown favourite food info");
            }
            newPlant = new Carnivorous(plant.getName(), plant.getColor(), plant.getCost(), isAvailable, plant.getFavouriteFood());
        } else if (type.equals("MONSTERA")) {
            if (plant.getLeafSize() == null) {
                throw new IllegalArgumentException("Unknown leaf size");
            }
            newPlant = new Monstera(plant.getName(), plant.getColor(), plant.getCost(), isAvailable, plant.getLeafSize());
        } else if (type.equals("SUCCULENT")) {
            if (plant.isSpiky() == null) {
                throw new IllegalArgumentException("Unknown spiky info");
            }
            newPlant = new Succulent(plant.getName(), plant.getColor(), plant.getCost(), isAvailable, plant.isSpiky());
        } else {
            throw new IllegalArgumentException("Unknown plant type");
        }

        plantRepository.addPlant(newPlant);
    }

    public Plant getPlant(Long id) { return plantRepository.getPlant(id); }

    public void removePlant(Long id) {
        if (!rentRepository.getCurrentRentsByPlantId(id).isEmpty() || !rentRepository.getPastRentsByPlantId(id).isEmpty()) {
            throw new ResourceConflictException("Can't remove this plant because it has history of rents");
        }
        plantRepository.removePlant(id);
    }

    public List<Plant> getPlants() { return plantRepository.getPlants(); }

    public List<Plant> findAllAvailable() { return plantRepository.findAllAvailable(); }

    public List<Plant> findAllUnavailable() { return plantRepository.findAllUnavailable(); }
}
