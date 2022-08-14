package ru.otus;

import ru.otus.tester.core.TestClassDefinitionExtractor;
import ru.otus.tester.core.TestExecutor;
import ru.otus.tester.core.TestResultPrinter;
import ru.otus.tester.simple_impl.TestClassDefinitionExtractorImpl;
import ru.otus.tester.simple_impl.TestExecutorImpl;
import ru.otus.tester.simple_impl.TestResultPrinterImpl;
import ru.otus.tester.simple_impl.TestRunner;

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
