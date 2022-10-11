package ru.otus.processor.homework;

import ru.otus.crm.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalTime;

import static ru.otus.processor.homework.EvenSecondException.EVEN_SECOND;

public class ProcessorThrowExceptionWhenEvenSecond implements Processor {

    private final TimeProvider timeProvider;

    public ProcessorThrowExceptionWhenEvenSecond(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {
        LocalTime time = timeProvider.getTime();
        if (time.getSecond() % 2 == 0) {
            throw new EvenSecondException(EVEN_SECOND);
        }
        return message;
    }
}
