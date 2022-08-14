package ru.otus.tester.simple_impl;

import ru.otus.tester.core.Test;
import ru.otus.tester.core.TestClassDefinition;
import ru.otus.tester.core.TestClassDefinitionExtractor;
import ru.otus.tester.util.TestRunnerException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TestClassDefinitionExtractorImpl implements TestClassDefinitionExtractor {

    @Override
    public TestClassDefinition extract(Class<?> testClazz) {
        Map<String, Method> methodByName = new HashMap<>();
        Map<String, List<Annotation>> annotationsByMethodName = new HashMap<>();
        Map<Class<? extends Annotation>, List<Method>> methodsByAnnotationClazz = new HashMap<>();

        Arrays.stream(testClazz.getDeclaredMethods())
                .forEach(method -> {
                    List<Annotation> testerAnnotations = Arrays.stream(method.getDeclaredAnnotations())
                            .filter(annotation -> TESTER_ANNOTATION_TYPES.contains(annotation.annotationType()))
                            .collect(Collectors.toList());

                    if (!testerAnnotations.isEmpty()) {
                        methodByName.put(method.getName(), method);
                        annotationsByMethodName.put(method.getName(), testerAnnotations);
                        testerAnnotations.forEach(annotation -> {
                            Class<? extends Annotation> annotationClazz = annotation.annotationType();
                            List<Method> methods = methodsByAnnotationClazz.get(annotationClazz);
                            if (methods == null) {
                                methods = new ArrayList<>();
                            }
                            methods.add(method);
                            methodsByAnnotationClazz.put(annotationClazz, methods);
                        });
                    }
                });

        if (methodsByAnnotationClazz.get(Test.class) == null) {
            throw new TestRunnerException(
                    "Class %s does not contains any @Test annotations".formatted(testClazz.getName()));
        }

        return new TestClassDefinition(testClazz, methodByName, annotationsByMethodName, methodsByAnnotationClazz);
    }
}
