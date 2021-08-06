package org.pdfencrypt.app.types;

public class AuthenticatedUserInfo {
    private final String login;
    private final String password;

    public AuthenticatedUserInfo(String login, String password) {
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
