package pasrest.managers;

import org.springframework.stereotype.Service;
import pasrest.Exeptions.ResourceConflictException;
import pasrest.Exeptions.ResourceNotFoundException;
import pasrest.api.DTOmappers.RentDTOMapper;
import pasrest.api.DTOs.CreateUpdateRentDTO;
import pasrest.api.DTOs.PlantResponseDTO;
import pasrest.api.DTOs.RentResponseDTO;
import pasrest.model.Plant;
import pasrest.model.Rent;
import pasrest.model.User;
import pasrest.model.UserTypes.Client;
import pasrest.model.repositories.PlantRepository;
import pasrest.model.repositories.RentRepository;
import pasrest.model.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class RentManager {
    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;

    public RentManager(RentRepository rentRepository, UserRepository userRepository, PlantRepository plantRepository) {
        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
    }

    public RentResponseDTO getRent(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        Rent rent = rentRepository.getRent(id);
        if (rent == null) {
            throw new ResourceNotFoundException("Rent with id " + id + " not found");
        }
        return RentDTOMapper.ModelToDTO(rent);
    }

    public RentResponseDTO createRent(CreateUpdateRentDTO rentDTO) {
        String userId = rentDTO.getClientId();
        User user = userRepository.getUser(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        }
        if (!user.isActive()) {
            throw new ResourceConflictException("User is deactivated, can't rent");
        }
        List<Rent> activeRents = rentRepository.getCurrentRentsByClientId(userId);
        if (user instanceof Client client) {
            if (activeRents.size() >= client.getMaxRents()) {
                throw new ResourceConflictException("Client has reached max active rents: " + client.getMaxRents());
            }
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
        Rent newRent = new Rent(user, plant, startTime);
        Rent rentWithId = rentRepository.addRent(newRent);
        return RentDTOMapper.ModelToDTO(rentWithId);
    }

    public void finishRent(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        rentRepository.finishRent(id);
    }

    public void removeRent(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        rentRepository.removeRent(id);
    }

    public List<RentResponseDTO> getPastRentsByClientId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        User user = userRepository.getUser(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return rentRepository.getPastRentsByClientId(id).stream()
                .map(RentDTOMapper::ModelToDTO)
                .toList();
    }

    public List<RentResponseDTO> getCurrentRentsByClientId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        User user = userRepository.getUser(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return rentRepository.getCurrentRentsByClientId(id).stream()
                .map(RentDTOMapper::ModelToDTO)
                .toList();
    }

    public List<RentResponseDTO> getPastRentsByPlantId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        Plant plant = plantRepository.getPlant(id);
        if (plant == null) {
            throw new ResourceNotFoundException("Plant with id " + id + " not found");
        }
        return rentRepository.getPastRentsByPlantId(id).stream()
                .map(RentDTOMapper::ModelToDTO)
                .toList();
    }

    public List<RentResponseDTO> getCurrentRentsByPlantId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        Plant plant = plantRepository.getPlant(id);
        if (plant == null) {
            throw new ResourceNotFoundException("Plant with id " + id + " not found");
        }
        return rentRepository.getCurrentRentsByPlantId(id).stream()
                .map(RentDTOMapper::ModelToDTO)
                .toList();
    }

    public List<RentResponseDTO> getRents() {
        return rentRepository.getRents().stream()
                .map(RentDTOMapper::ModelToDTO)
                .toList();
    }
}
