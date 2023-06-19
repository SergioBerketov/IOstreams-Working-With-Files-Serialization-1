package ru.netology;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static String[] products = { "Хлеб", "Молоко", "Гречка" };
    static int[] prices = { 30, 50, 80 };
    static File basketFile = new File("basket.json");
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        Basket basket = null;

        if (basketFile.exists()) {
            basket = Basket.loadFromJSONFile(basketFile);
        } else {
            basket = new Basket(products, prices);
        }

        System.out.println("Список возможных товаров для покупки:");

        for (int i = 0; i < products.length; i++) {
            System.out.printf((i + 1) + ". " +
                    products[i] + " " + prices[i] + " руб." + "\n");
        }

        ClientLog log = new ClientLog();

        while (true) {
            System.out.println("Выберите товар и количество или введите `end`" + "\n");
            String input = scanner.nextLine(); // 1 5
            if ("end".equals(input)) {
                log.exportAsCSV(new File("log.csv"));
                break;
            }

            String[] parts = input.split(" ");

            int numberOfProduct = Integer.parseInt(parts[0]) - 1;
            int productCount = Integer.parseInt(parts[1]);

            basket.addToCart(numberOfProduct, productCount);
            basket.saveJSON(basketFile);
            log.log(numberOfProduct, productCount);

            continue;
        }
        basket.printCart();

    }
}
