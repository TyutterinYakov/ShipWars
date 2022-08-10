package ru.yandex.shipwars.dao.impl;

import ru.yandex.shipwars.dao.AbstractStorage;
import ru.yandex.shipwars.model.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractStorageImpl<T extends Data> implements AbstractStorage<T> {

    protected final Map<Long, T> storage = new HashMap<>();
    private long generatorId = 1L;

    @Override
    public void create(T t) {
        t.setId(generatorId++);
        storage.put(t.getId(), t);
    }

    @Override
    public T getById(long id) {
        return storage.get(id);
    }

    @Override
    public List<T> getAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }

    @Override
    public void deleteAll() {
        generatorId = 0L;
        storage.clear();
    }
}
