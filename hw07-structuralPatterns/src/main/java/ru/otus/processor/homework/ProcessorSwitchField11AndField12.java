package ru.otus.processor.homework;

import ru.otus.crm.model.Message;
import ru.otus.processor.Processor;

public class ProcessorSwitchField11AndField12 implements Processor {

    @Override
    public Message process(Message message) {
        return message.toBuilder()
                .field11(message.getField12())
                .field12(message.getField11())
                .build();
    }
}
