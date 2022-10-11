package ru.otus.processor.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.crm.model.Message;

import java.time.LocalTime;
import java.util.stream.Stream;

import static ru.otus.processor.homework.EvenSecondException.EVEN_SECOND;

class ProcessorThrowExceptionWhenEvenSecondTest {

    private static Stream<Integer> generateEvenNumbers() {
        return Stream.iterate(0, x -> x + 2).limit(30);
    }

    @ParameterizedTest
    @MethodSource("generateEvenNumbers")
    @DisplayName("Должен выбрасывать исключение в четную секунду")
    public void process_in_even_second(Integer evenNumber) {
        var processor =
                new ProcessorThrowExceptionWhenEvenSecond(() -> LocalTime.of(1, 1, evenNumber));
        var message = new Message.Builder(1L).build();
        EvenSecondException thrown = Assertions.assertThrows(EvenSecondException.class, () -> processor.process(message));

        Assertions.assertEquals(EVEN_SECOND, thrown.getMessage());
    }

    private static Stream<Integer> generateOddNumbers() {
        return Stream.iterate(1, x -> x + 2).limit(30);
    }

    @ParameterizedTest
    @MethodSource("generateOddNumbers")
    @DisplayName("В нечетную секунду должен возвращать сообщение в неизменном виде")
    public void process_in_odd_second(Integer oddNumber) {
        var processor =
                new ProcessorThrowExceptionWhenEvenSecond(() -> LocalTime.of(1, 1, oddNumber));
        var messageIn = new Message.Builder(1L).build();

        Message messageOut = processor.process(messageIn);

        Assertions.assertEquals(messageIn, messageOut);
    }
}