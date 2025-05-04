package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Ingredients;
import model.UserData;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;


public class UserSteps {
    @Step("Создание пользователя с почтой, паролем и именем")
    public static Response createUser(String email, String password, String name, int expectedStatus) {
        UserData user = new UserData(email, password, name);
        Response response = CreateNewUser.postNewUser(email, password, name);
        checkStatusCode(response, expectedStatus);
        return response;
    }

    @Step("Проверка статуса ответа")
    public static void checkStatusCode(Response response, int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
    }
    @Step("Проверка тела ответа")
    public static void checkResponseBody(Response response, String key, Object expectedValue) {
        response.then().assertThat().body(key, equalTo(expectedValue));
    }

    @Step("Авторизация пользователя с почтой и паролем")
    public static  Response loginUser(String email, String password) {
        return LoginUser.postLoginUser(email, password);
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(String accessToken) {
        return DeleteUser.deleteUser(accessToken);
    }
    @Step("Изменение данных пользователя с авторизацией")
    public static Response upDateUser(String email, String name, String accessToken){
        return UpdateUser.patchUpdateUser(email, name, accessToken);
    }
    @Step("Изменение данных пользователя без авторизации")
    public static Response upDateUSerWithoutAuthorization(String email, String name){
        return UpdateUser.upDateUserWithoutToken(email, name);
}

    @Step("Собираем список id ингредиентов")
    public static List<String> collectIngredientIds(List<Ingredients> ingredients) {
        List<String> ids = new ArrayList<>();
        ids.add(ingredients.get(0).get_id()); // можно потом сделать гибко
        ids.add(ingredients.get(1).get_id());
        return ids;
    }
    @Step("Получение списка заказа с авторизацией")
    public static Response getOrderWithAuthorization(String accessToken){
        return GetUserOrder.getUserOrder(accessToken);
    }
    @Step("Проверка сообщения об ошибке")
    public static void checkErrorMessage(Response response, String expectedMessage) {
        response.then().body("message", equalTo(expectedMessage));
    }
}