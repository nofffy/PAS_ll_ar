package controllers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class userControllerTest {

    private String uniqueLogin(String prefix) {
        return prefix + "-" + UUID.randomUUID();
    }

    private String extractId(Response response) {
        String location = response.getHeader("Location");
        if (location == null) {
            throw new IllegalStateException("Brak nagłówka Location w odpowiedzi 201");
        }
        return location.substring(location.lastIndexOf('/') + 1);
    }

    private String createUser(String login, boolean active, String type) {
        String json = """
                {
                    "login": "%s",
                    "active": %s,
                    "type": "%s"
                }
                """.formatted(login, active, type);

        Response response = given().contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().response();

        return extractId(response);
    }

    //TESTY CRU(D)
    @Test
    public void CreateUser() {
        String login = uniqueLogin("loginnnovember");
        String json = """
                {
                    "login": "%s",
                    "active": true,
                    "type": "CLIENT"
                }
                """.formatted(login);

        given().contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201);

        given().when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("login", hasItem(login));
    }

    @Test
    public void ReadUser() {
        String login = uniqueLogin("read-user");
        String id = createUser(login, true, "CLIENT");

        given().when()
                .get("/api/users/" + id)
                .then()
                .statusCode(200)
                .body("login", equalTo(login))
                .body("active", equalTo(true));
    }

    @Test
    public void UpdateUser() {
        String login = uniqueLogin("update-user");
        String id = createUser(login, false, "CLIENT");

        String updatedLogin = uniqueLogin("unbanned_michael");
        String updatedJson = """
                {
                    "login": "%s",
                    "active": true,
                    "type": "CLIENT"
                }
                """.formatted(updatedLogin);

        given().contentType(ContentType.JSON)
                .body(updatedJson)
                .when()
                .put("/api/users/" + id)
                .then()
                .statusCode(204);

        given().when()
                .get("/api/users/" + id)
                .then()
                .statusCode(200)
                .body("login", equalTo(updatedLogin))
                .body("active", equalTo(true));
    }

    @Test
    public void DeactivateUser() {
        String login = uniqueLogin("deactivate-user");
        String id = createUser(login, true, "CLIENT");

        given().when()
                .get("/api/users/" + id)
                .then()
                .statusCode(200)
                .body("active", equalTo(true));

        given().when()
                .contentType(ContentType.JSON)
                .post("/api/users/" + id + "/deactivate")
                .then()
                .statusCode(200);

        given().when()
                .get("/api/users/" + id)
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
        String login = uniqueLogin("duplicate-user");
        createUser(login, true, "CLIENT");

        String duplicateJson = """
                {
                    "login": "%s",
                    "active": true,
                    "type": "CLIENT"
                }
                """.formatted(login);

        given().contentType(ContentType.JSON)
                .body(duplicateJson)
                .when()
                .post("/api/users")
                .then()
                .statusCode(409);
    }
}