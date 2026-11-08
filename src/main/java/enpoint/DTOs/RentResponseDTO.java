package enpoint.DTOs;

import java.time.LocalDateTime;

public class RentResponseDTO {
    private String id;
    private UserResponseDTO user;
    private PlantResponseDTO plant;
    private LocalDateTime rentStart;
    private LocalDateTime rentEnd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO userDTO) {
        this.user = userDTO;
    }

    public PlantResponseDTO getPlant() {
        return plant;
    }

    public void setPlant(PlantResponseDTO plantDTO) {
        this.plant = plantDTO;
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
