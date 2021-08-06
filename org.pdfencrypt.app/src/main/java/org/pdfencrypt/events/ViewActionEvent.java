package org.pdfencrypt.events;

import java.util.EventObject;

public abstract class ViewActionEvent<T> extends EventObject {
    private T eventData;

    public ViewActionEvent(Object source) {
        super(source);
    }

    public ViewActionEvent(Object source, T eventData) {
        this(source);
        this.eventData = eventData;
    }

    public T getEventData() {
        return eventData;
    }
}
