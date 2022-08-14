package ru.otus.tester.simple_impl;

import ru.otus.tester.core.*;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.otus.tester.util.TesterReflectionUtils.createInstance;
import static ru.otus.tester.util.TesterReflectionUtils.invokeMethod;

public class TestExecutorImpl implements TestExecutor {

    @Override
    public List<TestResult> execute(TestClassDefinition definition) {
        List<Method> testMethods =
                definition.getMethodsByAnnotationClazz(Test.class);

        System.out.println();
        System.out.println("METHOD CALL STACK BY INSTANCE HASHCODE: ");
        return testMethods.stream()
                .map(method -> {
                    TestResult testResult;

                    Object testInstance = createInstance(definition.getSourceClazz());

                    List<Method> beforeMethods = definition.getMethodsByAnnotationClazz(Before.class);
                    for (Method beforeMethod : beforeMethods) {
                        invokeMethod(beforeMethod, testInstance);
                    }

                    try {
                        invokeMethod(method, testInstance);
                        testResult = new TestResult(method.getName(), true);
                    } catch (RuntimeException e) {
                        testResult = new TestResult(method.getName(), e.getCause());
                    }

                    List<Method> afterMethods = definition.getMethodsByAnnotationClazz(After.class);
                    for (Method afterMethod : afterMethods) {
                        invokeMethod(afterMethod, testInstance);
                    }

                    return testResult;
                })
                .collect(Collectors.toList());
    }
}
