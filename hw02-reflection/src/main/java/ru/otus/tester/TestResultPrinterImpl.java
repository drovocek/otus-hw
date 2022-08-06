package ru.otus.tester;

import java.util.List;

public class TestResultPrinterImpl implements TestResultPrinter {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    @Override
    public void print(List<TestResult> results) {
        System.out.println();
        System.out.println("TEST INFO:");
        System.out.println("| Title | Result | Message |");
        results.forEach(result -> {
            String title = result.getTitle();
            String info = result.isSuccess() ?
                    ANSI_GREEN + "success" + ANSI_RESET :
                    ANSI_RED + "fail" + ANSI_RESET;
            String message = result.getCause()
                    .map(TesterReflectionUtils::findRootCause)
                    .map(Throwable::getMessage)
                    .orElse("-");
            System.out.printf("| %s | %s | %s |%n", title, info, "'" + message + "'");
        });

        int totalCount = results.size();
        long successCount = results.stream().filter(TestResult::isSuccess).count();
        long failCount = totalCount - successCount;
        System.out.println();
        System.out.printf(
                """
                           TEST RESULTS:
                             -  successfully: %s
                             -  failed: %s
                             -  total: %s
                        %n""", successCount, failCount, totalCount);
    }
}
