package model.repositories;

import model.Plant;

import java.util.List;

public interface PlantRepository {
    Plant getPlant(Long id); //UUID?
    boolean addPlant(Plant plant);
    boolean removePlant(Long id);
    //boolean updatePlant(Plant plant);

    //boolean savePlants(List<Plant> plants);
    List<Plant> getPlants();

    List<Plant> findAllAvailable();
    List<Plant> findAllUnavailable();
}
