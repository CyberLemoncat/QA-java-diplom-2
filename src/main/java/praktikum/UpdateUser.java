package praktikum;

import io.restassured.response.Response;
import model.UpdateUserData;

import static constants.Endpoints.UPDATE_USER;
import static io.restassured.RestAssured.given;

public class UpdateUser {
    public static Response patchUpdateUser (String email, String name, String accessToken) {
        UpdateUserData UpdateUserData = new UpdateUserData(email, name);
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(UpdateUserData)
                .when()
                .patch(UPDATE_USER);
    }
    public static Response upDateUserWithoutToken(String email, String name) {
        UpdateUserData data = new UpdateUserData(email, name);
        return given()
                .header("Content-type", "application/json")
                .body(data)
                .when()
                .patch(UPDATE_USER);
    }
}
