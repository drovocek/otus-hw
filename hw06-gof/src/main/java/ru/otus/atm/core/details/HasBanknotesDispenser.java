package ru.otus.atm.core.details;

import java.util.List;

public interface HasBanknotesDispenser<T extends HasDenomination> {

    List<T> unloadBanknotes(long sum);
}
