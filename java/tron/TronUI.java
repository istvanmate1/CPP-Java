
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class TronUI extends JFrame {

    private BoardPanel board;
    private final JPanel timePanel;
    private final JLabel timeLabel;

    private final Colour[] colors = {
        new Colour("White", Color.white),
        new Colour("Red", Color.red),
        new Colour("Green", Color.green),
        new Colour("Blue", Color.blue),
        new Colour("Yellow", Color.yellow),
        new Colour("Magenta", Color.magenta),
        new Colour("Cyan", Color.cyan),
        new Colour("Orange", Color.ORANGE)};

    public Timer getTimeTimer() {
        return timeTimer;
    }

    public JLabel getPlayerInfoTitle() {
        return playerInfoTitle;
    }

    public Timer getRenderTimer() {
        return renderTimer;
    }

    //Information about which color belongs to the motorcycles controled by    
    // the arrows and the WASD keys.
    private final JPanel playerInfoPanel;
    private final JLabel playerInfoTitle;
    private final JLabel playerInfoArrows;
    private final JLabel playerInfoWASDKeys;

    private Timer timeTimer;
    private Timer renderTimer;

    private Database database;

    public TronUI() {
        super("Tron");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //database = new Database();

        playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new BorderLayout());

        playerInfoTitle = new JLabel("Control Keys / Color");
        playerInfoTitle.setHorizontalAlignment(JLabel.CENTER);
        playerInfoTitle.setFont(new Font("Arial", Font.PLAIN, 20));
        playerInfoPanel.add(playerInfoTitle, BorderLayout.NORTH);

        //The color will be changed after newGame button.
        playerInfoArrows = new JLabel("Arrows / Blue");
        playerInfoArrows.setHorizontalAlignment(JLabel.CENTER);
        playerInfoArrows.setFont(new Font("Arial", Font.PLAIN, 20));
        playerInfoPanel.add(playerInfoArrows, BorderLayout.EAST);

        //The color will be changed after newGame button.
        playerInfoWASDKeys = new JLabel("WASD / Orange");
        playerInfoWASDKeys.setHorizontalAlignment(JLabel.CENTER);
        playerInfoWASDKeys.setFont(new Font("Arial", Font.PLAIN, 20));
        playerInfoPanel.add(playerInfoWASDKeys, BorderLayout.WEST);

        add(playerInfoPanel, BorderLayout.NORTH);

        board = new BoardPanel();
        add(board, BorderLayout.CENTER);

        timePanel = new JPanel();
        timePanel.setLayout(new BorderLayout());
        timeLabel = new JLabel("0s");
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timePanel.add(timeLabel, BorderLayout.CENTER);
        add(timePanel, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu start = new JMenu("Start");
        menuBar.add(start);

        JMenuItem newGame = new JMenuItem("New game");

        //Accelerator key: space.
        newGame.setAccelerator(KeyStroke.getKeyStroke(' '));

        newGame.addActionListener(new gameStartAction(this));
        start.add(newGame);

        //Database
        /*
        JMenuItem topPlayers = new JMenuItem(new AbstractAction("Top 10 players") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HighScoreWindow(database.getTopTenScores(),
                        TronUI.this);
            }
        });
        */

        //start.add(topPlayers);
        setJMenuBar(menuBar);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private int actualTime = 0;

    public void setActualTime(int actualTime) {
        this.actualTime = actualTime;
    }

    private final NewGameDialog newGameDialog = new NewGameDialog(this,
            "Name and color chooser", colors);

    class gameStartAction implements ActionListener {

        private final TronUI tronUI;

        public gameStartAction(TronUI tronUI) {
            this.tronUI = tronUI;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            //Without these two "if" statements a bug would occur.
            //bug: Click on "New game" several times before collision.
            //The motorcycle will move faster. The time will pass faster.
            if (timeTimer != null) {
                timeTimer.stop();
                actualTime = 0;
            }
            if (renderTimer != null) {
                renderTimer.stop();
            }

            newGameDialog.setVisible(true);
            if (newGameDialog.getButtonCode() != OKCancelDialog.OK) {
                return;
            }

            //If names are the same, colors are the same 
            // or any of them empty then error.
            /*
            else if(){
                //errorDialog
                //gets the error message
                //For example: The colors can't be the same.
                //...
                
                //The new game dialog should save the wrong input.
                //So the user can see what was wrong.
                
                //Or after the error dialog was closed we won't close the new
                // game dialog. Its content will be the same.
                
                //return needed
            }
             */
            //UI info
            playerInfoArrows.setText(newGameDialog.getArrowPlayerName() + " / "
                    + newGameDialog.getArrowColor().getName());
            playerInfoWASDKeys.setText(newGameDialog.getWasdPlayerName() + " / "
                    + newGameDialog.getWasdColor().getName());
            playerInfoTitle.setText("Player Names / Line Colors");

            //Database
            /*
            database.storeNewPlayer(newGameDialog.getArrowPlayerName());
            database.storeNewPlayer(newGameDialog.getWasdPlayerName());
            */

            timeTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ++actualTime;
                    timeLabel.setText(actualTime + "s");
                }
            });
            timeTimer.start();
            ///*//basic start 
            board = new BoardPanel(
                    new Motorcycle(newGameDialog.getArrowPlayerName(),
                            newGameDialog.getArrowColor().getColor(),
                            new Point(490, 250), Direction.LEFT, 0),
                    new Motorcycle(newGameDialog.getWasdPlayerName(),
                            newGameDialog.getWasdColor().getColor(),
                            new Point(10, 250), Direction.RIGHT, 0),
                    tronUI);
             //*/
 /*//test start 
            board = new BoardPanel(
                    new Motorcycle(newGameDialog.getArrowPlayerName(),
                            newGameDialog.getArrowColor().getColor(),
                            new Point(250, 10), Direction.DOWN, 0),
                    new Motorcycle(newGameDialog.getWasdPlayerName(),
                            newGameDialog.getWasdColor().getColor(),
                            new Point(250, 490), Direction.UP, 0),
                    tronUI);
             */
 /*//test start 
            board = new BoardPanel(
                    new Motorcycle(newGameDialog.getArrowPlayerName(),
                            newGameDialog.getArrowColor().getColor(),
                            new Point(250, 10), Direction.DOWN, 0),
                    new Motorcycle(newGameDialog.getWasdPlayerName(),
                            newGameDialog.getWasdColor().getColor(),
                            new Point(490, 250), Direction.LEFT, 0),
                    tronUI);
             */
            addKeyListener(keyAdapter);

            add(board, BorderLayout.CENTER);

            renderTimer = new Timer(30, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Will call the board's paintComponent method.
                    board.repaint();
                }
            });
            renderTimer.start();
        }
    }
    /*
    public Database getDatabase() {
        return database;
    }
    */

    private final KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent ke) {
            super.keyPressed(ke);
            int kk = ke.getKeyCode();
            board.keyBoardInput(kk);
        }
    };

    public KeyAdapter getKeyAdapter() {
        return keyAdapter;
    }
}
