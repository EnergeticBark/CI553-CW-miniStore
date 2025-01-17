/** The Packing Controller */
class PackingController {
    private final PackingModel model;

    /**
     * Constructor
     * @param model The model
     */
    PackingController(PackingModel model) {
        this.model = model;
    }

    /** Picked interaction from view */
    void doPacked() {
        model.doPacked();
    }
}

