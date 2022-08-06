package ru.otus.tester;

import java.util.Optional;

public final class TestResult {

    private final String title;
    private final boolean success;
    private final Throwable cause;

    public TestResult(String title, Throwable cause) {
        this.title = title;
        this.success = false;
        this.cause = cause;
    }

    public TestResult(String title, boolean success) {
        this.title = title;
        this.success = success;
        this.cause = null;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Optional<Throwable> getCause() {
        return Optional.ofNullable(this.cause);
    }

    public String getTitle() {
        return this.title;
    }
}
