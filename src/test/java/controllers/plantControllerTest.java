package controllers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class plantControllerTest {

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

    private String createPlant(String name, String color, double cost, boolean available, String type) {
        String json = """
                {
                    "name": "%s",
                    "color": "%s",
                    "cost": %s,
                    "available": %s,
                    "type": "%s"
                }
                """.formatted(name, color, cost, available, type);

        Response response = given().contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/plants")
                .then()
                .statusCode(201)
                .extract().response();

        return extractId(response);
    }

    //TESTY CRUD
    @Test
    public void CreatePlant() {
        String name = uniqueName("Nowa Testowa");
        String json = """
                {
                    "name": "%s",
                    "color": "yellow",
                    "cost": 45.5,
                    "available": true,
                    "type": "BASIC"
                }
                """.formatted(name);

        given().contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/plants")
                .then()
                .statusCode(201);

        given().when()
                .get("/api/plants")
                .then()
                .statusCode(200)
                .body("name", hasItem(name));
    }

    @Test
    public void ReadPlant() {
        String name = uniqueName("read-plant");
        String id = createPlant(name, "green", 89.0, true, "BASIC");

        given().when()
                .get("/api/plants/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("cost", equalTo(89.0F));
    }

    @Test
    public void UpdatePlant() {
        String name = uniqueName("update-plant");
        String id = createPlant(name, "green", 10.0, true, "BASIC");

        String updatedName = uniqueName("aktualizacja Kaktus");
        String updatedJson = """
                {
                    "name": "%s",
                    "color": "pink",
                    "cost": 25.0,
                    "available": true,
                    "type": "SUCCULENT",
                    "spiky": true
                }
                """.formatted(updatedName);

        given().contentType(ContentType.JSON)
                .body(updatedJson)
                .when()
                .put("/api/plants/" + id)
                .then()
                .statusCode(204);

        given().when()
                .get("/api/plants/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo(updatedName))
                .body("cost", equalTo(25.0F));
    }

    @Test
    public void DeletePlant() {
        String name = uniqueName("delete-plant");
        String id = createPlant(name, "green", 10.0, true, "BASIC");

        given().when()
                .delete("/api/plants/" + id)
                .then()
                .statusCode(204);

        given().when()
                .get("/api/plants/" + id)
                .then()
                .statusCode(404);
    }

    //TESTY NEGATYWNE
    @Test
    public void validationTest1() {
        String invalidPlantJson = """
                {
                    "name": "zbanowany michal",
                    "color": "black",
                    "cost": -50.0,
                    "available": true,
                    "type": "BASIC"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(invalidPlantJson)
                .when()
                .post("/api/plants")
                .then()
                .statusCode(400);
    }

    @Test
    public void validationTest2() {
        String invalidPlantJson = """
                {
                    "name": "zbanowany michalnumer2",
                    "cost": 50.0,
                    "available": true,
                    "type": "BASIC"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(invalidPlantJson)
                .when()
                .post("/api/plants")
                .then()
                .statusCode(400);
    }

    @Test
    public void isRentedTest() {
        // Budujemy stan: klient + roślina + rent
        String clientLogin = uniqueName("rented-client");
        String clientJson = """
                {
                    "login": "%s",
                    "active": true,
                    "type": "CLIENT"
                }
                """.formatted(clientLogin);

        Response clientResponse = given().contentType(ContentType.JSON)
                .body(clientJson)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().response();

        String clientId = extractId(clientResponse);

        String plantName = uniqueName("rented-plant");
        String plantId = createPlant(plantName, "green", 10.0, true, "BASIC");

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

        // Próba usunięcia wypożyczonej rośliny
        given().when()
                .delete("/api/plants/" + plantId)
                .then()
                .statusCode(409);
    }
}