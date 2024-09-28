package com.example.demo;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");

//        callPlatformThreads();
        callVirtualThreads();
    }

    private static void callVirtualThreads() {
        for (int i = 0; i< 1_000_000; i++) {
            System.out.println("thread nº " + i);
            Thread.ofVirtual().start(() -> {
                try {
                    Thread.sleep(10000000);
                } catch (Exception e) {
                    // deal with e
                }
            });
        }
    }

    private static void callPlatformThreads() {
        for (int i = 0; i< 1_000_000; i++) {
            System.out.println("thread nº " + i);
            new Thread(() -> {
                try {
                    Thread.sleep(10000000);
                } catch(Exception e) {
                    // deal with e
                }}).start();
        }
    }
}