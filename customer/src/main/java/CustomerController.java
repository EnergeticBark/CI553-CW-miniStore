/** The Customer Controller */
class CustomerController {
    private final CustomerModel model;

    /**
     * Constructor
     * @param model The model
     */
    CustomerController(CustomerModel model) {
        this.model = model;
    }

    /** Search interaction from view */
    void search(String searchQuery) {
        model.search(searchQuery);
    }

    /** Clear interaction from view */
    void clear() {
        model.clear();
    }
}
