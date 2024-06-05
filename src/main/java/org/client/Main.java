package org.client;

import com.google.protobuf.Empty;

import examples.GreeterGrpc;
import examples.HelloReply;
import examples.HelloRequest;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;

public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        GrpcClient client = GrpcClient.client(vertx);
        SocketAddress server = SocketAddress.inetSocketAddress(8080, "localhost");
        // client.request(server, GreeterGrpc.getSayHelloMethod()).onSuccess(req -> {
        // for (int i = 0; i < 10; i++) {
        // req.write(HelloRequest.newBuilder().setName("hieudang" + i).build());
        // }

        // // req.end(HelloRequest.newBuilder().setName("hieudang").build());
        // req.response().onSuccess(response -> {
        // response.handler(reply -> {
        // System.out.println(reply.getMessage());
        // });
        // });

        // req.end();
        // });

        client
                .request(server, GreeterGrpc.getSayHelloMethod())
                .compose(request -> {
                    for (int i = 0; i < 10; i++) {
                        request.write(HelloRequest.newBuilder().setName("hieudang" + i).build());
                    }
                    request.end();
                    return request.response();
                })
                .onSuccess(response -> {
                    response.handler(item -> {
                        System.out.println(item.getMessage());
                        // Process item
                    });
                    response.endHandler(v -> {
                        // Done
                    });
                    response.exceptionHandler(err -> {
                        // Something went bad
                    });
                });
    }
}
