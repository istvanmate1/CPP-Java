package GUI;

import bigcity.HighSchool;
import bigcity.Person;
import bigcity.Police;
import bigcity.Road;
import bigcity.Stadium;
import bigcity.University;
import bigcity.Zone;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import view.BigCityJframe;

public class SettingsDialog extends JDialog implements ChangeListener{
    protected JPanel btnPanel;
    protected JSlider slider;
    protected JLabel value;
    protected BigCityJframe frame;
    protected JLabel income;
    protected JLabel expenses;
    
    /**
     * Constructor
     * @param frame - BigCityJframe the owner of this dialog
     */
    public SettingsDialog(BigCityJframe frame) {
        super(frame, "Beállítások", true);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.frame = frame;
        slider = new JSlider(0, 100, frame.getCurrentTax());
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.addChangeListener(this);
        
        JButton ok = new JButton(
            new AbstractAction("OK") {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    frame.setTax(slider.getValue());
                    SettingsDialog.this.setVisible(false);
                }
            }
        );
        
        JButton cancel = new JButton(
            new AbstractAction("Cancel") {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    SettingsDialog.this.setVisible(false);
                }
            }
        );
        
        value = new JLabel("Adókulcs: " + frame.getCurrentTax() + "%");
        
        setLayout(new BorderLayout());
        btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.add(Box.createRigidArea(new Dimension(25,0)));
        btnPanel.add(ok);
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(value);
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(cancel);
        btnPanel.add(Box.createRigidArea(new Dimension(25,0)));
        add("Center", slider);
        
        JPanel stats = new JPanel();
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
        stats.add(btnPanel);
        JPanel statPanel = new JPanel();
        statPanel.setLayout(new BoxLayout(statPanel, BoxLayout.X_AXIS));
        income = new JLabel("Éves bevétel: " + calculateIncome());
        expenses = new JLabel("Éves kiadások: " + calculateExpenses());
        statPanel.add(Box.createRigidArea(new Dimension(25,0)));
        statPanel.add(income);
        statPanel.add(Box.createHorizontalGlue());
        statPanel.add(expenses);
        statPanel.add(Box.createRigidArea(new Dimension(25,0)));
        stats.add(statPanel);
        
        add("South",stats);
        
        
        
        setSize(new Dimension(500, 120));
    }
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        value.setText("Adókulcs: " + slider.getValue() + "%");
        income.setText("Éves bevétel: " + calculateIncome());
        expenses.setText("Éves kiadások: " + calculateExpenses());
    }
    
    /**
     * Sets this SettingsDialog visible
     */
    public void setActive() {
        setLocationRelativeTo(null);
        int tmp = frame.getCurrentTax();
        value.setText("Adókulcs: " + tmp + "%");
        income.setText("Éves bevétel: " + calculateIncome());
        expenses.setText("Éves kiadások: " + calculateExpenses());
        slider.setValue(tmp);
        this.setVisible(true);
    }
    
    private int calculateIncome() {
        int yearlyIncome = 0;
        for (Person p : frame.getEngine().getResidents()) {
            yearlyIncome += (double) (73 * slider.getValue()) / 100 * p.getEducationLevel().getLevel();
            //              (double) (      * taxPercentage)     / 100 * p.getEducationLevel().getLevel();
            if (null != p.getJob()) {
                yearlyIncome += (double) (109.5 * slider.getValue()) / 100 * p.getEducationLevel().getLevel();
            }
        }
        return yearlyIncome;
    }
    
    private int calculateExpenses() {
        int yearlyExpenses = 0;
        for (Zone zone : frame.getEngine().getBuildingsList()) {
            if (zone instanceof HighSchool) {
                yearlyExpenses -= 243;
            } else if (zone instanceof University) {
                yearlyExpenses -= 365;
            } else if (zone instanceof Police) {
                yearlyExpenses -= 365;
            } else if (zone instanceof Stadium) {
                yearlyExpenses -= 487;
            } else if (zone instanceof Road) {
                yearlyExpenses -= 61;
            }
        }
        return yearlyExpenses;
    }
}
