package org.example;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/*
 * class for most panels in this project
 * Xiang LIU
 * 2017-11-12
 */

@SuppressWarnings("serial")
public class BasePanel extends JPanel{
	
	public BasePanel(final LayoutManager layoutManager) {
		
		super(layoutManager);
		
		this.setDoubleBuffered(true);
		
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				BasePanel.this.requestFocus();	// 按下鼠标时为面板获得焦点
			}
			
		});
	}
	public BasePanel() {
		this(new FlowLayout());
	}
	
	public synchronized void repaint() {
		super.repaint();
	}
	
	public synchronized void updateUI() {
		super.updateUI();
	}
}