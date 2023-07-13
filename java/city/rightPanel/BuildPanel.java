package rightPanel;

import grid.Grid;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuildPanel extends JPanel {

    public static BuildButton selected = null;
    private Grid grid;

    /**
     * Sets the given button as selected (also changes it's color to yellow, and
     * sets the previous selected back to default color scheme)
     *
     * @param button
     */
    public void selectButton(BuildButton button) {
        deleteSelection();
        selected = button;
        selected.setBackground(Color.yellow);
        selected.btnPanel.setBackground(Color.yellow);
        grid.removeTheSelectionOfTheSelectedZone();
    }

    /**
     * Drops the selection from the selected button
     */
    public static void deleteSelection() {
        if (selected != null) {
            selected.setBackground(Color.gray);
            selected.btnPanel.setBackground(Color.gray);
            selected = null;
        }
    }

    /**
     * Constructor
     *
     * @param grid
     * @param builds The BuildButtons you want to display
     */
    public BuildPanel(Grid grid, BuildButton... builds) {
        super();
        this.grid=grid;
        
        XButton exit = new XButton(new deleteSelected(), grid);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel exitPanel = new JPanel();
        exitPanel.setLayout(new BoxLayout(exitPanel, BoxLayout.X_AXIS));
        exitPanel.add(Box.createHorizontalGlue());
        exitPanel.add(exit);
        exitPanel.setAlignmentX(0);
        exitPanel.setBackground(Color.GREEN);
        add(exitPanel);

        JPanel buildPanel = new JPanel();
        buildPanel.setLayout(new BoxLayout(buildPanel, BoxLayout.Y_AXIS));
        buildPanel.setBackground(Color.GREEN);
        for (BuildButton buildButton : builds) {
            buildPanel.add(buildButton);
            buildButton.addActionListener(new changeSelected());
        }
        add(buildPanel);
        setPreferredSize(new Dimension(170, 100));
    }
    
    
    /**
     * Selects the build button.
     */
    private class changeSelected implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            selectButton((BuildButton) ae.getSource());
        }
    }

    /**
     * Drops the selections from the build button.
     */
    public static class deleteSelected implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            BuildPanel.deleteSelection();
        }
    }
}
