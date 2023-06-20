package ru.netology;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static String[] products = { "Хлеб", "Молоко", "Гречка" };
    static int[] prices = { 30, 50, 80 };
    //static File basketFile = new File("basket.json");
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        //как проверить читается ли наш файл XML или нет:
        XMLReader settings = new XMLReader(new File("shop.xml"));
        File loadFile = new File(settings.loadFile);
        File saveFile = new File(settings.loadFile);
        File logFile = new File(settings.loadFile);

        //эту логику лучше вынести в отдельный метод
        Basket basket = createBasket(loadFile, settings.isLoad, settings.loadFormat);
        ClientLog log = new ClientLog();

        System.out.println("Список возможных товаров для покупки:");
        for (int i = 0; i < products.length; i++) {
            System.out.printf((i + 1) + ". " +
                    products[i] + " " + prices[i] + " руб." + "\n");
        }

        while (true) {

            System.out.println("Выберите товар и количество или введите `end`" + "\n");
            String input = scanner.nextLine(); // 1 5
            if ("end".equals(input)) {
                if (settings.isLog){
                    log.exportAsCSV(logFile);
                }
                break;
            }

            String[] parts = input.split(" ");
            int numberOfProduct = Integer.parseInt(parts[0]) - 1;
            int productCount = Integer.parseInt(parts[1]);
            basket.addToCart(numberOfProduct, productCount);
            if (settings.isLog){
                log.log(numberOfProduct, productCount);
            }
            if (settings.isSave) {
                switch (settings.saveFormat){
                    case "json" -> basket.saveJSON(saveFile);
                    case "txt" -> basket.saveTxt(saveFile);
                }
            }
        }
        basket.printCart();
    }

    private static Basket createBasket(File loadFile, boolean isLoad, String loadFormat) {
        Basket basket;
        if (isLoad && loadFile.exists()) {
            basket = switch (loadFormat) {
                case "json" -> Basket.loadFromJSONFile(loadFile);
                case "txt" -> Basket.loadFromTxtFile(loadFile);
                default -> new Basket(products, prices);
            };
        } else {
            basket = new Basket(products, prices);
        }
        return basket;
    }
}
