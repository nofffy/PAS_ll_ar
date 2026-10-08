package model.repositories;

import model.Rent;

import java.util.List;

public interface RentRepository {
    Rent getRent(Long id);
    boolean addRent(Rent rent);
    boolean removeRent(Long id);

    List<Rent> getRents();
//    int getRentsCount();
}
