package view;

import GUI.MainMenu;
import GUI.SettingsDialog;
import GUI.StatElement;
import bigcity.TimeSpeed;
import bigcity.Zone;
import grid.Grid;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.border.Border;
import model.CursorSignal;
import model.Engine;
import res.Assets;
import res.ResourceLoader;
import rightPanel.BuildPanel;
import rightPanel.BuildButton;
import rightPanel.buildingStatPanel.BuildingStatPanel;

public class BigCityJframe extends JFrame {

    public static SimpleDateFormat dateFormater
            = new SimpleDateFormat("yyyy-MM-dd");

    protected Engine engine;

    protected JButton destroyZone;

    protected Timer timer;
    protected Date date;
    protected boolean isStopped;
    protected TimeSpeed timeSpeed;
    protected SettingsDialog settings;
    protected String cityName;

    protected JPanel topPanel;
    protected BuildPanel buildPanel;
    protected BuildingStatPanel statPanel;
    protected Grid grid;
    private final int fieldSize;

    protected StatElement calendar;//Attila volt
    protected StatElement money;
    protected StatElement happy;

    /**
     * Constructor
     *
     * @param cityname - String, name of the city
     * @param load - boolean, loading an existing city or not
     */
    public BigCityJframe(String cityname, boolean load) {
        super(cityname);
        this.cityName = cityname;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showExitDialog();
            }
        });
        //exit options
        ImageIcon icon = new ImageIcon("GUI/house.png");
        setIconImage(icon.getImage());

        timeSpeed = TimeSpeed.DAY;
        this.timer = new Timer(3000, (ActionEvent e) -> {
            if (!isStopped) {
                timerTicked();
            }
        });
        timer.start();
        isStopped = load;

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Játék");
        JMenuItem saveJMenuItem = new JMenuItem(
                new AbstractAction("Mentés") {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.saveGame();
            }
        });
        JMenuItem disasterJMenuItem = new JMenuItem(
                new AbstractAction("Katasztrófa kérése") {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.makeDisaster();
            }
        });
        JMenuItem settingsJMenuItem = new JMenuItem(
                new AbstractAction("Beállítások") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (null == settings) {
                    settings = new SettingsDialog(BigCityJframe.this);
                }
                settings.setActive();
            }
        });
        JMenuItem exitJMenuItem = new JMenuItem(
                new AbstractAction("Kilépés") {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExitDialog();
            }
        });
        gameMenu.add(saveJMenuItem);
        gameMenu.add(disasterJMenuItem);
        gameMenu.add(settingsJMenuItem);
        gameMenu.add(exitJMenuItem);
        menuBar.add(gameMenu);
        JMenu timeMenu = new JMenu("Idő");
        JMenuItem speed1 = new JMenuItem(new AbstractAction("1 nap") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isStopped) {
                    isStopped = false;
                }
                timeSpeed = TimeSpeed.DAY;
            }
        });
        JMenuItem speed3 = new JMenuItem(new AbstractAction("10 nap") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isStopped) {
                    isStopped = false;
                }
                timeSpeed = TimeSpeed.DAYS;
            }
        });
        JMenuItem speed5 = new JMenuItem(new AbstractAction("1 hónap") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isStopped) {
                    isStopped = false;
                }
                timeSpeed = TimeSpeed.MONTH;
            }
        });
        JMenuItem startStop = new JMenuItem(
                new AbstractAction("Megállít/Elindít") {
            @Override
            public void actionPerformed(ActionEvent e) {
                isStopped = !isStopped;
            }
        });
        timeMenu.add(speed1);
        timeMenu.add(speed3);
        timeMenu.add(speed5);
        timeMenu.add(startStop);
        menuBar.add(timeMenu);
        this.setJMenuBar(menuBar);

        setLayout(new BorderLayout());

        this.fieldSize = 50;
        int width = 10;
        int height = 10;

        new Assets();

        this.date = new Date(0);

        engine = load ? new Engine(cityname, this)
                : new Engine(width, height, this.fieldSize, this);

        grid = new Grid(fieldSize, width, height, engine, this);

        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setPreferredSize(new Dimension(-1, 50));
        topPanel.setBackground(Color.GREEN.darker().darker());

        happy = new StatElement("view/happiness.png", "100%");
        happy.setTextColor(Color.MAGENTA.darker());

        calendar = new StatElement("view/calendar.png", "2023-03-19");
        calendar.setText(dateFormater.format(date));

        money = new StatElement("view/money.png", String.format("%,d", engine.getMoney()) + "$");
        money.setTextColor(Color.YELLOW.darker());

        topPanel.add(money);
        topPanel.add(happy);
        topPanel.add(calendar);

        BuildButton lakohely = new BuildButton("buildPanel/images/house.png", "Lakóhely", CursorSignal.RESIDENCE.getPriceL1());
        BuildButton ipariTerulet = new BuildButton("buildPanel/images/factory.png", "Ipari terület", CursorSignal.INDUSTRY.getPriceL1());
        BuildButton szolgaltatas = new BuildButton("buildPanel/images/store.png", "Szolgáltatás", CursorSignal.SERVICE.getPriceL1());
        BuildButton ut = new BuildButton("buildPanel/images/road.png", "Út", CursorSignal.ROAD.getPriceL1());
        BuildButton rendorseg = new BuildButton("buildPanel/images/police.png", "Rendőrség", CursorSignal.POLICE.getPriceL1());
        BuildButton stadion = new BuildButton("buildPanel/images/stadium.png", "Stadion", CursorSignal.STADIUM.getPriceL1());
        BuildButton iskola = new BuildButton("buildPanel/images/school.png", "Iskola", CursorSignal.HIGH_SCHOOL.getPriceL1());
        BuildButton egyetem = new BuildButton("buildPanel/images/university.png", "Egyetem", CursorSignal.UNIVERSITY.getPriceL1());

        lakohely.addActionListener((ActionEvent e) -> {
            Engine.setCursorSignal(CursorSignal.RESIDENCE);
        });

        ipariTerulet.addActionListener((ActionEvent e) -> {
            Engine.setCursorSignal(CursorSignal.INDUSTRY);
        });

        szolgaltatas.addActionListener((ActionEvent e) -> {
            Engine.setCursorSignal(CursorSignal.SERVICE);
        });

        ut.addActionListener((ActionEvent e) -> {
            Engine.setCursorSignal(CursorSignal.ROAD);
        });

        rendorseg.addActionListener((ActionEvent e) -> {
            Engine.setCursorSignal(CursorSignal.POLICE);
        });

        stadion.addActionListener((ActionEvent e) -> {
            Engine.setCursorSignal(CursorSignal.STADIUM);
        });

        iskola.addActionListener((ActionEvent e) -> {
            Engine.setCursorSignal(CursorSignal.HIGH_SCHOOL);
        });

        egyetem.addActionListener((ActionEvent e) -> {
            Engine.setCursorSignal(CursorSignal.UNIVERSITY);
        });

        buildPanel = new BuildPanel(grid,
                lakohely,
                ipariTerulet,
                szolgaltatas,
                ut,
                rendorseg,
                stadion,
                iskola,
                egyetem
        );

        /*
        // IDEIGLENES GOMB -----------------------------------------------------
        // Keep it for test. It's faster (not faster performance but easier to 
        // use) to destroy a zone with this. It makes easier to destroy multiple
        // zones.
        destroyZone = new JButton("destroyZone");
        destroyZone.addActionListener((ActionEvent e) -> {
            Engine.setCursorSignal(CursorSignal.DESTROY);
        });
        buildPanel.add(destroyZone);
        */

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(topPanel);
        leftPanel.add(grid);
        add(leftPanel, BorderLayout.WEST);
        add(buildPanel, BorderLayout.EAST);

        statPanel = null;

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public void showExitDialog() {
        boolean timeBefore = isStopped;
        isStopped = true;
        JDialog d = new JDialog(BigCityJframe.this, "Kilépés", true);
        d.getContentPane().setBackground(Color.GREEN);
        d.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        d.setLayout(new FlowLayout());

        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        // Set the border for the dialog
        d.getRootPane().setBorder(border);

        JButton resume = new JButton("Játék folytatása");
        resume.setBackground(new Color(240, 207, 96));
        JButton b = new JButton("Kilépés a főmenübe, játék mentése");
        b.setBackground(new Color(240, 207, 96));
        JButton a = new JButton("Kilépés a főmenübe mentés nélkül");
        a.setBackground(new Color(240, 207, 96));
        JButton c = new JButton("Kilépés mentés nélkül");
        c.setBackground(new Color(240, 207, 96));
        resume.addActionListener((ActionEvent e) -> {
            d.dispose();
            isStopped = timeBefore;
        });
        b.addActionListener((ActionEvent e) -> {
            engine.saveGame();
            d.setVisible(false);
            BigCityJframe.this.dispose();
            new MainMenu();
        });
        a.addActionListener((ActionEvent e) -> {
            d.setVisible(false);
            BigCityJframe.this.dispose();
            new MainMenu();
        });
        c.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        d.add(resume);
        d.add(a);
        d.add(b);
        d.add(c);
        d.setSize(230, 140);
        d.setLocationRelativeTo(null);
        d.setUndecorated(true);
        d.setVisible(true);
    }

    /**
     * The right JPanel will show the statistics of a zone.
     *
     * @param zone This zone's statistics will be shown.
     */
    public void changeRightPanelToStatPanel(Zone zone) {
        remove(buildPanel);
        if (null != statPanel) {
            remove(statPanel);
        }
        statPanel = new BuildingStatPanel(zone, this, grid);
        add(statPanel, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null);
        statPanel.repaint();
    }

    /**
     * The right JPanel will show the building JPanel.
     */
    public void changeRightPanelToBuildPanel() {
        remove(statPanel);
        statPanel = null;
        add(buildPanel, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null);
        buildPanel.repaint();
    }

    /**
     * Repaints the statistics panel and the grid
     */
    public void repaintStatPanelAndGrid() {
        if (null != statPanel) {
            if (null != statPanel.getpPanel()) {
                statPanel.getpPanel().updatePeople();
            }
            if (null != statPanel.getbStat()) {
                statPanel.getbStat().updateStats();
            }
        }
        grid.repaint();
    }

    /**
     * Simulates like a day passes
     */
    public void dayPassed() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        date = c.getTime();
        refreshDate();
        refreshMoney();
        engine.dayPassed();
    }

    /**
     * Simulates like 10 days passed
     */
    public void daysPassed() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 10);
        date = c.getTime();
        refreshDate();
        refreshMoney();
        engine.daysPassed();
    }

    /**
     * Simulates like a month passed
     */
    public void monthPassed() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        long before = date.getTime();
        c.add(Calendar.MONTH, 1);
        date = c.getTime();
        long after = date.getTime();
        int daysDiff = (int) TimeUnit.DAYS.convert(after - before, TimeUnit.MILLISECONDS);
        refreshDate();
        refreshMoney();
        engine.monthPassed(daysDiff);
    }

    /**
     * Calls the proper time manipulation function
     */
    public void timerTicked() {
        switch (timeSpeed) {
            case DAY ->
                dayPassed();
            case DAYS ->
                daysPassed();
            case MONTH ->
                monthPassed();
            default ->
                throw new AssertionError();
        }
    }

    /**
     * The new balance will be displayed.
     */
    public void refreshMoney() {
        money.setText(String.format("%,d", engine.getMoney()) + "$");
    }

    /**
     * The new date will be displayed.
     */
    public void refreshDate() {
        calendar.setText(dateFormater.format(date));
    }

    /**
     * The new average happiness will be displayed.
     *
     * @param happiness The percentage value of the average happiness.
     */
    public void setHappiness(double happiness) {
        happy.setText(Math.round(happiness) + "%");
    }

    /**
     * Repaints the grid(aka board, map).
     */
    public void refreshGrid() {
        grid.repaint();
    }

    /**
     * Increases the balance.
     *
     * @param money
     */
    public void addMoney(int money) {
        engine.addMoney(money);
        refreshMoney();
    }

    public Engine getEngine() {
        return engine;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public StatElement getHappy() {
        return happy;
    }

    public int getCurrentTax() {
        return engine.getTaxPercentage();
    }

    public void setTax(int tax) {
        engine.setTaxPercentage(tax);
    }

    /**
     * Creates a dialog when a conflictual destruction is triggered. Asks the
     * user if they really want to destroy the zone.
     *
     * @param message Description of the conflictual destruction.
     * @return True if the destruction has been approved.
     */
    public boolean conflictualDestructionOkCancleDialog(String message) {
        boolean timeWasStopped = isStopped;
        isStopped = true;
        ConflictualDestructionDialog dialog = new ConflictualDestructionDialog(
                this, "Destroy", message);
        if (!timeWasStopped) {
            isStopped = false;
        }
        return OKCancelDialog.OK == dialog.getButtonCode();
    }

    /**
     * Informs the user about the game over via a dialog. When this dialog
     * disappears the user gets back to the menu.
     *
     * @param message The reason of the game over.
     */
    public void gameOver(String message) {
        isStopped = true;
        JDialog gameOverDialog = new JDialog(BigCityJframe.this,
                "Game over", true);
        gameOverDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        gameOverDialog.setLayout(new BorderLayout());
        gameOverDialog.setPreferredSize(new Dimension(300, 300));

        JTextArea textArea = new JTextArea(message);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        gameOverDialog.add(textArea, BorderLayout.CENTER);

        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameOverDialog.setVisible(false);
                BigCityJframe.this.dispose();
                new MainMenu();
            }
        });
        gameOverDialog.add(ok, BorderLayout.SOUTH);

        gameOverDialog.pack();
        gameOverDialog.setLocationRelativeTo(null);
        gameOverDialog.setResizable(false);
        gameOverDialog.setVisible(true);
    }

    public BuildingStatPanel getStatPanel() {
        return statPanel;
    }
    public Grid getGrid(){
        return grid;
    }
    public String getCityName() {
        return cityName;
    }

    public long getDate() {
        return date.getTime();
    }

    public void setDate(long value) {
        this.date.setTime(value);
    }

    /**
     * Loads the city with the given name
     *
     * @param cityName - String, name of the city
     * @return - BigCityJframe, the corrent city jframe
     */
    public static BigCityJframe loadGame(String cityName) {
        BigCityJframe frame = new BigCityJframe(cityName, true);
        frame.getEngine().calculateHappieness();
        frame.setHappiness(frame.getEngine().getCombinedHappiness());
        frame.isStopped = false;
        return frame;
    }

    /**
     * Stops the time
     */
    public void stopTime() {
        isStopped = true;
    }

    /**
     * Starts the time
     */
    public void startTime() {
        isStopped = false;
    }
    public BuildPanel getBuildPanel(){
        return buildPanel;
    }
}
