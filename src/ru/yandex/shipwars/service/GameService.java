package ru.yandex.shipwars.service;

import ru.yandex.shipwars.dao.GameStorage;
import ru.yandex.shipwars.model.*;

import java.util.*;

public record GameService(PlayerService playerService,
                          ShipService shipService,
                          GameStorage gameStorage, Scanner scanner) {

    private static final int COUNT_PLAYERS = 2;
    private static final int SHIP_FOUR_CELL = 4;
    private static final int SHIP_THREE_CELL = 3;
    private static final int SHIP_TWO_CELL = 2;
    private static final int SHIP_ONE_CELL = 1;

    public void createPlayer() { //Создание игрока
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
        Player player1 = playerService.getAll().get(0);
        Player player2 = playerService.getAll().get(1);
        while (true) {
            if(gunPlayer(player1, player2) || gunPlayer(player2, player1)) {
                break;
            }
        }
    }


    private boolean gunPlayer(Player player1, Player player2) {
        System.out.println("Стреляет игрок: " + player1.getName());
        PlayingField playingField = gameStorage.getPlayingFieldByUserId(player2.getId());
        printGameFieldPlay(playingField);
        String cell = scanner.nextLine().trim();
        if (cell.length() < 2) {
            System.out.println("Некорректный формат ввода");
            return false;
        }
        int verticalPosition;
        LetterField letterField;
        CellField cellField;
        try {
            letterField = LetterField.valueOf(String.valueOf(cell.charAt(0)));
            verticalPosition = Integer.parseInt(cell.substring(1))-1;
            if (verticalPosition > 9 || //TODO
                    verticalPosition < 0) {
                throw new IllegalArgumentException();
            }
            cellField = playingField.getFields().get(letterField).get(verticalPosition);
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный формат ввода");
            return false;
        }
        if (cellField.isShot()) {
            System.out.println("Вы уже стреляли в эту цель. Выстрел не имел смысла");
            return false;
        }
        Ship ship = cellField.getShip();
        if (ship != null) {
            ship.setHealth(ship.getHealth() - 1);
            if (ship.getHealth() <= 0) {
                playingField.setCountShip(playingField.getCountShip() - 1);
                for (CellField shipCell : ship.getCells()) {
                    shipCell.setShot(true);
                }
                System.err.println("Убил");
                if (playingField.getCountShip() <= 0) {
                    System.err.println("С победой, игрок!");
                    return true;
                }
            }
            System.err.println("Попал!");
            cellField.setShot(true);
            gunPlayer(player1, player2);
        } else {
            System.err.println("Мимо!");
        }
        cellField.setShot(true);
        printGameFieldPlay(playingField);
        return false;
    }


    private void arrangeShips(Player player) { //Заполнение игрового поля
//        int countShips = SHIP_FOUR_CELL + SHIP_THREE_CELL + SHIP_TWO_CELL + SHIP_ONE_CELL;
        int countShips = 1; //TODO количество кораблей
        PlayingField playingField = new PlayingField(player.getId(), countShips);
        gameStorage.createPlayingField(playingField);
        installationShip(SHIP_FOUR_CELL, playingField, player);
//        for (int i = 0; i < 2; i++) {
//            installationShip(SHIP_THREE_CELL, playingField, player);
//        }
//        for (int i = 0; i < 3; i++) {
//            installationShip(SHIP_TWO_CELL, playingField, player);
//        }
//        for (int i = 0; i < 4; i++) {
//            installationShip(SHIP_ONE_CELL, playingField, player);
//        }
//        printGameFieldCreate(playingField.getId());

        for(int i = 0; i < 20; i++) { //Пропуск, чтобы не видеть, что вводил прошлый игрок
            System.out.println();
        }

    }

    private void installationShip(int cellShip, PlayingField playingField, Player player) {
        //установка корабля на соответсвующую позицию
        while (true) {
            System.out.println("На какой клетке будет находиться нос корабля (формат ввода A1(англ язык)) -" +
                    " корабль в " + cellShip + " клетки");
            String cell = scanner.nextLine().trim();
            if (cell.length() < 2) {
                System.out.println("Некорректный формат ввода");
                continue;
            }
            int verticalPosition;
            LetterField letterField;
            Location location;
            try {
                letterField = LetterField.valueOf(String.valueOf(cell.charAt(0)));
                verticalPosition = Integer.parseInt(cell.substring(1));
                if (verticalPosition > playingField.getSIZE_VERTICAL() ||
                        verticalPosition < 1) {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректный формат ввода");
                continue;
            }
            System.out.println("""
                    В какой плоскости будет расположен корабль:
                    1 - горизонтальной
                    2 - вертикальной
                    """);
            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    if (letterField.ordinal() + cellShip > playingField.getSIZE_HORIZONTAL()) {
                        System.out.println("Корабль выходит за пределы поля по горизонтали");
                        continue;
                    }
                    location = Location.HORIZONTAL;
                }
                case "2" -> {
                    if (verticalPosition + cellShip > playingField.getSIZE_VERTICAL()) {
                        System.out.println("Корабль выходит за пределы поля по вертикали");
                        continue;
                    }
                    location = Location.VERTICAL;
                }
                default -> {
                    System.out.println("Есть только два положения");
                    continue;
                }
            }

            Ship ship = new Ship(cellShip, location, player.getId());
            if (!checkCollision(ship, verticalPosition - 1, playingField, letterField)) {
                System.out.println("Корабль пересекается с соседним");
                continue;
            }
            printGameFieldCreate(playingField.getId());
            break;

        }
    }


    private boolean checkCollision(Ship ship, int verticalPosition, PlayingField playingField, LetterField letterField) {
        List<CellField> cellsShip = new ArrayList<>();
        Location location = ship.getLocation();
        int occupiedCells = verticalPosition + ship.getSize();
        if (location.equals(Location.VERTICAL)) {
            for (int i = verticalPosition; i < occupiedCells; i++) {
                CellField cellField = playingField.getFields().get(letterField).get(i);
                if (!cellField.isAvailable()) {
                    return false;
                }
                cellsShip.add(cellField);
            }
        } else {
            int count = letterField.ordinal();
            for (int i = verticalPosition; i < occupiedCells; i++) {
                CellField cellField = playingField.getFields().get(LetterField.values()[count++]).get(verticalPosition);
                if (!cellField.isAvailable()) {
                    return false;
                }
                cellsShip.add(cellField);
            }
        }

        setCellsShip(cellsShip, playingField, ship);
        return true;
    }


    private void setCellsShip(List<CellField> cellsShip, PlayingField playingField, Ship ship) {
        final int sizeHorizontal = playingField.getSIZE_HORIZONTAL();
        final int sizeVertical = playingField.getSIZE_VERTICAL();
        Map<LetterField, List<CellField>> fields = playingField.getFields();


        for (CellField cellField : cellsShip) {
            cellField.setAvailable(false);
            cellField.setShip(ship);
            int horizontalPosition = cellField.getHorizontalPosition();
            int verticalPosition = cellField.getVerticalPosition();
            if (horizontalPosition - 1 >= 0) { //Левая граница
                CellField cellFieldLeft = fields
                        .get(LetterField.values()[horizontalPosition - 1])
                        .get(verticalPosition);
                cellFieldLeft.setAvailable(false);
                ship.getCells().add(cellFieldLeft);

                if (verticalPosition + 1 < sizeVertical) { //Левая нижняя граница
                    CellField cellFieldLeftDown = fields
                            .get(LetterField.values()[horizontalPosition - 1])
                            .get(verticalPosition + 1);
                    cellFieldLeftDown.setAvailable(false);
                    ship.getCells().add(cellFieldLeftDown);
                }

                if (verticalPosition - 1 >= 0) { //Левая верхняя граница
                    CellField cellFieldLeftUp = fields
                            .get(LetterField.values()[horizontalPosition - 1])
                            .get(verticalPosition - 1);
                    cellFieldLeftUp.setAvailable(false);
                    ship.getCells().add(cellFieldLeftUp);
                }
            }

            if (horizontalPosition + 1 < sizeHorizontal) { //Правая граница
                CellField cellFieldRight = fields
                        .get(LetterField.values()[horizontalPosition + 1])
                        .get(verticalPosition);
                cellFieldRight.setAvailable(false);
                ship.getCells().add(cellFieldRight);

                if (verticalPosition + 1 < sizeVertical) { //Правая нижняя граница
                    CellField cellFieldRightDown = fields
                            .get(LetterField.values()[horizontalPosition + 1])
                            .get(verticalPosition + 1);
                    cellFieldRightDown.setAvailable(false);
                    ship.getCells().add(cellFieldRightDown);
                }

                if (verticalPosition - 1 >= 0) { //Правая верхняя граница
                    CellField cellFieldRightUp = fields
                            .get(LetterField.values()[horizontalPosition + 1])
                            .get(verticalPosition - 1);
                    cellFieldRightUp.setAvailable(false);
                    ship.getCells().add(cellFieldRightUp);
                }

            }

            if (verticalPosition - 1 >= 0) { //Верхняя граница
                CellField cellFieldUp = fields.get(LetterField.values()[horizontalPosition]).get(verticalPosition - 1);
                cellFieldUp.setAvailable(false);
                ship.getCells().add(cellFieldUp);
            }

            if (verticalPosition + 1 < sizeVertical) { //Нижняя граница
                CellField cellFieldDown = fields.get(LetterField.values()[horizontalPosition]).get(verticalPosition + 1);
                cellFieldDown.setAvailable(false);
                ship.getCells().add(cellFieldDown);
            }
        }



    }

    public void printGameFieldCreate(long id) {
        PlayingField playingField = gameStorage.getPlayingFiled(id);
        System.out.print("   ");
        Arrays.stream(LetterField.values()).forEach((n) -> System.out.print(" " + n + " "));
        for (int i = 0; i < playingField.getSIZE_VERTICAL(); i++) {
            System.out.println();
            if (playingField.getSIZE_VERTICAL() - 1 != i) {
                System.out.print(" ");
            }
            System.out.print(i + 1 + " ");
            for (LetterField letterField : LetterField.values()) {
                CellField cellField = playingField.getFields().get(letterField).get(i);
                if (cellField.getShip() == null) {
                    if (cellField.isAvailable()) {
                        System.out.print("   ");
                    } else {
                        System.out.print(" * ");
                    }

                } else {
                    System.out.print(" # ");
                }
            }
        }
        System.out.println();
    }

    public void printGameFieldPlay(PlayingField playingField) {
        System.out.print("   ");
        Arrays.stream(LetterField.values()).forEach((n) -> System.out.print(" " + n + " "));
        for (int i = 0; i < playingField.getSIZE_VERTICAL(); i++) {
            System.out.println();
            if (playingField.getSIZE_VERTICAL() - 1 != i) {
                System.out.print(" ");
            }
            System.out.print(i + 1 + " ");
            for (LetterField letterField : LetterField.values()) {
                CellField cellField = playingField.getFields().get(letterField).get(i);
                if (cellField.isShot()) {
                    Ship ship = cellField.getShip();
                    if (ship != null) {
                        System.out.print(" + ");
//                        if (ship.getHealth() <= 0) {
//                            for (CellField cell : ship.getCells()) {
//                                cell.setShot(true);
//                            }
//                        }
                    } else {
                        System.out.print(" - ");
                    }
                } else {
                    System.out.print("   ");
                }
            }
        }
        System.out.println();
    }
}
