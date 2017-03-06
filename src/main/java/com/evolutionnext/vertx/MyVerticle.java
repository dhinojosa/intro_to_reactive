package com.evolutionnext.vertx;

import io.vertx.core.*;
import io.vertx.core.eventbus.Message;

public class MyVerticle extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        getVertx().eventBus().consumer("latest-news",
                (Handler<Message<String>>) message ->
                        System.out.println("Received Message: " +
                                message.body()));
        startFuture.complete();
    }
}
