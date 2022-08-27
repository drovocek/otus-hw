package ru.otus.atm.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.atm.impl.test_utils.TestBanknote;
import ru.otus.atm.impl.test_utils.TestUtils;
import ru.otus.atm.core.ATMException;
import ru.otus.atm.core.Denomination;
import ru.otus.atm.core.Dispenser;

import java.util.List;
import java.util.Map;
import java.util.Queue;

import static ru.otus.atm.core.ATMException.NOT_ENOUGH_BANKNOTES;
import static ru.otus.atm.core.ATMException.NOT_SET_BANKNOTE_VAULT;

class DispenserImplTest {

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
        Dispenser<TestBanknote> dispenser = new DispenserImpl<>();
        dispenser.bind(container());

        List<TestBanknote> banknotes1 = dispenser.unload(100L);
        Assertions.assertTrue(banknotes1.stream().allMatch(b -> b.getDenomination().equals(Denomination.ONE_HUNDRED)));
        Assertions.assertEquals(100L, banknotes1.stream().mapToLong(b -> b.getDenomination().asNumber()).sum());

        List<TestBanknote> banknotes2 = dispenser.unload(100L);
        Assertions.assertTrue(banknotes2.stream().allMatch(b -> b.getDenomination().equals(Denomination.FIFTY)));
        Assertions.assertEquals(100L, banknotes2.stream().mapToLong(b -> b.getDenomination().asNumber()).sum());

        List<TestBanknote> banknotes3 = dispenser.unload(40L);
        Assertions.assertTrue(banknotes3.stream().allMatch(b -> b.getDenomination().equals(Denomination.TWENTY)));
        Assertions.assertEquals(40L, banknotes3.stream().mapToLong(b -> b.getDenomination().asNumber()).sum());
    }

    @Test
    @DisplayName("Должен выдавать ошибку, если сумму нельзя выдать минимальным количеством банкнот")
    void unloadAndThrowIfNotEnough() {
        Dispenser<TestBanknote> dispenser = new DispenserImpl<>();
        Map<Denomination, Queue<TestBanknote>> container = container();
        dispenser.bind(container);

        long sumInContainer = container.entrySet().stream()
                .mapToLong(entry -> (long) entry.getKey().asNumber() * entry.getValue().size())
                .sum();

        ATMException thrown = Assertions.assertThrows(ATMException.class, () -> dispenser.unload(sumInContainer + 1));

        Assertions.assertEquals(NOT_ENOUGH_BANKNOTES, thrown.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку при попытке использования, если не связан с контейнером")
    void unload_fail_if_not_set_container() {
        Dispenser<TestBanknote> dispenser = new DispenserImpl<>();
        ATMException thrown = Assertions.assertThrows(ATMException.class, () -> dispenser.unload(100L));
        Assertions.assertEquals(NOT_SET_BANKNOTE_VAULT, thrown.getMessage());
    }
}