import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * Returns a position for the client window on the screen
 * The clients are assumed to be all the same size 400x300
 * @author Mike Smith University of Brighton
 * @version 1.0
 */
class PosOnScreen {
    private final static int clientW = 420;
    private final static int clientH = 270;

    private static final int maxX; // Width of screen
    private static final int maxY; // Height of screen

    private static int cX = 0; // Initial window pos on screen
    private static int cY = 0; // Initial window pos on screen

    // class initializer
    // Will be called (once) when the class is loaded
    static {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        maxX = (int) bounds.getMaxX();
        maxY = (int) bounds.getMaxY();
    }

    /**
     * Calculate position of next window
     */
    private static void next() {
        if (cX + 2 * clientW > maxX) {
            if (cY + 2 * clientH < maxY) {
                cX = 0; cY += clientH;
            }
        } else {
            cX += clientW;
        }
        // No room on screen
        // All new windows are tiled on top of each other
    }

    /**
     * return position for new window on screen
     * @return position for new window
     */
    public static Point2D getPos() {
        Point2D pos = new Point2D(cX, cY);
        next();
        return pos;
    }
}
