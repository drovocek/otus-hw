package ru.otus.atm.impl.test_utils;

import ru.otus.atm.core.Denomination;
import ru.otus.atm.impl.ATM;
import ru.otus.atm.impl.AnalyzerImpl;
import ru.otus.atm.impl.DispenserImpl;
import ru.otus.atm.impl.ReceiverImpl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TestUtils {

    private static final Random random = new Random();

    private TestUtils() {
    }

    public static List<TestBanknote> randomBanknotes() {
        List<TestBanknote> banknotes = new ArrayList<>();

        Arrays.stream(Denomination.values())
                .forEach(denomination -> {
                    int limit = random.nextInt(1, 10);
                    Stream.generate(() -> new TestBanknote(denomination))
                            .limit(limit)
                            .forEach(banknotes::add);
                });

        return banknotes;
    }

    public static Map<Denomination, Queue<TestBanknote>> randomFilledContainer() {
        Map<Denomination, Queue<TestBanknote>> countByDenomination = new HashMap<>();
        Arrays.stream(Denomination.values())
                .forEach(denomination ->
                        countByDenomination.put(denomination, banknotes(denomination, random.nextInt(1, 10))));
        return countByDenomination;
    }

    public static Queue<TestBanknote> banknotes(Denomination denomination, int limit) {
        return Stream.generate(() -> new TestBanknote(denomination))
                .limit(limit)
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    public static ATM<TestBanknote> buildBaseImplAtm(Map<Denomination, Queue<TestBanknote>> container) {
        return new ATM<>(
                container,
                new ReceiverImpl<>(),
                new DispenserImpl<>(),
                new AnalyzerImpl<>()
        );
    }
}
