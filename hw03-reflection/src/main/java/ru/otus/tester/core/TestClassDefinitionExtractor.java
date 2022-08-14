package ru.otus.tester.core;

import java.lang.annotation.Annotation;
import java.util.List;

public interface TestClassDefinitionExtractor {

    List<Class<? extends Annotation>> TESTER_ANNOTATION_TYPES = List.of(Test.class, Before.class, After.class);

    TestClassDefinition extract(Class<?> testClazz);
}
