package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты авторизации пользователя")
public class LoginUserTest extends BaseTest {
    private static final String LOGIN_ENDPOINT = "/auth/login";
    private static final String REGISTER_ENDPOINT = "/auth/register";
    
    private void createTestUser(String email, String password, String name) {
        String requestBody = String.format(
            "{\"email\": \"%s\", \"password\": \"%s\", \"name\": \"%s\"}",
            email, password, name
        );
        
        given()
            .header("Content-type", "application/json")
            .body(requestBody)
            .post(REGISTER_ENDPOINT);
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверка успешной авторизации с валидными учетными данными")
    public void loginWithValidCredentials() {
        String email = "login_test_" + System.currentTimeMillis() + "@yandex.ru";
        String password = "password123";
        String name = "LoginUser";
        
        createTestUser(email, password, name);

        String requestBody = String.format(
            "{\"email\": \"%s\", \"password\": \"%s\"}",
            email, password
        );

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(LOGIN_ENDPOINT);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name))
                .body("accessToken", containsString("Bearer "));
    }

    @Test
    @DisplayName("Вход с неверными учетными данными")
    @Description("Проверка ошибки авторизации с неверным email и паролем")
    public void loginWithInvalidCredentials() {
        String email = "wrong_email_" + System.currentTimeMillis() + "@yandex.ru";
        String password = "wrongpassword";

        String requestBody = String.format(
            "{\"email\": \"%s\", \"password\": \"%s\"}",
            email, password
        );

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(LOGIN_ENDPOINT);

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
