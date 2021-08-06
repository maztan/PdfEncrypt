package org.pdfencrypt.gui.controller;

import javafx.scene.Node;

public class NodeAndController<N extends Node, C extends ViewControllerBase>{
    private final N node;
    private final C controller;

    public NodeAndController(N node, C controller) {
        this.node = node;
        this.controller = controller;
    }

    public N getNode() {
        return node;
    }

    public C getController() {
        return controller;
    }
}
