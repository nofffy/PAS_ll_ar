package db.repositories.Memory;

import Exeptions.ResourceConflictException;
import Exeptions.ResourceNotFoundException;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import model.Plant;
import model.repositories.PlantRepository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
@Typed(MemoryPlantRepository.class)
public class MemoryPlantRepository implements PlantRepository {
    private final Map<String, Plant> plants = new ConcurrentHashMap<>();

    private final Instance<MemoryRentRepository> rentRepository;
    private final PlantLockManager plantLockManager;

    @Inject
    public MemoryPlantRepository(PlantLockManager plantLockManager, Instance<MemoryRentRepository> rentRepository) {
        this.plantLockManager = plantLockManager;
        this.rentRepository = rentRepository;
    }

    public MemoryPlantRepository() {
        rentRepository = null;
        plantLockManager = null;
    }

    @Override
    public void addPlant(Plant plant) {
        if (plant == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        String newId = UUID.randomUUID().toString();
        plant.setId(newId);
        plants.put(plant.getId(), plant);
    }

    @Override
    public Plant getPlant(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        return plants.get(id);
    }

    @Override
    public void updatePlant(Plant plant) {
        if (plant == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        plants.put(plant.getId(), plant);
    }

    @Override
    public void removePlant(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }

        Object lock = plantLockManager.getLock(id);

        synchronized (lock) {
            MemoryRentRepository memoryRentRepository = rentRepository.get();

            if (memoryRentRepository.hasAnyRent(id)) {
                throw new ResourceConflictException("Plant with id " + id + " is or has been rented.");
            }

            Plant result = plants.remove(id);
            if (result == null) {
                throw new ResourceNotFoundException("Plant with id " + id + " not found");
            }
        }

    }

    @Override
    public List<Plant> getPlants() { return new ArrayList<>(plants.values()); }

    public boolean plantExists(String plantId) {
        return plants.containsKey(plantId);
    }
}
