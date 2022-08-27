package ru.otus.atm.core;

import java.util.Map;
import java.util.Queue;

public interface ContainerService<T extends HasDenomination> {

    void bind(Map<Denomination, Queue<T>> container);
}
