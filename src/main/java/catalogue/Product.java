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
    private String theProductNum; // Product number
    private String theDescription; // Description of product
    private String picture = "default.jpg"; // Filename of the product picture
    private double thePrice; // Price of product
    private int theQuantity; // Quantity involved

    public static final Comparator<Product> sortByNumber = Comparator.comparing(Product::getProductNum);

    /**
     * Construct a product details
     * @param aProductNum Product number
     * @param aDescription Description of product
     * @param aPrice The price of the product
     * @param aQuantity The Quantity of the product involved
     */
    public Product(String aProductNum, String aDescription, String picture, double aPrice, int aQuantity) {
        theProductNum = aProductNum; // Product number
        theDescription = aDescription; // Description of product
        this.picture = picture;
        thePrice = aPrice; // Price of product
        theQuantity = aQuantity; // Quantity involved
    }

    public Product(String aProductNum, String aDescription, double aPrice, int aQuantity) {
        theProductNum = aProductNum; // Product number
        theDescription = aDescription; // Description of product
        thePrice = aPrice; // Price of product
        theQuantity = aQuantity; // Quantity involved
    }

    public String getProductNum() {
        return theProductNum;
    }
    public String getDescription() {
        return theDescription;
    }
    public String getPicture() {
        return picture;
    }
    public double getPrice() {
        return thePrice;
    }
    public int getQuantity() {
        return theQuantity;
    }

    public void setProductNum(String aProductNum) {
        theProductNum = aProductNum;
    }
    public void setDescription(String aDescription) {
        theDescription = aDescription;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public void setPrice(double aPrice) {
        thePrice = aPrice;
    }
    public void setQuantity(int aQuantity) {
        theQuantity = aQuantity;
    }
}
