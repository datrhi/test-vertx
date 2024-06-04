package org.example.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.example.codecs.CustomMessage;
import org.example.codecs.CustomMessageCodec;

public class HttpVerticle extends AbstractVerticle {
  CustomMessageCodec customCodec = new CustomMessageCodec();

  @Override
  public void start(Promise<Void> startPromise) {
    HttpServer httpServer = vertx.createHttpServer();
    Router router = Router.router(vertx);
    vertx.eventBus().registerCodec(customCodec);

    router.route().handler(BodyHandler.create());
    router.route().path("/note/create").handler(this::createNote)
        .failureHandler(this::failureHandler);
    router.route().path("/note/gets").handler(this::getNoteList)
        .failureHandler(this::failureHandler);
    httpServer
        .requestHandler(router)
        .listen(8080, ar -> {
          if (ar.succeeded()) {
            System.out.println("Listen at port 8080");
            startPromise.complete();
          } else {
            System.out.println("Error: " + ar.cause().getMessage());
            startPromise.fail(ar.cause());
          }
        });
  }

  private void createNote(RoutingContext routingContext) {
    vertx.eventBus()
        .request(NoteVerticle.CREATE, routingContext.body().asJsonObject())
        .onComplete(ar -> {
          if (ar.succeeded()) {
            successHandler(routingContext, (JsonObject) ar.result().body());
          } else {
            routingContext.fail(ar.cause());
          }
        });
  }

  private void getNoteList(RoutingContext routingContext) {
    DeliveryOptions deliveryOptions = new DeliveryOptions().setCodecName(customCodec.name());
    vertx.eventBus()
        .request(NoteVerticle.GETS, new CustomMessage(999), deliveryOptions)
        .onComplete(ar -> {
          if (ar.succeeded()) {
            successHandler(routingContext, (JsonObject) ar.result().body());
          } else {
            routingContext.fail(ar.cause());
          }
        });
  }

  private void successHandler(RoutingContext routingContext, JsonObject data) {
    routingContext.response()
        .setStatusCode(200)
        .putHeader("Content-type", "application/json")
        .end(new JsonObject().put("success", true).put("data", data).toString());
  }

  private void failureHandler(RoutingContext routingContext) {
    routingContext.response()
        .setStatusCode(400)
        .putHeader("Content-Type", "application/json")
        .end(new JsonObject().put("success", false)
            .put("message", routingContext.failure().getMessage()).toString());
  }


  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    super.stop(stopPromise);
  }
}
