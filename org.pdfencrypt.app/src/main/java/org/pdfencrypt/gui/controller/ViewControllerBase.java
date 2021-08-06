package org.pdfencrypt.gui.controller;

import com.google.common.eventbus.EventBus;
import javafx.scene.Node;

public abstract class ViewControllerBase {
    private Node rootNode;
    private EventBus eventBus;

    /**
     * Sets the root node that this controller manages
     * @param root
     */
    public final void setRootNode(Node root){
        this.rootNode = root;
    }

    protected final Node getRootNode(){
        return rootNode;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
