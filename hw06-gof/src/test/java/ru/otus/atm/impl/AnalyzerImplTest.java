package ru.otus.atm.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.atm.impl.test_utils.TestBanknote;
import ru.otus.atm.impl.test_utils.TestUtils;
import ru.otus.atm.core.ATMException;
import ru.otus.atm.core.Analyzer;
import ru.otus.atm.core.Denomination;

import java.util.Map;
import java.util.Queue;
import java.util.stream.Stream;

import static ru.otus.atm.core.ATMException.NOT_SET_BANKNOTE_VAULT;

class AnalyzerImplTest {

    private static Stream<Map<Denomination, Queue<TestBanknote>>> generateRandomContainers() {
        return Stream.generate(TestUtils::randomFilledContainer).limit(10);
    }

    @ParameterizedTest
    @MethodSource("generateRandomContainers")
    @DisplayName("Должен выдавать сумму остатка денежных средств")
    void cashBalance(Map<Denomination, Queue<TestBanknote>> container) {
        Analyzer<TestBanknote> analyzer = new AnalyzerImpl<>();

        analyzer.bind(container);

        long expected = container.entrySet().stream()
                .mapToLong(entry -> (long) entry.getKey().asNumber() * entry.getValue().size())
                .sum();

        Assertions.assertEquals(expected, analyzer.cashBalance());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку при попытке использования, если не связан с контейнером")
    void cashBalance_fail_if_not_set_container() {
        Analyzer<TestBanknote> analyzer = new AnalyzerImpl<>();
        ATMException thrown = Assertions.assertThrows(ATMException.class, analyzer::cashBalance);
        Assertions.assertEquals(NOT_SET_BANKNOTE_VAULT, thrown.getMessage());
    }
}