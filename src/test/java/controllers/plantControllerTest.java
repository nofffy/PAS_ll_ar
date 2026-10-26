package controllers;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class plantControllerTest {
    //TESTY CRUD
    @Test
    public void CreatePlant() {
        String validPlantJson = """
                {
                    "name": "Nowa Testowa",
                    "color": "yellow",
                    "cost": 45.5,
                    "available": true,
                    "type": "BASIC"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(validPlantJson)
                .when()
                .post("/api/plants")
                .then()
                .statusCode(201);

        given().when()
                .get("/api/plants")
                .then()
                .statusCode(200)
                .body("name", hasItem("Nowa Testowa"));
    }

    @Test
    public void ReadPlant() {
        given().when()
                .get("/api/plants/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("se_jem_muchy"))
                .body("cost", equalTo(89.0F));
    }

    @Test
    public void UpdatePlant() {
        given().when()
                .get("/api/plants/3")
                .then()
                .statusCode(200)
                .body("name", equalTo("Kaktus Meksykański"));

        String updatedPlantJson = """
                {
                    "name": "aktualizacja Kaktus",
                    "color": "pink",
                    "cost": 25.0,
                    "available": true,
                    "type": "SUCCULENT",
                    "spiky": true
                }
                """;

        given().contentType(ContentType.JSON)
                .body(updatedPlantJson)
                .when()
                .put("/api/plants/3")
                .then()
                .statusCode(204);

        given().when()
                .get("/api/plants/3")
                .then()
                .statusCode(200)
                .body("name", equalTo("aktualizacja Kaktus"))
                .body("cost", equalTo(25.0F));
    }

    @Test
    public void DeletePlant() {
        given().when()
                .delete("/api/plants/4")
                .then()
                .statusCode(204);

        given().when()
                .get("/api/plants/4")
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
        given().when()
                .delete("/api/plants/2")
                .then()
                .statusCode(409);
    }
}