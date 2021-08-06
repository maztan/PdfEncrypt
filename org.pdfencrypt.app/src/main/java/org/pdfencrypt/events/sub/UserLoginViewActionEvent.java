package org.pdfencrypt.events.sub;

import org.pdfencrypt.app.types.UserLoginData;
import org.pdfencrypt.events.ViewActionEvent;

public class UserLoginViewActionEvent extends ViewActionEvent<UserLoginData> {
    public UserLoginViewActionEvent(Object source, UserLoginData eventData) {
        super(source, eventData);
    }
}
