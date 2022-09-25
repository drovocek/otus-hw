package otus.jdbc.mapper.impl;

import otus.jdbc.mapper.core.EntityClassMetaData;
import otus.jdbc.mapper.core.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> metaData;
    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSqlSql;
    private final String updateSqlSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> metaData) {
        this.metaData = metaData;

        String tableName = this.metaData.getName();

        this.selectAllSql = "SELECT * FROM %s".formatted(tableName);
        this.selectByIdSql = "SELECT * FROM %s WHERE id=?".formatted(tableName);

        String fieldNames = this.metaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        String values = this.metaData.getFieldsWithoutId().stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        this.insertSqlSql = "INSERT INTO %s(%s) VALUES (%s)".formatted(tableName, fieldNames, values);
        this.updateSqlSql = "UPDATE %s(%s) SET (%s) WHERE id=?".formatted(tableName, fieldNames, values);

        System.out.println(this.selectAllSql);
        System.out.println(this.selectByIdSql);
        System.out.println(this.insertSqlSql);
        System.out.println(this.updateSqlSql);
    }


    @Override
    public String getSelectAllSql() {
        return this.selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return this.selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return this.insertSqlSql;
    }

    @Override
    public String getUpdateSql() {
        return this.updateSqlSql;
    }
}
