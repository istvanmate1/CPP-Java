
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

public class NewGameDialog extends OKCancelDialog {

    private JComboBox comboWASD;
    private JComboBox comboArrows;

    private JTextField wasdPlayerName;
    private JTextField arrowPlayerName;

    public NewGameDialog(JFrame frame, String name, Colour[] colors) {
        super(frame, name);
        comboWASD = new JComboBox(colors);
        comboWASD.setRenderer(elementDisplay);

        comboArrows = new JComboBox(colors);
        comboArrows.setRenderer(elementDisplay);

        setLayout(new GridLayout(4, 2));
        add(new JLabel("WASD"));
        add(new JLabel("Arrows"));

        add(comboWASD);
        add(comboArrows);

        wasdPlayerName = new JTextField();
        arrowPlayerName = new JTextField();

        add(wasdPlayerName);
        add(arrowPlayerName);

        add(btnPanel);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public String getWasdPlayerName() {
        return wasdPlayerName.getText();
    }

    public String getArrowPlayerName() {
        return arrowPlayerName.getText();
    }

    public Colour getWasdColor() {

        return (Colour) comboWASD.getSelectedItem();
    }

    public Colour getArrowColor() {

        return (Colour) comboArrows.getSelectedItem();
    }

    @Override
    protected boolean processOK() {
        return true;
    }

    @Override
    protected void processCancel() {
    }

    private DefaultListCellRenderer elementDisplay = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList l, Object o, int index,
                boolean isSelected,
                boolean cellHasFocus) {
            super.getListCellRendererComponent(l, o, index, isSelected, cellHasFocus);
            BufferedImage im = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics g = im.createGraphics();
            g.setColor(((Colour) o).getColor());
            g.fillOval(0, 0, 15, 15);
            setIcon(new ImageIcon(im));
            return this;
        }
    };
}
