package components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class WhiteRoundeButton extends JButton {

  public WhiteRoundeButton(String text) {
    super(text);
    setFont(new Font(getFont().getFontName(), Font.BOLD, 30));
    setBorderPainted(false);
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    Color bg = Color.white; //배경색 결정
    Color fc = new Color(255, 214, 214); //글자색 결정
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
//    g2.setFont(new Font(getFont().getFontName(), getFont().getStyle(), 20));
    g2.drawString(getText(), textX, textY);
    g2.dispose();

    super.paintComponent(g);
  }
}