package ru.otus.logger.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LogInvocationHandler implements InvocationHandler {

    private final Object target;
    private final Map<String, Method> originalMethodsByName = new HashMap<>();

    public LogInvocationHandler(Object target) {
        this.target = target;

        for (Method method : target.getClass().getDeclaredMethods()) {
            this.originalMethodsByName.put(method.getName(), method);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Method originalMethod = originalMethodsByName.get(methodName);

        if (originalMethod.isAnnotationPresent(Log.class)) {
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
