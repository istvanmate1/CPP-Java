package bigcity;

import java.util.List;

public abstract class Workplace extends PrivateZone {

    protected List<Person> workers;

    public List<Person> getWorkers() {
        return workers;
    }

    @Override
    public void addPerson(Person p) {
        if (capacity > getSize()) {
            workers.add(p);
        }
    }

    public void clearWorkers() {
        workers.clear();
    }
}
