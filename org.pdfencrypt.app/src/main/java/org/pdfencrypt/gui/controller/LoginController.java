package org.pdfencrypt.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.pdfencrypt.app.types.UserLoginData;
import org.pdfencrypt.events.sub.UserLoginViewActionEvent;

public class LoginController extends ViewControllerBase {
    @FXML
    private TextField loginTextBox;

    @FXML
    private TextField passwordTextBox;

    @FXML
    void cancel(ActionEvent event) {
        loginTextBox.setText("");
        passwordTextBox.setText("");
    }

    @FXML
    void login(ActionEvent event) {
        String login = loginTextBox.getText().trim();
        String password = passwordTextBox.getText().trim();

        getEventBus().post(new UserLoginViewActionEvent(this, new UserLoginData(login, password)));
    }
}
