package ru.yandex.shipwars.dao.impl;

import ru.yandex.shipwars.dao.PlayerStorage;
import ru.yandex.shipwars.model.Player;

import java.util.List;


public class PlayerStorageImpl extends AbstractStorageImpl<Player> implements PlayerStorage {
    @Override
    public void create(Player player) {
        super.create(player);
    }

    @Override
    public void update(Player player) {
        storage.get(player.getId()).setName(player.getName());
    }

    @Override
    public Player getById(long id) {
        return super.getById(id);
    }

    @Override
    public List<Player> getAll() {
        return super.getAll();
    }

    @Override
    public void deleteById(long id) {
        super.deleteById(id);
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
    }
}
