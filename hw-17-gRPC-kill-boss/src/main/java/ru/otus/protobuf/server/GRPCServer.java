package ru.otus.protobuf.server;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;
    private static final Logger log = LoggerFactory.getLogger(GRPCServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        var valueGeneratorService = new NumberGeneratorService();

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(valueGeneratorService).build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            log.info("Received shutdown request");
            server.shutdown();
            log.info("Server stopped");
        }));

        log.info("Server is waiting for client, port:{}", SERVER_PORT);
        server.awaitTermination();
    }
}
