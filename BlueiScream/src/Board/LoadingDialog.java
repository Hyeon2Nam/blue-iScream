package Board;

import javax.swing.*;
import java.awt.*;

public class LoadingDialog extends JDialog {
    public LoadingDialog(Frame owner) {
        super(owner, "Loading", true);
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Connecting to the database, please wait...", JLabel.CENTER);
        add(label, BorderLayout.CENTER);
        setSize(300, 100);
        setLocationRelativeTo(owner);
    }
}