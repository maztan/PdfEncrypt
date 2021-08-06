package org.pdfencrypt.gui.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.pdfencrypt.app.PdfEncryptMain;
import org.pdfencrypt.app.encryption.PdfEncryptionService;
import org.pdfencrypt.app.util.FileSystemHelper;
import org.pdfencrypt.app.util.IProgress;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import static javafx.concurrent.Worker.State.RUNNING;

public class EncryptionController extends ViewControllerBase {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(EncryptionController.class);

    private final DirectoryChooser directoryChooser;
    private File selectedInputDirectory;
    private File selectedOutputDirectory;

    public EncryptionController(){
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(""); //TODO: set title
        encryptionService = new EncryptionService();
    }

    @FXML
    private void initialize(){
        encryptButton.disableProperty().bind(encryptionService.stateProperty().isEqualTo(RUNNING));
        encryptionService.setOnScheduled(workerStateEvent ->
            progressBar.progressProperty().bind(encryptionService.progressProperty()));
        encryptionService.setOnReady(workerStateEvent ->
                progressBar.progressProperty().unbind());

        encryptionService.setOnFailed(new EventHandler<WorkerStateEvent>() {
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
        });

        encryptionService.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
            @Override
            public void handle(WorkerStateEvent e) {
                Boolean result = (Boolean) e.getSource().getValue();
                log.info("service task completed");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Operation succeeded", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

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

    private static class EncryptionService extends Service {
        private Path inputDirectory;
        private Path outputDirectory;
        private String userPassword;
        private String ownerPassword;

        public void setInputDirectory(Path inputDirectory) {
            this.inputDirectory = inputDirectory;
        }

        public void setOutputDirectory(Path outputDirectory) {
            this.outputDirectory = outputDirectory;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public void setOwnerPassword(String ownerPassword) {
            this.ownerPassword = ownerPassword;
        }

        @Override
        protected Task createTask() {
            return new Task(){
                @Override
                protected Object call() throws Exception {
                    updateProgress(0,1);
                    List<File> inputFiles = FileSystemHelper.listFilesWithSubdirs(inputDirectory.toFile());
                    int numInputFiles = inputFiles.size();
                    int i =0;

                    //try {
                        for (File inputFile : inputFiles) {
                            Path relativePath = inputDirectory.relativize(inputFile.toPath().toAbsolutePath());
                            Path targetPath = outputDirectory.resolve(relativePath); //TODO: make sure relative path does not start with "/" (root component is it's name?)
                            //TODO: make sure target directory exists
                            // If the relative path has parent folder make sure it's created in the target directory
                            if (relativePath.getParent() != null) {
                                Files.createDirectories(targetPath.getParent());
                            }

                            String ext = FilenameUtils.getExtension(inputFile.getName()).toLowerCase(Locale.ROOT); //TODO: locale probably does not matter with extensions

                            if (ext.equals("pdf")) {
                                try {
                                    PdfEncryptionService.encryptPdf(inputFile.toPath(), targetPath, userPassword, ownerPassword);
                                } catch (IOException ex) {
                                    log.error("Error while encrypting file: " + inputFile.toString() + "(" + ex.toString() + ")");
                                }
                            }else{
                                Files.copy(inputFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                            }

                            i += 1;
                            updateProgress(i, numInputFiles);
                        }
                    /*}catch (Exception ex){
                        log.error(ex.toString());
                        throw ex;
                    }*/

                    updateProgress(1, 1);
                    return true;
                }
            };
        }
    };

    private EncryptionService encryptionService;

    private void showMissingValueAlert(String missingValueName, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    void encrypt(ActionEvent event) {
        //FileUtils.forceMkdirParent();
        // FIXME: add IProgress progress
        String userPassword = userPasswordTextBox.getText();
        if(userPassword == null || userPassword.trim().equals("")){
            showMissingValueAlert("user password", "User password is missing.");
            return;
        }

        String ownerPassword = ownerPasswordTextBox.getText();
        if(ownerPassword == null || ownerPassword.trim().equals("")){
            showMissingValueAlert("owner password", "Owner password is missing.");
            return;
        }

        if(selectedInputDirectory == null){
            showMissingValueAlert("input directory", "Input directory is not set.");
            return;
        }

        if(selectedOutputDirectory == null){
            showMissingValueAlert("output directory", "Output directory is not set.");
            return;
        }

        Path inputDirectory = selectedInputDirectory.toPath().toAbsolutePath();
        Path outputDirectory = selectedOutputDirectory.toPath().toAbsolutePath();

        encryptionService.setUserPassword(userPassword);
        encryptionService.setOwnerPassword(ownerPassword);
        encryptionService.setInputDirectory(inputDirectory);
        encryptionService.setOutputDirectory(outputDirectory);

        encryptionService.start();
    }
}
