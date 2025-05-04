package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Ingredients;
import model.RandomData;
import model.UserData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static praktikum.UserSteps.*;
import static praktikum.UserSteps.checkStatusCode;

public class GetUserOrderTest extends Model {
    public UserData randomUser;
    private UserSteps userSteps;
    private UserData user;
    private String accessToken;
    @Before
    public void createUserAndLogin() {
        randomUser = RandomData.generateRandomUser();
        Response response = UserSteps.createUser(randomUser.getEmail(), randomUser.getPassword(), randomUser.getName(), 200);
        UserSteps.checkStatusCode(response, 200);
        System.out.println(response.body().asString());

        Response loginResponse = UserSteps.loginUser(randomUser.getEmail(), randomUser.getPassword());
        UserSteps.checkStatusCode(loginResponse, 200);

        accessToken = loginResponse.jsonPath().getString("accessToken");
    }
    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Проверяем, что возвращаются заказы авторизованногопользователя")
    public void testGetUserOrdersWithAuth(){
        List<Ingredients> ingredientsList = GetIngredients.getAllIngredients();
        List<String> ingredientIds = UserSteps.collectIngredientIds(ingredientsList);

        Response orderResponse = CreateOrder.createOrderWithAuthorization(ingredientIds, accessToken);
        Response getOrderResponse = UserSteps.getOrderWithAuthorization(accessToken);

        getOrderResponse.then().statusCode(200);
        getOrderResponse.then().body("success", equalTo(true));
        getOrderResponse.then().body("orders.size()", greaterThan(0));
    }
    @Test
    @DisplayName("Получение заказов без авторизации")
    @Description("Проверяем, что без токена нельзя получить заказы пользователя")
    public void testGetUserOrdersWithoutAuth() {
        Response response = UserSteps.getOrderWithAuthorization(""); // пустой токен
        response.then().statusCode(401);
        response.then().body("success", equalTo(false));
        response.then().body("message", equalTo("You should be authorised"));
    }
    @After
    public void deleteCourier (){
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }
}
