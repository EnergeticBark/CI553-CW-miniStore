package clients.customer;

/**
 * The Customer Controller
 */
public class CustomerController {
    private final CustomerModel model;

    /**
     * Constructor
     * @param model The model
     */
    public CustomerController(CustomerModel model) {
        this.model = model;
    }

    /**
     * Check interaction from view
     * @param productNumber The product number to be checked
     */
    public void checkStock(String productNumber) {
        model.checkStock(productNumber);
    }

    public void search(String searchQuery) {
        model.search(searchQuery);
    }

    /**
     * Clear interaction from view
     */
    public void clear() {
        model.clear();
    }
}
