package controllers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class userControllerTest {

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

    //Testy CRUD

    @Test
    public void CreateUser() {
        String login = "loginnnovember";
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
        String login = "read-user";
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
        String login = "update-user";
        String id = createUser(login, false, "CLIENT");
        String updatedLogin = "unbanned_michael";
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
    public void ActivateUser() {
        String login = "activate-user";
        String id = createUser(login, false, "CLIENT");
        given().when()
                .get("/api/users/" + id)
                .then()
                .statusCode(200)
                .body("active", equalTo(false));
        given().when()
                .contentType(ContentType.JSON)
                .post("/api/users/" + id + "/activate")
                .then()
                .statusCode(200);
        given().when()
                .get("/api/users/" + id)
                .then()
                .statusCode(200)
                .body("active", equalTo(true));
    }

    @Test
    public void DeactivateUser() {
        String login = "deactivate-user";
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

    //Testy logiki

    @Test
    public void CreateUser_Client_HasMaxRents() {
        String login = "client-with-max";
        String id = createUser(login, true, "CLIENT");
        given().when()
                .get("/api/users/" + id)
                .then()
                .statusCode(200)
                .body("maxRents", notNullValue());
    }

    @Test
    public void GetUserByLogin_Found() {
        String login = "by-login-user";
        createUser(login, true, "CLIENT");
        given().when()
                .get("/api/users/login/" + login)
                .then()
                .statusCode(200)
                .body("login", equalTo(login));
    }

    @Test
    public void GetUsersByLoginPattern_ReturnsMatching() {
        String prefix = "z-pozdrowieniami-z-wakacji";
        String login1 = prefix + "-alpha";
        String login2 = prefix + "-sigma";
        String login3 = "other-user";
        createUser(login1, true, "CLIENT");
        createUser(login2, true, "CLIENT");
        createUser(login3, true, "CLIENT");

        given().when()
                .get("/api/users?loginPattern=" + prefix)
                .then()
                .statusCode(200)
                .body("login", hasItems(login1, login2))
                .body("login", not(hasItem(login3)));
    }

    @Test
    public void FindAllActive_ReturnsOnlyActive() {
        String activeLogin = "active-user";
        String inactiveLogin = "inactive-user";
        createUser(activeLogin, true, "CLIENT");
        createUser(inactiveLogin, false, "CLIENT");

        given().when()
                .get("/api/users?active=true")
                .then()
                .statusCode(200)
                .body("login", hasItem(activeLogin))
                .body("login", not(hasItem(inactiveLogin)));
    }

    @Test
    public void FindAllInactive_ReturnsOnlyInactive() {
        String activeLogin = "active-user-2";
        String inactiveLogin = "inactive-user-2";
        createUser(activeLogin, true, "CLIENT");
        createUser(inactiveLogin, false, "CLIENT");

        given().when()
                .get("/api/users?active=false")
                .then()
                .statusCode(200)
                .body("login", hasItem(inactiveLogin))
                .body("login", not(hasItem(activeLogin)));
    }

    //Testy negatywne

    @Test
    public void GetUser_NotFound() {
        given().when()
                .get("/api/users/000000000000000000000000")
                .then()
                .statusCode(404);
    }

    @Test
    public void GetUser_InvalidId() {
        given().when()
                .get("/api/users/not-a-valid-object-id")
                .then()
                .statusCode(404);
    }

    @Test
    public void GetUserByLogin_NotFound() {
        given().when()
                .get("/api/users/login/nonexistent-login-xyz")
                .then()
                .statusCode(404);
    }

    @Test
    public void UpdateUser_NotFound() {
        String json = """
                {
                    "login": "ghost-user",
                    "active": true,
                    "type": "CLIENT"
                }
                """;
        given().contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/users/000000000000000000000000")
                .then()
                .statusCode(404);
    }

    @Test
    public void UpdateUser_InvalidId() {
        String json = """
                {
                    "login": "ghost-user",
                    "active": true,
                    "type": "CLIENT"
                }
                """;
        given().contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/users/not-a-valid-object-id")
                .then()
                .statusCode(404);
    }

    @Test
    public void UpdateUser_ChangeLoginToExisting_ShouldFail() {
        String login1 = "existing-login-1";
        String login2 = "existing-login-2";
        createUser(login1, true, "CLIENT");
        String id2 = createUser(login2, true, "CLIENT");

        String updatedJson = """
                {
                    "login": "%s",
                    "active": true,
                    "type": "CLIENT"
                }
                """.formatted(login1);
        given().contentType(ContentType.JSON)
                .body(updatedJson)
                .when()
                .put("/api/users/" + id2)
                .then()
                .statusCode(409);
    }

    @Test
    public void ActivateUser_NotFound() {
        given().contentType(ContentType.JSON)
                .when()
                .post("/api/users/000000000000000000000000/activate")
                .then()
                .statusCode(404);
    }

    @Test
    public void DeactivateUser_NotFound() {
        given().contentType(ContentType.JSON)
                .when()
                .post("/api/users/000000000000000000000000/deactivate")
                .then()
                .statusCode(404);
    }

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
    public void validationTest_NullLogin() {
        String invalidUserJson = """
                {
                    "login": null,
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
    public void validationTest_NullType() {
        String invalidUserJson = """
                {
                    "login": "no-type-user",
                    "active": true,
                    "type": null
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
        String login = "duplicate-user";
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