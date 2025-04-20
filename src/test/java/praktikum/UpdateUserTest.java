package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.RandomData;
import model.UserData;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static praktikum.UserSteps.*;
import static praktikum.UserSteps.checkStatusCode;

public class UpdateUserTest extends BaseClass{

    public UserData randomUser;
    private UserSteps userSteps;
    private UserData user;
    private String accessToken;

    @Test
    @DisplayName("Успешное обновление данных пользователя с токеном")
    @Description("Обновляем email и имя, получаем 200 и новые данные в ответе")
    public void testSuccessfulUserUpdate() {
        randomUser = RandomData.generateRandomUser();
        user = randomUser;
        String newEmail = RandomData.generateRandomEmail();
        String newName = "Новое имя";
        Response response = (Response) createUser(randomUser.getEmail(), randomUser.getPassword(),randomUser.getName(), 200);
        checkStatusCode(response, 200);
        Response loginResponse = loginUser(randomUser.getEmail(), randomUser.getPassword());
        checkStatusCode(loginResponse, 200);
        System.out.println("Login response body: " + loginResponse.getBody().asString());
        accessToken = loginResponse.jsonPath().getString("accessToken");
        String refreshToken = loginResponse.jsonPath().getString("refreshToken");
        Response newResponse = UserSteps.upDateUser(newEmail, newName, accessToken);
        checkStatusCode(newResponse, 200);
        newResponse.then().body("user.email", equalTo(newEmail));
        newResponse.then().body("user.name", equalTo(newName));

}
    @Test
    @DisplayName("Обновление данных пользователя без авторизации")
    @Description("Обновляем данные почты и имени без токена авторизации")
    public void testUserUpdateWithoutAuthorization() {
        randomUser = RandomData.generateRandomUser();
        user = randomUser;
        String newEmail = RandomData.generateRandomEmail();
        String newName = "Новое имя";
        Response response = (Response) createUser(randomUser.getEmail(), randomUser.getPassword(),randomUser.getName(), 200);
        checkStatusCode(response, 200);
        Response newResponse = UserSteps.upDateUSerWithoutAuthorization(newEmail, newName);
        checkStatusCode(newResponse, 401);
        newResponse.then().body("success", equalTo(false));
        newResponse.then().body("message", equalTo("You should be authorised"));

    }
    @Test
    @DisplayName("Неуспешное обновление почты на уже используемую почту пользователя ")
    @Description("Обновляем email на уже успользуемый email, получаем 403")
    public void testUserUpdateDoubleEmail() {
        randomUser = RandomData.generateRandomUser();
        user = randomUser;
        String newEmail = RandomData.generateRandomEmail();
        String newName = "Новое имя";
        String doubleEmail = "email@mail.com";
        Response response = (Response) createUser(randomUser.getEmail(), randomUser.getPassword(),randomUser.getName(), 200);
        checkStatusCode(response, 200);
        Response loginResponse = loginUser(randomUser.getEmail(), randomUser.getPassword());
        checkStatusCode(loginResponse, 200);
        System.out.println("Login response body: " + loginResponse.getBody().asString());
        accessToken = loginResponse.jsonPath().getString("accessToken");
        String refreshToken = loginResponse.jsonPath().getString("refreshToken");
        Response newResponse = UserSteps.upDateUser(doubleEmail, newName, accessToken);
        checkStatusCode(newResponse, 403);
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
