package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UnderLineTextField extends JTextField {
    private Color bg;

    public UnderLineTextField(Color bg, Color fg) {
        this.bg = bg;

        setBorder(new EmptyBorder(5, 0, 10, 0));
        setBackground(null);
//        setPreferredSize(new Dimension(300, 50));
        setFont(new Font(getFont().getFontName(), getFont().getStyle(), 20));
        setForeground(fg);
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setColor(bg);

        int width = getWidth();
        int height = getHeight();

        g2.fillRect(1, height - 3, width, height);
        g2.dispose();
    }
}
