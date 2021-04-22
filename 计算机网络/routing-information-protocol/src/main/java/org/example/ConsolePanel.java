package org.example;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

/**
 * class for showing logs and Routing tables
 *
 * @author obby-xiang
 * @since 2017-11-12
 */
@SuppressWarnings("serial")
public class ConsolePanel extends BasePanel {

    private final JComboBox<LogPanel> comboBox;    // 日志面板组合框
    public LogPanel mainPanel;    // 显示所有日志的面板
    private LogPanel selectedPanel;    // 当前显示的日志面板

    public ConsolePanel() {

        this.setLayout(new BorderLayout());

        /** 初始化并设置参数 **/
        final DefaultComboBoxModel<LogPanel> model = new DefaultComboBoxModel<>();
        comboBox = new JComboBox<>(model);
        comboBox.setDoubleBuffered(true);
        mainPanel = addPanel(null);
        comboBox.setFont(new Font("consolas", Font.BOLD, 16));

        comboBox.setSelectedIndex(0);
        selectedPanel = mainPanel;

        this.add(mainPanel);
        this.add(comboBox, BorderLayout.NORTH);

        /** 注册监听器 **/
        comboBox.addItemListener(new ItemListener() {

            /** 选项状态改变 **/
            public synchronized void itemStateChanged(ItemEvent e) {
                if (comboBox.getSelectedItem() == selectedPanel)
                    return;

                /** 移除其他面板，显示选中的面板**/
                ConsolePanel.this.remove(selectedPanel);
                ConsolePanel.this.add(selectedPanel = (LogPanel) comboBox.getSelectedItem());
                ConsolePanel.this.updateUI();
            }
        });
    }

    /**
     * 添加面板用于显示日志
     **/
    public synchronized LogPanel addPanel(final Object object) {
        final LogPanel infoPanel = new LogPanel(object);
        int insert = 0;    // 面板信息插入的位置
        for (; insert < comboBox.getItemCount(); insert++) {
            if (comboBox.getItemAt(insert).toString().compareTo(infoPanel.getName()) > 0)
                break;
        }
        comboBox.insertItemAt(infoPanel, insert);
        return infoPanel;
    }

    /**
     * 删除相关面板
     **/
    public synchronized void removePanel(final Object object) {
        final String name = object instanceof XNetwork ? ((XNetwork) object).getName() : ((XRouter) object).getName();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (name.equals(comboBox.getItemAt(i).getName())) {
                final LogPanel panel = comboBox.getItemAt(i);
                if (selectedPanel == panel)
                    comboBox.setSelectedIndex(0);
                this.remove(panel);
                comboBox.removeItem(panel);
                return;
            }
        }
    }

    /**
     * 日志面板
     **/
    static class LogPanel extends BasePanel {

        private final JTextPane textPane;    // 文本窗格
        private final SimpleAttributeSet titleAttributeSet;
        private final SimpleAttributeSet textAttributeSet;    // 日志标题及正文属性

        public LogPanel(final Object object) {

            /** 根据参数创建相应的面板 **/
            if (object == null)
                this.setName("All");
            else if (object instanceof XNetwork)
                this.setName(((XNetwork) object).getName());
            else if (object instanceof XRouter)
                this.setName(((XRouter) object).getName());
            this.setLayout(new BorderLayout());


            textPane = new JTextPane();
            textPane.setDoubleBuffered(true);
            textPane.setEditable(false);

            this.setMinimumSize(new Dimension(16, 16));
            textPane.setMinimumSize(new Dimension(16, 16));

            /** 设置属性 **/
            titleAttributeSet = new SimpleAttributeSet();
            textAttributeSet = new SimpleAttributeSet();
            StyleConstants.setFontFamily(titleAttributeSet, "Consoal");
            StyleConstants.setFontSize(titleAttributeSet, 18);
            StyleConstants.setForeground(titleAttributeSet, new Color(255, 0, 255));
            StyleConstants.setFontFamily(textAttributeSet, "Consoal");
            StyleConstants.setFontSize(textAttributeSet, 18);

            if (object instanceof XRouter) {    // 添加路由表
                final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                        new JScrollPane(((XRouter) object).routerTable), new JScrollPane(textPane));
                splitPane.setOneTouchExpandable(true);
                this.add(splitPane);
            } else
                this.add(new JScrollPane(textPane));

            /** 清空日志弹出式菜单 **/
            final JPopupMenu popupMenu = new JPopupMenu();
            final JMenuItem clearItem = new JMenuItem("Clear");
            clearItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    append(null);
                }
            });
            popupMenu.add(clearItem);

            /** 为文本窗格添加鼠标事件监听器**/
            textPane.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {

                    super.mousePressed(e);
                    if (e.getButton() == MouseEvent.BUTTON3)    // 按下右键
                        popupMenu.show(textPane, e.getX(), e.getY());
                }
            });

        }

        /**
         * 为文本窗格追加文本
         **/
        public synchronized void append(final String text) {
            if (text == null) {    // 清空
                textPane.setText("");
                return;
            }
            try {
                textPane.getDocument().insertString(textPane.getDocument().getLength(),
                        new SimpleDateFormat(">>>yyyy-MM-dd HH:mm:ss SSS").format(System.currentTimeMillis()) + "\n      ",
                        titleAttributeSet);
                textPane.getDocument().insertString(textPane.getDocument().getLength(), text.replaceAll("\n", "\n\t") + "\n", textAttributeSet);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        public String toString() {
            return this.getName();
        }
    }

}
