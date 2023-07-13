
import java.awt.Color;

public class Colour {

    private Color color;
    private String name;

    public Colour(String name, Color color) {
        this.color = color;
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
