package ru.otus.atm.core;

public enum Denomination {

    ONE_HUNDRED(100),
    FIFTY(50),
    TWENTY(20),
    TEN(10),
    FIVE(5),
    TWO(2),
    ONE(1);

    private final int number;

    Denomination(int number) {
        this.number = number;
    }

    public int asNumber() {
        return this.number;
    }
}
