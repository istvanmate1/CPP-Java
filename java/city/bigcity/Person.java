package bigcity;

public class Person {

    protected String name;
    protected int age;
    protected double happiness;
    protected EducationLevel educationLevel;
    protected Zone home;
    protected Zone job;
    protected boolean male;

    //-1 means that this person doesn't have a job.
    protected int homeJobDistance = -1;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getHappiness() {
        return happiness;
    }
    
    public int getIntegerHappiness() {
        return (int)Math.round(happiness);
    }

    public EducationLevel getEducationLevel() {
        return educationLevel;
    }

    public boolean isMale() {
        return male;
    }

    public double changeHappinessBy(double value) {
        happiness += value;
        happinessGuard();
        return happiness;
    }

    /**
     * Increases the education level if possible
     */
    public void educate() {
        if (EducationLevel.UNIVERSITY == educationLevel) {
            return;
        }
        if (EducationLevel.HIGH_SCHOOL == educationLevel) {
            educationLevel = EducationLevel.UNIVERSITY;
            return;
        }
        if (EducationLevel.PRIMARY_SCHOOL == educationLevel) {
            educationLevel = EducationLevel.HIGH_SCHOOL;
        }
    }

    public int growOlder() {
        age += 1;
        return age;
    }

    public Person(String name, int age, double happiness, boolean male,
            EducationLevel educationLevel, Zone home, Zone job) {
        this.name = name;
        //Min 18.
        this.age = age;
        this.happiness = happiness;
        this.male = male;
        this.educationLevel = educationLevel;
        this.home = home;
        this.job = job;
        happinessGuard();
    }

    /**
     * Simulates the death of the person as setting their age to 18
     */
    public void die() {
        age = 18;
        educationLevel = EducationLevel.PRIMARY_SCHOOL;
    }
    
    public Zone getHome() {
        return home;
    }

    public Zone getJob() {
        return job;
    }

    public void setHome(Zone home) {
        this.home = home;
    }

    public void setJob(Zone job) {
        this.job = job;
    }

    public void setHomeJobDistance(int homeJobDistance) {
        this.homeJobDistance = homeJobDistance;
    }

    public int getHomeJobDistance() {
        return homeJobDistance;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
        happinessGuard();
    }
    
    private void happinessGuard() {
        if(happiness > 100)
            happiness = 100;
        else if(happiness < 0)
            happiness = 0;
    }
}
