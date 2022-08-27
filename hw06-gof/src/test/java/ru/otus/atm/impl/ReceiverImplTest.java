package ru.otus.atm.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.atm.impl.test_utils.TestBanknote;
import ru.otus.atm.impl.test_utils.TestUtils;
import ru.otus.atm.core.ATMException;
import ru.otus.atm.core.Denomination;
import ru.otus.atm.core.Receiver;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.otus.atm.core.ATMException.NOT_SET_BANKNOTE_VAULT;

class ReceiverImplTest {

    private static Stream<List<TestBanknote>> generateRandomBanknotes() {
        return Stream.generate(TestUtils::randomBanknotes).limit(10);
    }

    @ParameterizedTest
    @MethodSource("generateRandomBanknotes")
    @DisplayName("Должен принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)")
    void load(List<TestBanknote> banknotes) {
        Receiver<TestBanknote> receiver = new ReceiverImpl<>();
        Map<Denomination, Queue<TestBanknote>> container = new HashMap<>();
        receiver.bind(container);
        receiver.load(banknotes);
        List<TestBanknote> containerBanknotes = container.entrySet().stream()
                .flatMap(e -> e.getValue().stream())
                .collect(Collectors.toList());

        Assertions.assertEquals(banknotes.size(), containerBanknotes.size());
        Assertions.assertTrue(containerBanknotes.containsAll(banknotes));
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку при попытке использования, если не связан с контейнером")
    void load_fail_if_not_set_container() {
        Receiver<TestBanknote> receiver = new ReceiverImpl<>();
        ATMException thrown = Assertions.assertThrows(ATMException.class, () -> receiver.load(Collections.emptyList()));
        Assertions.assertEquals(NOT_SET_BANKNOTE_VAULT, thrown.getMessage());
    }
}