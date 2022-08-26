package ru.otus.atm.core;

public class ATMException extends RuntimeException {

    public static final String NOT_ENOUGH_BANKNOTES = "Sorry, but the ATM cannot give you the requested amount with a minimum amount of banknotes";

    public ATMException(String message) {
        super(message);
    }
}
