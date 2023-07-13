
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class BoardPanel extends JPanel {

    private Motorcycle motorcycleArrow;
    private Motorcycle motorcycleWASD;

    private TronUI tronUI;

    public BoardPanel() {
        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.BLACK);
        motorcycleArrow = null;
        motorcycleWASD = null;
    }

    public BoardPanel(Motorcycle motorcycleArrow, Motorcycle motorcycleWASD,
            TronUI tronUI) {

        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.BLACK);

        this.motorcycleArrow = motorcycleArrow;
        this.motorcycleWASD = motorcycleWASD;

        this.tronUI = tronUI;
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        if (motorcycleArrow != null && motorcycleWASD != null) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setStroke(new BasicStroke(2));
            ///*
            motorcycleArrow.moveTheCurrentPoint();
            g2.setColor(motorcycleArrow.getLineColor());
            motorcycleArrow.renderLines(g2);
            //*/
            ///*//For second player.
            motorcycleWASD.moveTheCurrentPoint();
            g2.setColor(motorcycleWASD.getLineColor());
            motorcycleWASD.renderLines(g2);
            //*/

            if (motorcycleArrow.draw(motorcycleWASD)) {
                tronUI.getTimeTimer().stop();
                tronUI.getRenderTimer().stop();
                tronUI.setActualTime(0);
                tronUI.removeKeyListener(tronUI.getKeyAdapter());
                tronUI.getPlayerInfoTitle().setText("Draw");
            } else if (motorcycleWASD.crashing(motorcycleArrow)) {
                tronUI.getTimeTimer().stop();
                tronUI.getRenderTimer().stop();
                tronUI.setActualTime(0);
                tronUI.removeKeyListener(tronUI.getKeyAdapter());
                //tronUI.getDatabase().increasePlayerScore(motorcycleArrow.getPlayerName());
                tronUI.getPlayerInfoTitle().setText("Winner: " + motorcycleArrow.getPlayerName());
            } else if (motorcycleArrow.crashing(motorcycleWASD)) {
                tronUI.getTimeTimer().stop();
                tronUI.getRenderTimer().stop();
                tronUI.setActualTime(0);
                tronUI.removeKeyListener(tronUI.getKeyAdapter());
                //tronUI.getDatabase().increasePlayerScore(motorcycleWASD.getPlayerName());
                tronUI.getPlayerInfoTitle().setText("Winner: " + motorcycleWASD.getPlayerName());
            }

        }

    }

    /**
     * The bridge between the input listener and the input processor.
     *
     */
    public void keyBoardInput(int keyCode) {
        String arrowNewDirection = null;
        String wasdNewDirection = null;
        if (keyCode == KeyEvent.VK_RIGHT) {
            arrowNewDirection = "right";
        } else if (keyCode == KeyEvent.VK_UP) {
            arrowNewDirection = "up";
        } else if (keyCode == KeyEvent.VK_LEFT) {
            arrowNewDirection = "left";
        } else if (keyCode == KeyEvent.VK_DOWN) {
            arrowNewDirection = "down";
        } else if (keyCode == KeyEvent.VK_D) {
            wasdNewDirection = "right";
        } else if (keyCode == KeyEvent.VK_W) {
            wasdNewDirection = "up";
        } else if (keyCode == KeyEvent.VK_A) {
            wasdNewDirection = "left";
        } else if (keyCode == KeyEvent.VK_S) {
            wasdNewDirection = "down";
        }

        if (arrowNewDirection != null) {
            motorcycleArrow.changeDirection(arrowNewDirection);
        }
        if (wasdNewDirection != null) {
            motorcycleWASD.changeDirection(wasdNewDirection);
        }
    }
}
