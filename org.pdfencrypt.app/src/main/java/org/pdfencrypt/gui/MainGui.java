package org.pdfencrypt.gui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.pdfencrypt.app.MainAppController;
import org.pdfencrypt.events.GuiViewType;
import org.pdfencrypt.events.ShowViewEventMessage;
import org.pdfencrypt.gui.controller.NodeAndController;
import org.pdfencrypt.gui.controller.ViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainGui extends Application {
    private static Logger log = LoggerFactory.getLogger(MainGui.class);
    private static MainAppController mainAppController;
    private ViewFactory viewFactory;
    private Stage primaryStage;

    public static void launch2(String... args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainAppController = new MainAppController();
        viewFactory = new ViewFactory(mainAppController.getAppState(), mainAppController.getEventBus());

        mainAppController.getEventBus().register(this);

        // Set window icon
        primaryStage.getIcons().add(new Image(MainGui.class.getResourceAsStream("/images/icons/main.png")));

        // stores a reference to the stage.
        this.primaryStage = primaryStage;

        // instructs the javafx system not to exit implicitly when the last application window is shut.
        Platform.setImplicitExit(false);

        // out stage will be translucent, so give it a transparent style.
        primaryStage.initStyle(StageStyle.UNIFIED);
        // Remove maximize button
        primaryStage.resizableProperty().setValue(Boolean.FALSE);

        primaryStage.setScene(viewFactory.getRootScene());
        mainAppController.getEventBus().post(new ShowViewEventMessage(GuiViewType.Login));
        //primaryStage.show();
    }

    @Subscribe
    public void handleShowViewEvent(ShowViewEventMessage showViewEvent) {
        switch(showViewEvent.getGuiView()){
            case Login:
                Platform.runLater(() -> {
                    loadView(viewFactory.getLoginView());
                });
                break;
            case Encryption:
                Platform.runLater(() -> {
                    loadView(viewFactory.getEncryprionView());
                });
                break;
        }
    }

    public void loadView(NodeAndController rootNodeAndController){
        final Pane mainContentPane = viewFactory.getRootController().getMainContentPane();

        mainContentPane.getChildren().clear();
        mainContentPane.getChildren().add(rootNodeAndController.getNode());

        primaryStage.show();
        //make sure not minimized
        ((Stage)primaryStage.getScene().getWindow()).setIconified(false);
        bringToFront();
    }

    /**
     * Brings the application window on top of other windows (if it was visible in the first place).
     * Run on JavaFX GUI thread.
     */
    private void bringToFront(){
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setAlwaysOnTop(false);
    }
}
