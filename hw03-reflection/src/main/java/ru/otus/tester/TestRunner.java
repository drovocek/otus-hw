package ru.otus.tester;

import java.util.List;

public record TestRunner(TestClassDefinitionExtractor definitionExtractor,
                         TestExecutor executor, TestResultPrinter printer) {

    public void run(String testClassName) {
        try {
            Class<?> testClazz = this.getClass().getClassLoader().loadClass(testClassName);
            TestClassDefinition definition = this.definitionExtractor.extract(testClazz);
            List<TestResult> results = this.executor.execute(definition);
            this.printer.print(
                    "Tests for class: %s".formatted(testClassName),
                    results);
        } catch (ClassNotFoundException e) {
            throw new TestRunnerException("Class with name %s does not exist".formatted(testClassName));
        }
    }
}
