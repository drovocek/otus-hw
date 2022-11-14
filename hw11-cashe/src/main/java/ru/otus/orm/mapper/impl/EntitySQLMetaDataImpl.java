package ru.otus.orm.mapper.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.orm.mapper.core.metadata.EntityClassMetaData;
import ru.otus.orm.mapper.core.metadata.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private static final Logger logger = LoggerFactory.getLogger(EntitySQLMetaDataImpl.class);

    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSqlSql;
    private final String updateSqlSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> metaData) {
        String tableName = metaData.getName();
        String idFieldName = metaData.getIdField().getName();

        this.selectAllSql = "SELECT * FROM %s".formatted(tableName);
        this.selectByIdSql = "SELECT * FROM %s WHERE %s=?".formatted(tableName, idFieldName);

        String fieldNames = metaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        String values = metaData.getFieldsWithoutId().stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        this.insertSqlSql = "INSERT INTO %s(%s) VALUES (%s)".formatted(tableName, fieldNames, values);
        this.updateSqlSql = "UPDATE %s(%s) SET (%s) WHERE %s=?".formatted(tableName, fieldNames, values, idFieldName);
    }


    @Override
    public String getSelectAllSql() {
        logger.info("getSelectAllSql OUT: {}", this.selectAllSql);
        return this.selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        logger.info("getSelectByIdSql OUT: {}", this.selectByIdSql);
        return this.selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        logger.info("getInsertSql OUT: {}", this.insertSqlSql);
        return this.insertSqlSql;
    }

    @Override
    public String getUpdateSql() {
        logger.info("getUpdateSql OUT: {}", this.updateSqlSql);
        return this.updateSqlSql;
    }
}
