package org.example.verticles;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.example.db.SimpleDatabase;
import org.example.di.AppComponent;
import org.example.di.AppModule;
import org.example.di.DaggerAppComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class HttpVerticleTest {

  private SimpleDatabase db;

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    NoteVerticle noteVerticle = new NoteVerticle();
    HttpVerticle httpVerticle = new HttpVerticle();
    AppComponent appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(vertx))
        .build();
    appComponent.inject(noteVerticle);
    vertx.deployVerticle(httpVerticle);
    vertx.deployVerticle(noteVerticle);
    testContext.completeNow();
  }

  @Test
  void createNote_shouldReturnSuccess(Vertx vertx, VertxTestContext testContext) {
    WebClient client = WebClient.create(vertx);

    JsonObject noteJson = new JsonObject()
        .put("id", "1")
        .put("content", "Test note content");

    client.post(8080, "localhost", "/note/create")
        .sendJsonObject(noteJson, ar -> {
          if (ar.succeeded()) {
            assertThat(ar.result().statusCode()).isEqualTo(200);
            JsonObject response = ar.result().bodyAsJsonObject();
            assertThat(response.getBoolean("success")).isTrue();
            testContext.completeNow();
          } else {
            testContext.failNow(ar.cause());
          }
        });
  }

  @Test
  void getNotes_shouldReturnNoteList(Vertx vertx, VertxTestContext testContext) {
    WebClient client = WebClient.create(vertx);

    client.get(8080, "localhost", "/note/gets")
        .send(ar -> {
          if (ar.succeeded()) {
            assertThat(ar.result().statusCode()).isEqualTo(200);
            JsonObject response = ar.result().bodyAsJsonObject();
            assertThat(response.getBoolean("success")).isTrue();
            System.out.println(ar.result().body().toString());
            testContext.completeNow();
          } else {
            testContext.failNow(ar.cause());
          }
        });
  }
}
