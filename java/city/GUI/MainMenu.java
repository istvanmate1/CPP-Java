package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class MainMenu extends JFrame {

    private JButton newGameBtn, resumeGameBtn, exitGame;
    private JLabel title;
    JLabel gap;
    private ImageIcon backgroundImage;
    protected LoadWindow load;
    /**
     * Constructor 
     */
    public MainMenu() {
        // Set the title of the JFrame
        setTitle("BigCity");

        this.load = new LoadWindow(this);
        
        // Load the background image
        backgroundImage = new ImageIcon("GUI/menu.png");

        // Create the buttons
        newGameBtn = new JButton("Új játék");
        resumeGameBtn = new JButton("Játék folytatása");
        exitGame = new JButton("Kilépés");
        
        exitGame.setBackground(new Color(99,196,102));
        newGameBtn.setFont(new Font("Verdana", Font.BOLD, 30));
        newGameBtn.setBackground(new Color(99,196,102));
        resumeGameBtn.setFont(new Font("Verdana", Font.BOLD, 18));
        resumeGameBtn.setBackground(new Color(99,196,102));
        exitGame.setFont(new Font("Verdana", Font.BOLD, 18));
        exitGame.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        newGameBtn.addActionListener((ActionEvent e) -> {
            new NameWindow(); 
            
            MainMenu.this.dispose();
        });
        resumeGameBtn.addActionListener((ActionEvent e) -> {
            load.showLoadWindow(this);
        });
        gap = new JLabel();
        
        //Create title label
        title = new JLabel("BigCity");
        title.setFont(new Font("Verdana", Font.BOLD, 65));

        // Set the layout of the JFrame to BorderLayout
        setLayout(new BorderLayout());

        // Create a JPanel to hold the buttons and set its layout to GridBagLayout
        JPanel buttonPanel = new JPanel(new GridBagLayout());

        // Set the background color of the buttonPanel to be transparent
        buttonPanel.setOpaque(false);

        // Add the buttons and the title label to the JPanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        buttonPanel.add(title, gbc);
        gbc.gridy++;
        buttonPanel.add(gap, gbc);
        gbc.gridy++;
        buttonPanel.add(newGameBtn, gbc);
        gbc.gridy++;
        buttonPanel.add(resumeGameBtn, gbc);
        gbc.gridy++;
        buttonPanel.add(exitGame, gbc);
        
        // Set the background image of the JFrame
        setContentPane(new JLabel(backgroundImage));

        // Set the layout of the content pane to GridBagLayout
        setLayout(new GridBagLayout());

        // Add the buttonPanel to the content pane
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.insets = new Insets(100, 100, 100, 100);
        add(buttonPanel, gbc2);

        // Set the size of the JFrame
        setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

        // Set the default close operation of the JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center the JFrame on the screen
        setSize(new Dimension(630,430));
        setResizable(false);
        
        //Hide titlebar
        setUndecorated(true);
        
        setLocationRelativeTo(null);
        
        // Set the JFrame to be visible
        setVisible(true);
    }

}
