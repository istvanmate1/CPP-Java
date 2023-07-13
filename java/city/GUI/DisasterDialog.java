package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import model.Coords;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import model.Engine;

public class DisasterDialog extends JDialog {

    private Engine e;
    private final ArrayList<Coords> points;

    /**
     * Constructor
     *
     * @param label - JLabel, this contains the visible text
     * @param e - Engine, owner of this dialog
     * @param row
     * @param col
     * @param points
     */
    public DisasterDialog(JLabel label, Engine e, int row, int col, ArrayList<Coords> points) {
        this.points = points;
        this.e = e;
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton ok = new JButton(
                new AbstractAction("OK") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (points != null) {
                    for (Coords point : points) {
                        DisasterDialog.this.e.removeImg(point.getX(), point.getY());
                    }
                }
                DisasterDialog.this.e.removeImg(row, col);
                try {
                Thread.sleep(1000); // Wait for one second
                } catch (InterruptedException e) {
                    System.out.println("Sleep error");
                }
                DisasterDialog.this.setVisible(false);
                e.startTime();
            }
        }
        );

        setLayout(new BorderLayout());
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        textPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        textPanel.add("North", label);
        textPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        add(textPanel);
        add("South", ok);
        this.pack();
        this.setModal(true);
    }

    /**
     * Set DisasterDialog visible
     */
    public void setActive() {
        e.stopTime();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
