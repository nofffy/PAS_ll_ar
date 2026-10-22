package dtos;

import jakarta.validation.constraints.NotNull;
import model.Plant;
import model.User;

import java.time.LocalDateTime;

public class RentDTO {
    @NotNull(message = "Client is required")
    private User client;

    @NotNull(message = "Plant is required")
    private Plant plant;

    private LocalDateTime rentStart;
    private LocalDateTime rentEnd;

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public LocalDateTime getRentStart() {
        return rentStart;
    }

    public void setRentStart(LocalDateTime rentStart) {
        this.rentStart = rentStart;
    }

    public LocalDateTime getRentEnd() {
        return rentEnd;
    }

    public void setRentEnd(LocalDateTime rentEnd) {
        this.rentEnd = rentEnd;
    }
}
