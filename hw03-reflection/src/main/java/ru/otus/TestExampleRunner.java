package ru.otus;

import ru.otus.tester.*;

/**
 * To start the application:
 * ./gradlew build
 * java -jar ./hw03-reflection/build/libs/gradleTestExampleRunner-0.1.jar
 */
public class TestExampleRunner {

    public static void main(String[] args) {
        TestClassDefinitionExtractor definitionExtractor = new TestClassDefinitionExtractorImpl();
        TestExecutor executor = new TestExecutorImpl();
        TestResultPrinter printer = new TestResultPrinterImpl();

        TestRunner runner = new TestRunner(definitionExtractor, executor, printer);
        runner.run("ru.otus.TestExample");
    }
}
