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

    /**
     * Query interaction from view
     * @param pn The product number to be checked
     */
    public void query(String pn) {
        model.query(pn);
    }

    /**
     * RStock interaction from view
     * @param pn       The product number to be re-stocked
     * @param quantity The quantity to be re-stocked
     */
    public void restock(String pn, String quantity) {
        model.restock(pn, quantity);
    }

    /**
     * Clear interaction from view
     */
    public void clear() {
        model.clear();
    }
}
