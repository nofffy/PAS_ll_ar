package pasrest.model.repositories;

import pasrest.model.Plant;

import java.util.List;

public interface PlantRepository {
    Plant getPlant(String id); //YES UUID!!
    Plant addPlant(Plant plant);
    void updatePlant(Plant plant);
    void removePlant(String id);

    List<Plant> getPlants();
}
