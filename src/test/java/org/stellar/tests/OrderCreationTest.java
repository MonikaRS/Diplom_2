package org.stellar.tests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.stellar.models.User;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.*;

@Epic("API тесты Stellar Burgers")
@Feature("Создание заказа")
public class OrderCreationTest extends BaseTest {
    private User testUser;

    @Before
    public void setUp() {
        testUser = User.getRandomUser();
        userApiClient.createUser(testUser);
    }

    @After
    public void tearDown() {
        if (testUser != null) {
            try {
                Response loginResponse = userApiClient.loginUser(testUser);
                String accessToken = userApiClient.getAccessToken(loginResponse);
                if (accessToken != null && !accessToken.isEmpty()) {
                    userApiClient.deleteUser(accessToken);
                }
            } catch (Exception e) {
                // Игнорируем ошибки при удалении
            }
        }
    }

    private String getAccessToken() {
        Response loginResponse = userApiClient.loginUser(testUser);
        return userApiClient.getAccessToken(loginResponse);
    }

    @Test
    @Story("Успешное создание заказа")
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Проверка успешного создания заказа авторизованным пользователем")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldCreateOrderWithAuthAndIngredients() {
        List<String> ingredients = orderApiClient.getValidIngredientIds().subList(0, 2);
        String accessToken = getAccessToken();
        
        Response response = orderApiClient.createOrderWithAuth(ingredients, accessToken);
        
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @Story("Ошибка создания заказа")
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка ошибки при создании заказа неавторизованным пользователем")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldNotCreateOrderWithoutAuth() {
        List<String> ingredients = orderApiClient.getValidIngredientIds().subList(0, 2);
        
        Response response = orderApiClient.createOrderWithoutAuth(ingredients);
        
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @Story("Ошибка создания заказа")
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при создании заказа без ингредиентов")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldNotCreateOrderWithoutIngredients() {
        List<String> emptyIngredients = new ArrayList<>();
        String accessToken = getAccessToken();
        
        Response response = orderApiClient.createOrderWithAuth(emptyIngredients, accessToken);
        
        response.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Story("Ошибка создания заказа")
    @DisplayName("Создание заказа с неверным хешем ингредиента")
    @Description("Проверка ошибки при создании заказа с невалидным ID ингредиента")
    @Severity(SeverityLevel.NORMAL)
    public void shouldNotCreateOrderWithInvalidIngredientHash() {
        List<String> invalidIngredients = List.of("invalid_hash_123456789");
        String accessToken = getAccessToken();
        
        Response response = orderApiClient.createOrderWithAuth(invalidIngredients, accessToken);
        
        response.then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @Story("Успешное создание заказа")
    @DisplayName("Создание заказа с одним ингредиентом")
    @Description("Проверка успешного создания заказа с одним ингредиентом")
    @Severity(SeverityLevel.NORMAL)
    public void shouldCreateOrderWithSingleIngredient() {
        List<String> singleIngredient = List.of(
            orderApiClient.getValidIngredientIds().get(0)
        );
        String accessToken = getAccessToken();
        
        Response response = orderApiClient.createOrderWithAuth(singleIngredient, accessToken);
        
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }
}
