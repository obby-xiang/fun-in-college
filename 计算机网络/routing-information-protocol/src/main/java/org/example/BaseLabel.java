package org.example;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * class for Network and Router to extend
 *
 * @author obby-xiang
 * @since 2017-11-12
 */
@SuppressWarnings("serial")
public class BaseLabel extends JLabel {

    public BaseLabel() {

        this.setDoubleBuffered(true);

        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                BaseLabel.this.getParent().requestFocus();    //按下鼠标时为容器获得焦点
            }
        });
    }

    public synchronized void repaint() {
        super.repaint();
    }

    public synchronized void updateUI() {
        super.updateUI();
    }
}
