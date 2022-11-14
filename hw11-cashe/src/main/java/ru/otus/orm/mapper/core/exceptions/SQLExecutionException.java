package ru.otus.orm.mapper.core.exceptions;

public class SQLExecutionException extends RuntimeException {

    public final static String HAS_ID = "to insert an object and id must be null";
    public final static String HAS_NOT_ID = "to insert an object and id must be not null";

    public SQLExecutionException(String message) {
        super(message);
    }
}
