package api.DTOmappers;

import api.DTOs.CreateUpdateUserDTO;
import api.DTOs.UserResponseDTO;
import model.User;
import model.UserTypes.*;

public class UserDTOMapper {
    public static User DTOToModel(CreateUpdateUserDTO userDTO) {
        if (userDTO.getType() == null) {
            throw new IllegalArgumentException("Type can't be null");
        }

        return switch(userDTO.getType()) {
            case ADMIN -> new Admin(userDTO.getLogin(), userDTO.getActive());
            case GARDENER -> new Gardener(userDTO.getLogin(), userDTO.getActive());
            case CLIENT ->  new Client(userDTO.getLogin(), userDTO.getActive());
        };
    }

    public static UserResponseDTO ModelToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
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
