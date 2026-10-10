package model.repositories.MemoryRepositories;

import jakarta.enterprise.context.ApplicationScoped;
import model.Rent;
import model.repositories.RentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class MemoryRentRepository implements RentRepository {
    private Map<Long, Rent> rents = new ConcurrentHashMap<>();
    private final Map<Long, Rent> activeRentsByPlantId = new ConcurrentHashMap<>();

    private final AtomicLong curId = new AtomicLong(1);

    @Override
    public Rent getRent(Long id) {
        if(id == null) {
            return null;
        }
        return rents.get(id);
    }

    @Override
    public synchronized boolean addRent(Rent rent)
    {
        if(rent == null) {
            return false;
        }
        Long plantId = rent.getPlant().getId();
        if(activeRentsByPlantId.containsKey(plantId)) {
            return false;
        }
        Long newId = curId.getAndIncrement();
        rent.setId(newId);
        rents.put(newId, rent);
        activeRentsByPlantId.put(plantId, rent);
        return true;
    }

    @Override
    public synchronized boolean finishRent(Long id) {
        if(id == null) {
            return false;
        }
        Rent rent = rents.get(id);
        if(rent == null || rent.getRentEnd() != null) {
            return false;
        }
        Long plantId = rent.getPlant().getId();
        activeRentsByPlantId.remove(plantId);
        rent.endRent();
        return true;
    }

    @Override
    public synchronized boolean removeRent(Long id) {
        if(id == null) {
            return false;
        }
        Rent rent = rents.get(id);
        if(rent == null) {
            return false;
        }
        Long plantId = rent.getPlant().getId();
        if(rent.getRentEnd() == null) {
            rents.remove(id);
            activeRentsByPlantId.remove(plantId);
            return true;
        }
        return false;
    }

    @Override
    public List<Rent> getPastRentsByClientId(Long id) {
        if(id == null) {
            return List.of();
        }
        return rents.values().stream().
                filter(rent -> rent.getClient().getId().equals(id) && rent.getRentEnd() != null).
                collect(Collectors.toList());
    }

    @Override
    public List<Rent> getCurrentRentsByClientId(Long id) {
        if(id == null) {
            return List.of();
        }
        return rents.values().stream().
                filter(rent -> rent.getClient().getId().equals(id) && rent.getRentEnd() == null).
                collect(Collectors.toList());
    }

    @Override
    public List<Rent> getPastRentsByPlantId(Long id) {
        if(id == null) {
            return List.of();
        }
        return rents.values().stream().
                filter(rent -> rent.getPlant().getId().equals(id) && rent.getRentEnd() != null).
                collect(Collectors.toList());
    }

    @Override
    public List<Rent> getCurrentRentsByPlantId(Long id) { //to zwraca i tak jeden obiekt ale takie ujednolicenie chyba lepsze dla wyzszej warstwy
        if(id == null) {
            return List.of();
        }
        Rent a = activeRentsByPlantId.get(id);
        if(a == null) {
            return List.of();
        }
        return List.of(a);
    }

    @Override
    public List<Rent> getRents() { return new ArrayList<>(rents.values()); }
}
