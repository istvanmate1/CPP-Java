package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Sütő Attila
 */
public class NameWindow extends JFrame{
    private ThreeColumnLayout panel;
    private ImageIcon backgroundImage;
    /**
     * Constructor
     */
    public NameWindow() {

        // Load the background image
        backgroundImage = new ImageIcon("GUI/menu.png");
        
        panel = new ThreeColumnLayout(this);

        // Set the layout of the JFrame to BorderLayout
        setLayout(new BorderLayout());

        

        // Set the background color of the buttonPanel to be transparent
        panel.setOpaque(false);
        
        // Set the background image of the JFrame
        setContentPane(new JLabel(backgroundImage));
        setLayout(new BorderLayout());
        add(panel,BorderLayout.NORTH);

        // Set the size of the JFrame
        setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());


        // Center the JFrame on the screen
        setSize(new Dimension(630,430));
        setResizable(false);
        
        //Hide titlebar
        setUndecorated(true);
        
        setLocationRelativeTo(null);
        
        ImageIcon icon = new ImageIcon("GUI/house.png");
        setIconImage(icon.getImage());
        
        // Set the JFrame to be visible
        setVisible(true);
    }
}
