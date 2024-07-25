package components;

import javax.swing.*;
import java.awt.*;

public class PinkPanel extends JPanel {
    public PinkPanel() {
        setBackground(new Color(255, 214, 214));
    }

    public PinkPanel(int n, String op) {
        setBackground(new Color(255, 214, 214));

        switch (op) {
            case "top":
                setBorder(BorderFactory.createEmptyBorder(n, 0, 0, 0));
                break;
            case "bottom":
                setBorder(BorderFactory.createEmptyBorder(0, 0, n, 0));
                break;
            case "left":
                setBorder(BorderFactory.createEmptyBorder(0, n, 0, 0));
                break;
            case "right":
                setBorder(BorderFactory.createEmptyBorder(0, 0, 0, n));
        }
    }
}
