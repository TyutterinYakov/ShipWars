package ru.yandex.shipwars.service;

import ru.yandex.shipwars.dao.PlayerStorage;
import ru.yandex.shipwars.dao.ShipStorage;
import ru.yandex.shipwars.model.Player;
import ru.yandex.shipwars.model.Ship;

public class ShipService extends AbstractService<ShipStorage, Ship> {

    private final PlayerStorage playerStorage;

    public ShipService(ShipStorage storage, PlayerStorage playerStorage) {
        super(storage);
        this.playerStorage = playerStorage;
    }

    @Override
    public void update(Ship ship) {
        storage.update(ship);
    }

    @Override
    public void create(Ship ship) {
        getPlayerById(ship.getPlayerId());
        super.create(ship);
    }

    private Player getPlayerById(long id) {
        Player player = playerStorage.getById(id);
        if (player == null) {
            throw new RuntimeException();
        }
        return player;
    }
}
