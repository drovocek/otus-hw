package ru.otus.tester;

import java.util.List;

public interface TestExecutor {

    List<TestResult> execute(TestClassDefinition definition);
}
