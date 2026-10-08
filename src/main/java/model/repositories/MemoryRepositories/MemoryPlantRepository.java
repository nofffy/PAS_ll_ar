package model.repositories.MemoryRepositories;

import model.Plant;
import model.repositories.PlantRepository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
            return false;  //TODO: zmiana boolean na void (albo return plant po dodaniu) i tego na wyjątki?
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
        return false;
    }

    @Override
    public boolean updatePlant(Plant plant) {
        return false;
    }

    @Override
    public boolean savePlants(List<Plant> plants) {
        return false;
    }

    @Override
    public List<Plant> getPlants() {
        return null;
    }

    @Override
    public List<Plant> findAllAvailable() {
        return null;
    }

    @Override
    public List<Plant> findAllUnavailable() {
        return null;
    }
}
