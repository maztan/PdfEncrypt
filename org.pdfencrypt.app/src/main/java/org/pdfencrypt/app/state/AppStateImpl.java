package org.pdfencrypt.app.state;

import com.google.common.eventbus.EventBus;
import org.pdfencrypt.events.sub.UserAuthenticationStateChangedEventMessage;
import org.pdfencrypt.app.types.AuthenticatedUserInfo;

import java.util.Locale;

public class AppStateImpl implements AppState {
    private final EventBus eventBus;
    private AuthenticatedUserInfo authenticatedUserInfo;

    public AppStateImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public Locale getLocale() {
        return Locale.US;
    }

    @Override
    public synchronized void setAuthenticatedUserInfo(AuthenticatedUserInfo authenticatedUserInfo) {
        this.authenticatedUserInfo = authenticatedUserInfo;
        eventBus.post(new UserAuthenticationStateChangedEventMessage(this, authenticatedUserInfo));
    }
}
