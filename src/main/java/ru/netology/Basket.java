package ru.netology;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Arrays;

public class Basket {

    protected String[] products;
    protected int[] prices;
    protected int[] inputedAmountProducts;
    protected int[] allCategoriesCost;
    protected int totalPrice = 0;

    public Basket() {
    }

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        this.inputedAmountProducts = new int[products.length];
        this.allCategoriesCost = new int[prices.length];
    }

    public void addToCart(int numberOfProduct, int productCount) {
        int currentPrice = prices[numberOfProduct];
        int sum = productCount * currentPrice;
        this.inputedAmountProducts[numberOfProduct] += productCount;
        this.allCategoriesCost[numberOfProduct] += sum;
        totalPrice += sum;
    }

    public void printCart() {
        System.out.println("Ваша корзина: ");

        for (int i = 0; i < products.length; i++) {
            if (inputedAmountProducts[i] == 0) {
                continue;
            } else {
                System.out.println(products[i] + " " + inputedAmountProducts[i] + " шт. " + prices[i] +
                        " руб/шт. " + allCategoriesCost[i] + " рублей в сумме");
            }
        }
        System.out.println("И того: " + totalPrice);
    }

    public void saveTxt(File textFile) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            for (String product : products) {
                out.print(product + " ");
            }
            out.println();

            for (int price : prices) {
                out.print(price + " ");
            }
            out.println();

            for (int imputed : inputedAmountProducts) {
                out.print(imputed + " ");
            }
            out.println();

            for (int catCost : allCategoriesCost) {
                out.print(catCost + " ");
            }
        }
    }

    static Basket loadFromTxtFile(File textFile){

        Basket basket = new Basket();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(textFile))) {

          String productsStr = bufferedReader.readLine();
          String pricesStr = bufferedReader.readLine();
          String inputedAmountProductsStr = bufferedReader.readLine();
          String allCategoriesCostStr = bufferedReader.readLine();

          basket.products = productsStr.split(" ");

          basket.prices = Arrays.stream(pricesStr.split(" "))
                  .map(Integer::parseInt)
                  .mapToInt(Integer::intValue)
                  .toArray();

            basket.inputedAmountProducts = Arrays.stream(inputedAmountProductsStr.split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(Integer::intValue)
                    .toArray();

            basket.allCategoriesCost = Arrays.stream(allCategoriesCostStr.split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(Integer::intValue)
                    .toArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return basket;
    }

    public void saveJSON(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
                          //(!ЗАПОМНИТЬ,но строку нужно собирать так же как в loadFromJSON)
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this);
            writer.print(json);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromJSONFile(File file) {
        Basket basket = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            StringBuilder builder = new StringBuilder();
                         //(!ЗАПОМНИТЬ цикл. Переменная string должна быть объявлена до цикла)
            String string;
            while ((string = reader.readLine()) != null) {
                builder.append(string);
            }
            Gson gson = new Gson();
            basket = gson.fromJson(builder.toString(),Basket.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return basket;
    }

    public int[] getInputedAmountProducts() {
        return inputedAmountProducts;
    }

    public int[] getAllCategoriesCost() {
        return allCategoriesCost;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
