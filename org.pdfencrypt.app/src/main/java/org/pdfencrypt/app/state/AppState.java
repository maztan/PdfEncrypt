package org.pdfencrypt.app.state;

import javafx.beans.property.ReadOnlyBooleanProperty;
import org.pdfencrypt.app.types.AuthenticatedUserInfo;

import java.util.Locale;

public interface AppState {
    Locale getLocale();

    void setAuthenticatedUserInfo(AuthenticatedUserInfo authenticatedUserInfo);
}