package catalogue;

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
    private final String productNumber; // Product number
    private final String description; // Description of product
    private String picture = "default.jpg"; // Filename of the product picture
    private final double price; // Price of product
    private int quantity; // Quantity involved

    public static final Comparator<Product> sortByNumber = Comparator.comparing(Product::getProductNumber);

    /**
     * Construct a product details
     * @param productNumber Product number
     * @param description Description of product
     * @param price The price of the product
     * @param quantity The quantity of the product involved
     */
    public Product(String productNumber, String description, String picture, double price, int quantity) {
        this.productNumber = productNumber;
        this.description = description; // Description of product
        this.picture = picture;
        this.price = price; // Price of product
        this.quantity = quantity; // Quantity involved
    }

    public Product(String productNumber, String description, double price, int quantity) {
        this.productNumber = productNumber;
        this.description = description; // Description of product
        this.price = price; // Price of product
        this.quantity = quantity; // Quantity involved
    }

    public String getProductNumber() {
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
