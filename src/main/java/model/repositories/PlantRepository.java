package model.repositories;

import model.Plant;

import java.util.List;

public interface PlantRepository {
    Plant getPlant(String id); //YES UUID!!
    void addPlant(Plant plant);
    void updatePlant(Plant plant);
    void removePlant(String id);

    List<Plant> getPlants();
}
