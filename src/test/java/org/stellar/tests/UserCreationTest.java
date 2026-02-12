package org.stellar.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.stellar.models.User;
import static org.hamcrest.Matchers.*;

@Epic("API тесты Stellar Burgers")
@Feature("Создание пользователя")
public class UserCreationTest extends BaseTest {
    private User testUser;
    private String accessToken;

    @Before
    public void setUp() {
        testUser = User.getRandomUser();
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userApiClient.deleteUser(accessToken);
        }
    }

    @Test
    @Story("Успешное создание пользователя")
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания нового пользователя с уникальными данными")
    @Severity(SeverityLevel.BLOCKER)
    public void shouldCreateUniqueUser() {
        Response createResponse = userApiClient.createUser(testUser);
        accessToken = userApiClient.getAccessToken(createResponse);

        createResponse.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", equalTo(testUser.getEmail()))
                .body("user.name", equalTo(testUser.getName()))
                .body("accessToken", containsString("Bearer"))
                .body("refreshToken", notNullValue());
    }

    @Test
    @Story("Ошибка создания существующего пользователя")
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Проверка ошибки при создании пользователя с уже существующим email")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldNotCreateExistingUser() {
        Response createResponse = userApiClient.createUser(testUser);
        accessToken = userApiClient.getAccessToken(createResponse);
        
        Response duplicateResponse = userApiClient.createUser(testUser);
        
        duplicateResponse.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @Story("Ошибка создания пользователя без обязательных полей")
    @DisplayName("Создание пользователя без email")
    @Description("Проверка ошибки при создании пользователя без указания email")
    @Severity(SeverityLevel.NORMAL)
    public void shouldNotCreateUserWithoutEmail() {
        User userWithoutEmail = new User(null, testUser.getPassword(), testUser.getName());
        Response response = userApiClient.createUser(userWithoutEmail);
        
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Ошибка создания пользователя без обязательных полей")
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка ошибки при создании пользователя без указания пароля")
    @Severity(SeverityLevel.NORMAL)
    public void shouldNotCreateUserWithoutPassword() {
        User userWithoutPassword = new User(testUser.getEmail(), null, testUser.getName());
        Response response = userApiClient.createUser(userWithoutPassword);
        
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Ошибка создания пользователя без обязательных полей")
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка ошибки при создании пользователя без указания имени")
    @Severity(SeverityLevel.NORMAL)
    public void shouldNotCreateUserWithoutName() {
        User userWithoutName = new User(testUser.getEmail(), testUser.getPassword(), null);
        Response response = userApiClient.createUser(userWithoutName);
        
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
