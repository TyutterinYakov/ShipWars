package ru.yandex.shipwars.service;

import ru.yandex.shipwars.dao.PlayerStorage;
import ru.yandex.shipwars.dao.ShipStorage;
import ru.yandex.shipwars.model.Player;


public class PlayerService extends AbstractService<PlayerStorage, Player> {

    private final ShipStorage shipStorage;

    public PlayerService(PlayerStorage storage, ShipStorage shipStorage) {
        super(storage);
        this.shipStorage = shipStorage;
    }

    @Override
    public void update(Player player) {
        storage.update(player);
    }

    @Override
    public void deleteById(long id) {
        super.deleteById(id);
        shipStorage.deleteShipsByPlayerId(id);
    }

    @Override
    public void deleteAll() {
        shipStorage.deleteAll();
        super.deleteAll();
    }
}
