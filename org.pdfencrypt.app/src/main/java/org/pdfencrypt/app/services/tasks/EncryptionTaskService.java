package org.pdfencrypt.app.services.tasks;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.io.FilenameUtils;
import org.pdfencrypt.app.services.encryption.PdfEncryptionService;
import org.pdfencrypt.app.util.FileSystemHelper;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;

public class EncryptionTaskService extends Service {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(EncryptionTaskService.class);

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
        return new Task() {
            @Override
            protected Object call() throws Exception {
                updateProgress(0, 1);
                List<File> inputFiles = FileSystemHelper.listFilesWithSubdirs(inputDirectory.toFile());
                int numInputFiles = inputFiles.size();
                int i = 0;

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
                            throw ex;
                        }
                    } else {
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
}
