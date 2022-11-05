package ru.otus.orm.mapper.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.orm.mapper.core.RowExtractor;
import ru.otus.orm.mapper.core.exceptions.RowExtractionException;
import ru.otus.orm.mapper.core.metadata.EntityClassMetaData;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class RowExtractorImpl<T> implements RowExtractor<T> {

    private static final Logger logger = LoggerFactory.getLogger(RowExtractorImpl.class);

    private final EntityClassMetaData<T> entityClassMetaData;

    public RowExtractorImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public List<Object> getFieldValuesWithoutId(Object object) {
        logger.info("getFieldValues IN: {}", object);
        List<Object> result = this.entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return field.get(object);
                    } catch (IllegalAccessException e) {
                        throw new RowExtractionException(e);
                    }
                })
                .toList();
        logger.info("getFieldValues OUT: {}", result);
        return result;
    }

    @Override
    public Optional<Object> getId(T object) {
        logger.info("getId IN: {}", object);
        try {
            Field field = this.entityClassMetaData.getIdField();
            field.setAccessible(true);
            Object value = field.get(object);
            logger.info("getId OUT: {}", value);
            return Optional.ofNullable(value);
        } catch (IllegalAccessException e) {
            throw new RowExtractionException(e);
        }
    }
}
