package ru.otus.protobuf.server;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.NumberGeneratorServiceGrpc;
import ru.otus.protobuf.generated.NumberResponse;
import ru.otus.protobuf.generated.RangeRequest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class NumberGeneratorService extends NumberGeneratorServiceGrpc.NumberGeneratorServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(NumberGeneratorService.class);
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    @Override
    public void getNumbersInRange(RangeRequest request,
                                  StreamObserver<NumberResponse> responseObserver) {
        long firstValue = request.getFirstValue();
        long lastValue = request.getLastValue();
        log.info("request for the new sequence of numbers, firstValue:{}, lastValue:{}", firstValue, lastValue);
        var currentValue = new AtomicLong(firstValue);

        Runnable task = () -> {
            var value = currentValue.getAndIncrement();

            responseObserver.onNext(buildResponse(value));
            if (value == lastValue) {
                executor.shutdown();
                responseObserver.onCompleted();
                log.info("sequence of numbers finished");
            }
        };

        this.executor.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);
    }

    private static NumberResponse buildResponse(long value) {
        return NumberResponse.newBuilder()
                .setValue(value)
                .build();
    }
}
