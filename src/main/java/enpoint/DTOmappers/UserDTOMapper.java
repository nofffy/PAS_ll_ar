package enpoint.DTOmappers;

import enpoint.DTOs.CreateUpdateUserDTO;
import enpoint.DTOs.UserResponseDTO;
import model.User;
import model.UserTypes.Admin;
import model.UserTypes.Client;
import model.UserTypes.Gardener;

public class UserDTOMapper {
    public static User DTOToModel(CreateUpdateUserDTO userDTO) {
        User newUser;

        boolean isActive = true;
        if (userDTO.getActive() != null) {
            isActive = userDTO.getActive();
        }

        String type = userDTO.getType();
        if (type.equals("ADMIN")) {
            newUser = new Admin(userDTO.getLogin(), isActive);
        } else if (type.equals("GARDENER")) {
            newUser = new Gardener(userDTO.getLogin(), isActive);
        } else if (type.equals("CLIENT")) {
            if (userDTO.getMaxRents() != null) {
                newUser = new Client(userDTO.getLogin(), isActive, userDTO.getMaxRents());
            } else {
                newUser = new Client(userDTO.getLogin(), isActive);
            }
        } else {
            throw new IllegalArgumentException("Unknown user type");
        }
        return newUser;
    }

    public static UserResponseDTO ModelToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setActive(user.isActive());

        if (user instanceof Admin) {
            dto.setType("ADMIN");
        } else if(user instanceof Gardener) {
            dto.setType("GARDENER");
        } else if(user instanceof Client) {
            dto.setType("CLIENT");
            dto.setMaxRents(((Client) user).getMaxRents());
        }
        return dto;
    }
}
