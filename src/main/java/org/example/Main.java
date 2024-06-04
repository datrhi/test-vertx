package org.example;

import io.vertx.core.Vertx;
import org.example.di.AppComponent;
import org.example.di.AppModule;
import org.example.di.DaggerAppComponent;
import org.example.verticles.HttpVerticle;
import org.example.verticles.NoteVerticle;

public class Main {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    NoteVerticle noteVerticle = new NoteVerticle();
    HttpVerticle httpVerticle = new HttpVerticle();
    AppComponent appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(vertx))
        .build();
    appComponent.inject(noteVerticle);
    vertx.deployVerticle(httpVerticle);
    vertx.deployVerticle(noteVerticle);
  }
}
