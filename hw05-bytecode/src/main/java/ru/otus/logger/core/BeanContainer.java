package ru.otus.logger.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class BeanContainer {

    public static Object create(Class<?> beanClazz) {
        Class<?>[] interfaces = beanClazz.getInterfaces();
        if (interfaces.length != 0) {
            return dynamicProxyInstance(beanClazz, interfaces);
        }
        return noProxyInstance(beanClazz);
    }

    private static Object dynamicProxyInstance(Class<?> beanClazz, Class<?>[] interfaces) {
        return Proxy.newProxyInstance(
                BeanContainer.class.getClassLoader(), interfaces, new LogInvocationHandler(noProxyInstance(beanClazz)));
    }

    private static Object noProxyInstance(Class<?> beanClazz) {
        try {
            return beanClazz.getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
