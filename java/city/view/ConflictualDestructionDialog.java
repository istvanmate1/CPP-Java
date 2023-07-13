package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ConflictualDestructionDialog extends OKCancelDialog {

    /**
     * This dialog is used when the player tries to destroy a zone where people
     * live, work. Trying to destroy a road which is the only possible path for
     * a citizen between their home and workplace also creates this dialog.
     *
     * @param jframe: The owner of this dialog.
     * @param name: The title of this dialog.
     * @param message: Describes why this dialog was created. The description of
     * the conflictual destruction. Information for the user.
     */
    public ConflictualDestructionDialog(JFrame jframe, String name,
            String message) {

        super(jframe, name);
        setLayout(new BorderLayout());

        JPanel textPanel = new JPanel();
        textPanel.setPreferredSize(new Dimension(300, 100));
        textPanel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea(message);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        textPanel.add(textArea, BorderLayout.CENTER);

        add(textPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

    }

    @Override
    protected boolean processOK() {
        return true;
    }

    @Override
    protected void processCancel() {

    }

}
