package org.stellar.models;

import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;

public class User {
    private final String email;
    private final String password;
    private final String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User getRandomUser() {
        String randomSuffix = RandomStringUtils.randomAlphanumeric(8).toLowerCase();
        return new User(
            "test_" + randomSuffix + "@yandex.ru",
            "pass_" + RandomStringUtils.randomAlphanumeric(8),
            "User_" + RandomStringUtils.randomAlphanumeric(6)
        );
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String getEmail() { 
        return email; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    public String getName() { 
        return name; 
    }
}
