package rightPanel.buildingStatPanel;

import bigcity.EducationLevel;
import bigcity.HighSchool;
import bigcity.Person;
import bigcity.PrivateZone;
import bigcity.School;
import bigcity.University;
import bigcity.Zone;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Engine;
import roundPanel.RoundedBorder;

public class BuildingStat extends JPanel {
    private Zone building;
    private final JLabel level;
    private JLabel size;
    private JLabel capacity;
    private Engine e;
    
    /**
     * Constructor
     * @param building - Zone, the zone about which the statistics are created
     */
    public BuildingStat(Zone building, Engine e) {
        super();
        this.e = e;
        setBackground(Color.WHITE);
        setBorder(new RoundedBorder(Color.WHITE, 4, 8));
        this.building = building;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        if(building instanceof PrivateZone tmp) {
            
            JLabel capacityText = new JLabel("Kapacitás: ");
            capacity = new JLabel(tmp.getCapacity()+ " fő");
            JPanel capacityPanel = new JPanel();
            capacityPanel.setBackground(Color.WHITE);
            capacityPanel.add(capacityText);
            capacityPanel.add(capacity);
            
            JLabel sizeText = new JLabel("Telítettség: ");
            size = new JLabel(tmp.getSize() + " fő");
            JPanel sizePanel = new JPanel();
            sizePanel.setBackground(Color.WHITE);
            sizePanel.add(sizeText);
            sizePanel.add(size);
            
            this.add(capacityPanel);
            this.add(sizePanel);
            
            level = new JLabel("3/"+building.getLevel());
        }
        else {
            int hsd = 0;
            int ud = 0;
            int rc = e.getResidents().size();
            for (Person p : e.getResidents()) {
                if (p.getEducationLevel() == EducationLevel.HIGH_SCHOOL) {
                    hsd++;
                } else if (p.getEducationLevel() == EducationLevel.UNIVERSITY) {
                    ud++;
                }
            }
            
            if (building instanceof HighSchool) {
                size = new JLabel(Math.round(rc*0.8) + "/" + (hsd+ud) + " fő");
                
                JLabel sizeText = new JLabel("<html>Érettségizettek<br/>száma:</html>");
                JPanel sizePanel = new JPanel();
                sizePanel.setBackground(Color.WHITE);
                sizePanel.add(sizeText);
                sizePanel.add(size);

                this.add(sizePanel);
                
            }
            else if (building instanceof University) {
                size = new JLabel(Math.round(rc*0.5) + "/" + ud + " fő");
                
                JLabel sizeText = new JLabel("Diplomások száma: ");
                JPanel sizePanel = new JPanel();
                sizePanel.setBackground(Color.WHITE);
                sizePanel.add(sizeText);
                sizePanel.add(size);

                this.add(sizePanel);
            }
            level = new JLabel("1/"+building.getLevel());
        }
        JLabel levelText = new JLabel("Épület szintje: ");
        JPanel levelPanel = new JPanel();
        levelPanel.add(levelText);
        levelPanel.add(level);
        levelPanel.setBackground(Color.WHITE);
        this.add(levelPanel);
        //setPreferredSize(new Dimension(150, 100));
        setMaximumSize(new Dimension(150, 1000));
    }
    
    /**
     * Update the statistic of the zone
     */
    public void updateStats() {
        if(building instanceof PrivateZone){
            level.setText("3/"+building.getLevel());
        } else {
            level.setText("1/"+building.getLevel());
        }
        if(building instanceof PrivateZone tmp) {
            size.setText(tmp.getSize() + " fő");
            capacity.setText(tmp.getCapacity()+ " fő");
        } else if (building instanceof School) {
            int hsd = 0;
            int ud = 0;
            int rc = e.getResidents().size();
            for (Person p : e.getResidents()) {
                if (p.getEducationLevel() == EducationLevel.HIGH_SCHOOL) {
                    hsd++;
                } else if (p.getEducationLevel() == EducationLevel.UNIVERSITY) {
                    ud++;
                }
            }
            if (building instanceof HighSchool) {
                size.setText(Math.round(rc*0.8) + "/" + (hsd+ud) + " fő");
            }
            else if (building instanceof University) {
                size.setText(Math.round(rc*0.5) + "/" + ud + " fő");
            }
            
        }
    }
}
