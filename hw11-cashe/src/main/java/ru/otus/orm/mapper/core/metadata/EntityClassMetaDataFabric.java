package ru.otus.orm.mapper.core.metadata;

public interface EntityClassMetaDataFabric {

    <T> EntityClassMetaData<T> computeIfAbsent(Class<T> entityClass);
}
