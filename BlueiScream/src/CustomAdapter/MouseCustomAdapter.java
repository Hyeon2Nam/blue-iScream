package CustomAdapter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

abstract public class MouseCustomAdapter extends MouseAdapter {
    private long mousePressedTime;
    private long delay = 1000;
    private Timer flashTimer;
    private Color originalForegroungColor;

    public MouseCustomAdapter() {}
    public MouseCustomAdapter(long delay) {
        this.delay = delay;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressedTime = e.getWhen();
        if(flashTimer != null)
            flashTimer.cancel();
        flashTimer = new Timer("flash timer");
        flashTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                originalForegroungColor = e.getComponent().getForeground();
                e.getComponent().setForeground(Color.LIGHT_GRAY);
            }
        }, delay);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        flashTimer.cancel();
        e.getComponent().setForeground(originalForegroungColor);
        if(e.getWhen() - mousePressedTime > delay)
            longActionPerformed(e);
        else
            shortActionPerformed(e);
    }

    public abstract void shortActionPerformed(MouseEvent e);
    public abstract void longActionPerformed(MouseEvent e);
}