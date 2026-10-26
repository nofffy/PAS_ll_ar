package controllers;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class userControllerTest {
    //TESTY CRU(D)
    @Test
    public void CreateUser() {
        String validUserJson = """
                {
                    "login": "loginnnovember",
                    "active": true,
                    "type": "CLIENT"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(validUserJson)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201);

        given().when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("login", hasItem("loginnnovember"));
    }

    @Test
    public void ReadUser() {
        given().when()
                .get("/api/users/1")
                .then()
                .statusCode(200)
                .body("login", equalTo("admin1"))
                .body("active", equalTo(true));
    }

    @Test
    public void UpdateUser() {
        given().when()
                .get("/api/users/4")
                .then()
                .statusCode(200)
                .body("login", equalTo("banned_michael"));

        String updatedUserJson = """
                {
                    "login": "unbanned_michael",
                    "active": true,
                    "type": "CLIENT"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(updatedUserJson)
                .when()
                .put("/api/users/4")
                .then()
                .statusCode(204);

        given().when()
                .get("/api/users/4")
                .then()
                .statusCode(200)
                .body("login", equalTo("unbanned_michael"))
                .body("active", equalTo(true));
    }

    @Test
    public void DeactivateUser() {
        given().when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("active", equalTo(true));

        given().when()
                .contentType(ContentType.JSON)
                .post("/api/users/2/deactivate")
                .then()
                .statusCode(200);

        given().when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("active", equalTo(false));
    }

    // TESTY NEGATYWNE
    @Test
    public void validationTest1() {
        String invalidUserJson = """
                {
                    "login": "",
                    "active": true,
                    "type": "CLIENT"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(invalidUserJson)
                .when()
                .post("/api/users")
                .then()
                .statusCode(400);
    }

    @Test
    public void validationTest2() {
        String invalidUserJson = """
                {
                    "login": "hacker",
                    "active": true,
                    "type": "OPSECLVLGOD"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(invalidUserJson)
                .when()
                .post("/api/users")
                .then()
                .statusCode(400);
    }

    @Test
    public void uniqueLoginTest() {
        String duplicateUserJson = """
                {
                    "login": "admin1",
                    "active": true,
                    "type": "CLIENT"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(duplicateUserJson)
                .when()
                .post("/api/users")
                .then()
                .statusCode(409);
    }
}