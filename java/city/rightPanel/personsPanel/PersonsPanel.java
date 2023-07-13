package rightPanel.personsPanel;

import bigcity.Person;
import java.awt.Dimension;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PersonsPanel extends JPanel{
    protected Dimension preferedDimension;
    protected List<Person> people;
    private static final int SINGLESIZE = 50;
    
    /**
     * Constructor
     * @param persons - Person collection, persons that the statistic panel is about
     */
    public PersonsPanel(Person... persons) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (Person person : persons) {
            add(new PersonStat(person));
            add(Box.createRigidArea(new Dimension(0,5)));
        }
        preferedDimension = new Dimension(150, persons.length*SINGLESIZE);
        setPreferredSize(preferedDimension);
        
    }
    
    /**
     * Constructor
     * @param persons - List, persons that the statistic panel is about
     */
    public PersonsPanel(List<Person> persons) {
        super();
        people = persons;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        updatePeople();
    }
    
    public Dimension getPreferedDimension(){
        Dimension tmp = new Dimension(preferedDimension);
        return tmp;
    }
    
    public void setDimension(Dimension d) {
        preferedDimension = d;
    }
    
    /**
     * Updates the displayed people
     */
    public void updatePeople() {
        removeAll();
        for (Person person : people) {
            add(new PersonStat(person));
            add(Box.createRigidArea(new Dimension(0,5)));
        }
        preferedDimension = new Dimension(130, people.size()*SINGLESIZE);
        setPreferredSize(preferedDimension);
        revalidate();
        repaint();
    }
}
