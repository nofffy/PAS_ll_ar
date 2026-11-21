package pasrest.api.DTOmappers;

import pasrest.api.DTOs.CreateUpdateUserDTO;
import pasrest.api.DTOs.UserResponseDTO;
import pasrest.model.User;
import pasrest.model.UserTypes.*;

public class UserDTOMapper {
    public static User DTOToModel(CreateUpdateUserDTO userDTO) {
        if (userDTO.getType() == null) {
            throw new IllegalArgumentException("Type can't be null");
        }

        return switch(userDTO.getType()) {
            case ADMIN -> new Admin(userDTO.getLogin(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getAge(), userDTO.getActive());
            case GARDENER -> new Gardener(userDTO.getLogin(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getAge(), userDTO.getActive());
            case CLIENT ->  new Client(userDTO.getLogin(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getAge(), userDTO.getActive());
        };
    }

    public static UserResponseDTO ModelToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setActive(user.isActive());

        UserType userType = user.getUserType();
        dto.setType(userType);

        switch (userType) {
            case ADMIN -> {}
            case GARDENER -> {}
            case CLIENT -> dto.setMaxRents(((Client) user).getMaxRents());
        }
        return dto;
    }
}
