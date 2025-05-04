package praktikum;

import io.restassured.response.Response;

import static constants.Endpoints.GET_USER_ORDER;
import static io.restassured.RestAssured.given;

public class GetUserOrder {
    public static Response getUserOrder (String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get(GET_USER_ORDER);
    }
}
