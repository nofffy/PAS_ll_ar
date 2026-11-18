package pasrest.api.DTOmappers;

import pasrest.api.DTOs.RentResponseDTO;
import pasrest.model.Rent;

public class RentDTOMapper {
    public static RentResponseDTO ModelToDTO(Rent rent) {
        RentResponseDTO dto = new RentResponseDTO();
        dto.setId(rent.getId());
        dto.setUser(UserDTOMapper.ModelToDTO(rent.getClient()));
        dto.setPlant(PlantDTOMapper.ModelToDTO(rent.getPlant()));
        dto.setRentStart(rent.getRentStart());
        dto.setRentEnd(rent.getRentEnd());
        return dto;
    }
}
