package components;

import java.awt.*;
import javax.swing.*;

public class ColorRoundButton extends JButton {

  private Color bg;
  private Color fc;
  private int fontSize;

  public ColorRoundButton(String text, Color bg, Color fc, int fontSize) {
    super(text);
    this.bg = bg;
    this.fc = fc;
    this.fontSize = fontSize;

    setFont(new Font(getFont().getFontName(), Font.BOLD, fontSize));
    setBorderPainted(false);
    setOpaque(false);
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