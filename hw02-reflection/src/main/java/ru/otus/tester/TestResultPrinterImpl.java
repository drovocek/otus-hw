package ru.otus.tester;

import java.util.List;

public class TestResultPrinterImpl implements TestResultPrinter {

    @Override
    public void print(List<TestResult> results) {
        System.out.println();
        System.out.println("TEST INFO:");
        System.out.println("| Title | Result | Message |");
        results.forEach(result -> {
            String title = result.getTitle();
            String info = result.isSuccess() ? "success" : "fail";
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
