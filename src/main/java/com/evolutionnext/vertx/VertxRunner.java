package com.evolutionnext.vertx;

import io.vertx.core.Vertx;

public class VertxRunner {
    public static void main(String[] args) {
        System.out.println("Here we go!");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle("com.evolutionnext.vertx.MyVerticle");
        vertx.eventBus().send("latest-news",
                "Justin Bieber imprisoned, cities rejoice");
    }
}
