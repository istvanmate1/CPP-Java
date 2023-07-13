package bigcity;

import java.util.ArrayList;
import java.util.List;
import res.Assets;

public class Residence extends PrivateZone {

    protected List<Person> residents;

    public Residence(int topLeftX, int topLeftY, int price) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        //this.size = 0;
        this.capacity = 8;
        this.level = 1;
        this.img = Assets.copperR;
        this.residents = new ArrayList<>();
        this.price = price;
    }

    public List<Person> getResidents() {
        return residents;
    }

    @Override
    public void addPerson(Person p) {
        if (capacity > getSize()) {
            residents.add(p);
        }
    }

    @Override
    public int getSize() {
        return residents.size();
    }

    @Override
    public int upgrade() {
        if (getLevel() < 3) {
            capacity *= 2;
            level++;
        }
        return level;
    }

    /**
     * Removes all residents
     */
    public void clearResidents() {
        residents.clear();
    }

}
