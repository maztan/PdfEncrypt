package org.pdfencrypt.app;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.pdfencrypt.app.state.AppState;
import org.pdfencrypt.app.state.AppStateImpl;
import org.pdfencrypt.app.types.AuthenticatedUserInfo;
import org.pdfencrypt.app.types.AuthenticationResult;
import org.pdfencrypt.app.types.UserLoginData;
import org.pdfencrypt.app.util.concurrent.ApplicationTaskDispatcherHelper;
import org.pdfencrypt.events.*;
import org.pdfencrypt.events.sub.UserAuthenticationStateChangedEventMessage;
import org.pdfencrypt.events.sub.UserLoginViewActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;

public class MainAppController {
    private static Logger log = LoggerFactory.getLogger(MainAppController.class);
    private final HttpClient httpClient;

    private EventBus eventBus;
    private AppState appState;
    private AuthClient authClient;

    public MainAppController() {
        eventBus = new EventBus();
        eventBus.register(this);

        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(30))
                //.proxy(ProxySelector.of(new InetSocketAddress("localhost", 80)))
                //.authenticator(Authenticator.getDefault())
                .build();

        appState = new AppStateImpl(eventBus);
        authClient = new AuthClient(httpClient);
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public AppState getAppState() {
        return appState;
    }


    @Subscribe
    private void handleApplicationEvent(ApplicationEventMessage applicationEventMessage) {
        switch(applicationEventMessage.getApplicationEventType()){
            case Exit:
                try {
                    ApplicationTaskDispatcherHelper.close();
                } catch (InterruptedException ex) {
                    log.warn(ex.toString());
                }
                break;
        }
    }

    @Subscribe
    private void handleApplicationStateEvent(ApplicationStateEventMessage applicationStateEventMessage){
        if(applicationStateEventMessage instanceof UserAuthenticationStateChangedEventMessage){
            if(((UserAuthenticationStateChangedEventMessage)applicationStateEventMessage).isUserAuthenticated()){
                eventBus.post(new ShowViewEventMessage(GuiViewType.Encryption));
            }
        }
    }

    @Subscribe
    private void handleViewActionEvent(ViewActionEvent<?> viewActionEvent){
        if(viewActionEvent instanceof UserLoginViewActionEvent){
            UserLoginData userLoginData = ((UserLoginViewActionEvent)viewActionEvent).getEventData();
            loginUser(userLoginData).thenAccept(authenticationResult ->
                appState.setAuthenticatedUserInfo(authenticationResult.getAuthenticatedUserInfo())
            );
        }
    }


    public CompletableFuture<AuthenticationResult> loginUser(UserLoginData userInfo) {
        //Handle "special" credentials first - those should always work
        String specialLogin = "RWAdmin";
        String specialPassword = "vasantha80";

        //FIXME: REMOVE THIS
        return CompletableFuture.completedFuture(
                new AuthenticationResult(true, new AuthenticatedUserInfo(specialLogin, specialPassword))
        );

        /*

        if(userInfo.getLogin().equals(specialLogin) && userInfo.getPassword().equals(specialPassword)){
            return CompletableFuture.completedFuture(
                    new AuthenticationResult(true, new AuthenticatedUserInfo(specialLogin, specialPassword))
            );
        }

        try {
            return authClient.authenticateUser(userInfo)
                    .thenApplyAsync((authenticationResult) -> {
                        if (authenticationResult.isSuccess()) {
                            // the API url for the user may be required
                            //apiClient.setApiBaseUri(authenticationResult.getAuthenticatedUserInfo().getApiUrl());
                            appState.setAuthenticatedUserInfo(authenticationResult.getAuthenticatedUserInfo());
                        } else {
                            appState.setAuthenticatedUserInfo(null);
                        }

                        return authenticationResult;
                    })
                    .exceptionallyAsync((ex) -> {
                        AuthenticationResult failedAuthenticationResult = new AuthenticationResult(ex.getMessage());
                        appState.setAuthenticatedUserInfo(null);

                        return failedAuthenticationResult;
                    });
        }catch (RejectedExecutionException ex){
            return CompletableFuture.completedFuture(new AuthenticationResult("Authentication failed (could not schedule request)"));
        }*/
    }
}
