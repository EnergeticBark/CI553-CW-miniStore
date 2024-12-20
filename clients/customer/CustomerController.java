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
     * @param pn The product number to be checked
     */
    public void doCheck(String pn) {
        model.doCheck(pn);
    }

    public void search(String searchQuery) {
        model.search(searchQuery);
    }

    /**
     * Clear interaction from view
     */
    public void doClear() {
        model.doClear();
    }
}
