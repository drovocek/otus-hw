package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;

import static ru.otus.appcontainer.ContextException.*;
import static ru.otus.appcontainer.ReflectionUtils.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final Map<Class<?>, List<Object>> appComponentsByType = new HashMap<>();

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
                .forEach(this::buildBeansAndStore);
    }

    private void buildBeansAndStore(Class<?> configClass) {
        Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(m -> m.getAnnotation(AppComponent.class).order()))
                .forEach(this::buildBeanAndStore);
    }

    private void buildBeanAndStore(Method method) {
        Class<?> configClass = method.getDeclaringClass();
        String beanNameBase = method.getAnnotation(AppComponent.class).name();
        if (appComponentsByName.containsKey(beanNameBase)) {
            throw new ContextException(DUPLICATE_BEAN_NAME.formatted(beanNameBase));
        }

        Object noArgInstance = getNoArgInstance(configClass);
        Object[] args = Arrays.stream(method.getParameterTypes())
                .map(this::getAppComponent)
                .toArray(Object[]::new);
        Object bean = invokeMethod(method, noArgInstance, args);
        Class<?> beanType = bean.getClass();

        this.appComponentsByName.put(beanNameBase, bean);
        store(beanType, bean);

        Class<?> methodReturnType = method.getReturnType();

        if (methodReturnType != beanType) {
            store(methodReturnType, bean);
        }
    }

    private void store(Class<?> beanType, Object bean) {
        List<Object> beans = Optional.ofNullable(this.appComponentsByType.get(beanType)).orElse(new ArrayList<>());
        beans.add(bean);
        this.appComponentsByType.put(beanType, beans);
    }

    private void checkConfigClass(Class<?>... configClasses) {
        Arrays.stream(configClasses).forEach(configClass -> {
            if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
                throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
            }
        });
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> beans = this.appComponentsByType.get(componentClass);
        if (beans == null) {
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
