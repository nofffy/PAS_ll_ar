package model;

import java.time.LocalDateTime;
import java.util.List;

public class Rent {
    private int id;
    private User client;
    private Plant plant;
    private LocalDateTime rentStart;
    private LocalDateTime rentEnd;


    //Chyba niepotrzebne, Chyba przy CRUDzie bedzie podawany obiekt zawsze ;p
//    public Rent(int id, User client, Plant plant) {
//        this.id = id;
//        this.client = client;
//        this.plant = this.plant;
//        this.rentStart = LocalDateTime.now();
//    }

    public Rent(User client, Plant plant, LocalDateTime rentEnd) {
        this.id = 0;
        this.client = client;
        this.plant = plant;
        this.rentStart = LocalDateTime.now();
        this.rentEnd = rentEnd;
    }

    public Rent(int id, User client, Plant plant, LocalDateTime rentStart, LocalDateTime rentEnd) {
        this.id = id;
        this.client = client;
        this.plant = plant;
        this.rentStart = rentStart;
        this.rentEnd = rentEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlants(Plant plant) {
        this.plant = plant;
    }

    public LocalDateTime getRentStart() {
        return rentStart;
    }

    public void setRentStart(LocalDateTime rentStart) {
        this.rentStart = rentStart;
    }

    public LocalDateTime getRentEnd() { return rentEnd; }

    public void setRentEnd(LocalDateTime rentEnd) { this.rentEnd = rentEnd; }
}
