package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Ingredients;
import model.RandomData;
import model.UserData;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static praktikum.UserSteps.*;
import static praktikum.UserSteps.checkStatusCode;

public class CreateOrderTest extends BaseClass {
    public UserData randomUser;
    private UserSteps userSteps;
    private UserData user;
    private String accessToken;

    @Test
    @DisplayName("Создание заказа с добавлением существующих ингредиентов")
    @Description("Создаём заказ, добавляя ингредиенты по одному в список")
    public void testCreateOrder() {

        randomUser = RandomData.generateRandomUser();
        user = randomUser;

        Response response = (Response) createUser(randomUser.getEmail(), randomUser.getPassword(),randomUser.getName(), 200);
        checkStatusCode(response, 200);

        Response loginResponse = loginUser(randomUser.getEmail(), randomUser.getPassword());
        checkStatusCode(loginResponse, 200);

        System.out.println("Login response body: " + loginResponse.getBody().asString());
        accessToken = loginResponse.jsonPath().getString("accessToken");
        String refreshToken = loginResponse.jsonPath().getString("refreshToken");

        List<Ingredients> ingredientsList = GetIngredients.getAllIngredients();
        List<String> ingredientIds = UserSteps.collectIngredientIds(ingredientsList);

        Response orderResponse = CreateOrder.createOrderWithAuthorization(ingredientIds, accessToken);
        orderResponse.then().statusCode(200);
        orderResponse.then().body("success", equalTo(true));

    }

    @Test
    @DisplayName("Создание заказа с добавлением существующих ингредиентов без авторизации")
    @Description("Создаём заказ, без авторизации с существующими ингридиентами")
    public void testCreateOrderWithoutAuthorization() {

        List<Ingredients> ingredientsList = GetIngredients.getAllIngredients();
        List<String> ingredientIds = UserSteps.collectIngredientIds(ingredientsList);

        Response orderResponse = CreateOrder.createOrderWithoutAuthorization(ingredientIds);
        orderResponse.then().statusCode(200);
        orderResponse.then().body("success", equalTo(true));

    }
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Создаём заказ, с пустым списком ингредиентов")
    public void testCreateOrderWithoutIngredients() {

        randomUser = RandomData.generateRandomUser();
        user = randomUser;

        Response response = (Response) createUser(randomUser.getEmail(), randomUser.getPassword(),randomUser.getName(), 200);
        checkStatusCode(response, 200);

        Response loginResponse = loginUser(randomUser.getEmail(), randomUser.getPassword());
        checkStatusCode(loginResponse, 200);

        System.out.println("Login response body: " + loginResponse.getBody().asString());
        accessToken = loginResponse.jsonPath().getString("accessToken");
        String refreshToken = loginResponse.jsonPath().getString("refreshToken");

        List<String> zeroIngredients = new ArrayList<>();

        Response orderResponse = CreateOrder.createOrderWithAuthorization(zeroIngredients, accessToken);
        orderResponse.then().statusCode(400);
        orderResponse.then().body("success", equalTo(false));
        orderResponse.then().body("message", equalTo("Ingredient ids must be provided"));

    }
    @Test
    @DisplayName("Создание заказа без ингредиентов и авторизации")
    @Description("Создаём заказ, с пустым списком ингредиентов, без автризации")
    public void testCreateOrderWithoutIngredientsAndAuthorization() {
        List<String> zeroIngredients = new ArrayList<>();

        Response response = CreateOrder.createOrderWithoutAuthorization(zeroIngredients);
        response.then().statusCode(400);
        response.then().body("success", equalTo(false));
        response.then().body("message", equalTo("Ingredient ids must be provided"));

    }
    @Test
    @DisplayName("Создание заказа с невалидным хешем ингредиента ")
    @Description("Передаем в теле несуществующий хеш ингредиента, возвращается ошибка")
    public void testCreateOrderWithInvalidIngredientId() {
        randomUser = RandomData.generateRandomUser();
        user = randomUser;

        Response response = (Response) createUser(randomUser.getEmail(), randomUser.getPassword(),randomUser.getName(), 200);
        checkStatusCode(response, 200);

        Response loginResponse = loginUser(randomUser.getEmail(), randomUser.getPassword());
        checkStatusCode(loginResponse, 200);

        System.out.println("Login response body: " + loginResponse.getBody().asString());
        accessToken = loginResponse.jsonPath().getString("accessToken");
        String refreshToken = loginResponse.jsonPath().getString("refreshToken");

        List<String> fakeIngredients = new ArrayList<>();
        fakeIngredients.add("invalid_ingredient_hash");

        Response orderResponse = CreateOrder.createOrderWithAuthorization(fakeIngredients, accessToken);
        orderResponse.then().statusCode(500);
    }
    @Test
    @DisplayName("Создание заказа с невалидным хешем ингредиента без авторизации")
    @Description("Передаем в теле несуществующий хеш ингредиента, возвращается ошибка")
    public void testCreateOrderWithInvalidIngredientIdWithoutAuthorization() {

        List<String> fakeIngredients = new ArrayList<>();
        fakeIngredients.add("invalid_ingredient_hash");

        Response response = CreateOrder.createOrderWithoutAuthorization(fakeIngredients);
        response.then().statusCode(500);
    }
    @After
    public void deleteCourier (){
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }
}
