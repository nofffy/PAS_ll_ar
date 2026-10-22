package model.repositories.MemoryRepositories;

import Exeptions.ResourceNotFoundException;
import model.Plant;
import model.repositories.PlantRepository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@ApplicationScoped
public class MemoryPlantRepository implements PlantRepository {

    private Map<Long, Plant> plants = new ConcurrentHashMap<>();

    private final AtomicLong curId = new AtomicLong(1);

    @Override
    public void addPlant(Plant plant) {
        if (plant == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        Long newId = curId.getAndIncrement();
        plant.setId(newId);
        plants.put(plant.getId(), plant);
    }

    @Override
    public Plant getPlant(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        return plants.get(id);
    }

    @Override
    public void removePlant(Long id) {
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
