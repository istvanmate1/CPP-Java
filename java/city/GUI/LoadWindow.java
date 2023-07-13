package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.OverlayLayout;
import view.BigCityJframe;

public class LoadWindow extends JFrame{
    protected JList<String> savedGames;
    protected MainMenu menu;
    protected JScrollPane listScroller;
    protected JPanel mainContent;
    
    /**
     * Constructor
     * @param menu - MainMenu, the menu that called this LoadWindow attached to
     */
    protected LoadWindow(MainMenu menu) {
        super();
        this.menu = menu;
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(630, 430));

        // Set background image
        Image backgroundImage = Toolkit.getDefaultToolkit().getImage("GUI/menu.png");
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setLayout(new OverlayLayout(backgroundLabel));

        // Create the content panel
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));

        //left
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JButton arrowButton = new JButton(new ImageIcon("GUI/arrow.png"));
        arrowButton.setPreferredSize(new Dimension(100, 100));
        arrowButton.addActionListener((ActionEvent e) -> {
            LoadWindow.this.setVisible(false);
            menu.setVisible(true);
        });
        arrowButton.setOpaque(false);
        arrowButton.setContentAreaFilled(false);
        arrowButton.setBorderPainted(false);
        left.add(Box.createRigidArea(new Dimension(0,10)));
        left.add(arrowButton);
        left.add(Box.createVerticalGlue());
        
        //center
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setPreferredSize(new Dimension(400, 430));
        center.add(Box.createRigidArea(new Dimension(0, 20)));

        this.mainContent = new JPanel();
        mainContent.setOpaque(false);
        refreshOptions();
        center.add(mainContent);
        
        JButton startGame = new JButton("Indítás");
        startGame.addActionListener((ActionEvent e) -> {
            if(!savedGames.isSelectionEmpty()) {
                BigCityJframe.loadGame(savedGames.getSelectedValue());
                LoadWindow.this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, 
                    new JLabel("Nincs kiválasztva egy korábbi mentés sem!"), 
                "Hiba az indításkor", JOptionPane.ERROR_MESSAGE);
            }
        });
        startGame.setFont(new Font("Verdana", Font.BOLD, 30));
        startGame.setBackground(new Color(99,196,102));
        JPanel startButtonPanel = new JPanel();
        startButtonPanel.setOpaque(false);
        startButtonPanel.setLayout(new BoxLayout(startButtonPanel, BoxLayout.X_AXIS));
        startButtonPanel.add(Box.createHorizontalGlue());
        startButtonPanel.add(startGame);
        startButtonPanel.add(Box.createHorizontalGlue());
        center.add(startButtonPanel);
        
        JButton deleteGame = new JButton("Törlés");
        deleteGame.addActionListener((ActionEvent e) -> {
            if(!savedGames.isSelectionEmpty()) {
                try {
                    List<String> options = new LinkedList<>();
                    Files.lines(Path.of("savedGames","savedGames.txt"))
                        .filter(n -> !savedGames.getSelectedValue().equals(n))
                        .forEach(n -> options.add(n));
                    
                    try(BufferedWriter writer = new BufferedWriter(new FileWriter(
                        "savedGames/savedGames.txt"))) {
                        for (String option : options) {
                            writer.write(option+"\n");
                        }
                    }
                    refreshOptions();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                
            } else {
                JOptionPane.showMessageDialog(null, 
                    new JLabel("Nincs kiválasztva egy korábbi mentés sem!"), 
                "Hiba az indításkor", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteGame.setFont(new Font("Verdana", Font.BOLD, 15));
        deleteGame.setBackground(new Color(130,2,0));
        deleteGame.setForeground(Color.WHITE);
        JPanel deleteButtonPanel = new JPanel();
        deleteButtonPanel.setOpaque(false);
        deleteButtonPanel.setLayout(new BoxLayout(deleteButtonPanel, BoxLayout.X_AXIS));
        deleteButtonPanel.add(Box.createHorizontalGlue());
        deleteButtonPanel.add(deleteGame);
        deleteButtonPanel.add(Box.createHorizontalGlue());
        center.add(Box.createRigidArea(new Dimension(0, 20)));
        center.add(deleteButtonPanel);
        
        center.add(Box.createVerticalGlue());

        //right
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.add(Box.createRigidArea(new Dimension(100, 100)));

        // Add components to the content panel
        content.add(left);
        content.add(Box.createRigidArea(new Dimension(20, 0)));
        content.add(Box.createHorizontalGlue());
        content.add(center);
        content.add(Box.createHorizontalGlue());
        content.add(Box.createRigidArea(new Dimension(20, 0)));
        content.add(right);

        // Add the content panel to the background label
        backgroundLabel.add(content);

        // Set the background label as the content pane of the frame
        setContentPane(backgroundLabel);

        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * Set LoadWindow visible
     * @param menu - MainMenu, the menu that called this LoadWindow attached to
     */
    public void showLoadWindow(MainMenu menu) {
        setLocationRelativeTo(null);
        refreshOptions();
        menu.setVisible(false);
        setVisible(true);
    }
    
    /**
     * Refreshes the loadble options
     */
    protected final void refreshOptions() {
        try {
            if(null != listScroller)
                mainContent.remove(listScroller);
            File thisGame = new File("savedGames/savedGames.txt");
            thisGame.getParentFile().mkdirs();
            thisGame.createNewFile();
            DefaultListModel<String> options = new DefaultListModel<>();
            Files.lines(Path.of("savedGames","savedGames.txt"))
                .forEach(n -> options.addElement(n));
            this.savedGames = new JList<>(options);
            listScroller = new JScrollPane(savedGames);
            listScroller.setPreferredSize(new Dimension(300, 250));
            JLabel title = new JLabel("Városnév:");
            title.setOpaque(true);
            title.setBackground(Color.white);
            title.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
            listScroller.setColumnHeaderView(title);
            mainContent.add(listScroller);
            this.revalidate();
            mainContent.repaint();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
