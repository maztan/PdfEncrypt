package org.pdfencrypt.gui.controller;

import com.google.common.eventbus.EventBus;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class RootController extends ViewControllerBase {

    @FXML
    private Pane mainContentPane;

    public Pane getMainContentPane() {
        return mainContentPane;
    }
}
