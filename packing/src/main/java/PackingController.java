/**
 * The Packing Controller
 */
public class PackingController {
    private final PackingModel model;

    /**
     * Constructor
     * @param model The model
     */
    public PackingController(PackingModel model) {
        this.model = model;
    }

    /**
     * Picked interaction from view
     */
    public void doPacked() {
        model.doPacked();
    }
}

