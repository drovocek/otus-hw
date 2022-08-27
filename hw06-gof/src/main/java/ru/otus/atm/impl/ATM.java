package ru.otus.atm.impl;

import ru.otus.atm.core.*;

import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ATM<T extends HasDenomination>
        implements Receiver<T>, Dispenser<T>, Analyzer<T> {

    private final Receiver<T> receiver;
    private final Dispenser<T> dispenser;
    private final Analyzer<T> analyzer;

    public ATM(Map<Denomination, Queue<T>> container,
               Receiver<T> receiver,
               Dispenser<T> dispenser,
               Analyzer<T> analyzer) {
        this.receiver = receiver;
        this.dispenser = dispenser;
        this.analyzer = analyzer;
        bind(container);
    }

    @Override
    public void bind(Map<Denomination, Queue<T>> container) {
        this.receiver.bind(container);
        this.dispenser.bind(container);
        this.analyzer.bind(container);
    }

    @Override
    public void load(List<T> banknotes) {
        this.receiver.load(banknotes);
    }

    @Override
    public List<T> unload(long sum) {
        return this.dispenser.unload(sum);
    }

    @Override
    public long cashBalance() {
        return this.analyzer.cashBalance();
    }
}
