package com.example.demo;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * This demo app shows the behavior of virtual threads using the invokeAny method.
 * First, with 2 long-running tasks, returning the lower latency thread
 * Second, with 3 tasks, with a failing tasks,  returning the lower latency thread
 * Third, whith 4 tasks, with a failing tasks,  and a complex tasks with 2 blocks, returning the lower latency thread
 * And finally 2 failing tasks, returning an exception.
 */
public class MainWithAnyBlockingOps {
    public static void main(String[] args) {

        System.out.println("Hello world!");

        callVirtualThreads();
    }

    private static void callVirtualThreads() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            String taskName1 = "Task 1";
            Callable<Boolean> callable1 = () -> callService(taskName1, 10000);
            String taskName2 = "Task 2";
            Callable<Boolean> callable2 = () -> callService(taskName2, 5000);
            String taskName3 = "Task 3";
            Callable<Boolean> callable3 = () -> {
                callService(taskName3, 1000);
                throw new Exception("Internal error on short running task");
            };
            String taskName4 = "Task 4";
            Callable<Boolean> callable4 = () -> {
                callService(taskName4, 1000);
                callService(taskName4, 2000);
                return true;
            };

            Collection<? extends Callable<Boolean>> tasksSet1 = List.of(
                    callable1,
                    callable2
            );

            Collection<? extends Callable<Boolean>> tasksSet2 = List.of(
                    callable1,
                    callable2,
                    callable3
            );

            Collection<? extends Callable<Boolean>> tasksSet3 = List.of(
                    callable1,
                    callable2,
                    callable3,
                    callable4
            );

            Collection<? extends Callable<Boolean>> tasksSet4 = List.of(
                    callable3,
                    callable3
            );
            long timeout = 30000;
            TimeUnit unit = TimeUnit.MILLISECONDS;
//            Boolean result1 = executor.invokeAny(tasksSet1, timeout, unit);
//            Boolean result2 = executor.invokeAny(tasksSet2, timeout, unit);
//            Boolean result3 = executor.invokeAny(tasksSet3, timeout, unit);
            Boolean result4 = executor.invokeAny(tasksSet4, timeout, unit);

//            System.out.println("result1 = " + result1);
//            System.out.println("result2 = " + result2);
//            System.out.println("result3 = " + result3);
            System.out.println("result4 = " + result4);
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    static boolean callService(String taskName, long sleep) {
        boolean status = false;
        try {
            System.out.println(Thread.currentThread() + " executing " + taskName);

            status = new URL("https://httpstat.us/200?sleep=" + sleep).getContent() != null;

            System.out.println(Thread.currentThread() + " completed " + taskName + " after " + sleep);

        } catch (Exception e) {
            // deal with e
            System.out.println(Thread.currentThread() + " exception " + taskName);
        }
        return status;
    }
}