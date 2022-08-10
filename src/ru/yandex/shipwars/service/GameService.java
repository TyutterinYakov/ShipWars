package ru.yandex.shipwars.service;

public class GameService {

    private final PlayerService playerService;
    private final ShipService shipService;

    public GameService(PlayerService playerService, ShipService shipService) {
        this.playerService = playerService;
        this.shipService = shipService;
    }





}
