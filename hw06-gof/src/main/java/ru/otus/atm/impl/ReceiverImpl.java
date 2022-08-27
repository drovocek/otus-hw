package ru.otus.atm.impl;

import ru.otus.atm.core.ATMException;
import ru.otus.atm.core.Denomination;
import ru.otus.atm.core.HasDenomination;
import ru.otus.atm.core.Receiver;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ReceiverImpl<T extends HasDenomination> implements Receiver<T> {

    private Map<Denomination, Queue<T>> container;

    @Override
    public void bind(Map<Denomination, Queue<T>> container) {
        this.container = container;
    }

    @Override
    public void load(List<T> banknotes) {
        if (this.container != null) {
            banknotes.forEach(this::putInDenominationCell);
        } else {
            throw new ATMException(ATMException.NOT_SET_BANKNOTE_VAULT);
        }
    }

    private void putInDenominationCell(T banknote) {
        Denomination denomination = banknote.getDenomination();
        this.container.putIfAbsent(denomination, new ArrayDeque<>());
        Queue<T> denominationCell = this.container.get(denomination);
        denominationCell.add(banknote);
    }
}
