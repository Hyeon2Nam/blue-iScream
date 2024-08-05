package components;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel {
    private final int TOTALWIDTH = 400;

    public Header(String lbTxt) {
        JLabel titleLb = new JLabel(lbTxt);

        setBackground(new Color(0, 38, 66));
        titleLb.setForeground(Color.white);
        titleLb.setFont(new Font(titleLb.getFont().getFontName(), titleLb.getFont().getStyle(), 20));
        titleLb.setHorizontalAlignment(JLabel.CENTER);

        setLayout(new BorderLayout());
        setSize(TOTALWIDTH, 70);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        add(titleLb, BorderLayout.CENTER);
    }

    public Header(String lbTxt, JButton eastBtn) {
        this(lbTxt);
        DarkPanel panel = new DarkPanel(30, "left");

        add(panel, BorderLayout.WEST);
        add(eastBtn, BorderLayout.EAST);
    }

    public Header(JButton westBtn, String lbTxt) {
        this(lbTxt);

        add(westBtn, BorderLayout.WEST);
    }

    public Header(JButton westBtn, String lbTxt, JButton eastBtn) {
        this(lbTxt);

        add(westBtn, BorderLayout.WEST);
        add(eastBtn, BorderLayout.EAST);
    }
}
