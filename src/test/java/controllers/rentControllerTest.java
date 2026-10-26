package controllers;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class rentControllerTest {
    //TESTY CRD
    @Test
    public void CreateRent() {
        String validRentJson = """
                {
                    "clientId": 5,
                    "plantId": 5
                }
                """;

        given().contentType(ContentType.JSON)
                .body(validRentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(201);

        given().when()
                .get("/api/rents")
                .then()
                .statusCode(200)
                .body("client.id", hasItem(5));
    }

    @Test
    public void ReadRent() {
        given().when()
                .get("/api/rents/1")
                .then()
                .statusCode(200)
                .body("client.login", equalTo("client123"))
                .body("plant.name", equalTo("monsterek"));
    }

    @Test
    public void DeleteRent() {
        String tempRentJson = """
                {
                    "clientId": 1,
                    "plantId": 1
                }
                """;

        given().contentType(ContentType.JSON)
                .body(tempRentJson)
                .post("/api/rents")
                .then()
                .statusCode(201);

        given().when()
                .delete("/api/rents/4")
                .then()
                .statusCode(204);

        given().when()
                .get("/api/rents/4")
                .then()
                .statusCode(404);
    }

    //TESTY NEGATYWNE

    @Test
    public void isAlreadyRentedTest() {
        String conflictRentJson = """
                {
                    "clientId": 3,
                    "plantId": 2
                }
                """;

        given().contentType(ContentType.JSON)
                .body(conflictRentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(409);
    }

    @Test
    public void inactiveClientTest() {
        String inactiveUserRentJson = """
                {
                    "clientId": 3,
                    "plantId": 3
                }
                """;

        given().contentType(ContentType.JSON)
                .body(inactiveUserRentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(409);
    }

    @Test
    public void validationTest() {
        String invalidRentJson = """
                {
                    "clientId": null,
                    "plantId": 3
                }
                """;

        given().contentType(ContentType.JSON)
                .body(invalidRentJson)
                .when()
                .post("/api/rents")
                .then()
                .statusCode(400);
    }
}