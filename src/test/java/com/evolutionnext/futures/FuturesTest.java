package com.evolutionnext.futures;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A suite of tests that
 * show various uses of Futures in Java.
 */
public class FuturesTest {
    /**
     * Simple test that show the sequential result of the output since Await.result blocks.  "Step 1" should show at the
     * command line first time, then the result of "Hello World" then "Step 2".  This should display always.
     * Finally the assertion will determine that we indeed got the result were looking for.
     *
     * @throws Exception if the timeout lapses waiting for the result
     */

    @Test
    public void testJavaUtilConcurrentFutures()
            throws Exception {
        ExecutorService executorService =
                Executors.newCachedThreadPool();

        Callable<String> callable
                = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println
                        ("Inside future:" +
                                Thread.currentThread().getName());
                Thread.sleep(5000);
                return "Asynchronous String Result";
            }
        };

        java.util.concurrent.Future<String> future =
                executorService.submit(callable);
        System.out.println("Processing 1");
        System.out.println(future.get()); //waits if necessary
        System.out.println("Processing 2");
    }


    @Test
    public void testJavaUtilConcurrentFuturesAsynchronously() throws Exception {
        ExecutorService executorService =
                Executors.newCachedThreadPool();

        Callable<String> asynchronousTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
                //something expensive
                Thread.sleep(500);
                return "Asynchronous String Result";
            }
        };

        java.util.concurrent.Future<String> future =
                executorService.submit(asynchronousTask);

        System.out.println("Processing Asynchronously 1");
        while (!future.isDone()) {
            //What to do while we are waiting
            System.out.println("Doing something else");
        }
        System.out.println(future.get()); //immediate
        System.out.println("Processing Asynchronously 2");
    }
}
