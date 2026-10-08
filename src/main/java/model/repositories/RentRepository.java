package model.repositories;

import model.Rent;

import java.util.List;

public interface RentRepository {
    Rent getRent(int id);
    boolean addRent(Rent rent);
    boolean removeRent(int id);

    List<Rent> getRents();
    int getRentsCount();
}
