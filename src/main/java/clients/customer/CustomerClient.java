package clients.customer;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone Customer Client
 */
public class CustomerClient {
    public static void main(String[] args) {
        // URL of stock reader.
        String stockURL = args.length < 1
                ? Names.STOCK_R // default location
                : args[0]; // supplied location

        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRInfo(stockURL);
        displayGUI(mrf); // Create GUI
    }

    private static void displayGUI(MiddleFactory mf) {
        JFrame window = new JFrame();
        window.setTitle("Customer Client (MVC RMI)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CustomerModel model = new CustomerModel(mf);
        CustomerView view = new CustomerView(window, 0, 0);
        CustomerController controller = new CustomerController(model);
        view.setController(controller);

        model.addPropertyChangeListener(view); // Add listener to the model
        window.setVisible(true); // Display Scree
    }
}
