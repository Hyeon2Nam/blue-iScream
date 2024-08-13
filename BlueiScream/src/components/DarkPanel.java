package components;

import javax.swing.*;
import java.awt.*;

public class DarkPanel extends JPanel {
    public DarkPanel() {
        setBackground(new Color(0, 38, 66));
    }

    public DarkPanel(int n, String op) {
        setBackground(new Color(0, 38, 66));

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
