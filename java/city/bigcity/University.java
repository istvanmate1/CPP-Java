package bigcity;

import res.Assets;

public class University extends School {
    /**
     * Gives university education if possible.
     *
     * @param person who gets educated
     * @return true if the education was successful
     */
    @Override
    public boolean educate(Person person) {
        if (EducationLevel.HIGH_SCHOOL == person.getEducationLevel()) {
            person.educate();
            return true;
        }
        return false;
    }

    public University(int topLeftX, int topLeftY, int maintenanceCost) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        level = 1;
        img = Assets.university;
        this.maintenanceCost = maintenanceCost;
    }

}
