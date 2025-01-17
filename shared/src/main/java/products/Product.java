package products;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Used to hold the following information about
 * a product: Product number, Description, Price, Stock level.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 20092506;
    private final int productNumber;
    private final String description;
    private String picture = "default.jpg"; // Filename of the product picture.
    private final double price;
    private int quantity;

    public static final Comparator<Product> sortByNumber = Comparator.comparing(Product::getProductNumber);

    /**
     * Construct a product details
     * @param productNumber Product number
     * @param description Description of product
     * @param price The price of the product
     * @param quantity The quantity of the product involved
     */
    public Product(int productNumber, String description, String picture, double price, int quantity) {
        this.productNumber = productNumber;
        this.description = description;
        this.picture = picture;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(int productNumber, String description, double price, int quantity) {
        this.productNumber = productNumber;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductNumber() {
        return productNumber;
    }
    public String getDescription() {
        return description;
    }
    public String getPicture() {
        return picture;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }

    public Product take(int amount) {
        quantity -= amount;
        return new Product(productNumber, description, picture, price, amount);
    }
    public void add(int amount) {
        this.quantity += amount;
    }

    public String showDetails() {
        return String.format(
                "%s : %7.2f (%2d) ",
                getDescription(),
                getPrice(),
                getQuantity()
        );
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
