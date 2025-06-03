package Service;

import java.util.*;
import Entity.Product;

public class ProductService {
    private List<Product> products = new ArrayList<>();

    public void insertProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public double calculateTotal(List<Product> selected) {
        return selected.stream().mapToDouble(Product::getPrice).sum();
    }
}
