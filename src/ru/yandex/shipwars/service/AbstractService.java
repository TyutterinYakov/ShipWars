package ru.yandex.shipwars.service;

import ru.yandex.shipwars.dao.AbstractStorage;
import ru.yandex.shipwars.model.Data;

import java.util.List;

public abstract class AbstractService<T extends AbstractStorage<E>, E extends Data> {

    protected final T storage;

    public AbstractService(T storage) {
        this.storage = storage;
    }

    public abstract void update(E e);

    public void create(E e) {
        storage.create(e);
    }

    public E getById(long id) {
        return storage.getById(id);
    }

    public List<E> getAll() {
        return storage.getAll();
    }

    public void deleteById(long id) {
        storage.deleteById(id);
    }

    public void deleteAll() {
        storage.deleteAll();
    }

}
