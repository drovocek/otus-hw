package ru.otus.atm.impl;

import ru.otus.atm.core.ATMException;
import ru.otus.atm.core.Analyzer;
import ru.otus.atm.core.Denomination;
import ru.otus.atm.core.HasDenomination;

import java.util.Map;
import java.util.Queue;

public class AnalyzerImpl<T extends HasDenomination> implements Analyzer<T> {

    private Map<Denomination, Queue<T>> container;

    @Override
    public void bind(Map<Denomination, Queue<T>> container) {
        this.container = container;
    }

    @Override
    public long cashBalance() {
        if (this.container != null) {
            return this.container.entrySet().stream()
                    .mapToLong(entry -> (long) entry.getKey().asNumber() * entry.getValue().size())
                    .sum();
        } else {
            throw new ATMException(ATMException.NOT_SET_BANKNOTE_VAULT);
        }
    }
}
