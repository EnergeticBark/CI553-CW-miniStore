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
        // If the first argument exists, use it as StockDAOs's URL, otherwise use the default URL.
        final String stockURL = args.isEmpty() ? Names.STOCK_DAO : args.getFirst();

        RemoteMiddleFactory rmf = new RemoteMiddleFactory();
        rmf.setStockURL(stockURL);

        stage.setTitle("BackDoor Client (MVC RMI)");

        BackDoorModel model = new BackDoorModel(rmf);
        BackDoorView view = new BackDoorView(stage, model, 0, 0);
        BackDoorController cont = new BackDoorController(model);
        view.setController(cont);
    }
}
