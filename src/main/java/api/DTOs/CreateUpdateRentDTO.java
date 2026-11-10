package api.DTOs;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateUpdateRentDTO {
    @NotNull(message = "Client id is required")
    private String clientId;

    @NotNull(message = "Plant id is required")
    private String plantId;

    private LocalDateTime rentStart;
    private LocalDateTime rentEnd;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
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

