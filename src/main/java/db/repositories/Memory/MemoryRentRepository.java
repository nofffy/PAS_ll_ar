package db.repositories.Memory;

import Exeptions.ResourceConflictException;
import Exeptions.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import model.Rent;
import model.repositories.RentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
@Typed(MemoryRentRepository.class)
public class MemoryRentRepository implements RentRepository {
    private final Map<String, Rent> rents = new ConcurrentHashMap<>();
    private final Map<String, Rent> activeRentsByPlantId = new ConcurrentHashMap<>();
    private final Map<String, Rent> allRentsByPlantId = new ConcurrentHashMap<>();

    private final PlantLockManager plantLockManager;
    private final Instance<MemoryPlantRepository> plantRepository;

    @Inject
    public MemoryRentRepository(PlantLockManager plantLockManager, Instance<MemoryPlantRepository> plantRepository) {
        this.plantLockManager = plantLockManager;
        this.plantRepository = plantRepository;
    }

    public MemoryRentRepository() {
        this.plantLockManager = null;
        this.plantRepository = null;
    }

    @Override
    public Rent getRent(String id) {
        if(id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        return rents.get(id);
    }

    @Override
    public void addRent(Rent rent) {
        if(rent == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        String plantId = rent.getPlant().getId();
        Object lock = plantLockManager.getLock(plantId);

        synchronized (lock) {
            MemoryPlantRepository pRepository = plantRepository.get();

            if (!pRepository.plantExists(plantId)) {
                throw new ResourceNotFoundException("Plant not found");
            }
            Rent existingRent = activeRentsByPlantId.putIfAbsent(plantId, rent);
            if(existingRent != null) {
                throw new ResourceConflictException("Plant with id " + plantId + " is rented");
            }
            Rent allRent = allRentsByPlantId.putIfAbsent(plantId, rent);
            String newId = UUID.randomUUID().toString();
            rent.setId(newId);
            rents.put(newId, rent);
        }

    }

    @Override
    public void finishRent(String id) {
        if(id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        Rent rent = rents.get(id);
        if(rent == null) {
            throw new ResourceNotFoundException("Rent doesn't exist");
        }
        if(rent.getRentEnd() != null) {
            throw new ResourceConflictException("Rent is already finished");
        }
        String plantId = rent.getPlant().getId();
        boolean wasRemoved = activeRentsByPlantId.remove(plantId, rent);
        if (wasRemoved) {
            rent.endRent();
        }
    }

    @Override
    public void removeRent(String id) {
        if (id == null) throw new IllegalArgumentException("Can't be null");
        Rent rent = rents.get(id);
        if (rent == null) throw new ResourceNotFoundException("Rent doesn't exist");
        if (rent.getRentEnd() != null) throw new ResourceConflictException("Can't remove finished rents");
        String plantId = rent.getPlant().getId();
        boolean wasRemoved = activeRentsByPlantId.remove(plantId, rent);
        if (!wasRemoved) {
            throw new ResourceConflictException("Rent state changed concurrently");
        }
        rents.remove(id);
    }

    @Override
    public List<Rent> getPastRentsByClientId(String id) {
        if(id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        return rents.values().stream()
                .filter(rent -> rent.getClient().getId().equals(id) && rent.getRentEnd() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rent> getCurrentRentsByClientId(String id) {
        if(id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        return rents.values().stream().
                filter(rent -> rent.getClient().getId().equals(id) && rent.getRentEnd() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rent> getPastRentsByPlantId(String id) {
        if(id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        return rents.values().stream().
                filter(rent -> rent.getPlant().getId().equals(id) && rent.getRentEnd() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rent> getCurrentRentsByPlantId(String id) { //to zwraca i tak jeden obiekt ale takie ujednolicenie chyba lepsze dla wyzszej warstwy
        if(id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        Rent a = activeRentsByPlantId.get(id);
        if(a == null) {
            return List.of();
        }
        return List.of(a);
    }

    @Override
    public List<Rent> getRents() { return new ArrayList<>(rents.values()); }

    public boolean hasAnyRent(String plantId) {
        return allRentsByPlantId.containsKey(plantId);
    }

}
