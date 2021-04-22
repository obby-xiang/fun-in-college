package org.example;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Locale;

/**
 * class for main interface
 *
 * @author obby-xiang
 * @since 2017-11-12
 */
@SuppressWarnings("serial")
public class HomeFrame extends JFrame {

    public static WorkspacePanel workspacePanel;    // 工作面板
    public static ToolPanel toolPanel;    // 工具面板
    public static ExplorerPanel explorerPanel;    // 资源管理面板
    public static ConsolePanel consolePanel;    // 控制台面板

    public HomeFrame() {

        super("ComputerNetwork Experiment04");    // 标题
        JOptionPane.setDefaultLocale(Locale.US);

        /** 窗口设置 **/
        this.setSize(1200, 900);
        this.setMinimumSize(new Dimension(1200, 900));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        /** 用于添加各面板的主面板 **/
        final BasePanel homePanel = new BasePanel();
        homePanel.setLayout(new BorderLayout());
        this.add(homePanel);

        /** 初始化各面板 **/
        workspacePanel = new WorkspacePanel();
        toolPanel = new ToolPanel();
        explorerPanel = new ExplorerPanel();
        consolePanel = new ConsolePanel();

        /** 各面板在主面板中的排布 **/
        final BasePanel workspace = new BasePanel(new BorderLayout()),
                tool = new BasePanel(new BorderLayout()),
                explorer = new BasePanel(new BorderLayout()),
                console = new BasePanel(new BorderLayout());
        workspace.setBorder(new TitledBorder("<html><font size='5' face='consolas'>Workspace</font>"));
        tool.setBorder(new TitledBorder("<html><font size='5' face='consolas'>Tool</font>"));
        explorer.setBorder(new TitledBorder("<html><font size='5' face='consolas'>Explorer</font>"));
        console.setBorder(new TitledBorder("<html><font size='5' face='consolas'>Console</font>"));

        workspace.add(new JScrollPane(workspacePanel));
        tool.add(new JScrollPane(toolPanel));
        explorer.add(new JScrollPane(explorerPanel));
        console.add(consolePanel);

        final JSplitPane splitPane1, splitPane2, splitPane3;
        splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, tool, workspace);
        splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, explorer, splitPane1);
        splitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, splitPane2, console);
        splitPane1.setDividerLocation(128);
        splitPane2.setDividerLocation(200);
        splitPane3.setDividerLocation(650);
        splitPane1.setOneTouchExpandable(true);
        splitPane2.setOneTouchExpandable(true);
        splitPane3.setOneTouchExpandable(true);
        homePanel.add(splitPane3);

        this.setVisible(true);
    }
}
