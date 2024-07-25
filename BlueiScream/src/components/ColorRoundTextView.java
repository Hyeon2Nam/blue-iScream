package components;

import javax.swing.*;
import java.awt.*;

public class ColorRoundTextView extends JTextArea {
    private Color bg;
    private Color fc;
    private final int MAXWIDTH = 10;
    private final int MAXHEIGHT = 100;


    public ColorRoundTextView(String text, Color bg, Color fc) {
        super();
        this.bg = bg;
        this.fc = fc;

        super.setText(text);
        super.setFont(new Font(getFont().getFontName(), getFont().getStyle(), 15));
        super.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        super.setEditable(false);
        super.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        int radius = 40;

        // 배경색 채우기
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, width, height, radius, radius);

        // 글자 그리기
        FontMetrics fontMetrics = g2.getFontMetrics();
        Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), g2).getBounds();

        int textX = (width - stringBounds.width) / 2;
        int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();

        g2.setColor(fc);
        g2.getFont();
        g2.drawString(getText(), textX, textY);
        g2.dispose();

        super.paintComponent(g);
    }
}
