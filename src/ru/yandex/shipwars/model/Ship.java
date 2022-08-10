package ru.yandex.shipwars.model;

public class Ship implements Data {
    private long id;
    private int size;
    private Location location;
    private long playerId;

    public Ship(int size, Location location, long userId) {
        this.size = size;
        this.location = location;
        this.playerId = userId;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
