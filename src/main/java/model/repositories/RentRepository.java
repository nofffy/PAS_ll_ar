package model.repositories;

import model.Rent;

import java.util.List;

public interface RentRepository {
    Rent getRent(String id);
    void addRent(Rent rent);
    void removeRent(String id);
    void finishRent(String id);

    List<Rent> getPastRentsByPlantId(String id);
    List<Rent> getCurrentRentsByPlantId(String id);
    List<Rent> getPastRentsByClientId(String id);
    List<Rent> getCurrentRentsByClientId(String id);
    List<Rent> getRents();
}
