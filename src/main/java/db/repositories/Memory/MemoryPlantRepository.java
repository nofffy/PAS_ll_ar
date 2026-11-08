package db.repositories.Memory;

import jakarta.enterprise.inject.Typed;
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

    public MemoryPlantRepository() {}

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
        plants.remove(id);
    }

    @Override
    public List<Plant> getPlants() { return new ArrayList<>(plants.values()); }

    @Override
    public List<Plant> findAllAvailable() {
        return plants.values().stream()
                .filter(Plant::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public List<Plant> findAllUnavailable() {
        return plants.values().stream()
                .filter(plant -> !plant.isAvailable())
                .collect(Collectors.toList());
    }
}
