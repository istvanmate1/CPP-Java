
package GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import view.BigCityJframe;

/**
 *
 * @author Sütő Attila
 */

public class ThreeColumnLayout extends JPanel {
    private final JFrame parent;
    /**
     * Constructor
     * @param parent 
     */
    public ThreeColumnLayout(JFrame parent) {
        this.parent = parent;
        // Create a GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // Add a JButton to the top of the first column
        JButton button = new JButton();
        button.addActionListener((ActionEvent e) -> {
            parent.dispose();
            new MainMenu();
        });
        
        ImageIcon buttonIcon = new ImageIcon("GUI/arrow.png");
        button.setIcon(buttonIcon);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.insets.set(10, 10, 0, 10);
        add(button, constraints);

        // Add a JLabel and a JTextField to the second column
        JLabel label = new JLabel("Város neve:");
        label.setFont(new Font("Verdana", Font.PLAIN, 45));
        JTextField textField = new JTextField(20);
        textField.setPreferredSize(new Dimension(200, 50));
        textField.setFont(new Font("Verdana", Font.PLAIN, 25));
        
        //Add JButton to second column
        JButton secondButton = new JButton("Indítás");
        secondButton.addActionListener((ActionEvent e) -> {
            if(textField.getText().isEmpty()){
                JLabel message = new JLabel("A város nevének megadása kötelező");
                message.setFont(new Font("Verdana", Font.PLAIN, 15));
                message.setPreferredSize(new Dimension(300, 100));
                JOptionPane.showMessageDialog(null, message, 
                "Hiba!", JOptionPane.ERROR_MESSAGE);
            }
            else{
                if( textField.getText().trim().isEmpty()){
                    JLabel message = new JLabel("A város nevének tartalmaznia kell legalább egy szót");
                    message.setFont(new Font("Verdana", Font.PLAIN, 15));
                    message.setPreferredSize(new Dimension(300, 100));
                    JOptionPane.showMessageDialog(null, message, 
                    "Hiba!", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    try{
                        File thisGame = new File("savedGames/savedGames.txt");
                        thisGame.getParentFile().mkdirs();
                        thisGame.createNewFile();
                        if(Files.lines(Path.of("savedGames","savedGames.txt"))
                            .anyMatch(n -> textField.getText().equals(n))) {
                            
                            JLabel message = new JLabel("Ez a város név már foglalt!");
                            message.setFont(new Font("Verdana", Font.PLAIN, 15));
                            message.setPreferredSize(new Dimension(300, 100));
                            JOptionPane.showMessageDialog(null, message, 
                            "Hiba!", JOptionPane.ERROR_MESSAGE);
                            
                        } else {
                            new BigCityJframe(textField.getText(), false);
                            parent.dispose();
                        }
                    } catch(IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
        secondButton.setFont(new Font("Verdana", Font.PLAIN, 25));
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.0;
        constraints.insets.set(10, 10, 0, 10);
        add(label, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets.set(0, 10, 10, 10);
        add(textField, constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.0;
        constraints.insets.set(0, 10, 10, 10);
        add(secondButton, constraints);

        // Add a placeholder component to the third column
        Component placeholder = Box.createHorizontalStrut(100);
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridheight = 3;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.insets.set(10, 10, 10, 10);
        add(placeholder, constraints);
    }
    
}
