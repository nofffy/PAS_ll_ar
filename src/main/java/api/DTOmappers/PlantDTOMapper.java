package api.DTOmappers;

import api.DTOs.CreateUpdatePlantDTO;
import api.DTOs.PlantResponseDTO;
import model.Plant;
import model.PlantTypes.*;

public class PlantDTOMapper {
    public static Plant DTOToModel(CreateUpdatePlantDTO plantDTO) {
        if (plantDTO.getType() == null) {
            throw new IllegalArgumentException("Type can't be null");
        }

        return switch (plantDTO.getType()) {
            case BASIC -> new Basic(plantDTO.getName(), plantDTO.getColor(), plantDTO.getCost());
            case CARNIVOROUS -> {
                if (plantDTO.getFavouriteFood() == null) {
                    throw new IllegalArgumentException("Unknown favourite food info");
                }
                yield new Carnivorous(plantDTO.getName(), plantDTO.getColor(), plantDTO.getCost(), plantDTO.getFavouriteFood());
            }
            case MONSTERA -> {
                if (plantDTO.getLeafSize() == null) {
                    throw new IllegalArgumentException("Unknown leaf size");
                }
                yield new Monstera(plantDTO.getName(), plantDTO.getColor(), plantDTO.getCost(), plantDTO.getLeafSize());
            }
            case SUCCULENT -> {
                if (plantDTO.isSpiky() == null) {
                    throw new IllegalArgumentException("Unknown spiky info");
                }
                yield new Succulent(plantDTO.getName(), plantDTO.getColor(), plantDTO.getCost(), plantDTO.isSpiky());
            }
        };
    }

    public static PlantResponseDTO ModelToDTO(Plant plant) {
        PlantResponseDTO dto = new PlantResponseDTO();
        dto.setId(plant.getId());
        dto.setName(plant.getName());
        dto.setColor(plant.getColor());
        dto.setCost(plant.getCost());

        PlantType type = plant.getType();
        dto.setType(type);

        switch (type) {
            case BASIC -> {}
            case CARNIVOROUS -> dto.setFavouriteFood(((Carnivorous) plant).getFavouriteFood());
            case MONSTERA -> dto.setLeafSize(((Monstera) plant).getLeafSize());
            case SUCCULENT -> dto.setSpiky(((Succulent) plant).isSpiky());
        }
        return dto;
    }
}
