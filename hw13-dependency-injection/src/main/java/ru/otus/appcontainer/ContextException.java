package ru.otus.appcontainer;

public class ContextException extends RuntimeException {

    public static final String DUPLICATE_BEAN_NAME = "A bean named '%s' is already common in context";
    public static final String BEAN_BY_NAME_DOES_NOT_CONTAINS = "A bean named '%s' does not contains in context";
    public static final String MORE_THEN_ONE_IMPL = "Bean with name '%s' has more then one impl";

    public ContextException(String message) {
        super(message);
    }
}
