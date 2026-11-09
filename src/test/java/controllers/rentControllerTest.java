package controllers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class rentControllerTest {

    private String uniqueName(String prefix) {
        return prefix + "-" + UUID.randomUUID();
    }

    private String extractId(Response response) {
        String location = response.getHeader("Location");
        if (location == null) {
            throw new IllegalStateException("Brak nagłówka Location w odpowiedzi 201");
        }
        return location.substring(location.lastIndexOf('/') + 1);
    }

    private String createClient(String login, boolean active) {
        String json = """
                {
                    "login": "%s",
                    "active": %s,
                    "type": "CLIENT"
                }
                """.formatted(login, active);
        Response response = given().contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().response();
        return extractId(response);
    }

    private String createPlant(String name) {
        String json = """
                {
                    "name": "%s",
                    "color": "green",
                    "cost": 10.0,
                    "available": true,
                    "type": "BASIC"
                }
                """.formatted(name);
        Response response = given().contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/plants")
                .then()
                .statusCode(201)
                .extract().response();
        return extractId(response);
    }

    private String createRent(String clientId, String plantId) {
        String json = """
                {
                    "clientId": "%s",
                    "plantId": "%s"
                }
                """.formatted(clientId, plantId);
        Response response = given().contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(201)
                .extract().response();
        return extractId(response);
    }

    @Test
    public void CreateRent() {
        String clientLogin = uniqueName("rent-client");
        String clientId = createClient(clientLogin, true);
        String plantId = createPlant(uniqueName("rent-plant"));
        String rentJson = """
                {
                    "clientId": "%s",
                    "plantId": "%s"
                }
                """.formatted(clientId, plantId);
        given().contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(201);
        given().when()
                .get("/api/rents")
                .then()
                .statusCode(200)
                .body("user.id", hasItem(clientId));
    }

    @Test
    public void ReadRent() {
        String clientLogin = uniqueName("read-rent-client");
        String clientId = createClient(clientLogin, true);
        String plantName = uniqueName("read-rent-plant");
        String plantId = createPlant(plantName);
        String rentId = createRent(clientId, plantId);
        given().when()
                .get("/api/rents/" + rentId)
                .then()
                .statusCode(200)
                .body("user.login", equalTo(clientLogin))
                .body("plant.name", equalTo(plantName));
    }

    @Test
    public void DeleteRent() {
        String clientId = createClient(uniqueName("del-rent-client"), true);
        String plantId = createPlant(uniqueName("del-rent-plant"));
        String rentId = createRent(clientId, plantId);
        given().when()
                .delete("/api/rents/" + rentId)
                .then()
                .statusCode(204);
        given().when()
                .get("/api/rents/" + rentId)
                .then()
                .statusCode(404);
    }

    @Test
    public void GetRent_NotFound() {
        given().when()
                .get("/api/rents/000000000000000000000000")
                .then()
                .statusCode(404);
    }

    @Test
    public void GetRent_InvalidId() {
        given().when()
                .get("/api/rents/not-a-valid-object-id")
                .then()
                .statusCode(404);
    }

    @Test
    public void DeleteRent_NotFound() {
        given().when()
                .delete("/api/rents/000000000000000000000000")
                .then()
                .statusCode(404);
    }

    @Test
    public void DeleteRent_InvalidId() {
        given().when()
                .delete("/api/rents/not-a-valid-object-id")
                .then()
                .statusCode(404);
    }

    @Test
    public void FinishRent_NotFound() {
        given().when()
                .post("/api/rents/000000000000000000000000/finish")
                .then()
                .statusCode(404);
    }

    @Test
    public void FinishRent_InvalidId() {
        given().when()
                .post("/api/rents/not-a-valid-object-id/finish")
                .then()
                .statusCode(404);
    }

    @Test
    public void GetCurrentRentsByClientId_UserNotFound() {
        given().when()
                .get("/api/rents/client/000000000000000000000000/current")
                .then()
                .statusCode(404);
    }

    @Test
    public void GetPastRentsByClientId_UserNotFound() {
        given().when()
                .get("/api/rents/client/000000000000000000000000/past")
                .then()
                .statusCode(404);
    }

    @Test
    public void GetCurrentRentsByPlantId_PlantNotFound() {
        given().when()
                .get("/api/rents/plant/000000000000000000000000/current")
                .then()
                .statusCode(404);
    }

    @Test
    public void GetPastRentsByPlantId_PlantNotFound() {
        given().when()
                .get("/api/rents/plant/000000000000000000000000/past")
                .then()
                .statusCode(404);
    }

    @Test
    public void isAlreadyRentedTest() {
        String clientId = createClient(uniqueName("conflict-client"), true);
        String plantId = createPlant(uniqueName("conflict-plant"));
        createRent(clientId, plantId);

        String conflictJson = """
                {
                    "clientId": "%s",
                    "plantId": "%s"
                }
                """.formatted(clientId, plantId);
        given().contentType(ContentType.JSON)
                .body(conflictJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(409);
    }

    @Test
    public void inactiveClientTest() {
        String clientId = createClient(uniqueName("inactive-client"), true);
        String plantId = createPlant(uniqueName("inactive-plant"));

        given().contentType(ContentType.JSON)
                .post("/api/users/" + clientId + "/deactivate")
                .then()
                .statusCode(200);

        String rentJson = """
                {
                    "clientId": "%s",
                    "plantId": "%s"
                }
                """.formatted(clientId, plantId);
        given().contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(409);
    }

    @Test
    public void validationTest() {
        String plantId = createPlant(uniqueName("validation-plant"));
        String invalidRentJson = """
                {
                    "clientId": null,
                    "plantId": "%s"
                }
                """.formatted(plantId);
        given().contentType(ContentType.JSON)
                .body(invalidRentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(400);
    }

    @Test
    public void validationTest_PlantIdNull() {
        String clientId = createClient(uniqueName("validation-client"), true);
        String invalidRentJson = """
                {
                    "clientId": "%s",
                    "plantId": null
                }
                """.formatted(clientId);
        given().contentType(ContentType.JSON)
                .body(invalidRentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(400);
    }

    @Test
    public void CreateRent_ClientNotFound() {
        String plantId = createPlant(uniqueName("no-client-plant"));
        String rentJson = """
                {
                    "clientId": "000000000000000000000000",
                    "plantId": "%s"
                }
                """.formatted(plantId);
        given().contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(404);
    }

    @Test
    public void CreateRent_PlantNotFound() {
        String clientId = createClient(uniqueName("no-plant-client"), true);
        String rentJson = """
                {
                    "clientId": "%s",
                    "plantId": "000000000000000000000000"
                }
                """.formatted(clientId);
        given().contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(404);
    }

    @Test
    public void maxRentsLimitTest() {
        String clientLogin = uniqueName("max-rents-client");
        String clientId = createClient(clientLogin, true);
        int actualMaxRents = given().when()
                .get("/api/users/" + clientId)
                .then()
                .statusCode(200)
                .extract().path("maxRents");

        for (int i = 0; i < actualMaxRents; i++) {
            String plantId = createPlant(uniqueName("plant-" + i));
            createRent(clientId, plantId);
        }

        String oneTooManyPlantId = createPlant(uniqueName("plant-too-many"));
        String conflictRentJson = """
                {
                    "clientId": "%s",
                    "plantId": "%s"
                }
                """.formatted(clientId, oneTooManyPlantId);
        given().contentType(ContentType.JSON)
                .body(conflictRentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(409);
    }

    @Test
    public void maxRentsLimitTest_AfterRemove_CanRentAgain() {
        String clientLogin = uniqueName("max-rents-remove-client");
        String clientId = createClient(clientLogin, true);
        int actualMaxRents = given().when()
                .get("/api/users/" + clientId)
                .then()
                .statusCode(200)
                .extract().path("maxRents");

        String firstRentId = null;
        for (int i = 0; i < actualMaxRents; i++) {
            String plantId = createPlant(uniqueName("max-remove-plant-" + i));
            String rentId = createRent(clientId, plantId);
            if (i == 0) firstRentId = rentId;
        }

        given().when()
                .delete("/api/rents/" + firstRentId)
                .then()
                .statusCode(204);

        String newPlantId = createPlant(uniqueName("max-remove-new-plant"));
        String rentJson = """
                {
                    "clientId": "%s",
                    "plantId": "%s"
                }
                """.formatted(clientId, newPlantId);
        given().contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(201);
    }

    @Test
    public void CreateRent_AfterRemove_PlantCanBeRentedAgain() {
        String clientId1 = createClient(uniqueName("reuse-rm-client-1"), true);
        String clientId2 = createClient(uniqueName("reuse-rm-client-2"), true);
        String plantId = createPlant(uniqueName("reuse-rm-plant"));

        String rentId = createRent(clientId1, plantId);

        given().when()
                .delete("/api/rents/" + rentId)
                .then()
                .statusCode(204);

        String rentJson = """
                {
                    "clientId": "%s",
                    "plantId": "%s"
                }
                """.formatted(clientId2, plantId);
        given().contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(201);
    }

    @Test
    public void CreateRent_WithCustomRentStart() {
        String clientId = createClient(uniqueName("custom-start-client"), true);
        String plantId = createPlant(uniqueName("custom-start-plant"));
        String rentJson = """
                {
                    "clientId": "%s",
                    "plantId": "%s",
                    "rentStart": "2024-01-15T10:00:00"
                }
                """.formatted(clientId, plantId);
        given().contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(201);

        given().when()
                .get("/api/rents")
                .then()
                .statusCode(200)
                .body("rentStart", hasItem(startsWith("2024-01-15")));
    }

    @Test
    public void CreateRent_WithoutRentStart_UsesNow() {
        String clientId = createClient(uniqueName("default-start-client"), true);
        String plantId = createPlant(uniqueName("default-start-plant"));
        String rentJson = """
                {
                    "clientId": "%s",
                    "plantId": "%s"
                }
                """.formatted(clientId, plantId);
        Response response = given().contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(201)
                .extract().response();
        String rentId = extractId(response);

        given().when()
                .get("/api/rents/" + rentId)
                .then()
                .statusCode(200)
                .body("rentStart", notNullValue());
    }

    @Test
    public void GetRents_ReturnsAllRents() {
        String clientId = createClient(uniqueName("all-rents-client"), true);
        String plantId1 = createPlant(uniqueName("all-rents-plant-1"));
        String plantId2 = createPlant(uniqueName("all-rents-plant-2"));
        String rentId1 = createRent(clientId, plantId1);
        String rentId2 = createRent(clientId, plantId2);

        given().when()
                .get("/api/rents")
                .then()
                .statusCode(200)
                .body("id", hasItems(rentId1, rentId2));
    }
}