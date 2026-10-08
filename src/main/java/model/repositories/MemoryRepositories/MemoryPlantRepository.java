package model.repositories.MemoryRepositories;

import model.Plant;
import model.repositories.PlantRepository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@ApplicationScoped
public class MemoryPlantRepository implements PlantRepository {

    private Map<Long, Plant> plants = new HashMap<Long, Plant>();

    private Long curId = 1L;

    private ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    @Override
    public boolean addPlant(Plant plant) {
        if (plant == null) {
            return false;  //TODO: zmiana boolean na void (albo return plant po dodaniu) i tego na wyjątki?, podobnie w innych repozytoriach
        }

        writeLock.lock();
        try {
            plant.setId(curId++);
            plants.put(plant.getId(), plant);
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Plant getPlant(Long id) {
        if  (id < 0 || id >= plants.size()) {
            return null;
        }
        readLock.lock();
        try {
            return plants.get(id);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean removePlant(Long id) {
        if (id == null) {
            return false;
        }
        writeLock.lock();
        try {
            plants.remove(id);
            return true;
        } finally {
            writeLock.unlock();
        }
    }


    @Override
    public List<Plant> getPlants() {
        readLock.lock();
        try {
            return new ArrayList<>(plants.values());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<Plant> findAllAvailable() {
        readLock.lock();
        try {
            return plants.values().stream()
                    .filter(Plant::isAvailable)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<Plant> findAllUnavailable() {
        readLock.lock();
        try {
            return plants.values().stream()
                    .filter(plant -> !plant.isAvailable())
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
}
