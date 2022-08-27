package ru.otus.atm.impl.test_utils;

import ru.otus.atm.core.Denomination;
import ru.otus.atm.core.HasDenomination;

public class TestBanknote implements HasDenomination {

    private final Denomination denomination;

    public TestBanknote(Denomination denomination) {
        this.denomination = denomination;
    }

    @Override
    public Denomination getDenomination() {
        return this.denomination;
    }
}
