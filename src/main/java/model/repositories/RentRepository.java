package model.repositories;

import model.Rent;

import java.util.List;

public interface RentRepository {
    Rent getRent(Long id);
    boolean addRent(Rent rent);
    boolean removeRent(Long id);
    boolean finishRent(Long id);

    List<Rent> getPastRentsByPlantId(Long id);
    List<Rent> getCurrentRentsByPlantId(Long id);
    List<Rent> getPastRentsByClientId(Long id);
    List<Rent> getCurrentRentsByClientId(Long id);
    List<Rent> getRents();
}
