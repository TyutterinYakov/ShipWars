package ru.yandex.shipwars.dao;

import ru.yandex.shipwars.model.Ship;

import java.util.List;

public interface ShipStorage extends AbstractStorage<Ship> {

    void update(Ship ship);

    List<Ship> getShipsByPlayerId(long id);

    void deleteShipsByPlayerId(long id);

}
