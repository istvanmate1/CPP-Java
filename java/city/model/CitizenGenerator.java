package model;

import bigcity.EducationLevel;
import bigcity.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CitizenGenerator {

    private List<String> maleNames;
    private List<String> femaleNames;

    public CitizenGenerator() {

        femaleNames = new ArrayList<>();
        femaleNames.add("Olivia");
        femaleNames.add("Emma");
        femaleNames.add("Charlotte");
        femaleNames.add("Amelia");
        femaleNames.add("Ava");
        femaleNames.add("Sophia");
        femaleNames.add("Isabella");
        femaleNames.add("Mia");
        femaleNames.add("Evelyn");
        femaleNames.add("Harper");

        maleNames = new ArrayList<>();
        maleNames.add("Liam");
        maleNames.add("Noah");
        maleNames.add("Oliver");
        maleNames.add("Elijah");
        maleNames.add("James");
        maleNames.add("William");
        maleNames.add("Benjamin");
        maleNames.add("Lucas");
        maleNames.add("Henry");
        maleNames.add("Theodore");
    }

    /**
     * Generates a person with random name, age and sex. The education level 
     * will be set to EducationLevel.PRIMARY_SCHOOL. The generated person won't
     * have job nor home.
     *
     * @return Person object.
     */
    public Person createCitizen() {

        Random random = new Random();

        //0: female, 1: male
        boolean male = random.nextInt(2) != 0;

        int minAge = 18;
        int maxAge = 60;
        int age = random.nextInt(maxAge - minAge + 1) + minAge;

        String name;
        if (male) {
            name = maleNames.get(random.nextInt(maleNames.size()));
        } else {
            name = femaleNames.get(random.nextInt(femaleNames.size()));
        }

        return new Person(
                name,
                age,
                100.0,
                male,
                EducationLevel.PRIMARY_SCHOOL,
                null,
                null
        );
    }
}
