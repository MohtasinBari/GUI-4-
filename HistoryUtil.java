package File;

import Entity.Product;
import java.io.*;
import java.util.List;

public class HistoryUtil {
    public static void saveHistory(String customerId, String method, List<Product> products, double total) {
        try (FileWriter writer = new FileWriter("purchase_history.txt", true)) {
            writer.write("Customer ID: " + customerId + "\nMethod: " + method + "\n");
            for (Product p : products)
                writer.write("- " + p.getName() + " (" + p.getBrand() + ") - BDT" + p.getPrice() + "\n");
            writer.write("Total: BDT" + total + "\n-----\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
