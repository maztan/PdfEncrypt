package org.pdfencrypt.events;

public class ApplicationEventMessage {
    private ApplicationEventType applicationEventType;

    public ApplicationEventMessage(ApplicationEventType applicationEventType) {
        this.applicationEventType = applicationEventType;
    }

    public ApplicationEventType getApplicationEventType() {
        return applicationEventType;
    }
}
