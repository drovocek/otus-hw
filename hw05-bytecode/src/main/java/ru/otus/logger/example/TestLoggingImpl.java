package ru.otus.logger.example;

import ru.otus.logger.core.Log;

public class TestLoggingImpl implements TestLogging {

    public void someMethodNotAnnotated() {
        System.out.println("<<< someMethodNotAnnotated() logic >>>");
    }

    @Log
    @Override
    public void someMethod1() {
        System.out.println("<<< someMethod1() logic >>>");
    }

    @Log
    @Override
    public void someMethod2(int param) {
        System.out.println("<<< someMethod2(1 param) logic >>>");
    }

    @Override
    public void someMethod2(int param1, String param2) {
        System.out.println("<<< someMethod2(2 param) logic >>>");
    }

    @Log
    @Override
    public void someMethod3(int param1, int param2) {
        System.out.println("<<< someMethod3(2 param) logic >>>");
    }

    @Log
    @Override
    public void someMethod4(int param1, int param2, int param3) {
        System.out.println("<<< someMethod4(3 param) logic >>>");
    }
}
