package com.evolutionnext.rxjava;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TickerPriceFinder {

    private final ExecutorService executorService;
    private final Random random;

    private TickerPriceFinder(Random random, ExecutorService executorService) {
        this.random = random;
        this.executorService = executorService;
    }

    public Future<Double> getPrice(String name) {
        return executorService.submit(() -> random.nextDouble() * 200.0);
    }

    public static TickerPriceFinder create() {
        Random random = new Random();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        return new TickerPriceFinder(random, executorService);
    }
}