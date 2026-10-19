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
            throw new IllegalArgumentException();
        }
        return rents.get(id);
    }

    @Override
    public void addRent(Rent rent) {
        if(rent == null) {
            throw new IllegalArgumentException();
        }
        Long plantId = rent.getPlant().getId();
        Rent existingRent = activeRentsByPlantId.putIfAbsent(plantId, rent);
        if(existingRent != null) {
            throw new IllegalArgumentException();
        }
        Long newId = curId.getAndIncrement();
        rent.setId(newId);
        rents.put(newId, rent);
    }

    @Override
    public void finishRent(Long id) {
        if(id == null) {
            throw new IllegalArgumentException();
        }
        Rent rent = rents.get(id);
        if(rent == null) {
            throw new IllegalArgumentException("Rent doesn't exist");
        }
        if(rent.getRentEnd() != null) {
            throw new IllegalStateException("This rent is already finished");
        }
        Long plantId = rent.getPlant().getId();
        boolean wasRemoved = activeRentsByPlantId.remove(plantId, rent);
        if (wasRemoved) {
            rent.endRent();
        }
    }

    @Override
    public void removeRent(Long id) {
        if(id == null) {
            throw new IllegalArgumentException();
        }
        Rent rent = rents.get(id);
        if(rent == null) {
            throw new IllegalArgumentException("Rent doesn't exist");
        }
        if(rent.getRentEnd() != null) {
            throw new IllegalStateException("Can't remove finished rents");
        }
        Long plantId = rent.getPlant().getId();
        boolean wasRemoved = activeRentsByPlantId.remove(plantId, rent);
        if (wasRemoved) {
            rent.endRent();
        }
    }

    @Override
    public List<Rent> getPastRentsByClientId(Long id) {
        if(id == null) {
            return List.of();
        }
        return rents.values().stream().
                filter(rent -> rent.getClient().getId().equals(id) && rent.getRentEnd() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rent> getCurrentRentsByClientId(Long id) {
        if(id == null) {
            return List.of();
        }
        return rents.values().stream().
                filter(rent -> rent.getClient().getId().equals(id) && rent.getRentEnd() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rent> getPastRentsByPlantId(Long id) {
        if(id == null) {
            return List.of();
        }
        return rents.values().stream().
                filter(rent -> rent.getPlant().getId().equals(id) && rent.getRentEnd() != null)
                .collect(Collectors.toList());
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
