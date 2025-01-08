package catalogue;

import products.Product;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;

/**
 * A collection of products,
 * used to record the products that are to be wished to be purchased.
 * @author Mike Smith University of Brighton
 * @version 2.2
 */
public class Basket extends ArrayList<Product> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;

    /**
     * Add a product to the Basket.
     * Product is appended to the end of the existing products
     * in the basket.
     * @param pr A product to be added to the basket
     * @return true if successfully adds the product
     */
    // Will be in the Java doc for Basket
    @Override
    public boolean add(Product pr) {
        return super.add(pr); // Call add in ArrayList
    }

    /**
     * Returns a description of the products in the basket suitable for printing.
     * @return a string description of the basket products
     */
    public String getDetails() {
        Locale uk = Locale.UK;
        StringBuilder sb = new StringBuilder(256);
        Formatter fr = new Formatter(sb, uk);
        String cSign = (Currency.getInstance(uk)).getSymbol();
        double total = 0.00;

        if (this.size() > 0) {
            for (Product pr: this) {
                int number = pr.getQuantity();
                fr.format("%-7s", pr.getProductNumber());
                fr.format("%-14.14s ", pr.getDescription());
                fr.format("(%3d) ", number);
                fr.format("%s%7.2f", cSign, pr.getPrice() * number);
                fr.format("\n");
                total += pr.getPrice() * number;
            }
            fr.format("----------------------------\n");
            fr.format("Total                       ");
            fr.format("%s%7.2f\n", cSign, total);
            fr.close();
        }
        return sb.toString();
    }
}
