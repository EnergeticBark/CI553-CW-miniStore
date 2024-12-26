package clients.backDoor;

import javafx.application.Application;
import javafx.stage.Stage;
import middle.Names;
import middle.RemoteMiddleFactory;

import java.util.List;

/**
 * The standalone BackDoor Client
 */
public class BackDoorClient extends Application {
    @Override
    public void start(Stage stage) {
        final List<String> args = getParameters().getRaw();
        // If the first or second arguments exist, use them as StockReader and Order's URLs.
        // Otherwise, use their default URLs.
        String stockURL = args.isEmpty() ? Names.STOCK_RW : args.getFirst();
        String orderURL = args.size() < 2 ? Names.ORDER : args.get(1);

        RemoteMiddleFactory rmf = new RemoteMiddleFactory();
        rmf.setStockRWInfo(stockURL);
        rmf.setOrderInfo(orderURL);

        stage.setTitle("BackDoor Client (MVC RMI)");

        BackDoorModel model = new BackDoorModel(rmf);
        BackDoorView view = new BackDoorView(stage, model, 0, 0);
        BackDoorController cont = new BackDoorController(model);
        view.setController(cont);
    }
}
