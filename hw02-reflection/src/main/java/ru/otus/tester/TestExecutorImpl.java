package ru.otus.tester;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.otus.tester.TesterReflectionUtils.createInstance;
import static ru.otus.tester.TesterReflectionUtils.invokeMethod;

public class TestExecutorImpl implements TestExecutor {

    @Override
    public List<TestResult> execute(TestClassDefinition definition) {
        List<Method> testMethods =
                definition.getMethodsByAnnotationClazz(Test.class);

        LinkedList<Object> testInstances = Stream.generate(() -> createInstance(definition.getSourceClazz()))
                .limit(testMethods.size())
                .collect(Collectors.toCollection(LinkedList::new));

        System.out.println();
        System.out.println("METHOD CALL STACK BY INSTANCE HASHCODE: ");
        return testMethods.stream()
                .map(method -> {
                    TestResult testResult;

                    Object testInstance = testInstances.poll();

                    List<Method> beforeMethods = definition.getMethodsByAnnotationClazz(Before.class);
                    for (Method beforeMethod : beforeMethods) {
                        invokeMethod(beforeMethod, testInstance);
                    }

                    try {
                        invokeMethod(method, testInstance);
                        testResult = new TestResult(method.getName(), true);
                    } catch (TestMethodInvokeException e) {
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
