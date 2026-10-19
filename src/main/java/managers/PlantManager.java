package managers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.Plant;
import model.repositories.MemoryRepositories.MemoryPlantRepository;
import model.repositories.RentRepository;

import java.util.List;

@ApplicationScoped
public class PlantManager {

    @Inject
    private MemoryPlantRepository mpr;

    @Inject
    private RentRepository rentRepository;

    public void createPlant(Plant plant) { mpr.addPlant(plant); }

    public Plant getPlant(Long id) { return mpr.getPlant(id); }

    public void removePlant(Long id) {
        if (!rentRepository.getCurrentRentsByPlantId(id).isEmpty() || !rentRepository.getPastRentsByPlantId(id).isEmpty()) {
            throw new IllegalStateException("Can't remove this plant because it has history of rents");
        }
        mpr.removePlant(id);
    }

    public List<Plant> getPlants() { return mpr.getPlants(); }

    public List<Plant> findAllAvailable() { return mpr.findAllAvailable(); }

    public List<Plant> findAllUnavailable() { return mpr.findAllUnavailable(); }
}
