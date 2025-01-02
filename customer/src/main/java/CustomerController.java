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
