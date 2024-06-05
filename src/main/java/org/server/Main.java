package org.server;

import examples.GreeterGrpc;
import examples.HelloReply;
import examples.HelloRequest;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.grpc.server.GrpcServerResponse;

public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        GrpcServer grpcServer = GrpcServer.server(vertx);
        grpcServer.callHandler(GreeterGrpc.getSayHelloMethod(), request -> {
            request.handler(
                    item -> {
                        System.out.println("item " + item.getName());
                        request.response()
                                .write(HelloReply.newBuilder().setMessage("Hello" + item.getName()).build());
                    });
            request.endHandler(v -> request.response().end());
            // request.handler(hello -> {

            // System.out.println("Request name: " + hello.getName());

            // HelloReply reply = HelloReply.newBuilder().setMessage("Hello " +
            // hello.getName()).build();
            // request.response().end(reply);

            // });
            // request.response().end();

        });

        HttpServer httpServer = vertx.createHttpServer();

        httpServer.requestHandler(grpcServer).listen(8080, ar -> {
            if (ar.succeeded()) {
                System.out.println("gRPC server listen at 8080");
            }
        });
    }
}
