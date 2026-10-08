package model;

import java.time.LocalDateTime;
import java.util.List;

public class Rent {
    private Long id = null;
    private User client;
    private Plant plant;
    private LocalDateTime rentStart;
    private LocalDateTime rentEnd = null;
    boolean archived = false;


    //Chyba niepotrzebne, Chyba przy CRUDzie bedzie podawany obiekt zawsze ;p
//    public Rent(Long id, User client, Plant plant) {
//        this.id = id;
//        this.client = client;
//        this.plant = this.plant;
//        this.rentStart = LocalDateTime.now();
//    }

    public Rent(User client, Plant plant) {
        this.id = null;
        this.client = client;
        this.plant = plant;
        this.rentStart = LocalDateTime.now();

    }

    public Rent(Long id, User client, Plant plant, LocalDateTime rentStart) {
        this.id = id;
        this.client = client;
        this.plant = plant;
        this.rentStart = rentStart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public void endRent() { this.rentEnd = LocalDateTime.now(); }

    public boolean isArchived() { return archived; }

    public void setArchived() { this.archived = true; }
}
