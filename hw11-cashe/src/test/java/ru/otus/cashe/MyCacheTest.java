package ru.otus.cashe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * VM options: -Xmx256m -Xms256m -Xlog:gc=debug
 */
@DisplayName("Класс MyCache(VM options: -Xmx256m -Xms256m -Xlog:gc=debug)")
class MyCacheTest {

    static class TestObject {
        final byte[] array = new byte[1024 * 1024];
    }

    @Test
    @DisplayName("должен очищаться при достижении лимита памяти -Xmx")
    void resetCashIfCanBeOutOfMemory() {
        HwCache<Integer, TestObject> cache = new MyCache<>();
        var size = 200;
        Map<Integer, TestObject> cacheStorage = fillCacheAndReturnStorage(cache, size);
        Assertions.assertTrue(cacheStorage.size() < size);
    }

    @Test
    @DisplayName("не должен очищаться при сборке мусора, если если лимит памяти не достигнут -Xmx")
    void notResetCashIfCanNotBeOutOfMemory() {
        HwCache<Integer, TestObject> cache = new MyCache<>();
        var size = 100;
        Map<Integer, TestObject> cacheStorage = fillCacheAndReturnStorage(cache, size);
        System.gc();
        Assertions.assertEquals(size, cacheStorage.size());
    }


    Map<Integer, TestObject> fillCacheAndReturnStorage(HwCache<Integer, TestObject> cache, int size) {
        try {
            for (var idx = 0; idx < size; idx++) {
                cache.put(idx, new TestObject());
            }

            Field cacheField = MyCache.class.getDeclaredField("cache");
            cacheField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<Integer, TestObject> cacheStorage = (Map<Integer, TestObject>) cacheField.get(cache);
            return cacheStorage;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}