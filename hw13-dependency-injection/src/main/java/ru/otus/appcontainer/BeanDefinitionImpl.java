package ru.otus.appcontainer;

import ru.otus.appcontainer.api.BeanDefinition;

public record BeanDefinitionImpl
        (String beanName, String beanClassName, String[] dependsOn, int order)
        implements BeanDefinition {
}
