package com.evolutionnext.projectreactor;


import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SuppressWarnings("Duplicates")
public class HotVsColdFluxTest {

    @Test
    public void testColdFlux() throws InterruptedException {
        Flux<Long> integerFlux = Flux.interval(Duration.ofSeconds(1));

        integerFlux.subscribe(integer -> System.out.println("Observer 1:" + integer),
                Throwable::printStackTrace, () -> System.out.println("Done"));

        Thread.sleep(3000);

        integerFlux.subscribe(integer -> System.out.println("Observer 2:" + integer),
                Throwable::printStackTrace, () -> System.out.println("Done"));

        Thread.sleep(5000);
    }

    @Test
    public void testHotFlux() throws InterruptedException {
        Flux<Long> integerFlux = Flux.interval(Duration.ofSeconds(1)).publish().autoConnect();

        integerFlux.subscribe(integer -> System.out.println("Observer 1:" + integer),
                Throwable::printStackTrace, () -> System.out.println("Done"));

        Thread.sleep(3000);

        integerFlux.subscribe(integer -> System.out.println("Observer 2:" + integer),
                Throwable::printStackTrace, () -> System.out.println("Done"));

        Thread.sleep(5000);
    }
}
