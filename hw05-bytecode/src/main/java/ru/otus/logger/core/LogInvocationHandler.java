package ru.otus.logger.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LogInvocationHandler implements InvocationHandler {

    private final Object target;
    private final List<String> logAnnotatedMethodIds = new ArrayList<>();

    public LogInvocationHandler(Object target) {
        this.target = target;

        for (Method method : target.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Log.class)) {
                this.logAnnotatedMethodIds.add(asId(method));
            }
        }
    }

    private String asId(Method method) {
        String[] split = method.toString().split("\\.");
        return split[split.length - 1];
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        String id = asId(method);

        if (logAnnotatedMethodIds.contains(id)) {
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
