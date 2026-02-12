package org.stellar.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.stellar.models.User;
import static io.restassured.RestAssured.given;

public class UserApiClient extends BaseApiClient {
    private static final String REGISTER_PATH = "/auth/register";
    private static final String LOGIN_PATH = "/auth/login";
    private static final String USER_PATH = "/auth/user";

    @Step("Создание нового пользователя")
    public Response createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user.toJson())
                .when()
                .post(REGISTER_PATH);
    }

    @Step("Авторизация пользователя")
    public Response loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user.toJson())
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Удаление пользователя")
    public void deleteUser(String token) {
        given()
                .spec(getAuthSpec(token))
                .when()
                .delete(USER_PATH);
    }

    @Step("Получение accessToken из ответа")
    public String getAccessToken(Response response) {
        return response.jsonPath().getString("accessToken");
    }
}
