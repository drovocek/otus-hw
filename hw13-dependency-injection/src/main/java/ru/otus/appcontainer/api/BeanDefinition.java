package ru.otus.appcontainer.api;

import javax.annotation.Nullable;
import java.util.Objects;

public interface BeanDefinition {

    int order();

    @Nullable
    String beanName();

    @Nullable
    String beanClassName();

    @Nullable
    String[] dependsOn();

    default boolean hasConstructorArgumentValues() {
        return Objects.requireNonNull(dependsOn()).length != 0;
    }
}
