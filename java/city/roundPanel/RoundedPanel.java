package roundPanel;

import java.awt.Color;
import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    /**
     * Constructor
     * @param color - Color, color of the background
     */
    public RoundedPanel(Color color) {
        super();
        setBackground(color);
        setBorder(new RoundedBorder(color, 2, 10));
    }
}
