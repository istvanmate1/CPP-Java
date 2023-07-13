package grid;

import java.awt.image.BufferedImage;
import model.Coords;

public class ImagesWithCoords {

    private BufferedImage image;
    private Coords coords;

    public ImagesWithCoords(BufferedImage image, Coords coords) {
        this.image = image;
        this.coords = coords;
    }

    public Coords getCoords() {
        return coords;
    }

    public BufferedImage getImage() {
        return image;
    }

}
