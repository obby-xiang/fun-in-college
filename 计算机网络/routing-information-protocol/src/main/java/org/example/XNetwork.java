package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * class for network
 *
 * @author obby-xiang
 * @since 2017-11-12
 */
@SuppressWarnings("serial")
public class XNetwork extends BaseLabel implements Serializable {

    /**
     * 公有的静态常量
     **/
    public static final String TAG = "Net";
    /**
     * 私有的静态常量
     **/
    private static final ImageIcon ICON = new ImageIcon(XNetwork.class.getResource("/images/network.png"));    // 图标
    public static final Dimension DIMENSION = new Dimension(ICON.getIconWidth(), ICON.getIconHeight());    // 尺寸
    /**
     * 公有的静态变量
     **/
    public static List<XNetwork> networks = new ArrayList<>();    // 网络
    /**
     * 私有的静态变量
     **/
    private static int count = 0;    // 网络数量
    /**
     * 私有的成员变量
     **/
    private final JLabel textLabel;    // 显示文字的标签
    /**
     * 公有的成员变量
     **/
    public List<XRouter> linkedRouters;    // 相连的路由器
    public ConsolePanel.LogPanel logPanel;
    private boolean checked;    // 是否选中
    private int mouseButton;

    public XNetwork() {

        /** 初始化成员变量 **/
        textLabel = new JLabel();
        checked = true;
        mouseButton = 0;
        linkedRouters = new ArrayList<>();

        this.setName(TAG + (++count));    // 名称，网络唯一标识符
        networks.add(this);

        /** 在其他面板中添加 **/
        HomeFrame.explorerPanel.addNetwork(this);
        logPanel = HomeFrame.consolePanel.addPanel(this);

        /** 组件参数设定 **/
        this.setSize(DIMENSION);
        this.setIcon(ICON);
        this.setLayout(new BorderLayout());
        textLabel.setHorizontalAlignment(CENTER);
        textLabel.setOpaque(false);

        this.add(textLabel);    // 添加显示文字的标签
        this.setChecked(checked);    // 初始设置为选中

        final Point pressedPosition = new Point();    // 用于记录鼠标按下的位置

        /** 添加鼠标事件监听器 **/
        this.addMouseListener(new MouseAdapter() {

            /** 按下鼠标 **/
            public synchronized void mousePressed(MouseEvent e) {

                mouseButton = e.getButton();    // 记录鼠标按下的按钮
                pressedPosition.setLocation(e.getX(), e.getY());    // 记录鼠标按下的位置

                super.mousePressed(e);

                if (e.getButton() == MouseEvent.BUTTON1) {    // 左键按下
                    if (HomeFrame.workspacePanel.isCtrlPressing()) {
                        if (checked)
                            HomeFrame.explorerPanel.cancelSelection(XNetwork.this);
                        else
                            HomeFrame.explorerPanel.addSelection(XNetwork.this);
                    } else {
                        HomeFrame.explorerPanel.cancelSelections();
                        HomeFrame.explorerPanel.addSelection(XNetwork.this);

                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {    // 右键按下
                    if (!checked) {
                        HomeFrame.explorerPanel.cancelSelections();
                        HomeFrame.explorerPanel.addSelection(XNetwork.this);
                    }
                    HomeFrame.workspacePanel.showPoupMenu(XNetwork.this, e.getX(), e.getY());
                }
            }

            /** 松开鼠标 **/
            public void mouseReleased(MouseEvent e) {

                XNetwork.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));    // 默认鼠标指针

                super.mouseReleased(e);

                HomeFrame.workspacePanel.updateUI();    // 更新组件
            }

        });

        /** 添加鼠标移动事件监听器 **/
        this.addMouseMotionListener(new MouseMotionAdapter() {

            public synchronized void mouseDragged(MouseEvent e) {

                XNetwork.this.setCursor(new Cursor(Cursor.MOVE_CURSOR));    // 设置鼠标指针

                super.mouseDragged(e);

                /** 按下鼠标左键拖动 **/
                if (mouseButton == MouseEvent.BUTTON1) {
                    int x = e.getX() + XNetwork.this.getX() - pressedPosition.x,
                            y = e.getY() + XNetwork.this.getY() - pressedPosition.y,
                            maxX = HomeFrame.workspacePanel.getWidth() - DIMENSION.width,
                            maxY = HomeFrame.workspacePanel.getHeight() - DIMENSION.height;
                    XNetwork.this.setLocation(x < 0 ? 0 : (x > maxX ? maxX : x), y < 0 ? 0 : (y > maxY ? maxY : y));    // 更新位置
                    WorkspacePanel.adjustPreferredSize();    // 调整容器理想大小
                    HomeFrame.workspacePanel.repaint();    // 容器重绘
                }
            }
        });

        /** 输出信息到面板 **/
        logPanel.append("added");
        HomeFrame.consolePanel.mainPanel.append(this.getName() + ":added");
    }

    /**
     * 网络是否选中
     **/
    public synchronized boolean isChecked() {
        return checked;
    }

    /**
     * 设置网络是否选中
     **/
    public synchronized void setChecked(final boolean isChecked) {
        checked = isChecked;
        textLabel.setText("<html><font size='5' face='consolas' color='"
                + (isChecked ? (HomeFrame.workspacePanel.isFocusOwner() ? "#F4EA2A" : "#E3E3E3") : "#CD3333") + "'>"
                + this.getName() + "</font>");
    }

    /**
     * 移除该网络
     **/
    public synchronized void remove() {

        /** 在相关面板中移除 **/
        HomeFrame.workspacePanel.remove(this);
        HomeFrame.explorerPanel.removeNetwork(this);
        HomeFrame.consolePanel.removePanel(this);

        networks.remove(this);

        /** 断开与相连路由器的连接 **/
        while (!this.linkedRouters.isEmpty())
            this.delink(this.linkedRouters.get(0));

        /** 输出信息到面板 **/
        HomeFrame.consolePanel.mainPanel.append(this.getName() + ":removed");

        HomeFrame.workspacePanel.updateUI();
    }

    /**
     * 获得与网络相连的路由器的位置
     **/
    public synchronized List<Point> getTerminalPoints() {
        final List<Point> points = new ArrayList<>();
        for (XRouter router : linkedRouters)
            points.add(new Point(router.getX() + XRouter.DIMENSION.width / 2,
                    router.getY() + XRouter.DIMENSION.height / 2));
        return points;

    }

    /**
     * 连接到路由器
     **/
    public synchronized void link(final XRouter router) {

        linkedRouters.add(router);
        router.linkedNetworks.add(this);

        router.networkLinked(this);

        /** 对相连的路由器排序 **/
        Collections.sort(router.linkedNetworks, new Comparator<XNetwork>() {
            public int compare(XNetwork n1, XNetwork n2) {
                return n1.getName().compareTo(n2.getName());
            }
        });

        /** 对相连路由器的相连网络排序 **/
        Collections.sort(linkedRouters, new Comparator<XRouter>() {
            public int compare(XRouter r1, XRouter r2) {
                return r1.getName().compareTo(r2.getName());
            }
        });

        /** 输出信息到面板 **/
        logPanel.append("linked with " + router.getName());
        router.logPanel.append("linked with " + this.getName());
        HomeFrame.consolePanel.mainPanel.append(this.getName() + ":linked with " + router.getName());
        HomeFrame.consolePanel.mainPanel.append(router.getName() + ":linked with " + this.getName());

        HomeFrame.workspacePanel.repaint();
    }

    /**
     * 与路由器断开连接
     **/
    public synchronized void delink(final XRouter router) {

        linkedRouters.remove(router);
        router.linkedNetworks.remove(this);

        router.networkUnlinked(this);

        /** 输出信息到面板 **/
        logPanel.append("delinked with " + router.getName());
        router.logPanel.append("delinked with " + this.getName());
        HomeFrame.consolePanel.mainPanel.append(this.getName() + ":delinked with " + router.getName());
        HomeFrame.consolePanel.mainPanel.append(router.getName() + ":delinked with " + this.getName());

        HomeFrame.workspacePanel.repaint();
    }

    /**
     * 可连接的（已连接的除外）路由器
     **/
    public synchronized List<XRouter> canLink() {
        final List<XRouter> routerList = new ArrayList<>();

        /** 未连接到的路由器 **/
        for (XRouter router : XRouter.routers) {
            if (!linkedRouters.contains(router))
                routerList.add(router);
        }
		/*for (XRouter router : linkedRouters) {
			for (XNetwork network : router.linkedNetworks) {
				for (XRouter r : network.linkedRouters)
					routerList.remove(r);
			}
		}*/
        return routerList;
    }

    /**
     * 传输路由表
     **/
    public synchronized void transfer(final String source, final List<XInfo> infoList) {

        final StringBuilder text = new StringBuilder();
        text.append("************************\n");
        for (XInfo info : infoList)
            text.append(info.getDestination() + "\t" + info.getDistance() + "\t" + info.getNextHop() + "\n");
        text.append("************************");

        /** 输出信息到面板 **/
        logPanel.append("transferred from " + source + "\n" + text);
        HomeFrame.consolePanel.mainPanel.append(this.getName() + ":transferred from " + source);

        for (XRouter router : linkedRouters) {
            if (!router.getName().equals(source)) {
                final List<XInfo> infos = new ArrayList<>();
                for (XInfo info : infoList)
                    infos.add(new XInfo(info));
                router.accept(source, infos);
            }
        }
    }

    public String toString() {
        return getName();
    }
}
