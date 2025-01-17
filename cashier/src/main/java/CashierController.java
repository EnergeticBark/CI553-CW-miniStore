/** The Cashier Controller */
class CashierController {
    private final CashierModel model;

    /**
     * Constructor
     * @param model The model
     */
    CashierController(CashierModel model) {
        this.model = model;
    }

    /**
     * Check interaction from view
     * @param productNumber the product number to be checked
     */
    void checkStock(String productNumber) {
        model.checkStock(productNumber);
    }

    /** Buy interaction from view */
    void buy() {
        model.buy();
    }

    /** Bought interaction from view */
    void bought() {
        model.bought();
    }
}
