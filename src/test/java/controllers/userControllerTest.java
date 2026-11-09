package controllers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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
    public void GetUserByLogin_Found() {
        String login = uniqueLogin("by-login-user");
        createUser(login, true, "CLIENT");
        given().when()
                .get("/api/users/login/" + login)
                .then()
                .statusCode(200)
                .body("login", equalTo(login));
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
    public void UpdateUser_ChangeType_ClientToAdmin() {
        String login = uniqueLogin("type-change-user");
        String id = createUser(login, true, "CLIENT");
        String updatedJson = """
                {
                    "login": "%s",
                    "active": true,
                    "type": "ADMIN"
                }
                """.formatted(login);
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
                .body("type", equalTo("ADMIN"));
    }

    @Test
    public void UpdateUser_ChangeLoginToExisting_ShouldFail() {
        String login1 = uniqueLogin("existing-login-1");
        String login2 = uniqueLogin("existing-login-2");
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
    public void ActivateUser_InvalidId() {
        given().contentType(ContentType.JSON)
                .when()
                .post("/api/users/not-a-valid-object-id/activate")
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
    public void DeactivateUser_InvalidId() {
        given().contentType(ContentType.JSON)
                .when()
                .post("/api/users/not-a-valid-object-id/deactivate")
                .then()
                .statusCode(404);
    }

    @Test
    public void GetUsersByLoginPattern_ReturnsMatching() {
        String prefix = "pattern-" + UUID.randomUUID();
        String login1 = prefix + "-alpha";
        String login2 = prefix + "-beta";
        String login3 = uniqueLogin("other-user");
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
        String activeLogin = uniqueLogin("active-user");
        String inactiveLogin = uniqueLogin("inactive-user");
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
        String activeLogin = uniqueLogin("active-user-2");
        String inactiveLogin = uniqueLogin("inactive-user-2");
        createUser(activeLogin, true, "CLIENT");
        createUser(inactiveLogin, false, "CLIENT");

        given().when()
                .get("/api/users?active=false")
                .then()
                .statusCode(200)
                .body("login", hasItem(inactiveLogin))
                .body("login", not(hasItem(activeLogin)));
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

    @Test
    public void CreateUser_Admin() {
        String login = uniqueLogin("admin-user");
        String id = createUser(login, true, "ADMIN");
        given().when()
                .get("/api/users/" + id)
                .then()
                .statusCode(200)
                .body("type", equalTo("ADMIN"));
    }

    @Test
    public void CreateUser_Gardener() {
        String login = uniqueLogin("gardener-user");
        String id = createUser(login, true, "GARDENER");
        given().when()
                .get("/api/users/" + id)
                .then()
                .statusCode(200)
                .body("type", equalTo("GARDENER"));
    }

    @Test
    public void CreateUser_Client_HasMaxRents() {
        String login = uniqueLogin("client-with-max");
        String id = createUser(login, true, "CLIENT");
        given().when()
                .get("/api/users/" + id)
                .then()
                .statusCode(200)
                .body("maxRents", notNullValue());
    }
}