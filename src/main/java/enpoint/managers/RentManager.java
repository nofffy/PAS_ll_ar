package enpoint.managers;

import Exeptions.ResourceNotFoundException;
import enpoint.DTOmappers.RentDTOMapper;
import enpoint.DTOs.CreateUpdateRentDTO;
import enpoint.DTOs.RentResponseDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Plant;
import model.Rent;
import model.User;
import model.repositories.PlantRepository;
import model.repositories.RentRepository;
import model.repositories.UserRepository;

import java.util.List;

@RequestScoped
public class RentManager {

    @Inject
    private RentRepository rentRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PlantRepository plantRepository;

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

    public RentResponseDTO createRent(CreateUpdateRentDTO rent) {
        Rent newRent = RentDTOMapper.DTOToModel(rent, userRepository, plantRepository);
        rentRepository.addRent(newRent);
        return RentDTOMapper.ModelToDTO(newRent);
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
