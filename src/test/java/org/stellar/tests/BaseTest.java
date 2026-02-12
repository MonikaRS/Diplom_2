package org.stellar.tests;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.stellar.clients.OrderApiClient;
import org.stellar.clients.UserApiClient;

public class BaseTest {
    protected static UserApiClient userApiClient;
    protected static OrderApiClient orderApiClient;

    @BeforeClass
    public static void setUpClass() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        userApiClient = new UserApiClient();
        orderApiClient = new OrderApiClient();
    }
}
