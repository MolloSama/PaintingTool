package actionlisten;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import paintimage.PaintingFrame;
import tools.AbstractTool;
import tools.Tool;
import tools.ToolKeyValueSet;


//按键事件处理类

public class MyActionListener extends AbstractAction {
	
	private String name = "";
	private PaintingFrame frame = null;
	private Color color = null;
	private Tool tool = null;
	private JPanel colorPanel = null;
	
	//针对颜色面板点击的actionlistener
	public MyActionListener(Color color, JPanel colorPanel) 
	{
		// 调用父构造器
		super();
		this.color = color;
		this.colorPanel = colorPanel;
	}

	//针对工具栏点击的actionlistener
	public MyActionListener(ImageIcon icon, String name, PaintingFrame frame) {
		// 调用父构造器
		super("", icon);
		this.name = name;
		this.frame = frame;
	}
	
	//重写动作表现监听方法
	public void actionPerformed(ActionEvent e) 
	{
		tool = name != "" ? ToolKeyValueSet.getToolInstance(frame, name) : tool;// 设置tool
		if (tool != null) {
			frame.setTool(tool);// 设置正在使用的tool
		}
		if (color != null) {
			AbstractTool.color = color;// 设置正在使用的颜色
			colorPanel.setBackground(color);
		}

	}

}
