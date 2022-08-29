package ru.otus.processor.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

class ProcessorSwitchField11AndField12Test {

    @Test
    @DisplayName("Должен менять местами значения field11 и field12")
    void process() {
        var processor = new ProcessorSwitchField11AndField12();
        var messageIn =
                new Message.Builder(1L)
                        .field1("field1")
                        .field2("field2")
                        .field3("field3")
                        .field4("field4")
                        .field5("field5")
                        .field6("field6")
                        .field7("field7")
                        .field8("field8")
                        .field9("field9")
                        .field10("field10")
                        .field11("field11")
                        .field12("field12")
                        .field13(new ObjectForMessage())
                        .build();
        var messageOut = processor.process(messageIn);

        Assertions.assertEquals(messageIn.getField1(), messageOut.getField1());
        Assertions.assertEquals(messageIn.getField2(), messageOut.getField2());
        Assertions.assertEquals(messageIn.getField3(), messageOut.getField3());
        Assertions.assertEquals(messageIn.getField4(), messageOut.getField4());
        Assertions.assertEquals(messageIn.getField5(), messageOut.getField5());
        Assertions.assertEquals(messageIn.getField6(), messageOut.getField6());
        Assertions.assertEquals(messageIn.getField7(), messageOut.getField7());
        Assertions.assertEquals(messageIn.getField8(), messageOut.getField8());
        Assertions.assertEquals(messageIn.getField9(), messageOut.getField9());
        Assertions.assertEquals(messageIn.getField10(), messageOut.getField10());
        Assertions.assertEquals(messageIn.getField11(), messageOut.getField12());
        Assertions.assertEquals(messageIn.getField12(), messageOut.getField11());
        Assertions.assertEquals(messageIn.getField13(), messageOut.getField13());
    }
}