package org.pdfencrypt.app.util.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Used generally for dispatching events
 */
public final class ApplicationTaskDispatcherHelper {
    private static Logger log = LoggerFactory.getLogger(ApplicationTaskDispatcherHelper.class);

    private static ExecutorService eventDispatcherExecutor;

    public static void init(){
        eventDispatcherExecutor = new ThreadPoolExecutor(1, 5, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(50),
                new NamingThreadFactory(ApplicationTaskDispatcherHelper.class.getSimpleName()), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    //FIXME: handle RejectedExecutionException
    public static <T> Future<T> submit(Callable<T> task){
        return eventDispatcherExecutor.submit(task);
    }
    public static Future<?> submit(Runnable task){
        return eventDispatcherExecutor.submit(task);
    }

    public static void close() throws InterruptedException {
        if(eventDispatcherExecutor != null){
            log.info("Awaiting ApplicationTaskDispatcherHelper shutdown.");
            if(!eventDispatcherExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
                eventDispatcherExecutor.shutdownNow();
            }
        }
    }
}
