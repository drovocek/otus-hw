package ru.otus.protobuf.client;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.NumberGeneratorServiceGrpc;
import ru.otus.protobuf.generated.NumberGeneratorServiceGrpc.NumberGeneratorServiceStub;
import ru.otus.protobuf.generated.RangeRequest;

public class GRPCClient {

    private static final Logger log = LoggerFactory.getLogger(GRPCClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private long value;

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var asyncClient = NumberGeneratorServiceGrpc.newStub(channel);

        new GRPCClient().printNumbers(asyncClient);

        log.info("numbers Client is shutting down...");
        channel.shutdown();
    }

    private void printNumbers(NumberGeneratorServiceStub asyncClient) {
        RangeRequest request = buildRequest();
        ClientObserver clientObserver = new ClientObserver();
        asyncClient.getNumbersInRange(request, clientObserver);

        for (int i = 0; i < 50; i++) {
            long nextValue = getNextValue(clientObserver);
            log.info("currentValue: {}", nextValue);
            sleepOneSecond();
        }
    }

    private long getNextValue(ClientObserver clientObserver) {
        return value += clientObserver.getLastValueAndReset() + 1;
    }

    private void sleepOneSecond() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static RangeRequest buildRequest() {
        return RangeRequest.newBuilder()
                .setFirstValue(0L)
                .setLastValue(30L)
                .build();
    }
}
