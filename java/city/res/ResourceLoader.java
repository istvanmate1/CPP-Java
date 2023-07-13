package res;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ResourceLoader {

    public static InputStream loadResource(String resName) {
        return ResourceLoader.class.getClassLoader().getResourceAsStream(resName);
    }

    public static Image loadImage(String resName) throws IOException {
        URL url = ResourceLoader.class.getClassLoader().getResource(resName);
        return ImageIO.read(url);
    }

    public static BufferedImage loadBufferedImage(String resName) throws IOException {
        URL url = ResourceLoader.class.getClassLoader().getResource(resName);
        return ImageIO.read(url);
    }

    /**
     * Opens a picture with the gives sizes
     * @param WIDTH     - int, width in pixels
     * @param HEIGHT    - int, height in pixels
     * @param filename  - String, the file name of the picture
     * @return          - BufferedImage, the opened and resized image
     */
    public static BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
        BufferedImage bi;
        try {
            ImageIcon ii = new ImageIcon(filename);
            bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D) bi.createGraphics();
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bi;
    }
}
