package view;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public abstract class OKCancelDialog extends JDialog {

    public static final int OK = 1;     // OK button code
    public static final int CANCEL = 0; // Cancel button code

    protected int btnCode;            // The pressed button's code.

    protected JPanel btnPanel;         // Contains the OK and Cancel buttons.

    protected JButton btnOK;             // OK button

    protected JButton btnCancel;         // Cancel button

    /**
     * Creates a dialog.
     *
     * @param frame possesses this dialog
     * @param name is the title of this dialog
     */
    protected OKCancelDialog(JFrame frame, String name) {
        super(frame, name, true);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        btnCode = CANCEL;
        btnOK = new JButton(actionOK);
        btnOK.setMnemonic('O');
        btnOK.setPreferredSize(new Dimension(90, 25));
        btnCancel = new JButton(actionCancel);
        // The Esc button can be used instead of the cancel button.
        KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        InputMap inputMap = btnCancel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = btnCancel.getActionMap();
        if (inputMap != null && actionMap != null) {
            inputMap.put(cancelKeyStroke, "cancel");
            actionMap.put("cancel", actionCancel);
        }
        btnCancel.setPreferredSize(new Dimension(90, 25));
        getRootPane().setDefaultButton(btnOK);
        btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(btnOK);
        btnPanel.add(btnCancel);
    }

    /**
     * @return the code (OK or CANCEL) of the button which was pressed to end
     * the dialog.
     */
    public int getButtonCode() {
        return btnCode;
    }

    /**
     * If the OK button is pressed this method will be executed.
     *
     * @return true if the dialog is satisfied and can be closed.
     */
    protected abstract boolean processOK();

    /**
     * If the CANCEL button is pressed this method will be executed.
     */
    protected abstract void processCancel();

    /**
     * The handler of the OK button's action.
     */
    private AbstractAction actionOK = new AbstractAction("OK") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (processOK()) {
                btnCode = OK;
                OKCancelDialog.this.setVisible(false);
            }
        }
    };

    /**
     * The handler of the CANCEL button's action.
     */
    private AbstractAction actionCancel = new AbstractAction("Cancel") {
        @Override
        public void actionPerformed(ActionEvent e) {
            processCancel();
            btnCode = CANCEL;
            OKCancelDialog.this.setVisible(false);
        }
    };
}
