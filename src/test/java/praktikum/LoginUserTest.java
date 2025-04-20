package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.RandomData;
import model.UserData;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static praktikum.UserSteps.*;
import static praktikum.UserSteps.createUser;

public class LoginUserTest extends BaseClass{
    public UserData randomUser;
    private UserSteps userSteps;
    private UserData user;
    private String accessToken;
    @Test
    @DisplayName("Проверка, что пользователь может авторизоваться")
    @Description("Для авторизации нужно передать все обязательные поля ")
    public void testCourierAuthorizationWithCorrectData() {
        randomUser = RandomData.generateRandomUser();
        user = randomUser;
        Response response = (Response) createUser(randomUser.getEmail(), randomUser.getPassword(),randomUser.getName(), 200);
        checkStatusCode(response, 200);
        Response loginResponse = loginUser(randomUser.getEmail(), randomUser.getPassword());
        checkStatusCode(loginResponse, 200);
        System.out.println("Login response body: " + loginResponse.getBody().asString());
        accessToken = loginResponse.jsonPath().getString("accessToken");
        String refreshToken = loginResponse.jsonPath().getString("refreshToken");
        assertNotNull("accessToken не должен быть null", accessToken);
        assertNotNull("refreshToken не должен быть null", refreshToken);
    }
    @Test
    @DisplayName("Проверка, что пользователь не может авторизоваться с неверной почтой")
    @Description("Заполнить валидными данными все поля, в поле email ввести неверные данные")
    public void testCourierAuthorizationWithInvalidEmail() {
        randomUser = RandomData.generateRandomUser();
        Response response = (Response) createUser(randomUser.getEmail(), randomUser.getPassword(),randomUser.getName(), 200);
        checkStatusCode(response, 200);
        Response loginResponse = loginUser("email@mail.com", randomUser.getPassword());
        checkStatusCode(loginResponse, 401);
        System.out.println(response.body().asString());
    }
    @Test
    @DisplayName("Проверка, что пользователь не может авторизоваться с неверным паролем")
    @Description("Заполнить валидными данными все поля, в поле пароль ввести неверные данные")
    public void testCourierAuthorizationWithInvalidPassword() {
        randomUser = RandomData.generateRandomUser();
        Response response = (Response) createUser(randomUser.getEmail(), randomUser.getPassword(),randomUser.getName(), 200);
        checkStatusCode(response, 200);
        Response loginResponse = loginUser(randomUser.getEmail(), "1234");
        checkStatusCode(loginResponse, 401);
        System.out.println(response.body().asString());

    }
    @Test
    @DisplayName("Проверка, что пользователь не может авторизоваться с пустым полем почты")
    @Description("Заполнить валидными данными все поля, поле email оставить пустым")
    public void testCourierAuthorizationWithoutEmail() {
        randomUser = RandomData.generateRandomUser();
        Response response = (Response) createUser(randomUser.getEmail(), randomUser.getPassword(),randomUser.getName(), 200);
        checkStatusCode(response, 200);
        Response loginResponse = loginUser("", randomUser.getPassword());
        checkStatusCode(loginResponse, 401);
        System.out.println(response.body().asString());
    }
    @Test
    @DisplayName("Проверка, что пользователь не может авторизоваться с пустым полем пароля")
    @Description("Заполнить валидными данными все поля, поле пароль оставить пустым")
    public void testCourierAuthorizationWithoutPassword() {
        randomUser = RandomData.generateRandomUser();
        Response response = (Response) createUser(randomUser.getEmail(), randomUser.getPassword(),randomUser.getName(), 200);
        checkStatusCode(response, 200);
        Response loginResponse = loginUser(randomUser.getEmail(), "");
        checkStatusCode(loginResponse, 401);
        System.out.println(response.body().asString());

    }
    @After
    public void deleteCourier (){
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }
}
