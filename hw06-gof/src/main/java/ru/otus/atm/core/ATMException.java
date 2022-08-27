package ru.otus.atm.core;

public class ATMException extends RuntimeException {

    public static final String NOT_ENOUGH_BANKNOTES = "Sorry, but the ATM cannot give you the requested amount with a minimum amount of banknotes";
    public static final String NOT_SET_BANKNOTE_VAULT = "Please, set Container before use component";

    public ATMException(String message) {
        super(message);
    }
}
