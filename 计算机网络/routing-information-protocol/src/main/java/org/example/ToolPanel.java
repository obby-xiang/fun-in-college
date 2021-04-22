package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * class for adding networks and routers with graphical interface
 *
 * @author obby-xiang
 * @since 2017-11-12
 */
@SuppressWarnings("serial")
public class ToolPanel extends BasePanel {

    public ToolPanel() {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 16));

        /** 用于添加的网络和路由器 **/
        final BasePanel toolBox = new BasePanel(new GridLayout(1, 2, 16, 0));
        final BaseLabel network = new BaseLabel(), router = new BaseLabel();
        network.setIcon(new ImageIcon(this.getClass().getResource("/images/network.png")));
        router.setIcon(new ImageIcon(this.getClass().getResource("/images/router.png")));
        network.setToolTipText("<html><font size='5' face='consolas'>network</font>");
        router.setToolTipText("<html><font size='5' face='consolas'>router</font>");

        this.addComponentListener(new ComponentAdapter() {

            /** 组件大小被改变 **/
            public synchronized void componentResized(ComponentEvent e) {
                super.componentResized(e);
                int y1 = toolBox.getY() + router.getY() + router.getHeight(),
                        y2 = toolBox.getY() + network.getY() + network.getHeight();
                ToolPanel.this.setPreferredSize(new Dimension(toolBox.getX() + router.getX() + router.getWidth() - 16,
                        y1 > y2 ? y1 : y2));    // 修改组件理想大小
                HomeFrame.workspacePanel.updateUI();
            }
        });

        /** 添加网络 **/
        network.addMouseListener(new MouseAdapter() {
            public synchronized void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1)
                    HomeFrame.workspacePanel.addNetwork();
            }
        });

        /** 添加路由器 **/
        router.addMouseListener(new MouseAdapter() {
            public synchronized void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1)
                    HomeFrame.workspacePanel.addRouter();
            }
        });

        toolBox.add(network);
        toolBox.add(router);
        this.add(toolBox);
    }
}
