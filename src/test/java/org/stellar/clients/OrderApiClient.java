package org.stellar.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.List;
import static io.restassured.RestAssured.given;

public class OrderApiClient extends BaseApiClient {
    private static final String ORDERS_PATH = "/orders";
    private static final String INGREDIENTS_PATH = "/ingredients";

    private String createOrderJson(List<String> ingredientIds) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("ingredients", new Gson().toJsonTree(ingredientIds));
        return new Gson().toJson(jsonObject);
    }

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(List<String> ingredientIds, String token) {
        return given()
                .spec(getAuthSpec(token))
                .body(createOrderJson(ingredientIds))
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(List<String> ingredientIds) {
        return given()
                .spec(getBaseSpec())
                .body(createOrderJson(ingredientIds))
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH);
    }

    @Step("Получение валидных ID ингредиентов")
    public List<String> getValidIngredientIds() {
        Response response = getIngredients();
        return response.jsonPath().getList("data._id", String.class);
    }
}
