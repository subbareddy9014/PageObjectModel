package utility;

import javax.swing.*;

public class Util {

    /**
     * Method to show info box when some error
     *
     * @param message     - Information
     * @param messageType - Information Type
     */
    public static void showMessage(String message, String messageType) {
        JFrame jFrame = new JFrame();
        switch (messageType.toLowerCase()) {
            case "information":
                JOptionPane.showMessageDialog(jFrame, message, "Alert", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "warning":
                JOptionPane.showMessageDialog(jFrame, message, "Alert", JOptionPane.WARNING_MESSAGE);
                break;
            case "error":
                JOptionPane.showMessageDialog(jFrame, message, "Alert", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }
}
