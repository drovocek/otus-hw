package ru.otus.processor.homework;

public class EvenSecondException extends RuntimeException {

    public final static String EVEN_SECOND = "Even second";

    public EvenSecondException(String message){
        super(message);
    }
}
