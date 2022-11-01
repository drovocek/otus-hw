package otus.jdbc.mapper.impl;

import otus.jdbc.mapper.core.metadata.EntityClassMetaData;
import otus.jdbc.mapper.core.metadata.EntityClassMetaDataFabric;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityClassMetaDataFabricImpl implements EntityClassMetaDataFabric {

    private final static EntityClassMetaDataFabricImpl INSTANCE = new EntityClassMetaDataFabricImpl();

    public static EntityClassMetaDataFabric getInstance() {
        return INSTANCE;
    }

    private final Map<Class<?>, EntityClassMetaData<?>> cash = new ConcurrentHashMap<>();

    private EntityClassMetaDataFabricImpl() {
    }

    @Override

    public <T> EntityClassMetaData<T> computeIfAbsent(Class<T> entityClass) {
        @SuppressWarnings("unchecked")
        EntityClassMetaData<T> entityClassMetaData =
                (EntityClassMetaData<T>) this.cash.computeIfAbsent(entityClass, entityClassKey -> new EntityClassMetaDataImpl<>(entityClass));
        return entityClassMetaData;
    }
}
