package pasrest.model.repositories;

import pasrest.model.Rent;

import java.util.List;

public interface RentRepository {
    Rent getRent(String id);
    Rent addRent(Rent rent);
    void removeRent(String id);
    void finishRent(String id);

    List<Rent> getPastRentsByPlantId(String id);
    List<Rent> getCurrentRentsByPlantId(String id);
    List<Rent> getPastRentsByClientId(String id);
    List<Rent> getCurrentRentsByClientId(String id);
    List<Rent> getRents();
}
