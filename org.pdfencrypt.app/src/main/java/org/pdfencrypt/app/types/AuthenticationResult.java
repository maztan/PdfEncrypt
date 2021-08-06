package org.pdfencrypt.app.types;

public class AuthenticationResult {
    private boolean isSuccess;
    private String errorMessage;
    private AuthenticatedUserInfo authenticatedUserInfo;

    public AuthenticationResult(boolean isSuccess, AuthenticatedUserInfo authenticatedUserInfo) {
        this.isSuccess = isSuccess;
        this.authenticatedUserInfo = authenticatedUserInfo;
    }

    public AuthenticationResult(String errorMessage) {
        this.errorMessage = errorMessage;
        this.isSuccess = false;
        this.authenticatedUserInfo = null;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public AuthenticatedUserInfo getAuthenticatedUserInfo() {
        return authenticatedUserInfo;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
