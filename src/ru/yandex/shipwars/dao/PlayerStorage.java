package ru.yandex.shipwars.dao;

import ru.yandex.shipwars.model.Player;

public interface PlayerStorage extends AbstractStorage<Player> {
    void update(Player player);
}
