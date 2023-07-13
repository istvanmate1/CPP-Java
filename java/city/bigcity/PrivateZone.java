package bigcity;

public abstract class PrivateZone extends Zone {
    protected int capacity;

    public int getCapacity() {
        return capacity;
    }
    
    public abstract int getSize();
    
    /**
     * Upgrades the level of the zone, if it is possible
     * @return - int, the new level of the zone
     */
    public abstract int upgrade();
    public abstract void addPerson(Person p);
    
}
