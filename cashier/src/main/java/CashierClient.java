import javafx.application.Application;
import javafx.stage.Stage;
import middle.Names;
import middle.RemoteMiddleFactory;

import java.util.List;

/** The standalone Cashier Client. */
public class CashierClient extends Application {
    @Override
    public void start(Stage stage) {
        final List<String> args = getParameters().getRaw();
        // If the first or second arguments exist, use them as StockDAO and OrderProcessor's URLs.
        // Otherwise, use their default URLs.
        String stockURL = args.isEmpty() ? Names.STOCK_DAO : args.getFirst();
        String orderURL = args.size() < 2 ? Names.ORDER_DAO : args.get(1);

        RemoteMiddleFactory rmf = new RemoteMiddleFactory();
        rmf.setStockURL(stockURL);
        rmf.setOrderURL(orderURL);

        stage.setTitle("Cashier Client (MVC RMI)");

        CashierModel model = new CashierModel(rmf);
        CashierView view = new CashierView(stage, model, 0, 0);
        CashierController controller = new CashierController(model);
        view.setController(controller);

        model.askForUpdate();
    }
}
