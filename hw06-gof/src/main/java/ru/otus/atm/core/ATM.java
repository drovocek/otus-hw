package ru.otus.atm.core;

import ru.otus.atm.core.details.HasBanknotesDispenser;
import ru.otus.atm.core.details.HasBanknotesReceiver;
import ru.otus.atm.core.details.HasCashBalanceAnalyzer;
import ru.otus.atm.core.details.HasDenomination;

import java.util.*;

import static ru.otus.atm.core.ATMException.NOT_ENOUGH_BANKNOTES;

public abstract class ATM<T extends HasDenomination>
        implements HasBanknotesReceiver<T>, HasBanknotesDispenser<T>, HasCashBalanceAnalyzer {

    protected final Map<Integer, Deque<T>> moneyVault = new TreeMap<>(Collections.reverseOrder());

    protected ATM() {
    }

    @Override
    public void loadBanknotes(List<T> banknotes) {
        banknotes.forEach(this::putInDenominationCell);
    }

    private void putInDenominationCell(T banknote) {
        int denomination = banknote.getDenomination();
        moneyVault.putIfAbsent(denomination, new ArrayDeque<>());
        Deque<T> denominationCell = moneyVault.get(denomination);
        denominationCell.add(banknote);
    }

    @Override
    public List<T> unloadBanknotes(long sum) {
        System.out.println("Upload: " + sum);
        List<T> banknotesForIssuance = new ArrayList<>();

        long sumForIssuance = 0;

        for (Map.Entry<Integer, Deque<T>> entry : moneyVault.entrySet()) {
            Integer denomination = entry.getKey();
            Deque<T> banknotes = entry.getValue();

            long balanceToBeCharged = sum - sumForIssuance;

            if (sumForIssuance == sum) {
                break;
            } else if (banknotes.isEmpty() && denomination > balanceToBeCharged) {
                continue;
            }

            long banknotesNeedForCharge = balanceToBeCharged / denomination;
            int banknotesAvailable = banknotes.size();
            long banknotesToBeCharged = banknotesNeedForCharge > banknotesAvailable ? banknotesAvailable : banknotesNeedForCharge;

            for (int i = 0; i < banknotesToBeCharged; i++) {
                T banknote = banknotes.pop();
                banknotesForIssuance.add(banknote);
                sumForIssuance += banknote.getDenomination();
            }
        }

        trowIfBanknotesSumLessThenTarget(banknotesForIssuance, sum);

        System.out.println("Upload result: " + banknotesForIssuance.stream().mapToLong(HasDenomination::getDenomination).sum());
        return banknotesForIssuance;
    }

    private void trowIfBanknotesSumLessThenTarget(List<T> banknotes, long target) {
        long sumInMinDenominationCell = banknotes.stream()
                .mapToLong(HasDenomination::getDenomination)
                .sum();
        if (sumInMinDenominationCell < target) {
            throw new ATMException(NOT_ENOUGH_BANKNOTES);
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
