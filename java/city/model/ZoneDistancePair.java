package model;

import bigcity.Zone;

/**
 * Created to find zones connected to a source zone with roads.
 */
public class ZoneDistancePair {

    private Zone zone;
    //Distance between a source zone and this class' zone on road.
    private int distance;

    public ZoneDistancePair(Zone zone, int distance) {
        this.zone = zone;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public Zone getZone() {
        return zone;
    }

}
