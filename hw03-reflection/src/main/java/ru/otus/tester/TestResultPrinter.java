package ru.otus.tester;

import java.util.List;

public interface TestResultPrinter {

    void print(String title, List<TestResult> results);
}
