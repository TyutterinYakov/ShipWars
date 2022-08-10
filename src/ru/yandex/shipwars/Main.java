package ru.yandex.shipwars;

public class Main {

    public static void main(String[] args) {
        System.err.println("Привет, добро пожаловать в морской бой!");
        while (true) {
            printMenu();
            
        }
    }



    private static void printMenu() {
        System.out.println(
                "Меню: \n" +
                        "1 - создать первого игрока и заполнить игровое поле\n" +
                        "2 - создать второго игрока и заполнить игровое поле\n" +
                        "3 - начать игру\n" +
                        "4 - выйти "
        );
    }
}
