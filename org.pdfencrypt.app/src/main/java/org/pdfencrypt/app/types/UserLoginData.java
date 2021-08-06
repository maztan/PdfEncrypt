package org.pdfencrypt.app.types;

public class UserLoginData {
    private final String login;
    private final String password;

    public UserLoginData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
