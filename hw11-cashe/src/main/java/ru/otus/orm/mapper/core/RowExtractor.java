package ru.otus.orm.mapper.core;

import java.util.List;
import java.util.Optional;

public interface RowExtractor<T> {

    List<Object> getFieldValuesWithoutId(Object object);

    Optional<Object> getId(T client);
}
