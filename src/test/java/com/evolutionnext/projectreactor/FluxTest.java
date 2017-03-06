package com.evolutionnext.projectreactor;


import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

@SuppressWarnings("Duplicates")
public class FluxTest {

    @Test
    public void testFlux() {
        Flux<Integer> integerFlux =
                Flux.create(new Consumer<FluxSink<Integer>>() {
            @Override
            public void accept(FluxSink<Integer> integerFluxSink) {
                integerFluxSink.next(10);
                integerFluxSink.next(50);
                integerFluxSink.next(100);
                integerFluxSink.complete();
            }
        });

        integerFlux.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println("Received Integer:" + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, new Runnable() {
            @Override
            public void run() {
                System.out.println("Done");
            }
        });
    }


    @Test
    public void testFluxWithSubscriber() throws InterruptedException {
        Flux<Integer> integerFlux = Flux.create(new Consumer<FluxSink<Integer>>() {
            @Override
            public void accept(FluxSink<Integer> integerFluxSink) {
                integerFluxSink.next(10);
                integerFluxSink.next(50);
                integerFluxSink.next(100);
                integerFluxSink.complete();
            }
        });

        integerFlux.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(10);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("Received Integer:" + integer);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done");
            }
        });

        Thread.sleep(1000);
    }
}
