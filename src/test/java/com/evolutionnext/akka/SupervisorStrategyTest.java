package com.evolutionnext.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static org.fest.assertions.Assertions.assertThat;


public class SupervisorStrategyTest {

    /**
     * Create any child actor and throw an Illegal Argument Exception
     *
     * @throws InterruptedException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testActor() throws Exception {
        ActorSystem system = ActorSystem.create("MySystem");

        Timeout timeout = new Timeout(5, TimeUnit.SECONDS);

        ActorRef grandparent = system.actorOf(
                Props.create(OneForOneGrandparentActor.class),
                "GrandparentActorJava");

        Future<Object> childActorFuture1 = Patterns.ask
                (grandparent,
                        Props.create(ExceptionalChildActor.class),
                        timeout);

        Thread.sleep(3000);

        childActorFuture1.onComplete(
                new OnComplete<Object>() {
                    @Override
                    public void onComplete(Throwable failure, Object actorRef)
                            throws Throwable {
                        ((ActorRef) actorRef)
                                .tell("IllegalArgumentException",
                                        null);
                    }
                }, system.dispatcher()
        );


        Thread.sleep(13000);
        System.out.println("Shutting Down Server");
        Await.result(system.terminate(), Duration.apply(10, TimeUnit.SECONDS));
    }
}
