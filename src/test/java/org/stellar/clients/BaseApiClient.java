package org.stellar.clients;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApiClient {
    protected static final String BASE_URL = "https://stellarburgers.education-services.ru";
    protected static final String API_PATH = "/api";

    protected RequestSpecBuilder getBaseSpecBuilder() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL + API_PATH)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter());
    }

    protected RequestSpecification getBaseSpec() {
        return getBaseSpecBuilder().build();
    }

    protected RequestSpecification getAuthSpec(String token) {
        return getBaseSpecBuilder()
                .addHeader("Authorization", token)
                .build();
    }
}