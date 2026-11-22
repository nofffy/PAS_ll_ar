package pasweb.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import pasweb.DTOs.ClientDTO;
import pasweb.DTOs.PlantDTO;
import pasweb.controllers.AppException;

import java.util.List;
import java.util.Map;

@Service
public class PlantApi {
    private final RestClient restClient;

    public PlantApi(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<PlantDTO> getAllPlants() {
        return restClient.get()
                .uri("/plants")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<PlantDTO> getAvailablePlants() {
        return restClient.get()
                .uri("/plants/available")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public PlantDTO getPlant(String id) {
        try {
            return restClient.get()
                    .uri("/plants/{id}", id)
                    .retrieve()
                    .body(PlantDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new AppException("Nie znaleziono rosliny o ID: " + id);
        }
    }

    public void createPlant(PlantDTO plant) {
        Map<String, Object> body = Map.of(
                "name", plant.getName(),
                "color", plant.getColor(),
                "cost", plant.getCost(),
                "type", plant.getType()
        );

        restClient.post()
                .uri("/plants")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}
