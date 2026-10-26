package managers;

import Exeptions.ResourceConflictException;
import Exeptions.ResourceNotFoundException;
import DTOs.RentDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Plant;
import model.Rent;
import model.User;
import model.repositories.PlantRepository;
import model.repositories.RentRepository;
import model.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequestScoped
public class RentManager {

    @Inject
    private RentRepository rentRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PlantRepository plantRepository;

    public Rent getRent(Long id) {
        Rent rent = rentRepository.getRent(id);
        if (rent == null) {
            throw new ResourceNotFoundException("Rent with id " + id + " not found");
        }
        return rent;
    }

    public void createRent(RentDTO rent) {
        Long userId = rent.getClientId();
        User user = userRepository.getUser(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        }
        if (!user.isActive()) {
            throw new ResourceConflictException("User is deactivated, can't rent");
        }

        Long plantId = rent.getPlantId();
        Plant plant = plantRepository.getPlant(plantId);
        if (plant == null) {
            throw new ResourceNotFoundException("Plant with id " + plantId + " not found");
        }

        LocalDateTime startTime;
        if (rent.getRentStart() != null) {
            startTime = rent.getRentStart();
        } else {
            startTime = LocalDateTime.now();
        }

        Rent newRent = new Rent(user, plant, startTime);
        rentRepository.addRent(newRent);
    }

    public void finishRent(Long id) { rentRepository.finishRent(id); }

    public void removeRent(Long id) { rentRepository.removeRent(id); }

    public List<Rent> getPastRentsByClientId(Long id) {
        User user = userRepository.getUser(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return rentRepository.getPastRentsByClientId(id);
    }

    public List<Rent> getCurrentRentsByClientId(Long id) {
        User user = userRepository.getUser(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return rentRepository.getCurrentRentsByClientId(id);
    }

    public List<Rent> getPastRentsByPlantId(Long id) {
        User user = userRepository.getUser(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return rentRepository.getPastRentsByPlantId(id);
    }

    public List<Rent> getCurrentRentsByPlantId(Long id) {
        User user = userRepository.getUser(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return rentRepository.getCurrentRentsByPlantId(id);
    }

    public List<Rent> getRents() { return rentRepository.getRents(); }
}
