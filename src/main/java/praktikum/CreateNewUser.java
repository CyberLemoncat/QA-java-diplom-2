package praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.UserData;

import static constants.Endpoints.CREATE_USER;
import static io.restassured.RestAssured.given;

public class CreateNewUser {
    public static Response postNewUser (String email,String password, String name) {
        UserData newUserData = new UserData(email, password, name);
        return given()
                .contentType(ContentType.JSON)
                .body(newUserData)
                .when()
                .post(CREATE_USER);
    }
}
