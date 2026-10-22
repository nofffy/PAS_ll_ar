package DTOs;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class RentDTO {
    @NotNull(message = "Client id is required")
    private Long clientId;

    @NotNull(message = "Plant id is required")
    private Long plantId;

    private LocalDateTime rentStart;
    private LocalDateTime rentEnd;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
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
