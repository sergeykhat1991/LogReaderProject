package ru.khat.logreader.Singleton;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadsPool {

    private static ThreadsPool instance;
    private ExecutorService service = Executors.newFixedThreadPool(10);

    private ThreadsPool() {
    }

    public static synchronized ThreadsPool getInstance() {
        if (instance == null) {
            instance = new ThreadsPool();
        }
        return instance;
    }

    public ExecutorService getService() {
        return service;
    }
}
