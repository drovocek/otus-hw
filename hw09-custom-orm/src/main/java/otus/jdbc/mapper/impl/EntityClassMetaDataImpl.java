package otus.jdbc.mapper.impl;

import otus.jdbc.mapper.core.metadata.EntityClassMetaData;
import otus.jdbc.mapper.core.metadata.Id;
import otus.jdbc.mapper.core.exceptions.MetaDataExtractionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static otus.jdbc.mapper.core.exceptions.MetaDataExtractionException.NOT_CONSTRUCTOR_WITH_NO_ARGUMENTS;
import static otus.jdbc.mapper.core.exceptions.MetaDataExtractionException.NO_ID_ANNOTATION;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        this.name = entityClass.getSimpleName();
        this.constructor = MetaDataExtractor.extractConstructor(entityClass);
        this.idField = MetaDataExtractor.extractIdField(entityClass);
        this.allFields = MetaDataExtractor.extractAllFields(entityClass);
        this.fieldsWithoutId = MetaDataExtractor.extractFieldsWithoutId(entityClass);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return this.constructor;
    }

    @Override
    public Field getIdField() {
        return this.idField;
    }

    @Override
    public List<Field> getAllFields() {
        return this.allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.fieldsWithoutId;
    }

    private static class MetaDataExtractor {

        public static <T> Constructor<T> extractConstructor(Class<T> entityClass) {
            try {
                return entityClass.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new MetaDataExtractionException(NOT_CONSTRUCTOR_WITH_NO_ARGUMENTS.formatted(entityClass));
            }
        }

        public static <T> Field extractIdField(Class<T> entityClass) {
            return Arrays.stream(entityClass.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Id.class))
                    .findFirst()
                    .orElseThrow(() -> new MetaDataExtractionException(NO_ID_ANNOTATION.formatted(entityClass)));
        }

        public static <T> List<Field> extractAllFields(Class<T> entityClass) {
            return Arrays.stream(entityClass.getDeclaredFields()).toList();
        }

        public static <T> List<Field> extractFieldsWithoutId(Class<T> entityClass) {
            return Arrays.stream(entityClass.getDeclaredFields())
                    .filter(field -> !field.isAnnotationPresent(Id.class))
                    .toList();
        }
    }
}
