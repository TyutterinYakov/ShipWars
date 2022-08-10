package ru.yandex.shipwars.dao;

import ru.yandex.shipwars.model.Data;

import java.util.List;

public interface AbstractStorage<T extends Data> {

    void create(T t);

    T getById(long id);

    List<T> getAll();

    void deleteById(long id);

    void deleteAll();

}
