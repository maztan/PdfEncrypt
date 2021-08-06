package org.pdfencrypt.app;

import org.pdfencrypt.app.state.AppState;
import org.pdfencrypt.app.state.AppStateImpl;
import org.pdfencrypt.app.util.concurrent.ApplicationTaskDispatcherHelper;
import org.pdfencrypt.gui.MainGui;
import org.pdfencrypt.gui.controller.ViewFactory;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import com.google.common.eventbus.EventBus;

public class PdfEncryptMain {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(PdfEncryptMain.class);

    public static void main(String[]args) {
        //see https://stackoverflow.com/a/9117188/478765
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        //Logger.getLogger("").setLevel(Level.FINEST);
        Logger.getLogger("").setLevel(Level.INFO);

        ApplicationTaskDispatcherHelper.init();// <- close it somewhere!
        MainGui.launch2(args);
    }
}
