package rightPanel.personsPanel;

import static res.ResourceLoader.scaleImage;
import bigcity.Person;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import roundPanel.RoundedPanel;

public class PersonStat extends JPanel{
    public Person person;
    private static BufferedImage male = scaleImage(14, 32, "rightPanel/images/man.png");
    private static BufferedImage female = scaleImage(14, 32, "rightPanel/images/woman.png");
    
    /**
     * Constructor
     * @param p - Person, the person about which the statistics are created
     */
    public PersonStat(Person p){
        setBackground(p.isMale() ? Color.CYAN : Color.PINK);
        person = p;
        JPanel panel = new JPanel();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(person.isMale() ? Color.CYAN : Color.PINK);
        panel.add(new JLabel(person.getName()));
        panel.add(new JLabel(String.valueOf(person.getAge())));
        panel.add(new JLabel("Műveltség: " + person.getEducationLevel().getLevel()));
        this.add(new JLabel(new ImageIcon(person.isMale() ? male : female)));
        this.add(Box.createRigidArea(new Dimension(10,0)));
        this.add(panel);
        this.add(Box.createRigidArea(new Dimension(20,0)));
        this.add(Box.createHorizontalGlue());
        JLabel happiness = new JLabel(person.getIntegerHappiness()+"%");
        happiness.setFont(new Font("Arial", Font.BOLD, 13));
        happiness.setForeground(Color.MAGENTA.darker());
        this.add(happiness);
        setPreferredSize(new Dimension(100, 50));
    }   
}
