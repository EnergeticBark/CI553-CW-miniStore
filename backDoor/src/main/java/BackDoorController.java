import products.Product;

/** The BackDoor Controller */
class BackDoorController {
    private final BackDoorModel model;

    /**
     * Constructor
     * @param model the model this controller will talk to.
     */
    BackDoorController(BackDoorModel model) {
        this.model = model;
    }

    /**
     * Product selection interaction from view
     * @param product which product the user selected from the table
     */
    void selectProduct(Product product) {
        if (product == null) {
            return;
        }
        model.selectProduct(product);
    }

    /** Restock interaction from view */
    void restock() {
        model.restock();
    }
}
