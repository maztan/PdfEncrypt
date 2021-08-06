package org.pdfencrypt.app;

import org.pdfencrypt.app.types.AuthenticatedUserInfo;
import org.pdfencrypt.app.types.AuthenticationResult;
import org.pdfencrypt.app.types.UserLoginData;
import org.pdfencrypt.app.util.concurrent.NamingThreadFactory;

import java.net.http.HttpClient;
import java.util.concurrent.*;

public class AuthClient {
    private HttpClient httpClient;
    /**
     * WARNING: Will throw RejectedExecutionException if can't schedule the task
     */
    private final ExecutorService requestProcessingExecutorService =
            new ThreadPoolExecutor(1, 5, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(50),
                    new NamingThreadFactory("ApiClient-RequestProcessing-"), new ThreadPoolExecutor.AbortPolicy() /*new ThreadPoolExecutor.CallerRunsPolicy()*/);


    public AuthClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public CompletableFuture<AuthenticationResult> authenticateUser(UserLoginData userInfo) {
        return CompletableFuture.completedFuture(
                new AuthenticationResult(true, new AuthenticatedUserInfo("fakelogion", "fakepassword")));
        /*try {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    UploadStatusResponse dataUploadResponse = uploadData(zippedStream, resourceName, uploadMimeType, uploadUri);
                    if (dataUploadResponse.statusCode() / 100 != 2) { //(integer division)
                        return false;
                    }
                    return true;
                } catch (IOException | InterruptedException ex) {
                    throw new CompletionException(ex);
                }
            }, requestProcessingExecutorService);
        } catch (RejectedExecutionException ex) {
            return CompletableFuture.failedFuture(new ApiClientTooManyRequestsException("Too many requests pending", ex));
        }*/
    }

    public void close(){
        if(requestProcessingExecutorService != null){
            requestProcessingExecutorService.shutdownNow();
        }
    }
}
