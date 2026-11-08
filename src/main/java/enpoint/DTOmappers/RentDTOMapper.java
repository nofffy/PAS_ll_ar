package enpoint.DTOmappers;

import Exeptions.ResourceConflictException;
import Exeptions.ResourceNotFoundException;
import enpoint.DTOs.CreateUpdateRentDTO;
import enpoint.DTOs.RentResponseDTO;
import model.Plant;
import model.Rent;
import model.User;
import model.repositories.PlantRepository;
import model.repositories.UserRepository;

import java.time.LocalDateTime;

public class RentDTOMapper {
    public static Rent DTOToModel(CreateUpdateRentDTO rentDTO, UserRepository userRepository, PlantRepository plantRepository) {
        String userId = rentDTO.getClientId();
        User user = userRepository.getUser(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        }
        if (!user.isActive()) {
            throw new ResourceConflictException("User is deactivated, can't rent");
        }

        String plantId = rentDTO.getPlantId();
        Plant plant = plantRepository.getPlant(plantId);
        if (plant == null) {
            throw new ResourceNotFoundException("Plant with id " + plantId + " not found");
        }

        LocalDateTime startTime;
        if (rentDTO.getRentStart() != null) {
            startTime = rentDTO.getRentStart();
        } else {
            startTime = LocalDateTime.now();
        }

        return new Rent(user, plant, startTime);
    }

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
