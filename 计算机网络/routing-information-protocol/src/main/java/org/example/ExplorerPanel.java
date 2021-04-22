package org.example;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * class for preview the networks and routers created
 *
 * @author obby-xiang
 * @since 2017-11-12
 */
@SuppressWarnings("serial")
public class ExplorerPanel extends BasePanel {

    private final DefaultMutableTreeNode root;
    private final DefaultMutableTreeNode networkRoot;
    private final DefaultMutableTreeNode routerRoot;    // 根	网络根	路由器根
    public JTree tree;    // 树

    public ExplorerPanel() {

        this.setLayout(new BorderLayout());

        /** 初始化变量并进行相关设置 **/
        root = new DefaultMutableTreeNode("Resource");
        tree = new JTree(root);
        tree.setDoubleBuffered(true);
        networkRoot = new DefaultMutableTreeNode("Network");
        routerRoot = new DefaultMutableTreeNode("Router");
        root.add(networkRoot);
        root.add(routerRoot);
        tree.setCellRenderer(new XDefaultTreeCellRenderer());    // 自定义渲染
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

        /** 添加选择事件监听器 **/
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public synchronized void valueChanged(TreeSelectionEvent e) {
                for (TreePath path : e.getPaths()) {
                    final DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    if (node.getParent() == networkRoot)
                        getNetwork(node).setChecked(tree.isPathSelected(path));
                    else if (node.getParent() == routerRoot)
                        getRouter(node).setChecked(tree.isPathSelected(path));
                }

            }
        });

        /** 弹出式快捷菜单 **/
        final JPopupMenu popupMenu = new JPopupMenu();
        final JMenuItem removeItem = new JMenuItem("<html><font size='4' face='consolas'>Remove</font>");    // 删除选项
        removeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));    // 为删除选项添加快捷键
        popupMenu.add(removeItem);


        final ActionListener removeListener = new ActionListener() {

            public synchronized void actionPerformed(ActionEvent arg0) {
                removeSelections();
            }
        };    // 删除选项动作事件监听器
        removeItem.addActionListener(removeListener);

        tree.registerKeyboardAction(removeListener, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK), WHEN_FOCUSED);    // 添加删除快捷键

        tree.addMouseListener(new MouseAdapter() {
            public synchronized void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3 && canRemove())
                    popupMenu.show(tree, e.getX(), e.getY());
            }
        });

        tree.expandPath(new TreePath(root.getPath()));    //展开树
        this.add(tree);
    }

    /**
     * 添加选中项
     **/
    public synchronized void addSelection(final Object object) {
        if (object instanceof XNetwork) {
            final XNetwork network = (XNetwork) object;
            if (network.isChecked())    // 已选中
                return;
            /** 从网络根遍历遍历直到找到并设置为选中 **/
            for (int i = 0; i < networkRoot.getChildCount(); i++) {
                if (((DefaultMutableTreeNode) networkRoot.getChildAt(i)).getUserObject().toString()
                        .equals(network.getName())) {
                    tree.addSelectionPath(new TreePath(((DefaultMutableTreeNode) networkRoot.getChildAt(i)).getPath()));
                    return;
                }
            }
        } else if (object instanceof XRouter) {
            final XRouter router = (XRouter) object;
            if (router.isChecked())    //已选中
                return;
            /** 从路由器根遍历直到找到并设置为选中 **/
            for (int i = 0; i < routerRoot.getChildCount(); i++) {
                if (((DefaultMutableTreeNode) routerRoot.getChildAt(i)).getUserObject().toString()
                        .equals(router.getName())) {
                    tree.addSelectionPath(new TreePath(((DefaultMutableTreeNode) routerRoot.getChildAt(i)).getPath()));
                    return;
                }
            }
        }
    }

    /**
     * 取消选中
     **/
    public synchronized void cancelSelection(final Object object) {
        if (!(object instanceof XNetwork ? ((XNetwork) object).isChecked() : ((XRouter) object).isChecked()))    // 未选中
            return;
        final String name = object instanceof XNetwork ? ((XNetwork) object).getName() : ((XRouter) object).getName();
        /** 在选中的路径中查找 **/
        for (TreePath path : tree.getSelectionPaths()) {
            if (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject().toString().equals(name))
                tree.removeSelectionPath(path);
        }
    }

    /**
     * 取消所有选中
     **/
    public void cancelSelections() {
        tree.clearSelection();
    }

    /**
     * 能否进行删除
     **/
    private synchronized boolean canRemove() {
        if (tree.isSelectionEmpty())    // 选中为空
            return false;
        /** 若只选中根、网路根和路由器根的一至三项，则不可删除；否则可以进行删除**/
        for (TreePath path : tree.getSelectionPaths()) {
            final TreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (node == root && (networkRoot.getChildCount() > 0 || routerRoot.getChildCount() > 0))
                return true;
            if (node == networkRoot && networkRoot.getChildCount() > 0)
                return true;
            if (node == routerRoot && routerRoot.getChildCount() > 0)
                return true;
            if (node.getParent() == networkRoot || node.getParent() == routerRoot)
                return true;
        }
        return false;
    }

    /**
     * 删除选中项
     **/
    public synchronized void removeSelections() {
        if (!canRemove())    // 不可进行删除
            return;
        for (TreePath path : tree.getSelectionPaths()) {
            tree.expandPath(path);
            if (path.getLastPathComponent() == root) {    // 根被选中，选中所有
                tree.removeSelectionPath(path);
                final TreePath networkPath = path.pathByAddingChild(networkRoot),
                        routerPath = path.pathByAddingChild(routerRoot);
                tree.expandPath(networkPath);
                tree.removeSelectionPath(networkPath);
                for (int i = 0; i < networkRoot.getChildCount(); i++)
                    tree.addSelectionPath(networkPath.pathByAddingChild(networkRoot.getChildAt(i)));
                tree.expandPath(routerPath);
                tree.removeSelectionPath(routerPath);
                for (int i = 0; i < routerRoot.getChildCount(); i++)
                    tree.addSelectionPath(routerPath.pathByAddingChild(routerRoot.getChildAt(i)));
            }
            if (path.getLastPathComponent() == networkRoot) {    // 网络根被选中，选中所有网络
                tree.removeSelectionPath(path);
                for (int i = 0; i < networkRoot.getChildCount(); i++)
                    tree.addSelectionPath(path.pathByAddingChild(networkRoot.getChildAt(i)));
            }
            if (path.getLastPathComponent() == routerRoot) {    // 路由器根被选中，选中所有路由器
                tree.removeSelectionPath(path);
                for (int i = 0; i < routerRoot.getChildCount(); i++)
                    tree.addSelectionPath(path.pathByAddingChild(routerRoot.getChildAt(i)));
            }
        }

        /** 询问是否删除选中项 **/
        if (JOptionPane.showConfirmDialog(ExplorerPanel.this.getRootPane(),
                "<html><font size='5' face='consolas'>Delete the selection(s)?</font>", "ComputerNetwork Experiment04",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
            return;
        for (TreePath path : tree.getSelectionPaths()) {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (node.getParent() == networkRoot)
                getNetwork(node).remove();
            else if (node.getParent() == routerRoot)
                getRouter(node).remove();
        }
        tree.updateUI();
    }

    /**
     * 添加网络节点
     **/
    public synchronized void addNetwork(final XNetwork network) {
        final DefaultMutableTreeNode node = new DefaultMutableTreeNode(network.getName());
        networkRoot.add(node);
        tree.expandPath(new TreePath(networkRoot.getPath()));
        tree.setSelectionPath(new TreePath(node.getPath()));
        tree.updateUI();
    }

    /**
     * 删除网络节点
     **/
    public synchronized void removeNetwork(final XNetwork network) {
        for (int i = 0; i < networkRoot.getChildCount(); i++) {
            if (((DefaultMutableTreeNode) networkRoot.getChildAt(i)).getUserObject().toString()
                    .equals(network.getName())) {
                networkRoot.remove(i);
                tree.updateUI();
                return;
            }
        }
    }

    /**
     * 获得节点对应的网络
     **/
    private synchronized XNetwork getNetwork(final DefaultMutableTreeNode node) {
        for (XNetwork network : XNetwork.networks) {
            if (network.getName().equals(node.getUserObject().toString()))
                return network;
        }
        return null;
    }

    /**
     * 添加路由器节点
     **/
    public synchronized void addRouter(final XRouter router) {
        final DefaultMutableTreeNode node = new DefaultMutableTreeNode(router.getName());
        routerRoot.add(node);
        tree.expandPath(new TreePath(routerRoot.getPath()));
        tree.setSelectionPath(new TreePath(node.getPath()));
        tree.updateUI();
    }

    /**
     * 删除路由器对应的节点
     **/
    public synchronized void removeRouter(final XRouter router) {
        for (int i = 0; i < routerRoot.getChildCount(); i++) {
            if (((DefaultMutableTreeNode) routerRoot.getChildAt(i)).getUserObject().toString()
                    .equals(router.getName())) {
                routerRoot.remove(i);
                tree.updateUI();
                return;
            }
        }
    }

    /**
     * 获得节点对应的路由器
     **/
    private synchronized XRouter getRouter(final DefaultMutableTreeNode node) {
        for (XRouter router : XRouter.routers) {
            if (router.getName().equals(node.getUserObject().toString()))
                return router;
        }
        return null;
    }

    /**
     * 获得选中项数量
     **/
    public synchronized int getSelectionCount() {
        return tree.getSelectionCount();
    }

    /**
     * 自定义树渲染
     **/
    class XDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            this.setBackgroundSelectionColor(tree.hasFocus() ? new Color(244, 234, 42) : new Color(227, 227, 227));    // 设置选中颜色
            this.setBorderSelectionColor(null);    // 设置选中边框为空
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            /** 设置相应的字体和图标 **/
            this.setFont(new Font("consolas", node == networkRoot || node == routerRoot ? Font.BOLD : Font.PLAIN,
                    node == root ? 20 : (node == networkRoot || node == routerRoot ? 18 : 16)));
            this.setIcon(new ImageIcon(this.getClass().getResource(node.getParent() == networkRoot || value == networkRoot ? "/images/network_icon.png"
                    : node.getParent() == routerRoot || value == routerRoot ? "/images/router_icon.png" : "/images/explorer_icon.png")));
            return this;
        }
    }
}
