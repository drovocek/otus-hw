package ru.otus.logger;

import ru.otus.logger.core.BeanContainer;
import ru.otus.logger.example.TestLogging;
import ru.otus.logger.example.TestLoggingImpl;

/**
 * To start the application:
 * ./gradlew build
 * java -jar ./hw05-bytecode/build/libs/testLoggingExampleRunner-0.1.jar
 */
public class ExampleRunner {

    public static void main(String[] args) {
        TestLogging testLogging = (TestLogging) BeanContainer.create(TestLoggingImpl.class);
        testLogging.someMethodNotAnnotated();
        testLogging.someMethod1();
        testLogging.someMethod2(777);
        testLogging.someMethod2(1, "s");
        testLogging.someMethod3(111, 222);
        testLogging.someMethod4(333, 444, 555);
    }
}
