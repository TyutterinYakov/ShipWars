package ru.yandex.shipwars.service;

import ru.yandex.shipwars.dao.GameStorage;
import ru.yandex.shipwars.model.*;

import java.util.List;
import java.util.Scanner;

public class GameService {

    private final PlayerService playerService;
    private final ShipService shipService;
    private final GameStorage gameStorage;
    private final Scanner scanner;
    private static final int COUNT_PLAYERS = 2;
    private static final int SHIP_FOUR_CELL = 1;
    private static final int SHIP_THREE_CELL = 2;
    private static final int SHIP_TWO_CELL = 3;
    private static final int SHIP_ONE_CELL = 4;

    public GameService(PlayerService playerService, ShipService shipService, GameStorage gameStorage, Scanner scanner) {
        this.playerService = playerService;
        this.shipService = shipService;
        this.gameStorage = gameStorage;
        this.scanner = scanner;
    }

    public void createPlayer() {
        if (playerService.getAll().size() == COUNT_PLAYERS) {
            System.out.println("В игре достаточно игроков, чтобы начать игру");
            return;
        }
        System.out.println("Введите имя игрока");
        String name = scanner.nextLine();
        Player player = new Player(name);
        playerService.create(player);
        arrangeShips(player);
    }

    public void startGame() {
        if (playerService.getAll().size() != COUNT_PLAYERS) {
            System.out.println("Не все игроки созданы, для начала создайте их");
        }
    }


    private void arrangeShips(Player player) { //TODO Заполнить игровое поле
        int countShips = SHIP_FOUR_CELL+SHIP_THREE_CELL+SHIP_TWO_CELL+SHIP_ONE_CELL;
        PlayingField playingField = new PlayingField(player.getId(), countShips);
        gameStorage.createPlayingField(playingField);
        installationShip(SHIP_FOUR_CELL, playingField, player);
//        installationShip(SHIP_THREE_CELL, playingField, player);
//        installationShip(SHIP_TWO_CELL, playingField, player);
//        installationShip(SHIP_ONE_CELL, playingField, player);
    }

    private void installationShip(int cellShip, PlayingField playingField, Player player) {
        //установка корабля на соответсвующую позицию
        while (true) {
            System.out.println("На какой клетке будет находиться нос корабля (формат ввода A1(англ язык))");
            String cell = scanner.nextLine().trim();
            if (cell.length() != 2) {
                System.out.println("Некорректный формат ввода");
                continue;
            }
            int verticalPosition;
            LetterField letterField;
            Location location;
            try {
                letterField = LetterField.valueOf(String.valueOf(cell.charAt(0)));
                verticalPosition = Integer.parseInt(String.valueOf(cell.charAt(1)));
                if (verticalPosition > playingField.getSIZE_VERTICAL()) {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректный формат ввода");
                continue;
            }
            System.out.println("В какой плоскости будет расположен корабль:\n" +
                    "1 - горизонтальное\n" +
                    "2 - вертикальное\n");
            switch (scanner.nextLine().trim()) {
                case "1": {
                    location = Location.HORIZONTAL;
                    break;
                } case "2": {
                    location = Location.VERTICAL;
                    break;
                } default: {
                    System.out.println("Есть только два положения");
                    continue;
                }
            }

            Ship ship = new Ship(cellShip, location, player.getId());
            checkCollision(ship, verticalPosition, letterField, playingField);

        }
    }


    private boolean checkCollision(Ship ship, int verticalPosition, LetterField letterField, PlayingField playingField) {
        if (letterField.ordinal() == 0) {

        }

        if (letterField.ordinal() == LetterField.values().length-1) {

        }

        List<CellField> startCell = playingField.getFields().get(letterField);
        for (int i = verticalPosition; i <= ship.getSize(); i++) {
            if(!startCell.get(i).isAvailable()) {
                return false;
            }
        }
//        for(int i = verticalPosition; )


        return true;
    }




    public void printGameField(long id) {
        PlayingField playingField = gameStorage.getPlayingFiled(id);
        for (LetterField letterField : LetterField.values()) {
            for (int i = 0; i < playingField.getSIZE_VERTICAL(); i++) {
                CellField cellField = playingField.getFields().get(letterField).get(i);
                if (cellField.getShip() != null) {
                    System.out.println(" ");
                } else {
                    System.out.println("+");
                }
            }
        }
    }
}
