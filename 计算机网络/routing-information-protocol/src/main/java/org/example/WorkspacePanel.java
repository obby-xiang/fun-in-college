package org.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/*
 * class for operating networks and routers with graphical interface
 * Xiang LIU
 * 2017-11-12
 */

@SuppressWarnings("serial")
public class WorkspacePanel extends BasePanel {

	private boolean ctrlPressing;	// 是否正在按Ctrl键
	private JPopupMenu popupMenu;	// 弹出式快捷菜单

	public WorkspacePanel() {

		this.setLayout(null);	// 设置布局管理器为空
		
		final GraphicsPanel graphicsPanel = new GraphicsPanel();	// 用于显示连线
		this.add(graphicsPanel, 0);

		this.addComponentListener(new ComponentAdapter() {
			/** 组件大小被改变 **/
			public synchronized void componentResized(ComponentEvent e) {
				super.componentResized(e);
				adjustPreferredSize();	// 调整理想大小
				graphicsPanel.setSize(WorkspacePanel.this.getSize());
				HomeFrame.workspacePanel.updateUI();
			}
		});

		/** 设置弹出式快捷菜单 **/
		popupMenu = new JPopupMenu();
		final JMenu linkMenu = new JMenu("<html><font size='4' face='consolas'>Link</font>"),
				delinkMenu = new JMenu("<html><font size='4' face='consolas'>Delink</font>");	// 连接和断开子菜单
		final JMenuItem linkItem = new JMenuItem("<html><font size='4' face='consolas'>Link</font>"),
				delinkItem = new JMenuItem("<html><font size='4' face='consolas'>Delink</font>"),
				selectAllItem=new JMenuItem("<html><font size='4' face='consolas'>Select all</font>"),
				removeItem = new JMenuItem("<html><font size='4' face='consolas'>Remove</font>");

		popupMenu.add(linkMenu);
		popupMenu.add(linkItem);
		popupMenu.add(delinkMenu);
		popupMenu.add(delinkItem);
		popupMenu.add(selectAllItem);
		popupMenu.add(removeItem);

		final ActionListener selectAllListener=new ActionListener() {
			public synchronized void actionPerformed(ActionEvent arg0) {
				for(XNetwork network: XNetwork.networks)
					HomeFrame.explorerPanel.addSelection(network);

				for(XRouter router: XRouter.routers)
					HomeFrame.explorerPanel.addSelection(router);
			}
		},removeListener=new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				HomeFrame.explorerPanel.removeSelections();
			}
		};	// 全选和删除动作事件监听器

		/** 注册动作事件监听器 **/
		selectAllItem.addActionListener(selectAllListener);
		removeItem.addActionListener(removeListener);
		
		/** 设置快捷键 **/
		selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK));
		this.registerKeyboardAction(selectAllListener, KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK),
				WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		removeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,InputEvent.CTRL_MASK));
		this.registerKeyboardAction(removeListener, KeyStroke.getKeyStroke(KeyEvent.VK_D,InputEvent.CTRL_MASK),
				WHEN_FOCUSED);

		/** 注册弹出菜单事件监听器 **/
		popupMenu.addPopupMenuListener(new PopupMenuListener() {

			/** 菜单即将可见 **/
			public synchronized void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				
				final List<XNetwork> networks = new ArrayList<>();	// 选中的网络
				final List<XRouter> routers = new ArrayList<>();	// 选中的路由器

				for (XNetwork network : XNetwork.networks) {
					if (network.isChecked())
						networks.add(network);
				}

				for (XRouter router : XRouter.routers) {
					if (router.isChecked()) {
						routers.add(router);
					}
				}

				selectAllItem.setEnabled(networks.size()+routers.size()< XNetwork.networks.size()+ XRouter.routers.size());	// 根据是否已经全选设置全选项是否可用
				
				switch (networks.size()+routers.size()) {

				case 0:	// 没有选中项
					if(selectAllItem.isEnabled()) {
						linkMenu.setVisible(false);
						linkItem.setVisible(false);
						delinkMenu.setVisible(false);
						delinkItem.setVisible(false);
						removeItem.setVisible(false);
					}
					break;
				case 1:	// 只有一个选中项
					linkItem.setEnabled(false);
					delinkItem.setEnabled(false);

					if (!networks.isEmpty()) {	// 选中项为网络
						final XNetwork network = networks.get(0);

						linkMenu.setVisible(!network.canLink().isEmpty());
						linkItem.setVisible(!linkMenu.isVisible());

						delinkMenu.setVisible(!network.linkedRouters.isEmpty());
						delinkItem.setVisible(!delinkMenu.isVisible());
						
						/** 如果连接子菜单可见，则为该菜单添加可连接到的路由器 **/
						if (linkMenu.isVisible()) {
							for (XRouter router : network.canLink()) {
								final JMenuItem item = new JMenuItem(
										"<html><font size='4' face='consolas'>" + router.getName() + "</font>");

								item.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										network.link(router);
									}
								});
								linkMenu.add(item);
							}
						}
						
						/** 如果断开子菜单可见，则为该菜单添加已连接的路由器 **/
						if (delinkMenu.isVisible()) {
							for (XRouter router : network.linkedRouters) {
								final JMenuItem item = new JMenuItem(
										"<html><font size='4' face='consolas'>" + router.getName() + "</font>");

								item.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										network.delink(router);
									}
								});
								delinkMenu.add(item);
							}
						}
					} else {	// 选中项为路由器
						final XRouter router = routers.get(0);
						
						final List<XRouter> canLinkRouters=new ArrayList<>();
						for(XRouter r: XRouter.routers) {
							if(r!=router)
								canLinkRouters.add(r);
						}
						
						linkMenu.setVisible(!(router.canLink().isEmpty() && canLinkRouters.isEmpty()));
						linkItem.setVisible(!linkMenu.isVisible());

						delinkMenu.setVisible(!router.linkedNetworks.isEmpty());
						delinkItem.setVisible(!delinkMenu.isVisible());
						
						/** 如果连接子菜单可见，则为该菜单添加可连接到的网络和路由器 **/
						if (linkMenu.isVisible()) {
							for (XNetwork network : router.canLink()) {
								final JMenuItem item = new JMenuItem(
										"<html><font size='4' face='consolas'>" + network.getName() + "</font>");
								item.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										router.link(network);
									}
								});
								linkMenu.add(item);
							}
							for (XRouter r : canLinkRouters) {
								final JMenuItem item = new JMenuItem(
										"<html><font size='4' face='consolas'>" + r.getName() + "</font>");
								item.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										final XNetwork network = WorkspacePanel.this.addNetwork();
										router.link(network);
										r.link(network);
									}
								});
								linkMenu.add(item);
							}
						}
						
						/** 如果断开子菜单可见，则为该菜单添加已连接的网络 **/
						if (delinkMenu.isVisible()) {

							for (XNetwork network : router.linkedNetworks) {
								final JMenuItem item = new JMenuItem(
										"<html><font size='4' face='consolas'>" + network.getName() + "</font>");

								item.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										router.delink(network);
									}
								});
								delinkMenu.add(item);
							}
						}
					}
					break;
				default:	// 二至多个选中项
					linkMenu.setVisible(false);
					linkItem.setVisible(true);
					linkItem.setEnabled(false);

					delinkMenu.setVisible(false);
					delinkItem.setVisible(true);
					delinkItem.setEnabled(false);
					
					/** 根据选中项中是否有连接（包括与未选中项的连接）设置断开菜单项是否可用 **/
					for (XNetwork network : networks) {
						if (!network.linkedRouters.isEmpty()) {
							delinkItem.setEnabled(true);
							break;
						}
					}

					if (!delinkItem.isEnabled()) {
						for (XRouter router : routers) {
							if (!router.linkedNetworks.isEmpty()) {
								delinkItem.setEnabled(true);
								break;
							}
						}
					}

					if (delinkItem.isEnabled()) {	// 断开菜单项可用
						delinkItem.addActionListener(new ActionListener() {
							
							/** 注册动作事件监听器 **/
							public synchronized void actionPerformed(ActionEvent arg0) {
								/** 删除选中项中所有连接（包括与未选中项的连接） **/
								for (XNetwork network : networks) {
									while (!network.linkedRouters.isEmpty())
										network.delink(network.linkedRouters.get(0));
								}
								for (XRouter router : routers) {
									while (!router.linkedNetworks.isEmpty())
										router.delink(router.linkedNetworks.get(0));
								}
							}
						});
					}
					break;
				}
			}
			
			/** 快捷菜单即将不可见 **/
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				linkMenu.removeAll();
				delinkMenu.removeAll();
				removeItem.setVisible(true);
			}

			public void popupMenuCanceled(PopupMenuEvent arg0) {
			}
		});

		/** 添加按键事件监听器 **/
		this.addKeyListener(new KeyAdapter() {

			/** 按下按键 **/
			public synchronized void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL)	// 按下Ctrl键
					ctrlPressing = true;
				super.keyPressed(e);
			}

			/** 松开按键 **/
			public synchronized void keyReleased(KeyEvent e) {
				ctrlPressing = false;
				super.keyReleased(e);
			}
		});

		/** 注册焦点事件监听器 **/
		this.addFocusListener(new FocusAdapter() {

			/** 失去焦点 **/
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				ctrlPressing = false;	//设置没有按着Ctrl键
				for(XNetwork network: XNetwork.networks)
					network.setChecked(network.isChecked());;
					for(XRouter router: XRouter.routers)
						router.setChecked(router.isChecked());
			}

			/** 获得焦点 **/
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				for(XNetwork network: XNetwork.networks)
					network.setChecked(network.isChecked());;
					for(XRouter router: XRouter.routers)
						router.setChecked(router.isChecked());
			}
		});

		/** 注册鼠标事件监听器 **/
		this.addMouseListener(new MouseAdapter() {

			/** 按下鼠标 **/
			public void mousePressed(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON3) {	// 按下鼠标右键
					HomeFrame.explorerPanel.cancelSelections();
					return;
				}
				super.mousePressed(e);
			}

			/** 点击鼠标 **/
			public void mouseClicked(MouseEvent e) {
				HomeFrame.explorerPanel.cancelSelections();
				if (e.getButton() == MouseEvent.BUTTON3&& XNetwork.networks.size()+ XRouter.routers.size()!=0)
					popupMenu.show(WorkspacePanel.this, e.getX(), e.getY());
				super.mouseClicked(e);
			}
		});

		/** 注册鼠标移动事件监听器 **/
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				HomeFrame.explorerPanel.cancelSelections();
				super.mouseDragged(e);

			}
		});

	}

	/** 调整面板理想大小 **/
	public synchronized static void adjustPreferredSize() {
		final Dimension dimension = new Dimension(0, 0);
		int w, h;
		for (XRouter router : XRouter.routers) {
			w = router.getX() + router.getWidth();
			h = router.getY() + router.getHeight();
			if (w > dimension.width)
				dimension.width = w;
			if (h > dimension.height)
				dimension.height = h;
		}
		for (XNetwork network : XNetwork.networks) {
			w = network.getX() + network.getWidth();
			h = network.getY() + network.getHeight();
			if (w > dimension.width)
				dimension.width = w;
			if (h > dimension.height)
				dimension.height = h;
		}
		HomeFrame.workspacePanel.setPreferredSize(dimension);
	}

	/** 是否按着Ctrl键 **/
	public synchronized boolean isCtrlPressing() {
		return ctrlPressing;
	}

	/** 显示快捷菜单 **/
	public synchronized void showPoupMenu(final Object panel, final int x, final int y) {
		popupMenu.show(panel instanceof XNetwork ? (XNetwork) panel : (XRouter) panel, x, y);
	}

	/** 用于画线的面板，置于工作区面板底层 **/
	class GraphicsPanel extends JPanel {
		
		public GraphicsPanel() {
			this.setOpaque(false);	// 设置面板透明
		}

		public synchronized void paint(Graphics g) {
			super.paint(g);
			/** 绘制连线 **/
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setStroke(new BasicStroke(2.0f));
			graphics2d.setColor(new Color(26, 250, 41));
			for (XRouter router : XRouter.routers) {
				final Point point = new Point(router.getX() + XRouter.DIMENSION.width / 2,
						router.getY() + XRouter.DIMENSION.height / 2);
				for (Point p : router.getTerminalPoints())
					graphics2d.drawLine(point.x, point.y, p.x, p.y);
			}
		}
	}

	/** 添加网络 **/
	public synchronized XNetwork addNetwork() {
		final Dimension dimension = this.getSize();
		final XNetwork network = new XNetwork();
		network.setLocation(
				new Random().nextInt(dimension.width < 600 ? 600 : dimension.width - XNetwork.DIMENSION.width),
				new Random().nextInt(dimension.height < 300 ? 300 : dimension.height - XNetwork.DIMENSION.height));	// 随机设置网络位置
		this.add(network, 0);
		adjustPreferredSize();
		this.updateUI();
		return network;
	}

	/** 添加路由器 **/
	public synchronized XRouter addRouter() {
		final Dimension dimension = this.getSize();
		final XRouter router = new XRouter();
		router.setLocation(
				new Random().nextInt(dimension.width < 600 ? 600 : dimension.width - XRouter.DIMENSION.width),
				new Random().nextInt(dimension.height < 300 ? 300 : dimension.height - XRouter.DIMENSION.height));	// 随机设置路由器位置
		this.add(router, 0);
		adjustPreferredSize();
		this.updateUI();
		return router;
	}

}