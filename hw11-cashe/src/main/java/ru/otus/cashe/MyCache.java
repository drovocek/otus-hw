package ru.otus.cashe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache;
    private final List<HwListener<K, V>> listeners;

    public MyCache() {
        this.cache = new WeakHashMap<>();
        this.listeners = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        this.cache.put(key, value);
        callListeners(key, value, Action.PUT);
    }

    @Override
    public void remove(K key) {
        V value = this.cache.get(key);
        this.cache.remove(key);
        callListeners(key, value, Action.REMOVE);
    }

    @Override
    public V get(K key) {
        V value = this.cache.get(key);
        callListeners(key, value, Action.GET);
        return value;
    }

    private void callListeners(K key, V value, Action action) {
        this.listeners.forEach(listener -> listener.notify(key, value, action.name()));
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        this.listeners.remove(listener);
    }

    private enum Action {
        GET,
        REMOVE,
        PUT
    }
}
