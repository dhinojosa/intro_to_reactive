package com.evolutionnext.akka;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Graph;
import akka.stream.Materializer;
import akka.stream.SinkShape;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ReactiveStreamsTest {

    @Test
    public void testReactiveStream() throws InterruptedException, TimeoutException {
        final ActorSystem system =
                ActorSystem.create("MySystem");

        final Materializer mat =
                ActorMaterializer.create(system);

        Source<Integer, NotUsed> source = Source.range(1, 5);

        Flow<Integer, Integer, NotUsed> flow = Flow
                .fromFunction(x -> x + 1);

        Source<Integer, NotUsed> source2 = source.via(flow);

        Sink<Integer, CompletionStage<Integer>> fold =
                Sink.<Integer, Integer>fold(0, (next, total) -> total + next);

        CompletionStage<Integer> integerCompletionStage = source2.runWith(fold, mat);
        integerCompletionStage.thenAccept(System.out::println);
        Thread.sleep(3000);
        Await.ready(system.terminate(), Duration.apply(10, TimeUnit.SECONDS));
    }

    @Test
    public void testReactiveStreamRefined() throws InterruptedException, TimeoutException {
        final ActorSystem system =
                ActorSystem.create("MySystem");

        final Materializer mat =
                ActorMaterializer.create(system);

        Source<Integer, NotUsed> source = Source.range(1, 5).map(x -> x + 1)
                .fold(0, (next, total) -> total + next);

        CompletionStage<Integer> integerCompletionStage =
                source.runWith(Sink.head(), mat);

        integerCompletionStage.thenAccept(System.out::println);
        Thread.sleep(3000);
        Await.ready(system.terminate(), Duration.apply(10, TimeUnit.SECONDS));
    }
}
