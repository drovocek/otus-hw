package otus.jdbc.mapper.impl;

import otus.jdbc.mapper.core.EntityClassMetaData;
import otus.jdbc.mapper.core.EntitySQLMetaData;

import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> metaData;
    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSqlSql;
    private final String updateSqlSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> metaData) {
        this.metaData = metaData;
        this.selectAllSql = "SELECT * FROM %s".formatted(this.metaData.getName());
        this.selectByIdSql = "SELECT * FROM %s WHERE id=?".formatted(this.metaData.getName());

        String values = this.metaData.getFieldsWithoutId().stream()
                .map(field -> field.getName().concat("=?"))
                .collect(Collectors.joining(","));

        this.insertSqlSql = "INSERT INTO %s VALUES(%s)".formatted(this.metaData.getName(), values);
        this.updateSqlSql = "UPDATE %s SET %s WHERE id=?".formatted(this.metaData.getName(), values);
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
