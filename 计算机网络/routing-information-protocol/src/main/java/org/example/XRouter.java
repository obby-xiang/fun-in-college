package org.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/*
 * class for router
 * Xiang LIU
 * 2017-11-12
 */

@SuppressWarnings("serial")
public class XRouter extends BaseLabel {

	/** 私有的静态常量 **/
	private static final long INTERVAL = 10000, MAXIMUM_TIME = 15000; // 发送路由表时间间隔 规定时间内没有收到某路由器路由表则标记该路由器不可达
	private static final ImageIcon ICON = new ImageIcon("images/router.png"); // 图标

	/** 公有的静态常量 **/
	public static final String TAG = "R";
	public static final Dimension DIMENSION = new Dimension(ICON.getIconWidth(), ICON.getIconHeight()); // 尺寸

	/** 私有的静态变量 **/
	private static int count = 0; // 路由器数量

	/** 公有的静态变量 **/
	public static List<XRouter> routers = new ArrayList<>(); // 路由器

	/** 私有的成员变量 **/
	private JLabel textLabel; // 显示文字的标签
	private boolean checked; // 是否选中
	private int mouseButton; // 用于记录鼠标按下的按钮
	private ArrayList<XInfo> infoList; // 路由表信息
	private Map<String, Long> acceptTime; // 接收各路由表信息的时间
	private Timer timer; // 定时器

	/** 公有的成员变量 **/
	public JTable routerTable; // 路由表
	public List<XNetwork> linkedNetworks; // 连接的网络
	public ConsolePanel.LogPanel logPanel;

	public XRouter() {

		/** 初始化成员变量 **/
		textLabel = new JLabel();
		checked = true;
		mouseButton = 0;
		routerTable = new JTable(new DefaultTableModel(new String[] { "Destination", "Distance", "NextHop" }, 0));
		linkedNetworks = new ArrayList<>();
		infoList = new ArrayList<>();
		acceptTime = new HashMap<>();
		timer = new Timer();

		this.setName(TAG + (++count)); // 名称，路由器唯一标识符
		routers.add(this);

		/** 在其他面板中添加 **/
		HomeFrame.explorerPanel.addRouter(this);
		logPanel = HomeFrame.consolePanel.addPanel(this);

		/** 组件参数设定 **/
		this.setSize(DIMENSION);
		this.setIcon(ICON);
		this.setLayout(new BorderLayout());
		textLabel.setHorizontalAlignment(CENTER);
		textLabel.setOpaque(false);

		this.add(textLabel); // 添加显示文字的标签
		this.setChecked(checked); // 初始设置为选中

		final Point pressedPosition = new Point(); // 用于记录鼠标按下的位置

		/** 添加鼠标事件监听器 **/
		this.addMouseListener(new MouseAdapter() {

			/** 按下鼠标 **/
			public synchronized void mousePressed(MouseEvent e) {

				mouseButton = e.getButton(); // 记录鼠标按下的按钮
				pressedPosition.setLocation(e.getX(), e.getY()); // 记录鼠标按下的位置

				super.mousePressed(e);

				if (e.getButton() == MouseEvent.BUTTON1) { // 左键按下

					/** 是否按下Ctrl键 **/
					if (HomeFrame.workspacePanel.isCtrlPressing()) {
						if (checked)
							HomeFrame.explorerPanel.cancelSelection(XRouter.this);
						else
							HomeFrame.explorerPanel.addSelection(XRouter.this);
					} else {
						HomeFrame.explorerPanel.cancelSelections();
						HomeFrame.explorerPanel.addSelection(XRouter.this);

					}
				} else if (e.getButton() == MouseEvent.BUTTON3) { // 右键按下
					if (!checked) {
						HomeFrame.explorerPanel.cancelSelections();
						HomeFrame.explorerPanel.addSelection(XRouter.this);
					}
					HomeFrame.workspacePanel.showPoupMenu(XRouter.this, e.getX(), e.getY());
				}
			}

			/** 松开鼠标 **/
			public void mouseReleased(MouseEvent e) {

				XRouter.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 默认鼠标指针

				super.mouseReleased(e);

				HomeFrame.workspacePanel.updateUI(); // 更新组件
			}

		});

		/** 添加鼠标移动事件监听器 **/
		this.addMouseMotionListener(new MouseMotionAdapter() {

			/** 鼠标拖动 **/
			public synchronized void mouseDragged(MouseEvent e) {

				XRouter.this.setCursor(new Cursor(Cursor.MOVE_CURSOR)); // 设置鼠标指针

				super.mouseDragged(e);

				/** 按下鼠标左键拖动 **/
				if (mouseButton == MouseEvent.BUTTON1) {
					int x = e.getX() + XRouter.this.getX() - pressedPosition.x,
							y = e.getY() + XRouter.this.getY() - pressedPosition.y,
							maxX = HomeFrame.workspacePanel.getWidth() - DIMENSION.width,
							maxY = HomeFrame.workspacePanel.getHeight() - DIMENSION.height;
					XRouter.this.setLocation(x < 0 ? 0 : (x > maxX ? maxX : x), y < 0 ? 0 : (y > maxY ? maxY : y)); // 更新位置
					WorkspacePanel.adjustPreferredSize(); // 调整容器理想大小
					HomeFrame.workspacePanel.repaint(); // 容器重绘
				}
			}
		});

		/** 路由表组件设置 **/
		routerTable.getTableHeader().setFont(new Font("consolas", Font.BOLD, 20));
		routerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		routerTable.setEnabled(false);
		for(int i=0;i<routerTable.getColumnCount();i++) {
			routerTable.setDefaultRenderer(routerTable.getColumnClass(i), new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					DefaultTableCellRenderer renderer=(DefaultTableCellRenderer) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					this.setFont(new Font("Consoal", Font.PLAIN, 18));
					this.setHorizontalAlignment(CENTER);
					this.setForeground("16".equals(value.toString())?new Color(205,51,51):Color.BLACK);
					return renderer;
				}
			});
		}

		/** 输出信息到面板 **/
		logPanel.append("added");
		HomeFrame.consolePanel.mainPanel.append(this.getName()+":added");

		/** 定时 **/
		timer.schedule(new TimerTask() {

			@SuppressWarnings("rawtypes")
			public synchronized void run() {

				final Iterator iterator = acceptTime.entrySet().iterator();
				final List<String> disabledRouter = new ArrayList<>();

				while (iterator.hasNext()) {
					final Map.Entry entry = (Map.Entry) iterator.next();

					/** 规定时间内没有收到某路由器路由表则标记该路由器不可达 **/
					if (System.currentTimeMillis() - (long) entry.getValue() > MAXIMUM_TIME) {
						synchronized (infoList) {
							for (XInfo info : infoList) {
								synchronized (info) {
									if (info.getNextHop().equals(entry.getKey())) {
										info.setDistance(16);
										disabledRouter.add((String) entry.getKey());

										/** 输出信息到面板 **/
										logPanel.append("cannot reach "+info.getNextHop());
										HomeFrame.consolePanel.mainPanel.append(XRouter.this.getName()+":cannot reach "+info.getNextHop());
									}
								}
							}
						}
					}
				}
				updateTable(); // 刷新路由表组件

				/** 在记录接收时间的哈希表中删除相关记录 **/
				for (String disabled : disabledRouter)
					acceptTime.remove(disabled);

				broadcast(); // 发送路由表
			}

		}, 0, INTERVAL);
	}

	/** 设置路由器是否选中 **/
	public synchronized void setChecked(final boolean isChecked) {
		checked = isChecked;
		textLabel.setText("<html><font size='5' face='consolas' color='"
				+ (isChecked ? (HomeFrame.workspacePanel.isFocusOwner() ? "#F4EA2A" : "#E3E3E3") : "#CD3333") + "'>"
				+ this.getName() + "</font>"); // 根据是否选中及是否获得焦点设置文字颜色
	}

	/** 路由器是否选中 **/
	public synchronized boolean isChecked() {
		return checked;
	}

	/** 移除该路由器 **/
	public synchronized void remove() {
		this.timer.cancel(); // 终止定时器

		/** 在相关面板中移除 **/
		HomeFrame.workspacePanel.remove(this);
		HomeFrame.explorerPanel.removeRouter(this);
		HomeFrame.consolePanel.removePanel(this);

		routers.remove(this);

		/** 断开与相连网络的连接 **/
		while (!this.linkedNetworks.isEmpty())
			delink(this.linkedNetworks.get(0));

		/** 输出信息到面板 **/
		HomeFrame.consolePanel.mainPanel.append(this.getName()+":removed");

		HomeFrame.workspacePanel.updateUI();
	}

	/** 获得与路由器相连网络的位置 **/
	public synchronized List<Point> getTerminalPoints() {
		final List<Point> points = new ArrayList<>();
		for (XNetwork network : linkedNetworks)
			points.add(new Point(network.getX() + XNetwork.DIMENSION.width / 2,
					network.getY() + XNetwork.DIMENSION.height / 2));
		return points;

	}

	/** 连接到网络 **/
	public synchronized void link(final XNetwork network) {
		linkedNetworks.add(network);
		network.linkedRouters.add(this);

		networkLinked(network);

		/** 对相连的网络排序 **/
		Collections.sort(linkedNetworks, new Comparator<XNetwork>() {
			public int compare(XNetwork n1, XNetwork n2) {
				return n1.getName().compareTo(n2.getName());
			}
		});

		/** 对相连网络的相连路由器排序 **/
		Collections.sort(network.linkedRouters, new Comparator<XRouter>() {
			public int compare(XRouter r1, XRouter r2) {
				return r1.getName().compareTo(r2.getName());
			}
		});

		/** 输出信息到面板 **/
		logPanel.append("linked with "+network.getName());
		network.logPanel.append("linked with "+this.getName());
		HomeFrame.consolePanel.mainPanel.append(this.getName()+":linked with "+network.getName());
		HomeFrame.consolePanel.mainPanel.append(network.getName()+":linked with "+this.getName());

		HomeFrame.workspacePanel.repaint();
	}

	/** 与网络断开连接 **/
	public synchronized void delink(final XNetwork network) {

		linkedNetworks.remove(network);
		network.linkedRouters.remove(this);

		networkUnlinked(network);

		/** 输出信息到面板 **/
		logPanel.append("delinked with "+network.getName());
		network.logPanel.append("delinked with "+this.getName());
		HomeFrame.consolePanel.mainPanel.append(this.getName()+":delinked with "+network.getName());
		HomeFrame.consolePanel.mainPanel.append(network.getName()+":delinked with "+this.getName());

		HomeFrame.workspacePanel.repaint();
	}

	/** 可连接的（已连接的除外）网络 **/
	public synchronized List<XNetwork> canLink() {
		final List<XNetwork> networkList = new ArrayList<>();

		/** 未连接到的网络 **/
		for (XNetwork network : XNetwork.networks) {
			if (!linkedNetworks.contains(network))
				networkList.add(network);
		}

		/*
		 * for (XNetwork network : linkedNetworks) { for (XRouter router :
		 * network.linkedRouters) { for (XNetwork net : router.linkedNetworks)
		 * networkList.remove(net); } }
		 */
		return networkList;
	}

	/*
	 * public synchronized List<XRouter> canLinkRouters() { final List<XRouter>
	 * routerList = new ArrayList<>(routers); routerList.remove(this); for (XNetwork
	 * network : linkedNetworks) { for (XRouter router : network.linkedRouters)
	 * routerList.remove(router); } return routerList; }
	 */

	/** 发送路由表 **/
	private synchronized void broadcast() {
		if(linkedNetworks.isEmpty()) {
			/** 输出信息到面板 **/
			logPanel.append("no linking");
			HomeFrame.consolePanel.mainPanel.append(this.getName()+":no linking");
			return;
		}

		final StringBuilder text=new StringBuilder();
		text.append("************************\n");
		for(XInfo info:infoList)
			text.append(info.getDestination()+"\t"+info.getDistance()+"\t"+info.getNextHop()+"\n");
		text.append("************************");

		/** 输出信息到面板 **/
		logPanel.append("broadcast\n"+text);
		HomeFrame.consolePanel.mainPanel.append(this.getName()+":broadcast");

		for (XNetwork network : linkedNetworks) {
			final List<XInfo> infos = new ArrayList<>();
			synchronized (infoList) {
				for (XInfo info : infoList) {
					synchronized (info) {
						infos.add(new XInfo(info));

					}
				}
			}
			network.transfer(this.getName(), infos); // 网络传输
		}
	}

	/** 接收路由表 **/
	public synchronized void accept(final String source, final List<XInfo> infos) {

		final StringBuilder text=new StringBuilder();
		text.append("************************\n");
		for(XInfo info:infos)
			text.append(info.getDestination()+"\t"+info.getDistance()+"\t"+info.getNextHop()+"\n");
		text.append("************************");

		/** 输出信息到面板 **/
		logPanel.append("accepted from "+source+"\n"+text);
		HomeFrame.consolePanel.mainPanel.append(this.getName()+":accepted from "+source);

		synchronized (acceptTime) {
			acceptTime.put(source, System.currentTimeMillis()); // 更新接收时间
		}

		for (XInfo info : infos) {
			/** 修改收到的路由表 **/
			info.setDistance(info.getDistance() < 16 ? info.getDistance() + 1 : 16);
			info.setNextHop(source);

			synchronized (infoList) {
				boolean networkExist = false;
				for (XInfo i : infoList) {
					synchronized (i) {
						/** 存在目的网络 **/
						if (i.getDestination().equals(info.getDestination())) {
							networkExist = true;
							if (i.getNextHop().equals(info.getNextHop())) // 下一跳路由器即发送路由表的路由器，更新距离
								i.setDistance(info.getDistance());
							else if (i.getDistance() > info.getDistance()) { // 下一跳路由器不是发送路由表的路由器且距离比收到的路由器大，更新路由表
								i.setDistance(info.getDistance());
								i.setNextHop(info.getNextHop());
							}
							break;
						}
					}
				}
				/** 不存在目的网络 **/
				if (!networkExist)
					infoList.add(new XInfo(info));
			}
		}

		updateTable();
	}

	/** 已连接到网络，更新路由表 **/
	public synchronized void networkLinked(final XNetwork network) {
		synchronized (infoList) {
			for (XInfo info : infoList) {
				synchronized (info) {
					/** 目的网络已存在，修改相关信息 **/
					if (info.getDestination().equals(network.getName())) {
						info.setDistance(1);
						info.setNextHop(null);
						return;
					}
				}
			}
			infoList.add(new XInfo(network.getName(), 1, null)); // 目的网络不存在则添加信息
		}

		updateTable();
	}

	/** 相连网络已断开，更新路由表 **/
	public synchronized void networkUnlinked(final XNetwork network) {
		synchronized (infoList) {
			for (XInfo info : infoList) {
				synchronized (info) {
					if (info.getDestination().equals(network.getName())) {
						info.setDistance(16);
						break;
					}
				}
			}

		}

		updateTable();
	}

	/** 更新路由表组件 **/
	private synchronized void updateTable() {

		routerTable.removeAll();
		final DefaultTableModel model = ((DefaultTableModel) routerTable.getModel());
		model.setRowCount(infoList.size());
		for (int i = 0; i < infoList.size(); i++) {
			final XInfo info = infoList.get(i);
			model.setValueAt(info.getDestination(), i, 0);
			model.setValueAt(info.getDistance(), i, 1);
			model.setValueAt(info.getNextHop(), i, 2);
		}

		HomeFrame.consolePanel.updateUI();
	}

	public String toString() {
		return getName();
	}
}