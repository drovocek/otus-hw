package ru.otus.atm.core.details;

import java.util.List;

public interface HasBanknotesReceiver<T extends HasDenomination> {

    void loadBanknotes(List<T> banknotes);
}
