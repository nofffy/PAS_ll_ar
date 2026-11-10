package api.DTOmappers;

import api.DTOs.CreateUpdatePlantDTO;
import api.DTOs.PlantResponseDTO;
import model.Plant;
import model.PlantTypes.Basic;
import model.PlantTypes.Carnivorous;
import model.PlantTypes.Monstera;
import model.PlantTypes.Succulent;

public class PlantDTOMapper {
    public static Plant DTOToModel(CreateUpdatePlantDTO plantDTO) {
        Plant newPlant;

        boolean isAvailable = true;
        if (plantDTO.isAvailable() != null) {
            isAvailable = plantDTO.isAvailable();
        }

        String type = plantDTO.getType();
        if (type.equals("BASIC")) {
            newPlant = new Basic(plantDTO.getName(), plantDTO.getColor(), plantDTO.getCost(), isAvailable);
        } else if (type.equals("CARNIVOROUS")) {
            if (plantDTO.getFavouriteFood() == null) {
                throw new IllegalArgumentException("Unknown favourite food info");
            }
            newPlant = new Carnivorous(plantDTO.getName(), plantDTO.getColor(), plantDTO.getCost(), isAvailable, plantDTO.getFavouriteFood());
        } else if (type.equals("MONSTERA")) {
            if (plantDTO.getLeafSize() == null) {
                throw new IllegalArgumentException("Unknown leaf size");
            }
            newPlant = new Monstera(plantDTO.getName(), plantDTO.getColor(), plantDTO.getCost(), isAvailable, plantDTO.getLeafSize());
        } else if (type.equals("SUCCULENT")) {
            if (plantDTO.isSpiky() == null) {
                throw new IllegalArgumentException("Unknown spiky info");
            }
            newPlant = new Succulent(plantDTO.getName(), plantDTO.getColor(), plantDTO.getCost(), isAvailable, plantDTO.isSpiky());
        } else {
            throw new IllegalArgumentException("Unknown plant type");
        }
        return newPlant;
    }

    public static PlantResponseDTO ModelToDTO(Plant plant) {
        PlantResponseDTO dto = new PlantResponseDTO();
        dto.setId(plant.getId());
        dto.setName(plant.getName());
        dto.setColor(plant.getColor());
        dto.setCost(plant.getCost());
        dto.setAvailable(plant.isAvailable());

        if (plant instanceof Basic) {
            dto.setType("BASIC");
        } else if (plant instanceof Carnivorous c) {
            dto.setType("CARNIVOROUS");
            dto.setFavouriteFood(c.getFavouriteFood());
        } else if (plant instanceof Monstera m) {
            dto.setType("MONSTERA");
            dto.setLeafSize(m.getLeafSize());
        } else if (plant instanceof Succulent s) {
            dto.setType("SUCCULENT");
            dto.setSpiky(s.isSpiky());
        }
        return dto;
    }
}
