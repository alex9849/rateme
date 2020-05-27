package de.hskl.rateme.model;

import de.hskl.rateme.util.Validator;

public class LoginData {
    @Validator.Required
    String username;

    @Validator.Required
    String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
