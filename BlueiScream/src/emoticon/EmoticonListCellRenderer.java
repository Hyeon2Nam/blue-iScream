package emoticon;

import javax.swing.*;
import java.awt.*;

public class EmoticonListCellRenderer extends JPanel implements ListCellRenderer<EmoticonItem> {
    private static final long serialVersionUID = 1L;
    private JLabel label;

    public EmoticonListCellRenderer() {
        setLayout(new BorderLayout());
        label = new JLabel();
        add(label, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends EmoticonItem> list, EmoticonItem value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            label.setText(value.getText());

            ImageIcon icon = new ImageIcon(value.getImagePath());
            Image img = icon.getImage();
            Image resizedImage = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            label.setIcon(resizedIcon);
        }
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
