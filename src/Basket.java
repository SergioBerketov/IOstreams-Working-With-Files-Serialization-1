import java.io.*;
import java.sql.Array;
import java.util.Arrays;

public class Basket implements Serializable {
    private static final long serialVersionUID = 1L;

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

    public void saveBin(File file) {
        try (ObjectOutputStream objWrt = new ObjectOutputStream(new FileOutputStream(file))) {
            objWrt.writeObject(this);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromBinFile(File file) {
        Basket basket = null;

        try (ObjectInputStream objRd = new ObjectInputStream(new FileInputStream(file))){
            basket = (Basket) objRd.readObject();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
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
