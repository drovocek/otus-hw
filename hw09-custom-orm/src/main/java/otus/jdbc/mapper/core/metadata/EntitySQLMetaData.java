package otus.jdbc.mapper.core.metadata;

/**
 * Создает SQL - запросы
 */
public interface EntitySQLMetaData {
    String getSelectAllSql();

    String getSelectByIdSql();

    String getInsertSql();

    String getUpdateSql();
}
