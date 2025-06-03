package Entity;


abstract class AbstractProduct {
    private String name, brand;
    private double price;

    public AbstractProduct(String name, String brand, double price) {
        this.name = name;
        this.brand = brand;
        this.price = price;
    }

    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }

    
    public abstract String getDisplayText();
}


public class Product extends AbstractProduct {

    public Product(String name, String brand, double price) {
        super(name, brand, price);
    }

    @Override
    public String getDisplayText() {
        return getName() + " by " + getBrand() + " - BDT" + getPrice();
    }
}
