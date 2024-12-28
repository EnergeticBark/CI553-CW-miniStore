import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import middle.LocalMiddleFactory;
import middle.MiddleFactory;

/**
 * Starts all the clients (user interface)  as a single application.
 * Good for testing the system using a single application.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 * @author  Shine University of Brighton
 * @version year-2024
 */
public class Main extends Application {
    /**
     * Starts the system (Non-distributed)
     */
    @Override
    public void start(Stage stage) {
        //DEBUG.set(true); /* Lots of debug info */
        MiddleFactory mlf = new LocalMiddleFactory(); // Direct access
        startCustomerGUI_MVC(mlf);
        startCashierGUI_MVC(mlf);
        startCashierGUI_MVC(mlf); // you can create multiple clients
        startPackingGUI_MVC(mlf);
        startBackDoorGUI_MVC(mlf);
    }

    /**
     * start the Customer client, -search product
     * @param mlf A factory to create objects to access the stock list
     */
    public void startCustomerGUI_MVC(MiddleFactory mlf) {
        Stage stage = new Stage();
        stage.setTitle("Customer Client MVC");
        Point2D pos = PosOnScreen.getPos();

        CustomerModel model = new CustomerModel(mlf);
        CustomerView view = new CustomerView(stage, model, (int) pos.getX(), (int) pos.getY());
        CustomerController cont = new CustomerController(model);
        view.setController(cont);
    }

    /**
     * start the cashier client - customer check stock, buy product
     * @param mlf A factory to create objects to access the stock list
     */
    public void startCashierGUI_MVC(MiddleFactory mlf) {
        Stage stage = new Stage();
        stage.setTitle("Cashier Client MVC");
        Point2D pos = PosOnScreen.getPos();

        CashierModel model = new CashierModel(mlf);
        CashierView view = new CashierView(stage, model, (int) pos.getX(), (int) pos.getY());
        CashierController cont = new CashierController(model);
        view.setController(cont);

        model.askForUpdate(); // Initial display
    }

    /**
     * start the Packing client - for warehouse staff to pack the bought order for customer, one order at a time
     * @param mlf A factory to create objects to access the stock list
     */
    public void startPackingGUI_MVC(MiddleFactory mlf) {
        Stage stage = new Stage();
        stage.setTitle("Packing Client MVC");
        Point2D pos = PosOnScreen.getPos();

        PackingModel model = new PackingModel(mlf);
        PackingView view = new PackingView(stage, model, (int) pos.getX(), (int) pos.getY());
        PackingController cont = new PackingController(model);
        view.setController(cont);
    }

    /**
     * start the BackDoor client - store staff to check and update stock
     * @param mlf A factory to create objects to access the stock list
     */
    public void startBackDoorGUI_MVC(MiddleFactory mlf) {
        Stage stage = new Stage();
        stage.setTitle("BackDoor Client MVC");
        Point2D pos = PosOnScreen.getPos();

        BackDoorModel model = new BackDoorModel(mlf);
        BackDoorView view = new BackDoorView(stage, model, (int) pos.getX(), (int) pos.getY());
        BackDoorController cont = new BackDoorController(model);
        view.setController(cont);
    }
}
