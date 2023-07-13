package grid;

import bigcity.Industry;
import bigcity.Police;
import bigcity.PrivateZone;
import bigcity.Residence;
import bigcity.Stadium;
import bigcity.Zone;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import model.Coords;
import model.CursorSignal;
import model.Engine;
import res.Assets;
import view.BigCityJframe;

public class Grid extends JPanel {

    //The size of a field in px. (square)
    /*
    The pixels start with the index 0. If the whole map is just one field 
    with a size of 50 pixels, then the range is 0-49.
     */
    private int fieldSize;
    //The number of columns.
    private int width;
    //The number of rows.
    private int height;

    private int mousePositionX;
    private int mousePositionY;

    //The last field's indexes. The cursor is on this field.
    private int oldRowIndex;
    private int oldColumnIndex;

    private Engine engine;

    private BigCityJframe bigCityJFrame;

    private Zone selectedZone = null;

    private ArrayList<ImagesWithCoords> images;

    public Grid(int fieldSize, int width, int height, Engine engine,
            BigCityJframe bifCityJFrame) {
        this.fieldSize = fieldSize;
        this.width = width;
        this.height = height;
        this.engine = engine;
        this.bigCityJFrame = bifCityJFrame;

        mousePositionX = -1;
        mousePositionY = -1;

        images = new ArrayList<>();

        setPreferredSize(new Dimension(width * fieldSize, height * fieldSize));
        setBackground(new Color(34, 177, 77));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (engine.getCursorSignal() == CursorSignal.SELECT) {

                    int row = mousePositionY / fieldSize;
                    int column = mousePositionX / fieldSize;
                    Zone target = engine.getCell(row, column);
                    if (null != target) {
                        bigCityJFrame.changeRightPanelToStatPanel(target);
                        selectedZone = target;
                        repaint();
                    }
                } else if (engine.getCursorSignal() == CursorSignal.DESTROY) {
                    if (engine.destroyZone(mousePositionY / fieldSize,
                            mousePositionX / fieldSize, fieldSize,
                            false)) {
                        repaint();
                    }
                } else {
                    if (engine.build(mousePositionY / fieldSize,
                            mousePositionX / fieldSize, fieldSize, false)) {
                        repaint();
                    }
                }
                bigCityJFrame.refreshMoney();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mousePositionX = e.getX();
                mousePositionY = e.getY();

                int newColumnIndex = mousePositionX / fieldSize;
                int newRowIndex = mousePositionY / fieldSize;
                //Repaint only if the cursor moves to another field.
                if (newColumnIndex != oldColumnIndex
                        || newRowIndex != oldRowIndex) {
                    oldColumnIndex = newColumnIndex;
                    oldRowIndex = newRowIndex;
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                //No hover shown.
                mousePositionX = -1;
                mousePositionY = -1;
                repaint();
                oldColumnIndex = -1;
                oldRowIndex = -1;
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                oldRowIndex = e.getY() / fieldSize;
                oldColumnIndex = e.getX() / fieldSize;
                repaint();
            }
        });

    }

    /**
     * Paints the built zones, range of zones and the hover of the mouse.
     *
     * @param g The canvas we paint on.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                if (null != engine.getCell(row, column)) {
                    changeImageAccordingSaturationAndLevel(
                            engine.getCell(row, column));

                    g2.drawImage(engine.getCell(row, column).getImg(),
                            engine.getCell(row, column).getTopLeftX(),
                            engine.getCell(row, column).getTopLeftY(),
                            fieldSize * engine.getCell(row, column).
                                    getCursorSignal().getWidth(),
                            fieldSize * engine.getCell(row, column).
                                    getCursorSignal().getHeight(),
                            null);
                }
            }
        }

        images.forEach(image -> g2.drawImage(image.getImage(),
                image.getCoords().getX() * fieldSize,
                image.getCoords().getY() * fieldSize,
                fieldSize,
                fieldSize,
                null)
        );

        if (mousePositionX != -1 && mousePositionY != -1) {
            int rowStart = mousePositionY / fieldSize;
            int rowEnd = mousePositionY / fieldSize
                    + engine.getCursorSignal().getHeight() - 1;
            int columnStart = mousePositionX / fieldSize;
            int columnEnd = mousePositionX / fieldSize
                    + engine.getCursorSignal().getWidth() - 1;

            //Hover.
            if (engine.getCursorSignal() == CursorSignal.DESTROY) {
                //Transparent orange for hover.
                g2.setColor(new Color(1f, 0.647f, 0f, 0.8f));
                paintArea(rowStart, rowEnd, columnStart, columnEnd, g2);
            } else if (engine.getCursorSignal() == CursorSignal.SELECT) {
                //Transparent yellow-gold for hover.
                g2.setColor(new Color(0.98f, 0.843f, 0f, 0.8f));
                paintArea(rowStart, rowEnd, columnStart, columnEnd, g2);
            } else {
                //Zone type selected to build.
                //Transparent blue for hover.
                g2.setColor(new Color(0f, 0f, 1f, 0.125f));
                //Can't build on the position of the mouse. Change color to red.
                if (false == engine.areaInsideGridAndFree(rowStart, rowEnd,
                        columnStart, columnEnd)) {
                    //Transparent red for hover.
                    g2.setColor(new Color(1f, 0f, 0f, 0.6f));
                }
                //Draw hover rectangle.
                paintArea(rowStart, rowEnd, columnStart, columnEnd, g2);
            }

        }

        paintHoverAroundTheZoneWithRange(g2);
    }

    /**
     * Paints the hover around some zones when selected or the cursor is above
     * them. Some zones(Police, Stadium, Industry) have a range affecting the
     * happiness of people.
     *
     * @param g2 The canvas we paint on.
     */
    private void paintHoverAroundTheZoneWithRange(Graphics2D g2) {
        ArrayList<Coords> fieldsInsideRange = new ArrayList<>();

        if (null != selectedZone) {
            if (selectedZone instanceof Industry) {
                fieldsInsideRange = engine.findCoordsInsideRange(selectedZone, Industry.range);
            } else if (selectedZone instanceof Police) {
                fieldsInsideRange = engine.findCoordsInsideRange(
                        selectedZone, Police.range);
            } else if (selectedZone instanceof Stadium) {
                fieldsInsideRange = engine.findCoordsInsideRange(
                        selectedZone, Stadium.range);
            }
        }

        //Transparent yellow for hover.
        g2.setColor(new Color(0.98f, 0.843f, 0f, 0.5f));

        if (!fieldsInsideRange.isEmpty()) {
            for (Coords coords : fieldsInsideRange) {
                g2.fillRect(coords.getX() * fieldSize, coords.getY() * fieldSize,
                        fieldSize, fieldSize);
            }
            return;
        }

        //The mouse exited the grid.
        if (mousePositionX == -1 || mousePositionY == -1) {
            return;
        }

        Zone target = engine.getCell(mousePositionY / fieldSize,
                mousePositionX / fieldSize);
        int row = mousePositionY / fieldSize * fieldSize;
        int column = mousePositionX / fieldSize * fieldSize;

        if (engine.getCursorSignal() == CursorSignal.INDUSTRY) {
            fieldsInsideRange = engine.findCoordsInsideRange(new Industry(column, row, -1),
                    Industry.range);
        } else if (engine.getCursorSignal() == CursorSignal.POLICE) {
            fieldsInsideRange = engine.findCoordsInsideRange(
                    new Police(column, row, -1),
                    Police.range);
        } else if (engine.getCursorSignal() == CursorSignal.STADIUM) {
            fieldsInsideRange = engine.findCoordsInsideRange(
                    new Stadium(column, row, -1),
                    Stadium.range);
        } else if (target != null) {
            if (engine.getCursorSignal() == CursorSignal.SELECT
                    || engine.getCursorSignal() == CursorSignal.DESTROY) {
                if (target instanceof Industry) {
                    fieldsInsideRange = engine.findCoordsInsideRange(target, Industry.range);
                } else if (target instanceof Police) {
                    fieldsInsideRange = engine.findCoordsInsideRange(
                            target, Police.range);
                } else if (target instanceof Stadium) {
                    fieldsInsideRange = engine.findCoordsInsideRange(
                            target, Stadium.range);
                }
            } else { //The target zone doesn't have range.
                return;
            }

        } else {
            return;
        }

        for (Coords coords : fieldsInsideRange) {
            g2.fillRect(coords.getX() * fieldSize, coords.getY() * fieldSize,
                    fieldSize, fieldSize);
        }

    }

    /**
     * Call this function if you want to remove the hover around (fields inside
     * the selected zone's range painted with transparent colour) the selected
     * zone.
     */
    public void removeTheSelectionOfTheSelectedZone() {
        selectedZone = null;
        repaint();
    }

    /**
     * Sets the image of a zone according its level and the number of people
     * live or work on it.
     *
     * @param zone Its image will change on the grid.
     */
    private void changeImageAccordingSaturationAndLevel(Zone zone) {
        if (zone instanceof PrivateZone privateZone) {
            int level = zone.getLevel();
            int capacity;
            int size;
            if (zone instanceof Residence residence) {
                capacity = residence.getCapacity();
                size = residence.getSize();
                switch (level) {
                    case 1 -> {
                        if (0 == size) {
                            zone.setImg(Assets.copperR);
                        } else if (0 < size && size < capacity / 2) {
                            zone.setImg(Assets.house);
                        } else {//capacity / 2 <= size
                            zone.setImg(Assets.houses);
                        }
                    }
                    case 2 -> {
                        if (0 == size) {
                            zone.setImg(Assets.silverR);
                        } else if (0 < size && size < capacity / 2) {
                            zone.setImg(Assets.bigHouse);
                        } else {//capacity / 2 <= size
                            zone.setImg(Assets.bigHouses);
                        }
                    }
                    case 3 -> {
                        if (0 == size) {
                            zone.setImg(Assets.goldR);
                        } else if (0 < size && size < capacity / 2) {
                            zone.setImg(Assets.panel);
                        } else {//capacity / 2 <= size
                            zone.setImg(Assets.panels);
                        }
                    }
                    default -> {
                    }
                }
            } else if (zone instanceof Industry industry) {
                capacity = industry.getCapacity();
                size = industry.getSize();
                switch (level) {
                    case 1 -> {
                        if (0 == size) {
                            zone.setImg(Assets.copperI);
                        } else if (0 < size && size < capacity / 2) {
                            zone.setImg(Assets.ranch);
                        } else {//capacity / 2 <= size
                            zone.setImg(Assets.ranches);
                        }
                    }
                    case 2 -> {
                        if (0 == size) {
                            zone.setImg(Assets.silverI);
                        } else if (0 < size && size < capacity / 2) {
                            zone.setImg(Assets.mine);
                        } else {//capacity / 2 <= size
                            zone.setImg(Assets.mines);
                        }
                    }
                    case 3 -> {
                        if (0 == size) {
                            zone.setImg(Assets.goldI);
                        } else if (0 < size && size < capacity / 2) {
                            zone.setImg(Assets.factory);
                        } else {//capacity / 2 <= size
                            zone.setImg(Assets.factories);
                        }
                    }
                    default -> {
                    }
                }
            } else {//Service
                capacity = privateZone.getCapacity();
                size = privateZone.getSize();
                switch (level) {
                    case 1 -> {
                        if (0 == size) {
                            zone.setImg(Assets.copperS);
                        } else if (0 < size && size < capacity / 2) {
                            zone.setImg(Assets.shop);
                        } else {//capacity / 2 <= size
                            zone.setImg(Assets.shops);
                        }
                    }
                    case 2 -> {
                        if (0 == size) {
                            zone.setImg(Assets.silverS);
                        } else if (0 < size && size < capacity / 2) {
                            zone.setImg(Assets.bigShop);
                        } else {//capacity / 2 <= size
                            zone.setImg(Assets.bigShops);
                        }
                    }
                    case 3 -> {
                        if (0 == size) {
                            zone.setImg(Assets.goldS);
                        } else if (0 < size && size < capacity / 2) {
                            zone.setImg(Assets.mall);
                        } else {//capacity / 2 <= size
                            zone.setImg(Assets.malls);
                        }
                    }
                    default -> {
                    }
                }
            }
        }
    }

    public int getFieldSize() {
        return fieldSize;
    }

    /**
     * Created to test the draw performance. Every field will have an image.
     */
    public void fillGrid() {
        Engine.setCursorSignal(CursorSignal.ROAD);
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                engine.build(row, column, fieldSize, false);
            }
        }
        repaint();
    }

    /**
     * Paints fields with transparent colours. When a zone is selected all of
     * its fields gets coloured.
     *
     * @param rowStart is the top Y coordinate of the zone
     * @param rowEnd is the bottom Y coordinate of the zone
     * @param columnStart is the left X coordinate of the zone
     * @param columnEnd is the right X coordinate of the zone
     * @param g2 is used for to draw on it
     */
    private void paintArea(int rowStart, int rowEnd,
            int columnStart, int columnEnd, Graphics2D g2) {
        if (CursorSignal.SELECT == engine.getCursorSignal()
                || CursorSignal.DESTROY == engine.getCursorSignal()) {
            Zone target = engine.getCell(rowStart, columnStart);
            if (null != target) {
                rowEnd = target.getTopLeftY() / fieldSize
                        + target.getCursorSignal().getHeight() - 1;
                rowStart = target.getTopLeftY() / fieldSize;
                columnEnd = target.getTopLeftX() / fieldSize
                        + target.getCursorSignal().getWidth() - 1;
                columnStart = target.getTopLeftX() / fieldSize;
            }
        }
        for (int row = rowStart; row <= rowEnd; row++) {
            for (int column = columnStart; column <= columnEnd; column++) {
                if (row < height && column < width) {
                    g2.fillRect(column * fieldSize,
                            row * fieldSize,
                            fieldSize,
                            fieldSize);
                }
            }
        }
    }

    public void placeImage(int row, int column, BufferedImage image) {
        images.add(new ImagesWithCoords(image, new Coords(column, row)));
        this.repaint();
    }

    public void removeImage(int row, int column) {
        images.removeIf(image -> image.getCoords().getX() == column
                && image.getCoords().getY() == row);
        this.repaint();
    }

}
