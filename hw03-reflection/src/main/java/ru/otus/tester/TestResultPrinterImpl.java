package ru.otus.tester;

import java.util.List;

public class TestResultPrinterImpl implements TestResultPrinter {

    @Override
    public void print(String title, List<TestResult> results) {
        printTitle(title);
        printInfo(results);
        printStatistic(results);
    }

    private void printTitle(String title) {
        System.out.println();
        System.out.println("<<< " + title + " >>>");
    }

    private void printStatistic(List<TestResult> results) {
        System.out.println();
        int totalCount = results.size();
        long successCount = results.stream().filter(TestResult::isSuccess).count();
        long failCount = totalCount - successCount;
        System.out.printf("""
                   TESTS STATISTIC:
                     -  successfully: %s
                     -  failed: %s
                     -  total: %s
                %n""", successCount, failCount, totalCount);
    }

    private void printInfo(List<TestResult> results) {
        System.out.println();
        System.out.println("   TESTS INFO:");
        System.out.println("   | Description | Result | Exception message |");
        results.forEach(result -> {
            String methodTitle = result.getTitle();
            String info = result.isSuccess() ? "success" : "fail";
            String message = result.getCause()
                    .map(TesterReflectionUtils::findRootCause)
                    .map(Throwable::getMessage)
                    .orElse("-");
            System.out.printf("   | %s | %s | %s |%n", methodTitle, info, "'" + message + "'");
        });
    }
}
