package pasrest.managers;

import org.springframework.stereotype.Service;
import pasrest.Exeptions.ResourceNotFoundException;
import pasrest.api.DTOmappers.UserDTOMapper;
import pasrest.api.DTOs.CreateUpdateUserDTO;
import pasrest.api.DTOs.UserResponseDTO;
import pasrest.model.User;
import pasrest.model.repositories.UserRepository;

import java.util.List;

@Service
public class UserManager {
    private final UserRepository userRepository;

    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(CreateUpdateUserDTO user) {
        User newUser = UserDTOMapper.DTOToModel(user);
        User userWithId = userRepository.addUser(newUser);
        return UserDTOMapper.ModelToDTO(userWithId);
    }

    public UserResponseDTO getUser(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        User user = userRepository.getUser(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return UserDTOMapper.ModelToDTO(user);
    }

    public UserResponseDTO getUserByLogin(String login) {
        if (login == null) {
            throw new IllegalArgumentException("Login can't be null");
        }
        User user = userRepository.getUserByLogin(login);
        if (user == null) {
            throw new ResourceNotFoundException("User with login: " + login + " not found");
        }
        return UserDTOMapper.ModelToDTO(user);
    }

    public List<UserResponseDTO> getUsersByLogin(String loginPattern) {
        if (loginPattern == null) {
            throw new IllegalArgumentException("Login can't be null");
        }
        return userRepository.getUsersByLoginPattern(loginPattern).stream()
                .map(UserDTOMapper::ModelToDTO)
                .toList();
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.getUsers().stream()
                .map(UserDTOMapper::ModelToDTO)
                .toList();
    }

    public void updateUser(String id, CreateUpdateUserDTO user) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        User existingUser = userRepository.getUser(id);
        if (existingUser == null) {
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }
        User updatedUser = UserDTOMapper.DTOToModel(user);
        updatedUser.setId(id);
        userRepository.updateUser(updatedUser);
    }

    public void activateUser(String id) {
        if (id == null) throw new IllegalArgumentException("Id can't be null");
        userRepository.setActive(id, true);
    }

    public void deactivateUser(String id) {
        if (id == null) throw new IllegalArgumentException("Id can't be null");
        userRepository.setActive(id, false);
    }

    public List<UserResponseDTO> findAllActiveUsers() {
        return userRepository.findAllActive().stream()
                .map(UserDTOMapper::ModelToDTO)
                .toList();
    }

    public List<UserResponseDTO> findAllInactiveUsers() {
        return userRepository.findAllInactive().stream()
                .map(UserDTOMapper::ModelToDTO)
                .toList();
    }
}
