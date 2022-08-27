package ru.otus.atm.core;

import java.util.List;

public interface Receiver<T extends HasDenomination>
        extends ContainerService<T> {

    void load(List<T> banknotes);
}
