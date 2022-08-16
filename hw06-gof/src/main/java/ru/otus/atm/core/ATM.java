package ru.otus.atm.core;

import ru.otus.atm.core.details.HasBanknotesDispenser;
import ru.otus.atm.core.details.HasBanknotesReceiver;
import ru.otus.atm.core.details.HasCashBalanceAnalyzer;
import ru.otus.atm.core.details.HasDenomination;

import java.util.*;

public abstract class ATM<T extends HasDenomination>
        implements HasBanknotesReceiver<T>, HasBanknotesDispenser<T>, HasCashBalanceAnalyzer {

    protected final Map<Integer, List<T>> moneyVault = new TreeMap<>();

    protected ATM() {
    }

    @Override
    public void loadBanknotes(List<T> banknotes) {
        banknotes.forEach(this::putInDenominationCell);
    }

    private void putInDenominationCell(T banknote) {
        int denomination = banknote.getDenomination();
        moneyVault.putIfAbsent(denomination, new ArrayList<>());
        List<T> denominationCell = moneyVault.get(denomination);
        denominationCell.add(banknote);
    }

    @Override
    public List<T> unloadBanknotes(long sum) {
        List<T> banknotes = ((TreeMap<Integer, List<T>>) moneyVault).firstEntry().getValue();
        trowIfBanknotesSumLessThenTarget(banknotes, sum);
        return banknotes;
    }

    private void trowIfBanknotesSumLessThenTarget(List<T> banknotes, long target) {
        long sumInMinDenominationCell = banknotes.stream()
                .mapToLong(HasDenomination::getDenomination)
                .sum();
        if (sumInMinDenominationCell < target) {
            throw new ATMException("""
                    Sorry, but the ATM cannot give you the requested amount with a minimum amount of banknotes
                    """);
        }
    }

    @Override
    public long getCashBalance() {
        return moneyVault.values().stream()
                .flatMap(Collection::stream)
                .mapToLong(HasDenomination::getDenomination)
                .sum();
    }
}
