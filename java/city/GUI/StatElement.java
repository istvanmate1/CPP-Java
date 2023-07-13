package GUI;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import res.ResourceLoader;
import roundPanel.RoundedBorder;

public class StatElement extends JPanel {
    protected JLabel text;
    /**
     * Constructor
     * @param filename
     * @param text 
     */
    public StatElement(String filename, String text) {
        setPreferredSize(new Dimension(150, 40));
        
        JLabel image = new JLabel(new ImageIcon(ResourceLoader.scaleImage(26, 26, filename)));
        this.text = new JLabel(text);
        this.text.setForeground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        add(image);
        add(Box.createHorizontalGlue());
        add(this.text);
        setBorder(new RoundedBorder(Color.BLACK, 2, 20));
        setBackground(Color.BLACK);
    }
   
    public JLabel getText() {
        return text;
    }

    public void setText(String text) {
        this.text.setText(text);
    }
    
    public void setTextColor(Color c){
       text.setForeground(c);
    }
}
