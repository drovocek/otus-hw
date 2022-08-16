package ru.otus.atm.example;

import ru.otus.atm.core.details.HasDenomination;

public enum DollarBanknot implements HasDenomination {

    ONE(1),
    TWO(2),
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    ONE_HUNDRED(100);

    private final int denomination;

    DollarBanknot(int denomination) {
        this.denomination = denomination;
    }

    @Override
    public int getDenomination() {
        return this.denomination;
    }
}
