package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.RandomData;
import model.UserData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static praktikum.UserSteps.*;
import static praktikum.UserSteps.createUser;

public class LoginUserTest extends Model {

    private String accessToken;
    private String refreshToken;
    @Before
    public void createUser() {
        randomUser = RandomData.generateRandomUser();
        Response response = UserSteps.createUser(randomUser.getEmail(), randomUser.getPassword(), randomUser.getName(), 200);
        UserSteps.checkStatusCode(response, 200);
        System.out.println(response.body().asString());
    }
    @Test
    @DisplayName("Проверка, что пользователь может авторизоваться")
    @Description("Для авторизации нужно передать все обязательные поля")
    public void testUserAuthorizationWithCorrectData() {
        Response loginResponse = UserSteps.loginUser(randomUser.getEmail(), randomUser.getPassword());
        UserSteps.checkStatusCode(loginResponse, 200);

        accessToken = loginResponse.jsonPath().getString("accessToken");
        refreshToken = loginResponse.jsonPath().getString("refreshToken");

        assertNotNull("accessToken не должен быть null", accessToken);
        assertNotNull("refreshToken не должен быть null", refreshToken);
    }

    @Test
    @DisplayName("Проверка, что пользователь не может авторизоваться с неверной почтой")
    @Description("Заполнить валидными данными все поля, в поле email ввести неверные данные")
    public void testUserAuthorizationWithInvalidEmail() {
        Response loginResponse = UserSteps.loginUser("wrongemail@mail.com", randomUser.getPassword());
        UserSteps.checkStatusCode(loginResponse, 401);
    }

    @Test
    @DisplayName("Проверка, что пользователь не может авторизоваться с неверным паролем")
    @Description("Заполнить валидными данными все поля, в поле пароль ввести неверные данные")
    public void testUserAuthorizationWithInvalidPassword() {
        Response loginResponse = UserSteps.loginUser(randomUser.getEmail(), "wrongPassword");
        UserSteps.checkStatusCode(loginResponse, 401);
    }

    @Test
    @DisplayName("Проверка, что пользователь не может авторизоваться с пустым полем почты")
    @Description("Заполнить валидными данными все поля, поле email оставить пустым")
    public void testUserAuthorizationWithoutEmail() {
        Response loginResponse = UserSteps.loginUser("", randomUser.getPassword());
        UserSteps.checkStatusCode(loginResponse, 401);
    }

    @Test
    @DisplayName("Проверка, что пользователь не может авторизоваться с пустым полем пароля")
    @Description("Заполнить валидными данными все поля, поле пароль оставить пустым")
    public void testUserAuthorizationWithoutPassword() {
        Response loginResponse = UserSteps.loginUser(randomUser.getEmail(), "");
        UserSteps.checkStatusCode(loginResponse, 401);
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserSteps.deleteUser(accessToken);
        }
    }
}
