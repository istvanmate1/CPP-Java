package model;

import bigcity.Residence;
import bigcity.Workplace;

public class ResidenceWorkplaceDistance
        implements Comparable<ResidenceWorkplaceDistance> {

    private Residence residence;
    private Workplace workplace;
    private int distance;

    public ResidenceWorkplaceDistance(Residence residence, Workplace workplace,
            int distance) {
        this.residence = residence;
        this.workplace = workplace;
        this.distance = distance;
    }

    @Override
    public int compareTo(ResidenceWorkplaceDistance other) {
        return this.distance - other.getDistance();
    }

    public int getDistance() {
        return distance;
    }

    public Residence getResidence() {
        return residence;
    }

    public Workplace getWorkplace() {
        return workplace;
    }

}
