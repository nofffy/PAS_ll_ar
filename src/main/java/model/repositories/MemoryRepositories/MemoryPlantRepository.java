package model.repositories.MemoryRepositories;

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
    public boolean addPlant(Plant plant) {
        if (plant == null) {
            return false;  //TODO: zmiana boolean na void (albo return plant po dodaniu) i tego na wyjątki?, podobnie w innych repozytoriach
        }
        Long newId = curId.getAndIncrement();
        plant.setId(newId);
        plants.put(plant.getId(), plant);
        return true;
    }

    @Override
    public Plant getPlant(Long id) {
        if (id == null) {
            return null;
        }
        return plants.get(id);
    }

    @Override
    public boolean removePlant(Long id) {
        if (id == null) {
            return false;
        }
        plants.remove(id);
        return true;
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
