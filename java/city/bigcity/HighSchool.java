package bigcity;

import res.Assets;

public class HighSchool extends School {
    /**
     * Gives high school education if possible.
     *
     * @param person who gets educated
     * @return true if the education was successful
     */
    @Override
    public boolean educate(Person person) {
        if (EducationLevel.PRIMARY_SCHOOL == person.getEducationLevel()) {
            person.educate();
            return true;
        }
        return false;
    }

    public HighSchool(int topLeftX, int topLeftY, int maintenanceCost) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;

        level = 1;
        img = Assets.highSchool;
        this.maintenanceCost = maintenanceCost;
    }

}
