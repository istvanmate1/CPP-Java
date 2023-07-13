package roundPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder;

public class RoundedBorder extends AbstractBorder {
    
    private Insets insets;
    private BasicStroke stroke;
    private Color color;
    private int thickness;
    private int radii;
    private int strokePadding;

    /*
    public RoundedBorder(Color color) {
        new RoundedBorder(color, 4, 8);
    }
    */

    /**
     * Constructor
     * @param color     - Color, color of the graphics context
     * @param thickness - int, thickness of the border
     * @param radii     - int, rounding value
     */
    public RoundedBorder(Color color, int thickness, int radii) {
        strokePadding = thickness / 2;
        int padding = radii + strokePadding;
        insets = new Insets(0, padding, 0, padding);
        stroke = new BasicStroke(thickness);
        this.color = color;
        this.thickness = thickness;
        this.radii = radii;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        RoundRectangle2D.Double oval = new RoundRectangle2D.Double(strokePadding, strokePadding, 
            width - thickness, height - thickness, radii*2, radii*2);

        Area area = new Area(oval);
        Area spareSpace = new Area(new Rectangle(0, 0, width, height));
        spareSpace.subtract(area);

        g2.setClip(spareSpace);
        g2.clearRect(0, 0, width, height);
        g2.setClip(null);

        Component parent  = c.getParent();
        if (parent!=null) {
            Color bg = parent.getBackground();
            Area borderRegion = new Area(new Rectangle(0,0,width, height));
            borderRegion.subtract(area);
            g2.setClip(borderRegion);
            g2.setColor(bg);
            g2.fillRect(0, 0, width, height);
            g2.setClip(null);
        }

        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);
    }
}
