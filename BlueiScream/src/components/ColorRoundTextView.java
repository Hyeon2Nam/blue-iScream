package components;

import javax.swing.*;
import java.awt.*;

public class ColorRoundTextView extends JTextArea {
    private Color bg;
    private Color fc;
    private int radius;

    public ColorRoundTextView(String text, Color bg, Color fc) {
        super();
        super.setRows(calcRow());
        super.setColumns(calcCol());
        this.bg = bg;
        this.fc = fc;
        this.radius = 20;

        setText(text);
        setFont(new Font(getFont().getFontName(), getFont().getStyle(), 15));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        setEditable(false);
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

    private int calcCol() {
        String[] str = getText().split("\n");
        return str[0].length();
    }

    private int calcRow() {
        String[] str = getText().split("\n");
        return str.length;
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