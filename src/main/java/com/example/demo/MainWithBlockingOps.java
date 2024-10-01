package com.example.demo;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWithBlockingOps {
    public static void main(String[] args) {

        System.out.println("Hello world!");

        callVirtualThreads();
    }

    private static void callVirtualThreads() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i <= 10; i++) {
                String taskName = "Task" + i;
                executor.execute(() -> callService(taskName));
            }
        }
    }

    static void callService(String taskName) {
        try {
            System.out.println(Thread.currentThread() + " executing " + taskName);

            new URL("https://httpstat.us/200?sleep=2000").getContent();

            System.out.println(Thread.currentThread() + " completed " + taskName);

        } catch (Exception e) {
            // deal with e
        }

    }
}