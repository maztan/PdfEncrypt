package org.pdfencrypt.app.util.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class NamingThreadFactory implements ThreadFactory {
    private final String prefix;
    private int threadNuber = 0;

    public NamingThreadFactory(String prefix){
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setName(prefix + threadNuber);
        return t;
    }
}
