package pasweb.DTOs;

import java.time.LocalDateTime;

public class RentDTO {
    private String id;
    private ClientDTO client;
    private PlantDTO plant;
    private LocalDateTime rentStart;
    private LocalDateTime rentEnd;

    public RentDTO() {}

    public RentDTO(ClientDTO client, PlantDTO plant) {
        this.client = client;
        this.plant = plant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public PlantDTO getPlant() {
        return plant;
    }

    public void setPlant(PlantDTO plant) {
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
