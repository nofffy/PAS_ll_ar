package model.repositories;

import model.Plant;

import java.util.List;

public interface PlantRepository {
    Plant getPlant(Long id); //UUID?
    void addPlant(Plant plant);
    void removePlant(Long id);

    List<Plant> getPlants();
    List<Plant> findAllAvailable();
    List<Plant> findAllUnavailable();
}
