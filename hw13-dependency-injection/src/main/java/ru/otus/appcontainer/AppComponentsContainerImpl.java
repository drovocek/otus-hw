package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

import static ru.otus.appcontainer.ContextException.*;
import static ru.otus.appcontainer.ReflectionUtils.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final List<Object> appComponents = new ArrayList<>();

    public AppComponentsContainerImpl(String configsPath) {
        Reflections reflections = new Reflections(configsPath);
        Class<?>[] configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                .toArray(Class<?>[]::new);

        processConfig(configClasses);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?>... configClasses) {
        checkConfigClass(configClasses);

        Arrays.stream(configClasses)
                .sorted(Comparator.comparing(o -> o.getAnnotation(AppComponentsContainerConfig.class).order()))
                .flatMap(this::extractBeanData)
                .forEach(this::store);
    }

    private void checkConfigClass(Class<?>... configClasses) {
        Arrays.stream(configClasses).forEach(configClass -> {
            if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
                throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
            }
        });
    }

    private record BeanData(String name, Object bean) {
    }

    private Stream<BeanData> extractBeanData(Class<?> configClass) {
        return Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(m -> m.getAnnotation(AppComponent.class).order()))
                .map(this::throwIfBeanNameIsRegistered)
                .map(this::extractBeanData);
    }

    private Method throwIfBeanNameIsRegistered(Method method) {
        String beanName = method.getAnnotation(AppComponent.class).name();
        if (appComponentsByName.containsKey(beanName)) {
            throw new ContextException(DUPLICATE_BEAN_NAME.formatted(beanName));
        }
        return method;
    }

    private BeanData extractBeanData(Method method) {
        Class<?> configClass = method.getDeclaringClass();
        String beanName = method.getAnnotation(AppComponent.class).name();
        Object bean = buildBean(method, configClass);

        return new BeanData(beanName, bean);
    }

    private Object buildBean(Method method, Class<?> configClass) {
        Object noArgInstance = getNoArgInstance(configClass);
        Object[] args = Arrays.stream(method.getParameterTypes())
                .map(this::getAppComponent)
                .toArray(Object[]::new);
        return invokeMethod(method, noArgInstance, args);
    }

    private void store(BeanData beanData) {
        Object bean = beanData.bean;
        this.appComponentsByName.put(beanData.name, bean);
        this.appComponents.add(bean);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> beans = this.appComponents.stream()
                .filter(bean -> componentClass.isAssignableFrom(bean.getClass()))
                .toList();
        if (beans.isEmpty()) {
            throw new ContextException(BEAN_BY_NAME_DOES_NOT_CONTAINS.formatted(componentClass.getName()));
        } else if (beans.size() > 1) {
            throw new ContextException(MORE_THEN_ONE_IMPL.formatted(componentClass.getName()));
        }
        return uncheckedCast(beans.get(0));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object bean = Optional.ofNullable(this.appComponentsByName.get(componentName))
                .orElseThrow(() -> new ContextException(BEAN_BY_NAME_DOES_NOT_CONTAINS.formatted(componentName)));
        return uncheckedCast(bean);
    }
}
