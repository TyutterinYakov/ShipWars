package ru.yandex.shipwars.dao.impl;

import ru.yandex.shipwars.dao.ShipStorage;
import ru.yandex.shipwars.model.Ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipStorageImpl extends AbstractStorageImpl<Ship> implements ShipStorage {

    private final Map<Long, List<Ship>> shipsUserId = new HashMap<>();

    @Override
    public void create(Ship ship) {
        super.create(ship);
        long userId = ship.getPlayerId();
        List<Ship> shipsUser = shipsUserId.computeIfAbsent(userId, (ships) -> new ArrayList<>());
        shipsUser.add(ship);
    }

    @Override
    public void update(Ship ship) {
        Ship updateShip = storage.get(ship.getId());
        updateShip.setLocation(ship.getLocation());
    }

    @Override
    public List<Ship> getShipsByPlayerId(long id) {
        return shipsUserId.get(id);
    }

    @Override
    public void deleteShipsByPlayerId(long id) {
        List<Ship> removeShips = shipsUserId.remove(id);
        if(removeShips != null) {
            removeShips.stream().map(Ship::getId).forEach(storage::remove);
        }
    }

    @Override
    public void deleteById(long id) {
        long userId = storage.get(id).getPlayerId();
        shipsUserId.remove(userId);
        super.deleteById(id);
    }

    @Override
    public void deleteAll() {
        shipsUserId.clear();
        super.deleteAll();
    }

}
