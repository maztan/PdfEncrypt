package org.pdfencrypt.app.services.encryption;

import com.itextpdf.kernel.pdf.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


public class PdfEncryptionService {
    public static void encryptPdf(Path srcFilePath, Path destFilePath, String userPassword, String ownerPassword) throws IOException {
        byte[] userPasswordBytes = userPassword.getBytes(StandardCharsets.UTF_8);
        byte[] ownerPasswordBytes = ownerPassword.getBytes(StandardCharsets.UTF_8);

        PdfDocument pdfDoc = new PdfDocument(
                new PdfReader(srcFilePath.toAbsolutePath().toString()),
                new PdfWriter(destFilePath.toAbsolutePath().toString(), new WriterProperties().setStandardEncryption(
                        userPasswordBytes,
                        ownerPasswordBytes,
                        EncryptionConstants.ALLOW_PRINTING,
                        EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA))
        );
        pdfDoc.close();
    }

    public static void decryptPdf(Path srcFilePath, Path destFilePath, String ownerPassword) throws IOException {
        //byte[] userPasswordBytes = userPassword.getBytes(StandardCharsets.UTF_8);
        byte[] ownerPasswordBytes = ownerPassword.getBytes(StandardCharsets.UTF_8);

        ReaderProperties readerProperties = new ReaderProperties().setPassword(ownerPasswordBytes);
        PdfReader pdfReader = new PdfReader(srcFilePath.toAbsolutePath().toString(), readerProperties);
        PdfDocument pdfDocument = new PdfDocument(pdfReader, new PdfWriter(destFilePath.toAbsolutePath().toString()));
        pdfDocument.close();
    }
}