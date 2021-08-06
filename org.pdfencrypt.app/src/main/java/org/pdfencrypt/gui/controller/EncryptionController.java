package org.pdfencrypt.gui.controller;

import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.pdfencrypt.app.services.tasks.DecryptionTaskService;
import org.pdfencrypt.app.services.tasks.EncryptionTaskService;
import org.pdfencrypt.app.util.FileSystemHelper;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;

import static javafx.concurrent.Worker.State.RUNNING;

public class EncryptionController extends ViewControllerBase {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(EncryptionController.class);

    private final DirectoryChooser directoryChooser;
    private File selectedInputDirectory;
    private File selectedOutputDirectory;

    public EncryptionController(){
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(""); //TODO: set title
        encryptionTaskService = new EncryptionTaskService();
        decryptionTaskService = new DecryptionTaskService();
    }

    @FXML
    private void initialize(){
        encryptButton.disableProperty().bind(encryptionTaskService.stateProperty().isEqualTo(RUNNING));
        encryptionTaskService.setOnScheduled(workerStateEvent ->
            progressBar.progressProperty().bind(((Service)workerStateEvent.getSource()).progressProperty()));
        decryptionTaskService.setOnScheduled(workerStateEvent ->
                progressBar.progressProperty().bind(((Service)workerStateEvent.getSource()).progressProperty()));
        /*encryptionService.setOnReady(workerStateEvent ->
                progressBar.progressProperty().unbind());*/

        encryptionTaskService.setOnFailed(onEncryptionTaskFailed);
        encryptionTaskService.setOnSucceeded(onEncryptionTaskSucceded);

        decryptionTaskService.setOnFailed(onEncryptionTaskFailed);
        decryptionTaskService.setOnSucceeded(onEncryptionTaskSucceded);
    }

    private EventHandler<WorkerStateEvent> onEncryptionTaskFailed  = new EventHandler<WorkerStateEvent>() {
        @Override
        public void handle(WorkerStateEvent e) {
            Throwable exception = e.getSource().getException();
            if (exception != null) {
                log.error(exception.toString());
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Operation failed. See logs for more information.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    };

    private EventHandler<WorkerStateEvent> onEncryptionTaskSucceded =  new EventHandler<WorkerStateEvent>(){
        @Override
        public void handle(WorkerStateEvent e) {
            Boolean result = (Boolean) e.getSource().getValue();
            log.info("service task completed");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Operation succeeded", ButtonType.OK);
            alert.showAndWait();
        }
    };

    @FXML
    private TextField userPasswordTextBox;

    @FXML
    private TextField ownerPasswordTextBox;

    @FXML
    private Button inputDirButton;

    @FXML
    private Button outputDirButton;

    @FXML
    private Button encryptButton;

    @FXML
    private Button decryptButton;

    @FXML
    private Label inputDirLabel;

    @FXML
    private Label outputDirLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    void selectInputDir(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        selectedInputDirectory = directoryChooser.showDialog(thisStage);

        inputDirLabel.setText(selectedInputDirectory != null ? selectedInputDirectory.getAbsolutePath().toString() : "");
    }

    @FXML
    void selectOutputDir(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        selectedOutputDirectory = directoryChooser.showDialog(thisStage);

        outputDirLabel.setText(selectedOutputDirectory != null ? selectedOutputDirectory.getAbsolutePath().toString() : "");
    }

    private EncryptionTaskService encryptionTaskService;
    private DecryptionTaskService decryptionTaskService;

    private void showErrorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message,  ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    void encrypt(ActionEvent event) {
        //FileUtils.forceMkdirParent();
        String userPassword = userPasswordTextBox.getText();
        if(userPassword == null || userPassword.trim().equals("")){
            showErrorAlert("User password is missing.");
            return;
        }

        String ownerPassword = ownerPasswordTextBox.getText();
        if(ownerPassword == null || ownerPassword.trim().equals("")){
            showErrorAlert("Owner password is missing.");
            return;
        }

        if(selectedInputDirectory == null){
            showErrorAlert("Input directory is not set.");
            return;
        }

        if(selectedOutputDirectory == null){
            showErrorAlert("Output directory is not set.");
            return;
        }

        Path inputDirectory = selectedInputDirectory.toPath().toAbsolutePath();
        Path outputDirectory = selectedOutputDirectory.toPath().toAbsolutePath();

        if(FileSystemHelper.isChildPath(inputDirectory, outputDirectory)){
            showErrorAlert("Input directory cannot be a subdirectory of output directory");
        }

        if(FileSystemHelper.isChildPath(outputDirectory, inputDirectory)){
            showErrorAlert("Output directory cannot be a subdirectory of input directory");
        }

        encryptionTaskService.setUserPassword(userPassword);
        encryptionTaskService.setOwnerPassword(ownerPassword);
        encryptionTaskService.setInputDirectory(inputDirectory);
        encryptionTaskService.setOutputDirectory(outputDirectory);

        encryptionTaskService.start();
    }

    @FXML
    void decrypt(ActionEvent event) {
        //FileUtils.forceMkdirParent();

        String ownerPassword = ownerPasswordTextBox.getText();
        if(ownerPassword == null || ownerPassword.trim().equals("")){
            showErrorAlert("Owner password is missing.");
            return;
        }

        if(selectedInputDirectory == null){
            showErrorAlert("Input directory is not set.");
            return;
        }

        if(selectedOutputDirectory == null){
            showErrorAlert("Output directory is not set.");
            return;
        }

        Path inputDirectory = selectedInputDirectory.toPath().toAbsolutePath();
        Path outputDirectory = selectedOutputDirectory.toPath().toAbsolutePath();

        if(FileSystemHelper.isChildPath(inputDirectory, outputDirectory)){
            showErrorAlert("Input directory cannot be a subdirectory of output directory");
        }

        if(FileSystemHelper.isChildPath(outputDirectory, inputDirectory)){
            showErrorAlert("Output directory cannot be a subdirectory of input directory");
        }

        encryptionTaskService.setOwnerPassword(ownerPassword);
        encryptionTaskService.setInputDirectory(inputDirectory);
        encryptionTaskService.setOutputDirectory(outputDirectory);

        encryptionTaskService.start();
    }
}
