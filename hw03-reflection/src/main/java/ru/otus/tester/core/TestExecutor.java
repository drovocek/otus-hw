package ru.otus.tester.core;

import java.util.List;

public interface TestExecutor {

    List<TestResult> execute(TestClassDefinition definition);
}
