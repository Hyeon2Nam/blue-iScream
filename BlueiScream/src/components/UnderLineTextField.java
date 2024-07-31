package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UnderLineTextField extends JTextField {

    public UnderLineTextField() {
        setBorder(new EmptyBorder(5, 0, 10, 0));
        setBackground(null);
        setFont(new Font(getFont().getFontName(), getFont().getStyle(), 20));
        setForeground(Color.white);
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setColor(Color.white);

        int width = getWidth();
        int height = getHeight();

        g2.fillRect(1, height - 3, width, height);
        g2.dispose();
    }
}
