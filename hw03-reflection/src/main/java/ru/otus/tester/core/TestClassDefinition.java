package ru.otus.tester.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestClassDefinition {

    private final Class<?> sourceClazz;
    private final Map<String, Method> methodByName;
    private final Map<String, List<Annotation>> annotationsByMethodName;
    private final Map<Class<? extends Annotation>, List<Method>> methodsByAnnotationClazz;

    public TestClassDefinition(Class<?> sourceClazz, Map<String, Method> methodByName, Map<String, List<Annotation>> annotationsByMethodName, Map<Class<? extends Annotation>, List<Method>> methodsByAnnotationClazz) {
        this.sourceClazz = sourceClazz;
        this.methodByName = methodByName;
        this.annotationsByMethodName = annotationsByMethodName;
        this.methodsByAnnotationClazz = methodsByAnnotationClazz;
    }

    public Class<?> getSourceClazz() {
        return this.sourceClazz;
    }

    public Optional<Method> getMethodByName(String name) {
        return Optional.ofNullable(this.methodByName.get(name));
    }

    public List<Annotation> getAnnotationsByMethodName(String name) {
        return annotationsByMethodName.get(name);
    }

    public List<Method> getMethodsByAnnotationClazz(Class<? extends Annotation> annotationClazz) {
        return this.methodsByAnnotationClazz.get(annotationClazz);
    }
}
