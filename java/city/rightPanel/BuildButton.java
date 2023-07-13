package rightPanel;

import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ImageIcon;

public class BuildButton extends JButton {

    protected JPanel btnPanel;
    protected String text;
    protected int price;
    
    //protected BuildPanel buildPanel;

    /**
     * Constructor
     *
     * @param url - The url of the image
     * @param name - Name of the building
     * @param price - Price of the building
     */
    public BuildButton(String url, String name, int price/*, BuildPanel buildPanel*/) {
        super();
      //  this.buildPanel=buildPanel;
        this.text = name;
        this.price = price;
        this.setLayout(new BorderLayout());
        this.setBackground(Color.gray);
        btnPanel = new JPanel();
        btnPanel.setLayout(new BorderLayout());
        btnPanel.setBackground(Color.gray);
        btnPanel.setOpaque(false);
        JLabel priceLabel = new JLabel(price + "$");
        priceLabel.setForeground(Color.green);
        btnPanel.add(new JLabel(name + " "), BorderLayout.LINE_START);
        btnPanel.add(priceLabel, BorderLayout.LINE_END);

        JLabel tmp = new JLabel(new ImageIcon(res.ResourceLoader
                .scaleImage(32, 32, url)));
        tmp.setBorder(new CompoundBorder(tmp.getBorder(),
                new EmptyBorder(0, 0, 0, 10)));
        this.add(tmp, BorderLayout.LINE_START);
        this.add(btnPanel, BorderLayout.CENTER);
    }

}
