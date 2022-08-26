package ru.otus.atm.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.atm.core.ATM;
import ru.otus.atm.core.ATMException;
import ru.otus.atm.core.details.HasDenomination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static ru.otus.atm.core.ATMException.NOT_ENOUGH_BANKNOTES;
import static ru.otus.atm.example.DollarBanknot.values;

class DollarATMTest {

    private final ATM<DollarBanknot> dollarAtm = new DollarATM();

    private static Stream<List<DollarBanknot>> generateData() {
        return Stream.generate(DollarATMTest::randomBanknotes).limit(10);
    }

    private static final Random random = new Random();

    public static List<DollarBanknot> randomBanknotes() {
        List<DollarBanknot> banknotes = new ArrayList<>();
        int banknotesListCount = random.nextInt(1, 100);
        int banknotesCount = values().length;
        for (int i = 0; i < banknotesListCount; i++) {
            int skip = random.nextInt(banknotesCount);
            DollarBanknot banknote = Arrays.stream(values()).skip(skip).findFirst().get();
            banknotes.add(banknote);
        }
        return banknotes;
    }

    @ParameterizedTest
    @MethodSource("generateData")
    @DisplayName("Должен принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)")
    void loadBanknotes(List<DollarBanknot> banknotes) {
        dollarAtm.loadBanknotes(banknotes);
        long expectedSum = banknotes.stream()
                .mapToLong(HasDenomination::getDenomination)
                .sum();
        Assertions.assertEquals(expectedSum, dollarAtm.getCashBalance());
    }

    @Test
    @DisplayName("Должен выдавать запрошенную сумму минимальным количеством банкнот")
    void unloadBanknotes() {
        List<DollarBanknot> banknotes = List.of(
                DollarBanknot.ONE, DollarBanknot.ONE, DollarBanknot.ONE, DollarBanknot.ONE, DollarBanknot.ONE,
                DollarBanknot.TWO, DollarBanknot.TWO,
                DollarBanknot.FIVE, DollarBanknot.FIVE,
                DollarBanknot.TEN, DollarBanknot.TEN, DollarBanknot.TEN, DollarBanknot.TEN, DollarBanknot.TEN,
                DollarBanknot.TWENTY, DollarBanknot.TWENTY,
                DollarBanknot.FIFTY,
                DollarBanknot.ONE_HUNDRED
        );
        dollarAtm.loadBanknotes(banknotes);
        List<DollarBanknot> unloadedBanknotes1 = dollarAtm.unloadBanknotes(51);
        Assertions.assertEquals(List.of(DollarBanknot.FIFTY, DollarBanknot.ONE), unloadedBanknotes1);

        List<DollarBanknot> unloadedBanknotes2 = dollarAtm.unloadBanknotes(51);
        Assertions.assertEquals(List.of(
                DollarBanknot.TWENTY, DollarBanknot.TWENTY, DollarBanknot.TEN, DollarBanknot.ONE), unloadedBanknotes2);

        List<DollarBanknot> unloadedBanknotes3 = dollarAtm.unloadBanknotes(16);
        Assertions.assertEquals(List.of(
                DollarBanknot.TEN, DollarBanknot.FIVE, DollarBanknot.ONE), unloadedBanknotes3);
    }


    @ParameterizedTest
    @MethodSource("generateData")
    @DisplayName("Должен выдавать ошибку, если сумму нельзя выдать минимальным количеством банкнот")
    void unloadBanknotesAndThrowIfNotEnough(List<DollarBanknot> banknotes) {
        long AtmAmount = banknotes.stream().mapToLong(DollarBanknot::getDenomination).sum();
        ATMException thrown = Assertions.assertThrows(ATMException.class, () -> dollarAtm.unloadBanknotes(AtmAmount + 1));

        Assertions.assertEquals(NOT_ENOUGH_BANKNOTES, thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("generateData")
    @DisplayName("Должен выдавать сумму остатка денежных средств")
    void getCashBalance(List<DollarBanknot> banknotes) {
        dollarAtm.loadBanknotes(banknotes);
        long expectedSum = banknotes.stream().mapToLong(DollarBanknot::getDenomination).sum();
        Assertions.assertEquals(expectedSum, dollarAtm.getCashBalance());
    }
}