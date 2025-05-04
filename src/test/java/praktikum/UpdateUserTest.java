package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.RandomData;
import model.UserData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static praktikum.UserSteps.*;
import static praktikum.UserSteps.checkStatusCode;

public class UpdateUserTest extends Model {

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
    }

    @Test
    @DisplayName("Успешное обновление только почты пользователя с токеном")
    @Description("Обновляем только email пользователя, получаем 200 и обновлённый email в ответе")
    public void testSuccessfulUserEmailUpdate() {
        String newEmail = RandomData.generateRandomEmail();

        Response loginResponse = UserSteps.loginUser(randomUser.getEmail(), randomUser.getPassword());
        UserSteps.checkStatusCode(loginResponse, 200);
        accessToken = loginResponse.jsonPath().getString("accessToken");

        Response newResponse = UserSteps.upDateUser(newEmail, null, accessToken);
        UserSteps.checkStatusCode(newResponse, 200);
        newResponse.then().body("user.email", equalTo(newEmail));
    }

    @Test
    @DisplayName("Успешное обновление только имени пользователя с токеном")
    @Description("Обновляем только имя пользователя, получаем 200 и обновлённое имя в ответе")
    public void testSuccessfulUserNameUpdate() {
        String newName = "Новое имя";

        Response loginResponse = UserSteps.loginUser(randomUser.getEmail(), randomUser.getPassword());
        UserSteps.checkStatusCode(loginResponse, 200);
        accessToken = loginResponse.jsonPath().getString("accessToken");

        Response newResponse = UserSteps.upDateUser(null, newName, accessToken);
        UserSteps.checkStatusCode(newResponse, 200);
        newResponse.then().body("user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Обновление только почты пользователя без авторизации")
    @Description("Пытаемся обновить только email без токена авторизации")
    public void testUserEmailUpdateWithoutAuthorization() {
        String newEmail = RandomData.generateRandomEmail();

        Response newResponse = UserSteps.upDateUSerWithoutAuthorization(newEmail, null);
        UserSteps.checkStatusCode(newResponse, 401);
        newResponse.then().body("success", equalTo(false));
        newResponse.then().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Обновление только имени пользователя без авторизации")
    @Description("Пытаемся обновить только имя без токена авторизации")
    public void testUserNameUpdateWithoutAuthorization() {
        String newName = "Новое имя";

        Response newResponse = UserSteps.upDateUSerWithoutAuthorization(null, newName);
        UserSteps.checkStatusCode(newResponse, 401);
        newResponse.then().body("success", equalTo(false));
        newResponse.then().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Неуспешное обновление почты на уже используемую почту пользователя")
    @Description("Обновляем email на уже используемый email, получаем 403")
    public void testUserUpdateDoubleEmail() {
        String doubleEmail = "email@mail.com"; // Уже существующий email
        String newName = "Новое имя";

        Response loginResponse = UserSteps.loginUser(randomUser.getEmail(), randomUser.getPassword());
        UserSteps.checkStatusCode(loginResponse, 200);
        System.out.println("Login response body: " + loginResponse.getBody().asString());
        accessToken = loginResponse.jsonPath().getString("accessToken");

        Response newResponse = UserSteps.upDateUser(doubleEmail, newName, accessToken);
        UserSteps.checkStatusCode(newResponse, 403);
        newResponse.then().body("success", equalTo(false));
        newResponse.then().body("message", equalTo("User with such email already exists"));
    }

    @After
    public void deleteCourier (){
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }
}
