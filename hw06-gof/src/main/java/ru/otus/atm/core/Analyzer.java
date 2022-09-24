package ru.otus.atm.core;

public interface Analyzer<T extends HasDenomination>
        extends ContainerService<T> {

    long cashBalance();
}
