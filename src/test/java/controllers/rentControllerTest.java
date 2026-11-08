package controllers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

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

    //TESTY CRD
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

    //TESTY NEGATYWNE
    @Test
    public void isAlreadyRentedTest() {
        String clientId = createClient(uniqueName("conflict-client"), true);
        String plantId = createPlant(uniqueName("conflict-plant"));

        // Pierwszy rent — sukces
        createRent(clientId, plantId);

        // Drugi rent tej samej rośliny — konflikt
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

        // Deaktywacja klienta
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
}