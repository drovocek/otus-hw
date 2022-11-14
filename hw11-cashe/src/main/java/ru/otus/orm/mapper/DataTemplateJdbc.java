package ru.otus.orm.mapper;

import ru.otus.orm.mapper.core.RowExtractor;
import ru.otus.orm.mapper.core.RowMapper;
import ru.otus.orm.mapper.core.exceptions.SQLExecutionException;
import ru.otus.orm.mapper.core.metadata.EntityClassMetaData;
import ru.otus.orm.mapper.core.metadata.EntitySQLMetaData;
import ru.otus.orm.mapper.impl.EntityClassMetaDataFabricImpl;
import ru.otus.orm.mapper.impl.EntitySQLMetaDataImpl;
import ru.otus.orm.mapper.impl.RowExtractorImpl;
import ru.otus.orm.mapper.impl.RowMapperImpl;
import ru.otus.orm.otus.core.repository.DataTemplate;
import ru.otus.orm.otus.core.repository.executor.DbExecutor;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.otus.orm.mapper.core.exceptions.SQLExecutionException.HAS_ID;
import static ru.otus.orm.mapper.core.exceptions.SQLExecutionException.HAS_NOT_ID;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final RowMapper<T> rowMapper;
    private final RowExtractor<T> rowExtractor;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, RowMapper<T> rowMapper, RowExtractor<T> rowExtractor) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.rowMapper = rowMapper;
        this.rowExtractor = rowExtractor;
    }

    public DataTemplateJdbc(DbExecutor dbExecutor, Class<T> entityClass) {
        this.dbExecutor = dbExecutor;
        EntityClassMetaData<T> classMetaDataManager = EntityClassMetaDataFabricImpl.getInstance().computeIfAbsent(entityClass);
        this.entitySQLMetaData = new EntitySQLMetaDataImpl(classMetaDataManager);
        this.rowMapper = new RowMapperImpl<>(classMetaDataManager);
        this.rowExtractor = new RowExtractorImpl<>(classMetaDataManager);
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return this.dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), this.rowMapper::mapRow);
    }

    @Override
    public List<T> findAll(Connection connection) {
        return this.dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(),
                Collections.emptyList(), this.rowMapper::mapRows).orElse(Collections.emptyList());
    }

    @Override
    public long insert(Connection connection, T client) {
        Optional<Object> idOpt = this.rowExtractor.getId(client);
        if (idOpt.isPresent()) {
            throw new SQLExecutionException(HAS_ID);
        }
        List<Object> values = this.rowExtractor.getFieldValuesWithoutId(client);
        return this.dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), values);
    }

    @Override
    public void update(Connection connection, T client) {
        Optional<Object> idOpt = this.rowExtractor.getId(client);
        if (idOpt.isEmpty()) {
            throw new SQLExecutionException(HAS_NOT_ID);
        }
        List<Object> values = this.rowExtractor.getFieldValuesWithoutId(client);
        values.add(idOpt.get());
        this.dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), values);
    }
}
