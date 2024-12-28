import javafx.application.Application;
import javafx.stage.Stage;
import middle.Names;
import middle.RemoteMiddleFactory;

import java.util.List;

/**
 * The standalone warehouse Packing Client. warehouse staff to pack the bought order
 * @author  Mike Smith University of Brighton
 * @version 2.0
 * @author  Shine University of Brighton
 * @version year 2024
 */
public class PackingClient extends Application {
    @Override
    public void start(Stage stage) {
        final List<String> args = getParameters().getRaw();
        // If the first or second arguments exist, use them as StockReadWriter and Order's URLs.
        // Otherwise, use their default URLs.
        String stockURL = args.isEmpty() ? Names.STOCK_RW : args.getFirst();
        String orderURL = args.size() < 2 ? Names.ORDER : args.get(1);

        RemoteMiddleFactory rmf = new RemoteMiddleFactory();
        rmf.setStockRWInfo(stockURL);
        rmf.setOrderInfo(orderURL);

        stage.setTitle("Packing Client (RMI MVC)");

        PackingModel model = new PackingModel(rmf);
        PackingView view = new PackingView(stage, model, 0, 0);
        PackingController cont = new PackingController(model, view);
        view.setController(cont);
    }
}