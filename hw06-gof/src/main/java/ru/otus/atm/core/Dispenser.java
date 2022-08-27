package ru.otus.atm.core;

import java.util.List;

public interface Dispenser<T extends HasDenomination>
        extends ContainerService<T> {

    List<T> unload(long sum);
}
