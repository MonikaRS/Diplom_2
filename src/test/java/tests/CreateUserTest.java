package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты создания пользователя")
public class CreateUserTest extends BaseTest {
    private static final String REGISTER_ENDPOINT = "/auth/register";

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания нового пользователя с уникальными данными")
    public void createUniqueUser() {
        String email = "test_" + System.currentTimeMillis() + "@yandex.ru";
        String password = "password123";
        String name = "TestUser_" + System.currentTimeMillis();

        String requestBody = String.format(
            "{\"email\": \"%s\", \"password\": \"%s\", \"name\": \"%s\"}",
            email, password, name
        );

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(REGISTER_ENDPOINT);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Проверка ошибки при попытке создать пользователя с уже существующим email")
    public void createExistingUser() {
        String email = "test-user@yandex.ru";
        String password = "password123";
        String name = "ExistingUser";

        String requestBody = String.format(
            "{\"email\": \"%s\", \"password\": \"%s\", \"name\": \"%s\"}",
            email, password, name
        );

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(REGISTER_ENDPOINT);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля")
    @Description("Проверка ошибки при создании пользователя без заполнения обязательного поля email")
    public void createUserWithoutRequiredField() {
        String requestBody = "{\"password\": \"password123\", \"name\": \"TestUser\"}";

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(REGISTER_ENDPOINT);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
