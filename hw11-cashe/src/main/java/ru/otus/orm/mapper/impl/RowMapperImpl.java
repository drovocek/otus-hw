package ru.otus.orm.mapper.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.orm.mapper.core.RowMapper;
import ru.otus.orm.mapper.core.exceptions.RowMappingException;
import ru.otus.orm.mapper.core.metadata.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapperImpl<T> implements RowMapper<T> {

    private static final Logger logger = LoggerFactory.getLogger(RowMapperImpl.class);

    private final EntityClassMetaData<T> entityClassMetaData;

    public RowMapperImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public T mapRow(ResultSet rs) {
        try {
            Constructor<T> constructor = this.entityClassMetaData.getConstructor();
            T object = constructor.newInstance();
            rs.next();
            entityClassMetaData.getAllFields().forEach(field -> {
                try {
                    field.setAccessible(true);
                    field.set(object, rs.getObject(field.getName()));
                } catch (IllegalAccessException | SQLException e) {
                    throw new RowMappingException(e);
                }
            });
            logger.info("mapRow OUT: {}", object);
            return object;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | SQLException e) {
            throw new RowMappingException(e);
        }
    }

    @Override
    public List<T> mapRows(ResultSet rs) {
        List<T> result = new ArrayList<>();
        try {
            while (rs.next()) {
                T object = mapRow(rs);
                result.add(object);
            }
        } catch (SQLException e) {
            throw new RowMappingException(e);
        }
        logger.info("mapRows OUT: {}", result);
        return result;
    }
}
