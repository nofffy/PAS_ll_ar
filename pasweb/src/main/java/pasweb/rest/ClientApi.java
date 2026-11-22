package pasweb.rest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import pasweb.controllers.AppException;
import pasweb.DTOs.ClientDTO;

import java.util.Map;

@Service
public class ClientApi {
    private final RestClient restClient;

    public ClientApi(RestClient restClient) {
        this.restClient = restClient;
    }

    public ClientDTO getClientByLogin(String login) {
        try {
            return restClient.get()
                    .uri("/users/login/{login}", login)
                    .retrieve()
                    .body(ClientDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new AppException("Nie znaleziono klienta o loginie: " + login);
        }
    }

    public ClientDTO getClient(String id) {
        try {
            return restClient.get()
                    .uri("/users/{id}", id)
                    .retrieve()
                    .body(ClientDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new AppException("Nie znaleziono klienta o ID: " + id);
        }
    }

    public void registerClient(ClientDTO newClient) {
        Map<String, Object> body = Map.of(
                "login", newClient.getLogin(),
                "password", newClient.getPassword(),
                "email", newClient.getEmail(),
                "age", newClient.getAge(),
                "type", "CLIENT",
                "active", true
        );

        restClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}
