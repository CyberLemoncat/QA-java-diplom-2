package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.RandomData;
import model.UserData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static praktikum.UserSteps.*;

public class CreateUserTest extends Model {
    public UserData randomUser;
    private UserSteps userSteps;
    private String accessToken;
    private UserData user;


    @Test
    @DisplayName("Проверка создания нового уникального пользователя")
    @Description("Для создания пользователя необходимо заполнить обязательные поля валидными данными")
    public void testCreateUserWithCorrectData(){
        randomUser = RandomData.generateRandomUser();
        Response response = UserSteps.createUser(randomUser.getEmail(), randomUser.getPassword(), randomUser.getName(), 200);
        UserSteps.checkStatusCode(response, 200);
        System.out.println(response.body().asString());
    }
    @Test
    @DisplayName("Ошибка при создании одинаковых пользователей")
    @Description("Создать пользователя, заполнив все обязательные поля, создать второго пользователя заполнив обязательные поля одинаковыми данными")
    public void testCreateDoubleUsersAccount() {
        randomUser = RandomData.generateRandomUser();
        Response response = UserSteps.createUser(randomUser.getEmail(), randomUser.getPassword(), randomUser.getName(), 200);
        UserSteps.checkStatusCode(response, 200);
        System.out.println(response.body().asString());

        Response doubleResponse = UserSteps.createUser(randomUser.getEmail(), randomUser.getPassword(),randomUser.getName(), 403);
        checkResponseBody(doubleResponse, "message", "User already exists");

    }
    @Test
    @DisplayName("Ошибка при создании пользователя без почты")
    @Description("Создать пользователя, заполнив все обязательные поля, кроме почты")
    public void testCreateCouriersAccountWithoutLogin() {
        randomUser = RandomData.generateRandomUser();
        Response response = UserSteps.createUser("", randomUser.getPassword(),randomUser.getName(), 403);
        checkResponseBody(response, "message", "Email, password and name are required fields");
        System.out.println(response.body().asString());
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без пароля")
    @Description("Создать пользователя, заполнив все обязательные поля, кроме пароля")
    public void testCreateCouriersAccountWithoutPassword() {
        randomUser = RandomData.generateRandomUser();
        Response response = UserSteps.createUser(randomUser.getEmail(), "", randomUser.getName(), 403);;
        checkResponseBody(response, "message", "Email, password and name are required fields");
        System.out.println(response.body().asString());
    }

    @Test
    @DisplayName("Успешное создание аккаунта пользователя без имени")
    @Description("Для авторизации нужно передать все обязательные поля, оставить поле Имя пустым")
    public void createCouriersAccountWithoutFirstName() {
        randomUser = RandomData.generateRandomUser();
        Response response = UserSteps.createUser(randomUser.getEmail(), randomUser.getPassword(), "", 403);;
        checkResponseBody(response, "message", "Email, password and name are required fields");
        System.out.println(response.body().asString());
    }

    @After
    public void deleteCourier() {
        if (accessToken != null) {
            DeleteUser.deleteUser(accessToken);
        }
    }
}
