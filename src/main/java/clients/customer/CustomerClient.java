package clients.customer;

import javafx.application.Application;
import javafx.stage.Stage;
import middle.Names;
import middle.RemoteMiddleFactory;

import java.util.List;

/**
 * The standalone Customer Client
 */
public class CustomerClient extends Application {
    @Override
    public void start(Stage stage) {
        final List<String> args = getParameters().getRaw();
        // If it exists, use the first argument as StockReader's URL, otherwise use the default URL.
        final String stockURL = args.isEmpty() ? Names.STOCK_R : args.getFirst();

        RemoteMiddleFactory rmf = new RemoteMiddleFactory();
        rmf.setStockRInfo(stockURL);

        stage.setTitle("Customer Client (MVC RMI)");

        CustomerModel model = new CustomerModel(rmf);
        CustomerView view = new CustomerView(stage, model, 0, 0);
        CustomerController controller = new CustomerController(model);
        view.setController(controller);
    }
}
