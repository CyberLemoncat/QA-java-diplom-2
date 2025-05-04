package praktikum;

import io.restassured.response.Response;

import model.OrderData;

import java.util.List;

import static constants.Endpoints.CREATE_ORDER;
import static io.restassured.RestAssured.given;

public class CreateOrder {
    public static Response createOrderWithoutAuthorization(List<String> ingredientIds) {
        OrderData order = new OrderData(ingredientIds);
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(CREATE_ORDER);
    }
    public static Response createOrderWithAuthorization(List<String> ingredientIds, String token) {
        OrderData order = new OrderData(ingredientIds);
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(order)
                .when()
                .post(CREATE_ORDER);
    }
}
