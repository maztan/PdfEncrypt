package org.pdfencrypt.gui.controller;

import com.google.common.eventbus.EventBus;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import org.pdfencrypt.app.state.AppState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ResourceBundle;

public class ViewFactory {
    private static Logger log = LoggerFactory.getLogger(ViewFactory.class);
    private final EventBus eventBus;
    private final AppState appState;

    private Scene rootScene;
    private RootController rootController;

    private NodeAndController<Parent, LoginController> loginViewParentAndController;
    private NodeAndController<Parent, EncryptionController> encryptionViewParentAndController;

    public ViewFactory(AppState appState, EventBus eventBus){
        this.appState = appState;
        this.eventBus = eventBus;
    }

    private ResourceBundle languageResourceBundle;
    private void setLanguageResourceForLoader(FXMLLoader loader){
        if(languageResourceBundle == null){
            languageResourceBundle = ResourceBundle.getBundle("bundles.LanguageBundle", appState.getLocale());
        }
        loader.setResources(languageResourceBundle);
    }

    /**
     * Gets the scene into which other scenes will be loaded as children
     * @return
     */
    public Scene getRootScene() {
        if (rootScene == null) {
            FXMLLoader mainFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/root.fxml"));
            setLanguageResourceForLoader(mainFxmlLoader);

            Parent root = null;
            try {
                root = mainFxmlLoader.load();
            } catch (IOException e) {
                log.error(e.toString());
            }
            rootController = mainFxmlLoader.getController();
            rootController.setEventBus(eventBus);

            rootScene = new Scene(root);
            rootScene.setFill(Color.TRANSPARENT);
        }

        return rootScene;
    }

    public RootController getRootController(){
        if(rootController == null){
            // This will initialize the controller
            getRootScene();
        }

        return rootController;
    }

    public NodeAndController<Parent, LoginController> getLoginView() {

        if (loginViewParentAndController == null) {

            FXMLLoader mainFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            setLanguageResourceForLoader(mainFxmlLoader);

            Parent root = null;
            try {
                root = mainFxmlLoader.load();
            } catch (IOException e) {
                log.error(e.toString());
            }
            LoginController controller = mainFxmlLoader.getController();

            controller.setRootNode(root);
            controller.setEventBus(eventBus);

            loginViewParentAndController = new NodeAndController<Parent, LoginController>(root, controller);
        }

        return loginViewParentAndController;
    }

    public NodeAndController<Parent, EncryptionController> getEncryprionView() {

        if (encryptionViewParentAndController == null) {

            FXMLLoader mainFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/encryption.fxml"));
            setLanguageResourceForLoader(mainFxmlLoader);

            Parent root = null;
            try {
                root = mainFxmlLoader.load();
            } catch (IOException e) {
                log.error(e.toString());
            }
            EncryptionController controller = mainFxmlLoader.getController();

            controller.setRootNode(root);
            controller.setEventBus(eventBus);

            encryptionViewParentAndController = new NodeAndController<Parent, EncryptionController>(root, controller);
        }

        return encryptionViewParentAndController;
    }
}
