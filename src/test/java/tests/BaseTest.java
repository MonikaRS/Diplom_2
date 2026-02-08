package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;

public class BaseTest {
    protected static final String BASE_URL = "https://stellarburgers.education-services.ru/api";

    @Before
    public void setUp() {
        // Базовый URL - статический, можно установить один раз
        RestAssured.baseURI = BASE_URL;

        // ВАЖНО: Фильтры должны быть НЕСТАТИЧЕСКИМИ, поэтому используем @Before, а не @BeforeClass
        RestAssured.filters(
                new AllureRestAssured(),          // для Allure отчётов
                new RequestLoggingFilter(),       // логирование запросов в консоль
                new ResponseLoggingFilter()       // логирование ответов в консоль
        );

        // Дополнительные настройки если нужны
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
