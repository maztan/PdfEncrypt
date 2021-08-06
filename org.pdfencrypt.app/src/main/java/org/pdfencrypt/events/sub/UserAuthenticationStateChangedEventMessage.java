package org.pdfencrypt.events.sub;

import org.pdfencrypt.app.state.AppState;
import org.pdfencrypt.app.types.AuthenticatedUserInfo;
import org.pdfencrypt.events.ApplicationStateEventMessage;

import java.util.EventObject;

public class UserAuthenticationStateChangedEventMessage extends ApplicationStateEventMessage {
    private AuthenticatedUserInfo authenticatedUserInfo;

    public UserAuthenticationStateChangedEventMessage(AppState source, AuthenticatedUserInfo authenticatedUserInfo) {
        this.authenticatedUserInfo = authenticatedUserInfo;
    }

    /**
     * Returns true if this event denotes user state as authenticated
     * @return
     */
    public boolean isUserAuthenticated(){
        return authenticatedUserInfo != null;
    }

    /**
     * Returns null if user is not/no longer authenticated
     * @return
     */
    public AuthenticatedUserInfo getAuthenticatedUserInfo() {
        return authenticatedUserInfo;
    }
}
