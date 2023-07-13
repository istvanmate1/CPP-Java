package rightPanel.buildingStatPanel;

import bigcity.EducationLevel;
import bigcity.HighSchool;
import bigcity.Industry;
import bigcity.Person;
import bigcity.Police;
import bigcity.PrivateZone;
import bigcity.Residence;
import bigcity.Road;
import bigcity.Stadium;
import bigcity.Workplace;
import bigcity.Zone;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import model.CursorSignal;
import model.Engine;
import rightPanel.XButton;
import rightPanel.personsPanel.PersonsPanel;
import view.BigCityJframe;
import grid.Grid;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import res.ResourceLoader;

public class BuildingStatPanel extends JPanel {

    protected PersonsPanel pPanel;
    protected BuildingStat bStat;
    protected boolean hasCitizens;
    protected BigCityJframe bigCityJFrame;
    protected Zone zone;
    private JLabel textLabel;
    
    protected Grid grid;
    
    public BuildingStatPanel(Zone zone, BigCityJframe bigCityJFrame, Grid grid) {
        this.grid=grid;
        JLabel name = new JLabel();
        if (zone instanceof PrivateZone) {
            if (zone instanceof Residence) {
                name.setText("Lakóingatlan");
            } else if (zone instanceof Industry) {
                name.setText("Ipari épület");
            } else {
                name.setText("Szolgáltatás");
            }
        } else {
            if (zone instanceof Road) {
                name.setText("Út");
            } else if (zone instanceof Stadium) {
                name.setText("Stadion");
            } else if (zone instanceof Police) {
                name.setText("Rendőrség");
            } else if (zone instanceof HighSchool) {
                name.setText("Középiskola");
            } else {
                name.setText("Egyetem");
            }
        }

        setBackground(Color.GREEN.brighter());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel exitPanel = new JPanel();
        exitPanel.setLayout(new BoxLayout(exitPanel, BoxLayout.X_AXIS));
        exitPanel.setBackground(Color.GREEN.brighter());
        exitPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        exitPanel.add(name);
        exitPanel.add(Box.createHorizontalGlue());
        exitPanel.add(new XButton((ActionEvent e) -> {
            bigCityJFrame.changeRightPanelToBuildPanel();
            bigCityJFrame.repaint();
        },grid));
        add(exitPanel);

        this.bigCityJFrame = bigCityJFrame;

        /*JButton addPerson = new JButton("Ember hozzáadása");
        addPerson.addActionListener((ActionEvent e) -> {
            if (zone instanceof Residence tmp) {
                tmp.addPerson(new Person("Norbi", 22, 89,
                        true, EducationLevel.UNIVERSITY,
                        tmp, null));
                pPanel.updatePeople();
                bStat.updateStats();
                revalidate();
                repaint();
                bigCityJFrame.repaint();
            } else if (zone instanceof Workplace tmp) {
                tmp.addPerson(new Person("Anna", 21, 88,
                        false, EducationLevel.UNIVERSITY,
                        tmp, null));
                pPanel.updatePeople();
                bStat.updateStats();
                revalidate();
                repaint();
                bigCityJFrame.repaint();
            }
        });
        if (zone instanceof PrivateZone) {
            JPanel asd = new JPanel();
            asd.add(addPerson);
            //add(addPerson);
            add(asd);
        }*/

        this.zone = zone;
        if (zone instanceof Residence tmp) {
            pPanel = new PersonsPanel(tmp.getResidents());
            hasCitizens = true;
        } else if (zone instanceof Workplace tmp) {
            pPanel = new PersonsPanel(tmp.getWorkers());
            hasCitizens = true;
        } else {
            hasCitizens = false;
        }
        bStat = new BuildingStat(zone, bigCityJFrame.getEngine());
        addElements();
        setPreferredSize(new Dimension(170, 100));
    }

    private void addElements() {
        add(Box.createRigidArea(new Dimension(0, 20)));
        if (hasCitizens) {
            JScrollPane personsScroll = new JScrollPane(pPanel);
            add(personsScroll);
            add(Box.createRigidArea(new Dimension(0, 20)));
        }
        add(bStat);
        add(Box.createRigidArea(new Dimension(0, 20)));
        JPanel upgradePanel = new JPanel();
        JButton upgradeButton = new JButton();
        upgradeButton.setLayout(new BoxLayout(upgradeButton, BoxLayout.X_AXIS));
        upgradeButton.setPreferredSize(new Dimension(80,40));
        int padding = 5;
        int spaceWidth=10;
        upgradeButton.setBorder(new EmptyBorder(padding, padding, padding, padding));
               

        textLabel = new JLabel("-");
        //label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        Image img = null;
        try {
            img = ResourceLoader.loadBufferedImage("GUI/update_arrow.png");
        } catch (IOException ex) {
            Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        ImageIcon arrowIcon = new ImageIcon(img);
        //upgradeButton.add(label);
        JLabel imgLabel = new JLabel(arrowIcon);
        imgLabel.setPreferredSize(new Dimension(40,40));
        upgradeButton.add(imgLabel);
        upgradePanel.add(Box.createHorizontalGlue());
        upgradeButton.add(Box.createRigidArea(new Dimension(spaceWidth/2, 0)));
        upgradeButton.add(textLabel);
        if (zone instanceof PrivateZone tmp) {
            if (tmp.getLevel() < 3) {
                
                    int price;
                    if (tmp instanceof Residence) {
                        price = tmp.getLevel() == 1
                                ? CursorSignal.RESIDENCE.getPriceL2()
                                : CursorSignal.RESIDENCE.getPriceL3();
                    } else if (tmp instanceof Industry) {
                        price = tmp.getLevel() == 1
                                ? CursorSignal.INDUSTRY.getPriceL2()
                                : CursorSignal.INDUSTRY.getPriceL3();
                    } else {
                        price = tmp.getLevel() == 1
                                ? CursorSignal.SERVICE.getPriceL2()
                                : CursorSignal.SERVICE.getPriceL3();
                    }
                    //tmp.upgrade();
                    //bigCityJFrame.addMoney(-price);
                
                textLabel.setText(String.valueOf(price)+"$");
            }
            upgradeButton.addActionListener((ActionEvent e) -> {
                if (tmp.getLevel() < 3) {
                    int price;
                    int nextPrice=0;
                    if (tmp instanceof Residence) {
                        if(tmp.getLevel() == 1){nextPrice=CursorSignal.RESIDENCE.getPriceL3();}
                        //if(tmp.getLevel() == 2){nextPrice=CursorSignal.RESIDENCE.getPriceL3();}
                        price = tmp.getLevel() == 1
                                ? CursorSignal.RESIDENCE.getPriceL2()
                                : CursorSignal.RESIDENCE.getPriceL3();
                    } else if (tmp instanceof Industry) {
                        if(tmp.getLevel() == 1){nextPrice=CursorSignal.INDUSTRY.getPriceL3();}
                        //if(tmp.getLevel() == 2){nextPrice=CursorSignal.INDUSTRY.getPriceL3();}
                        price = tmp.getLevel() == 1
                                ? CursorSignal.INDUSTRY.getPriceL2()
                                : CursorSignal.INDUSTRY.getPriceL3();
                    } else {
                        if(tmp.getLevel() == 1){nextPrice=CursorSignal.SERVICE.getPriceL3();}
                        //if(tmp.getLevel() == 2){nextPrice=CursorSignal.SERVICE.getPriceL3();}
                        price = tmp.getLevel() == 1
                                ? CursorSignal.SERVICE.getPriceL2()
                                : CursorSignal.SERVICE.getPriceL3();
                    }
                    tmp.upgrade();
                    bigCityJFrame.addMoney(-price);
                    
                    textLabel.setText(String.valueOf(nextPrice)+"$");
                    System.out.println(nextPrice);
                    bigCityJFrame.validate();
                    bigCityJFrame.repaint();
                    bStat.updateStats();
                }
                if (tmp.getLevel() == 3) {
                    textLabel.setText("-");
                }
                bStat.updateStats();
                
                bigCityJFrame.getEngine().moveEveryOne();
                bigCityJFrame.repaint();
            });
        }
        upgradeButton.setBackground(Color.CYAN);

        JButton destroyButton = new JButton();
        Image destroyImg = null;
        try {
            destroyImg = ResourceLoader.loadBufferedImage("GUI/destroy.png");
        } catch (IOException ex) {
            Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        ImageIcon destroyIcon = new ImageIcon(destroyImg);
        //upgradeButton.add(label);
        JLabel destroyLabel = new JLabel(destroyIcon);
        destroyButton.add(destroyLabel);
        destroyButton.setPreferredSize(new Dimension(80,40));
        destroyButton.addActionListener((ActionEvent e) -> {
            bigCityJFrame.getEngine().destroyZone(
                    zone.getTopLeftY() / bigCityJFrame.getFieldSize(),
                    zone.getTopLeftX() / bigCityJFrame.getFieldSize(),
                    bigCityJFrame.getFieldSize(),
                    false);
            Engine.setCursorSignal(CursorSignal.SELECT);
            bigCityJFrame.changeRightPanelToBuildPanel();
            bigCityJFrame.repaint();
            grid.removeTheSelectionOfTheSelectedZone();
        });
        destroyButton.setBackground(new Color(233, 78, 61));
         destroyButton.setOpaque(false); // Set the button to be transparent
        destroyButton.setContentAreaFilled(false); // Set the content area to be transparent
        destroyButton.setBorderPainted(false); // Remove the button border

        upgradePanel.setLayout(new BoxLayout(upgradePanel, BoxLayout.X_AXIS));
        upgradePanel.setBackground(Color.GREEN.brighter());
        upgradePanel.add(Box.createHorizontalGlue());
        upgradePanel.add(upgradeButton);
        upgradePanel.add(Box.createHorizontalGlue());
        upgradePanel.add(destroyButton);
        upgradePanel.add(Box.createHorizontalGlue());

        this.add(upgradePanel);

    }

    public Zone getZone() {
        return zone;
    }

    public PersonsPanel getpPanel() {
        return pPanel;
    }

    public BuildingStat getbStat() {
        return bStat;
    }

}
