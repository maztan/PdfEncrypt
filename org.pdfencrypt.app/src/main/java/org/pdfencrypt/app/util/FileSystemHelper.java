package org.pdfencrypt.app.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public final class FileSystemHelper {
    public static List<File> listFilesWithSubdirs(File dir){
        List<File> files = (List<File>) FileUtils.listFiles(dir, null, true);
        return files;
    }
}
