package ru.otus.protobuf.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.NumberResponse;

public class ClientObserver implements StreamObserver<NumberResponse> {

    private static final Logger log = LoggerFactory.getLogger(ClientObserver.class);

    private long lastValue;

    @Override
    public void onNext(NumberResponse response) {
        long serverValue = response.getValue();
        log.info("serverValue: {}", serverValue);
        setLastValue(serverValue);
    }

    @Override
    public void onError(Throwable t) {
        log.error("got error", t);
    }

    @Override
    public void onCompleted() {
        log.info("\n\nrequest completed");
    }

    private synchronized void setLastValue(long lastValue) {
        this.lastValue = lastValue;
    }

    public synchronized long getLastValueAndReset() {
        var lastValuePrev = this.lastValue;
        this.lastValue = 0;
        return lastValuePrev;
    }
}
