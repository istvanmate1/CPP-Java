package bigcity;

public enum EducationLevel {
    PRIMARY_SCHOOL(1),
    HIGH_SCHOOL(2),
    UNIVERSITY(3);
    
    protected int level;
    private EducationLevel(int level) {
        this.level = level;
    }
    
    public int getLevel() {
        return level;
    }
}
