package rightPanel;

import grid.Grid;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.CursorSignal;
import model.Engine;

public class XButton extends JButton {

    /**
     * Constructor
     *
     * @param ae - ActionListener, action that the button does on click
     * @param grid
     */
    public XButton(ActionListener ae, Grid grid) {
        super();
        this.setText("X");
        this.setBackground(Color.red);
        this.setFont(new Font("Courier", Font.BOLD, 12));
        this.addActionListener(ae);
        addActionListener((ActionEvent e) -> {
            Engine.setCursorSignal(CursorSignal.SELECT);
            grid.removeTheSelectionOfTheSelectedZone();
        });
    }
}
