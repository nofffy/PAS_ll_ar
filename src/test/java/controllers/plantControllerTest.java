package controllers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class plantControllerTest {
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

    private String createClient(String login) {
        String json = """
                {
                    "login": "%s",
                    "active": true,
                    "type": "CLIENT"
                }
                """.formatted(login);
        Response response = given().contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/users")
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

    //Testy CRUD

    @Test
    public void CreatePlant() {
        String name = "Nowa Testowa";
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
        String name = "read-plant";
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
        String name = "update-plant";
        String id = createPlant(name, "green", 10.0, true, "BASIC");
        String updatedName = "aktualizacja Kaktus";
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
        String name = "delete-plant";
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

    //Testy logiki

    @Test
    public void DeletePlant_WithoutRents_ShouldSucceed() {
        String plantId = createPlant("no-rent-plant", "green", 10.0, true, "BASIC");
        given().when()
                .delete("/api/plants/" + plantId)
                .then()
                .statusCode(204);
    }

    //Testy negatywne

    @Test
    public void GetPlant_NotFound() {
        given().when()
                .get("/api/plants/000000000000000000000000")
                .then()
                .statusCode(404);
    }

    @Test
    public void UpdatePlant_NotFound() {
        String json = """
                {
                    "name": "ghost",
                    "color": "green",
                    "cost": 10.0,
                    "available": true,
                    "type": "BASIC"
                }
                """;
        given().contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/plants/000000000000000000000000")
                .then()
                .statusCode(404);
    }

    @Test
    public void DeletePlant_NotFound() {
        given().when()
                .delete("/api/plants/000000000000000000000000")
                .then()
                .statusCode(404);   // ← dostajesz 204, bo removePlant cicho nic nie robi
    }

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
    public void validationTest_InvalidType() {
        String invalidPlantJson = """
                {
                    "name": "invalid-type-plant",
                    "color": "green",
                    "cost": 10.0,
                    "available": true,
                    "type": "UNKNOWN_TYPE"
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
    public void validationTest_EmptyName() {
        String invalidPlantJson = """
                {
                    "name": "",
                    "color": "green",
                    "cost": 10.0,
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
    public void isRentedDeleteTest() {
        String clientId = createClient("rented-client");
        String plantId = createPlant("rented-plant", "green", 10.0, true, "BASIC");
        createRent(clientId, plantId);

        given().when()
                .delete("/api/plants/" + plantId)
                .then()
                .statusCode(409);
    }
}