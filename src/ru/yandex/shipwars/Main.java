package ru.yandex.shipwars;

import ru.yandex.shipwars.dao.impl.GameStorageImpl;
import ru.yandex.shipwars.dao.impl.PlayerStorageImpl;
import ru.yandex.shipwars.dao.impl.ShipStorageImpl;
import ru.yandex.shipwars.service.GameService;
import ru.yandex.shipwars.service.PlayerService;
import ru.yandex.shipwars.service.ShipService;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final GameService gameService = new GameService(
            new PlayerService(new PlayerStorageImpl(), new ShipStorageImpl()),
            new ShipService(new ShipStorageImpl(), new PlayerStorageImpl()),
            new GameStorageImpl(),
            scanner);

    public static void main(String[] args) {
        System.err.println("Привет, добро пожаловать в морской бой!");
        while (true) {
            printMenu();
            String button = scanner.nextLine().trim();
            switch (button) {
                case "1": {
                    System.out.println("..."); //TODO можно было бы добавить настройку по количество кораблей
                  break;
                } case "2": {
                    gameService.createPlayer();
                    break;
                } case "3": {
                    gameService.startGame();
                    break;
                } case "4": {

                    break;
                } default: {
                    System.out.println("Такой комманды нет");
                }

            }
        }
    }


    private static void printMenu() {
        System.out.println(
                "Меню: \n" +
                        "1 - настройки игры\n" +
                        "2 - создать нового игрока и заполнить игровое поле\n" +
                        "3 - начать игру\n" +
                        "4 - выйти "
        );
    }
}
