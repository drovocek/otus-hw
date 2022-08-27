package ru.otus.atm.impl;

import ru.otus.atm.core.ATMException;
import ru.otus.atm.core.Denomination;
import ru.otus.atm.core.Dispenser;
import ru.otus.atm.core.HasDenomination;

import java.util.*;

import static ru.otus.atm.core.ATMException.NOT_ENOUGH_BANKNOTES;
import static ru.otus.atm.core.ATMException.NOT_SET_BANKNOTE_VAULT;

public class DispenserImpl<T extends HasDenomination> implements Dispenser<T> {

    private Map<Denomination, Queue<T>> container;

    @Override
    public void bind(Map<Denomination, Queue<T>> container) {
        this.container = container;
    }

    @Override
    public List<T> unload(long sum) {
        if (this.container != null) {

            List<T> banknotesForIssuance = new ArrayList<>();
            long sumForIssuance = 0;

            for (Map.Entry<Denomination, Queue<T>> entry : new TreeMap<>(this.container).entrySet()) {
                int denomination = entry.getKey().asNumber();
                Queue<T> banknotes = entry.getValue();

                long balanceToBeCharged = sum - sumForIssuance;

                if (sumForIssuance == sum) {
                    break;
                } else if (banknotes.isEmpty() && denomination > balanceToBeCharged) {
                    continue;
                }

                long banknotesNeedForCharge = balanceToBeCharged / denomination;
                for (int i = 0; i < banknotesNeedForCharge; i++) {
                    T banknote = banknotes.poll();
                    if (banknote == null) {
                        break;
                    }
                    banknotesForIssuance.add(banknote);
                    sumForIssuance += banknote.getDenomination().asNumber();
                }
            }

            trowIfBanknotesSumLessThenTarget(banknotesForIssuance, sum);

            return banknotesForIssuance;

        } else {
            throw new ATMException(NOT_SET_BANKNOTE_VAULT);
        }
    }

    private void trowIfBanknotesSumLessThenTarget(List<T> banknotes, long target) {
        long sumInMinDenominationCell = banknotes.stream()
                .mapToLong(banknote -> banknote.getDenomination().asNumber())
                .sum();
        if (sumInMinDenominationCell < target) {
            throw new ATMException(NOT_ENOUGH_BANKNOTES);
        }
    }
}
