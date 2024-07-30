package components;

import javax.swing.*;
import java.awt.*;

public class ColorRoundLabel extends JLabel {
    private Color bg;
    private int radius;

    public ColorRoundLabel(Color bg) {
        super();
        this.bg = bg;
        this.radius = 30;

        setFont(new Font(getFont().getFontName(), getFont().getStyle(), 11));
        setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getRadius(), getRadius());
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bg);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getRadius(), getRadius());
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public Insets getInsets() {
        int value = getRadius() / 2;
        return new Insets(value, value, value, value);
    }
}
