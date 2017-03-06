package com.evolutionnext.projectreactor;


import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.function.Consumer;

@SuppressWarnings("Duplicates")
public class FluxBackpressureTest {

    private Flux<Integer> crazedFlux;

    @Before
    public void setUp() {
        crazedFlux = Flux.create(new Consumer<FluxSink<Integer>>() {
            @Override
            public void accept(FluxSink<Integer> sink) {
                int i = 0;
                //noinspection InfiniteLoopStatement
                while (true) {
                    sink.next(i);
                    i++;
                }
            }
        }, FluxSink.OverflowStrategy.DROP);
    }

    @Test
    public void testFlux() throws InterruptedException {
        crazedFlux.publishOn(Schedulers.newSingle("my-new-thread"))
                .subscribe(n -> {
                    try {
                        Thread.sleep(5); //Wait to fill the buffer some more.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(n);
                }, Throwable::printStackTrace);
        Thread.sleep(4000);
    }
}
