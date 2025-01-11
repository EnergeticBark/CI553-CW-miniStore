import products.Product;

/**
 * The BackDoor Controller
 */
public class BackDoorController {
    private final BackDoorModel model;

    /**
     * Constructor
     * @param model The model
     */
    public BackDoorController(BackDoorModel model) {
        this.model = model;
    }

    public void selectProduct(Product product) {
        if (product == null) {
            return;
        }
        model.selectProduct(product);
    }

    /**
     * restock interaction from view
     */
    public void restock() {
        model.restock();
    }
}
