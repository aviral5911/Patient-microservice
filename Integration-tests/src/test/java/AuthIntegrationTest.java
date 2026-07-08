import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;



public class AuthIntegrationTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnOkWithJWTToken() {

        String loginPayload = """
                  {
                    "email": "testuser@test.com",
                    "password": "password123"
                  }
                """;

        // given - when - then (setup , fire , assertions )
        Response response =
                given()
                        .contentType("application/json")
                        .body(loginPayload)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .body("token", notNullValue())
                        .extract()
                        .response();

        System.out.println("Generated Token: " + response.jsonPath().getString("token"));
    }

    @Test
    public void shouldReturn401OnInvalidCredentials() {
        String credentials = """
                {
                    "email": "testwronguser@test.com",
                    "password": "wrong_password123"
                }
                """;

        given()
                .contentType("application/json")
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldReturnPatients() {
        String credentials = """
                {
                    "email": "testuser@test.com",
                    "password": "password123"
                  }
                """;

        Response response =
                given()
                        .contentType("application/json")
                        .body(credentials)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        String token = response.jsonPath().getString("token");

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/patient")
                .then()
                .statusCode(200)
                .body("patients", notNullValue());


    }
}
