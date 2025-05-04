package praktikum;

import io.restassured.response.Response;
import model.LoginUserData;

import static constants.Endpoints.USER_LOGIN;
import static io.restassured.RestAssured.given;

public class LoginUser {
    public static Response postLoginUser (String email, String password) {

        LoginUserData newLoginUserData = new LoginUserData(email, password);
        return given()
                .header("Content-type", "application/json")
                .body(newLoginUserData)
                .when()
                .post(USER_LOGIN);
    }
}
