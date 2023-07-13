package model;

public class Coords {

    //column fo grid 
    private int x;
    //row of grid
    private int y;
    //Created for breadth-first search. Indicates the current depth.
    private int step;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
        step = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

}
