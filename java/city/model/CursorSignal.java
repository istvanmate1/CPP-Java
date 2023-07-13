package model;

/**
 * The indicator of the user's current activity.
 */
public enum CursorSignal {
    //Private zones.
    POLICE(1, 1, 300, 0, 0),
    STADIUM(2, 2, 300, 0, 0),
    HIGH_SCHOOL(2, 1, 300, 0, 0),
    UNIVERSITY(2, 2, 500, 0, 0),
    ROAD(1, 1, 50, 0, 0),
    //Public zones.
    RESIDENCE(1, 1, 50, 100, 300),
    INDUSTRY(1, 1, 100, 300, 1000),
    SERVICE(1, 1, 50, 100, 300),
    //User activities.
    DESTROY(1, 1, 0, 0, 0),
    SELECT(1, 1, 0, 0, 0);

    private int width;
    private int height;

    private int priceL1;
    private int priceL2;
    private int priceL3;

    private CursorSignal(int width, int height, int priceL1, int priceL2,
            int priceL3) {
        this.width = width;
        this.height = height;
        this.priceL1 = priceL1;
        this.priceL2 = priceL2;
        this.priceL3 = priceL3;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPriceL1() {
        return priceL1;
    }

    public int getPriceL2() {
        return priceL2;
    }

    public int getPriceL3() {
        return priceL3;
    }

}
