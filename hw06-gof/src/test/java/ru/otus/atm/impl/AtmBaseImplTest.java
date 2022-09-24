package ru.otus.atm.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.atm.impl.test_utils.TestBanknote;
import ru.otus.atm.impl.test_utils.TestUtils;
import ru.otus.atm.core.Denomination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AtmBaseImplTest {

    private static Stream<List<TestBanknote>> generateRandomBanknotes() {
        return Stream.generate(TestUtils::randomBanknotes).limit(10);
    }

    private static Stream<Map<Denomination, Queue<TestBanknote>>> generateRandomContainers() {
        return Stream.generate(TestUtils::randomFilledContainer).limit(10);
    }

    @ParameterizedTest
    @MethodSource("generateRandomBanknotes")
    @DisplayName("Должен принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)")
    void load(List<TestBanknote> banknotes) {
        Map<Denomination, Queue<TestBanknote>> container = new HashMap<>();
        ATM<TestBanknote> atm = TestUtils.buildBaseImplAtm(container);

        atm.load(banknotes);
        List<TestBanknote> containerBanknotes = container.entrySet().stream()
                .flatMap(e -> e.getValue().stream())
                .collect(Collectors.toList());

        Assertions.assertEquals(banknotes.size(), containerBanknotes.size());
        Assertions.assertTrue(containerBanknotes.containsAll(banknotes));
    }

    @ParameterizedTest
    @MethodSource("generateRandomContainers")
    @DisplayName("Должен выдавать сумму остатка денежных средств")
    void cashBalance(Map<Denomination, Queue<TestBanknote>> container) {
        ATM<TestBanknote> atm = TestUtils.buildBaseImplAtm(container);

        long expected = container.entrySet().stream()
                .mapToLong(entry -> (long) entry.getKey().asNumber() * entry.getValue().size())
                .sum();

        Assertions.assertEquals(expected, atm.cashBalance());
    }

    private static Map<Denomination, Queue<TestBanknote>> container() {
        return Map.of(
                Denomination.ONE, TestUtils.banknotes(Denomination.ONE, 100),
                Denomination.TWO, TestUtils.banknotes(Denomination.TWO, 50),
                Denomination.FIVE, TestUtils.banknotes(Denomination.FIVE, 20),
                Denomination.TEN, TestUtils.banknotes(Denomination.TEN, 10),
                Denomination.TWENTY, TestUtils.banknotes(Denomination.TWENTY, 5),
                Denomination.FIFTY, TestUtils.banknotes(Denomination.FIFTY, 2),
                Denomination.ONE_HUNDRED, TestUtils.banknotes(Denomination.ONE_HUNDRED, 1)
        );
    }

    @Test
    @DisplayName("Должен выдавать запрошенную сумму минимальным количеством банкнот")
    void unload() {
        ATM<TestBanknote> atm = TestUtils.buildBaseImplAtm(container());

        List<TestBanknote> banknotes1 = atm.unload(100L);
        Assertions.assertTrue(banknotes1.stream().allMatch(b -> b.getDenomination().equals(Denomination.ONE_HUNDRED)));
        Assertions.assertEquals(100L, banknotes1.stream().mapToLong(b -> b.getDenomination().asNumber()).sum());

        List<TestBanknote> banknotes2 = atm.unload(100L);
        Assertions.assertTrue(banknotes2.stream().allMatch(b -> b.getDenomination().equals(Denomination.FIFTY)));
        Assertions.assertEquals(100L, banknotes2.stream().mapToLong(b -> b.getDenomination().asNumber()).sum());

        List<TestBanknote> banknotes3 = atm.unload(40L);
        Assertions.assertTrue(banknotes3.stream().allMatch(b -> b.getDenomination().equals(Denomination.TWENTY)));
        Assertions.assertEquals(40L, banknotes3.stream().mapToLong(b -> b.getDenomination().asNumber()).sum());
    }
}