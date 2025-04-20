package praktikum;

import io.restassured.response.Response;

import static constants.Endpoints.DELETE_USER;
import static io.restassured.RestAssured.given;

public class DeleteUser {
    public static Response deleteUser(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .delete(DELETE_USER);
    }
}

