package clients.cashier;

/**
 * The Cashier Controller
 */
public class CashierController {
    private final CashierModel model;

    /**
     * Constructor
     * @param model The model
     */
    public CashierController(CashierModel model) {
        this.model = model;
    }

    /**
     * Check interaction from view
     * @param pn The product number to be checked
     */
    public void checkStock(String pn) {
        model.checkStock(pn);
    }

    /**
     * Buy interaction from view
     */
    public void buy() {
        model.buy();
    }

    /**
     * Bought interaction from view
     */
    public void bought() {
        model.bought();
    }
}
