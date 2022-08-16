package ru.otus.atm.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.atm.core.ATM;
import ru.otus.atm.core.details.HasDenomination;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.otus.atm.example.DollarBanknot.values;

class DollarATMTest {

    private final ATM<DollarBanknot> dollarAtm = new DollarATM();

    private static Stream<List<DollarBanknot>> generateData() {
        return Stream.of(
                randomBanknotes(),
                randomBanknotes(),
                randomBanknotes(),
                randomBanknotes(),
                randomBanknotes()
        );
    }

    private static Random random = new Random();

    public static List<DollarBanknot> randomBanknotes() {
        List<DollarBanknot> banknotes = new ArrayList<>();
        int banknotesListCount = random.nextInt(100);
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

    @ParameterizedTest
    @MethodSource("generateData")
    @DisplayName("Должен выдавать запрошенную сумму минимальным количеством банкнот")
    void unloadBanknotes(List<DollarBanknot> banknotes) {
        dollarAtm.loadBanknotes(banknotes);
        
        TreeMap<Integer, List<DollarBanknot>> sortedByDenominationMap =
                banknotes.stream().collect(Collectors.groupingBy(DollarBanknot::getDenomination, TreeMap::new,Collectors.toList()));
        List<DollarBanknot> minDenominationCell = sortedByDenominationMap.firstEntry().getValue();
        long minDenominationCellSum = minDenominationCell.stream().mapToLong(DollarBanknot::getDenomination).sum();
        List<DollarBanknot> banknots = dollarAtm.unloadBanknotes(minDenominationCellSum);
    }
//
//    @Test
//    @DisplayName("Должен выдавать ошибку, если сумму нельзя выдать минимальным количеством банкнот")
//    void unloadBanknotes() {
//        dollarAtm.unloadBanknotes();
//    }
//
//    @Test
//    @DisplayName("Должен выдавать сумму остатка денежных средств")
//    void getCashBalance() {
//        dollarAtm.getCashBalance();
//    }
}