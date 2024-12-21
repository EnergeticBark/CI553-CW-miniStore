package clients.customer;

import clients.Picture;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Implements the Customer view.
 */
public class CustomerView implements PropertyChangeListener {
    // Width and height of the window in pixels.
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    // Button labels.
    private static final String CHECK = "Check";
    private static final String SEARCH = "Search";
    private static final String CLEAR = "Clear";

    // Button width and height in pixels.
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 40;

    // Button height, including margins on the top and bottom.
    private static final int BUTTON_OUTER_HEIGHT = 50;

    private final Picture picture = new Picture(80,80);
    private final JLabel actionLabel = new JLabel();
    private final JTextField inputField = new JTextField();
    private final JTextArea outputText = new JTextArea();

    private CustomerController controller = null;

    /**
     * Construct the view
     * @param rpc   Window in which to construct
     * @param x     x-coordinate of position of window on screen
     * @param y     y-coordinate of position of window on screen
     */
    public CustomerView(RootPaneContainer rpc, int x, int y) {
        Container cp = rpc.getContentPane(); // Content Pane
        Container rootWindow = (Container) rpc; // Root Window
        cp.setLayout(null); // No layout manager
        rootWindow.setSize(WIDTH, HEIGHT); // Size of Window
        rootWindow.setLocation(x, y);

        JLabel pageTitle = new JLabel("Search products");
        pageTitle.setBounds(110, 0, 270, 20);
        cp.add(pageTitle);

        // Check button.
        JButton checkButton = new JButton(CHECK);
        checkButton.setBounds(16, 25, BUTTON_WIDTH, BUTTON_HEIGHT);
        checkButton.addActionListener( // Callback code
                _ -> controller.checkStock(inputField.getText())
        );
        cp.add(checkButton); // Add to canvas

        // Search button.
        JButton searchButton = new JButton(SEARCH);
        searchButton.setBounds(16, 25 + BUTTON_OUTER_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        searchButton.addActionListener( // Callback code
                _ -> controller.search(inputField.getText())
        );
        cp.add(searchButton); // Add to canvas

        // Clear button.
        JButton clearButton = new JButton(CLEAR);
        clearButton.setBounds(16, 25 + BUTTON_OUTER_HEIGHT * 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        clearButton.addActionListener( // Callback code
                _ -> controller.clear()
        );
        cp.add(clearButton); // Add to canvas

        // Picture area.
        picture.setBounds(16, 25 + BUTTON_OUTER_HEIGHT * 3, 80, 80);
        cp.add(picture); // Add to canvas
        picture.clear();

        actionLabel.setBounds(110, 25 , 270, 20); // Message area
        cp.add(actionLabel); // Add to canvas

        inputField.setBounds(110, 50, 270, 40); // Product no area
        cp.add(inputField); // Add to canvas

        // Scrolling pane
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(110, 100, 270, 160);
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        outputText.setFont(font); // Uses font
        cp.add(scrollPane); // Add to canvas
        scrollPane.getViewport().add(outputText); // In TextArea

        rootWindow.setVisible(true); // Make visible;
        inputField.requestFocus(); // Focus is here
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     * @param controller   The controller
     */
    public void setController(CustomerController controller) {
        this.controller = controller;
    }

    /**
     * Update the view
     * @param evt The event source and property that has changed
     */
    public void propertyChange(PropertyChangeEvent evt) {
        CustomerModel model = (CustomerModel) evt.getSource();
        String message = (String) evt.getNewValue();
        actionLabel.setText(message);
        ImageIcon image = model.getPicture(); // Image of product
        if (image == null) {
            picture.clear(); // Clear picture
        } else {
            picture.set(image); // Display picture
        }
        outputText.setText(model.getBasket().getDetails());
        inputField.requestFocus(); // Focus is here
    }
}
