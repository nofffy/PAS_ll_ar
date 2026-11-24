package pasweb.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import pasweb.DTOs.RentDTO;
import pasweb.controllers.AppException;

import java.util.List;
import java.util.Map;

@Service
public class RentApi {
    private final RestClient restClient;

    public RentApi(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<RentDTO> getUserRents(String clientId) {
        try {
            return restClient.get()
                    .uri("/rents/client/{id}/current", clientId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (HttpClientErrorException.NotFound e) {
            throw new AppException("Nie znaleziono klienta o ID: " + clientId);
        }
    }

    public List<RentDTO> getAllRents() {
        return restClient.get()
                .uri("/rents")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public void createRent(RentDTO rentDTO) {
        Map<String, Object> body = Map.of(
                "clientId", rentDTO.getClient().getId(),
                "plantId", rentDTO.getPlant().getId()
        );

        restClient.post()
                .uri("/rents")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    public void finishRent(String rentId) {
        try {
            restClient.post()
                    .uri("/rents/{id}/finish", rentId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new AppException("Nie znaleziono wypozyczenia o ID: " + rentId);
        }
    }

    public void deleteRent(String rentId) {
        try {
            restClient.delete()
                    .uri("/rents/{id}", rentId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new AppException("Nie znaleziono wypozyczenia o ID: " + rentId);
        }
    }
}
