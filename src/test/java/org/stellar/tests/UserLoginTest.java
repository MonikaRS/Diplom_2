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
@Feature("Авторизация пользователя")
public class UserLoginTest extends BaseTest {
    private User testUser;
    private String accessToken;

    @Before
    public void setUp() {
        testUser = User.getRandomUser();
        Response createResponse = userApiClient.createUser(testUser);
        accessToken = userApiClient.getAccessToken(createResponse);
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userApiClient.deleteUser(accessToken);
        }
    }

    @Test
    @Story("Успешная авторизация")
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверка успешной авторизации с валидными учетными данными")
    @Severity(SeverityLevel.BLOCKER)
    public void shouldLoginWithValidCredentials() {
        Response loginResponse = userApiClient.loginUser(testUser);
        
        loginResponse.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", equalTo(testUser.getEmail()))
                .body("user.name", equalTo(testUser.getName()))
                .body("accessToken", containsString("Bearer"))
                .body("refreshToken", notNullValue());
    }

    @Test
    @Story("Ошибка авторизации")
    @DisplayName("Вход с неверным email")
    @Description("Проверка ошибки при входе с несуществующим email")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldNotLoginWithInvalidEmail() {
        User userWithInvalidEmail = new User(
            "invalid_" + testUser.getEmail(),
            testUser.getPassword(),
            testUser.getName()
        );
        
        Response loginResponse = userApiClient.loginUser(userWithInvalidEmail);
        
        loginResponse.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Story("Ошибка авторизации")
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка ошибки при входе с неверным паролем")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldNotLoginWithInvalidPassword() {
        User userWithInvalidPassword = new User(
            testUser.getEmail(),
            "wrong_password_123",
            testUser.getName()
        );
        
        Response loginResponse = userApiClient.loginUser(userWithInvalidPassword);
        
        loginResponse.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Story("Ошибка авторизации")
    @DisplayName("Вход с пустым email")
    @Description("Проверка ошибки при входе без указания email")
    @Severity(SeverityLevel.NORMAL)
    public void shouldNotLoginWithEmptyEmail() {
        User userWithEmptyEmail = new User("", testUser.getPassword(), testUser.getName());
        Response loginResponse = userApiClient.loginUser(userWithEmptyEmail);
        
        loginResponse.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Story("Ошибка авторизации")
    @DisplayName("Вход с пустым паролем")
    @Description("Проверка ошибки при входе без указания пароля")
    @Severity(SeverityLevel.NORMAL)
    public void shouldNotLoginWithEmptyPassword() {
        User userWithEmptyPassword = new User(testUser.getEmail(), "", testUser.getName());
        Response loginResponse = userApiClient.loginUser(userWithEmptyPassword);
        
        loginResponse.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
