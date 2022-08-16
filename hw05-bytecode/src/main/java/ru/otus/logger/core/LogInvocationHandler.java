package ru.otus.logger.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LogInvocationHandler implements InvocationHandler {

    private final Object target;
    private final List<String> logAnnotatedMethodNames = new ArrayList<>();

    public LogInvocationHandler(Object target) {
        this.target = target;

        for (Method method : target.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Log.class)) {
                this.logAnnotatedMethodNames.add(method.getName());
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        if (logAnnotatedMethodNames.contains(methodName)) {
            String logInfo = "executed method: %s".formatted(methodName);
            if (args != null) {
                int paramIdx = 0;
                StringBuilder sb = new StringBuilder();
                for (Object arg : args) {
                    sb.append(", ").append("arg").append(paramIdx++).append(": ").append(arg);
                }
                logInfo += sb;
            }

            System.out.println(logInfo);
        }

        return method.invoke(target, args);
    }
}
