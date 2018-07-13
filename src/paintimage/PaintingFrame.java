package paintimage;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import actionlisten.MyActionListener;
import filechooser.CreateNewImage;
import tools.AbstractTool;
import tools.Tool;
import tools.ToolKeyValueSet;

//窗口
public class PaintingFrame extends JFrame 
{
	
	//业务逻辑
	private PaintingOption service = new PaintingOption();
	
	//初始化屏幕的尺寸
	private Dimension screenSize = service.getScreenSize();
	
	//设置默认画板
	private JPanel drawSpace = createDrawSpace();
	
	//设置缓冲图片
	private CreateNewImage bufferedImage = new CreateNewImage((int) screenSize.getWidth() / 2,
												(int) screenSize.getHeight() / 2,
												BufferedImage.TYPE_INT_RGB);
	
	//设置当前使用的工具
	private Tool tool = null;
	
	//设置画图对象
	Graphics g = bufferedImage.getGraphics();
	
	//颜色显示面板
	private JPanel currentColorPanel = null;
	
	//颜色选择器
	private JColorChooser colorChooser = getColorChooser();
	
	//菜单的事件监听器
	ActionListener menuListener = new ActionListener() 
	{
		
		public void actionPerformed(ActionEvent e) 
		{
			service.menuDo(PaintingFrame.this, e.getActionCommand());
		}
	};
	
	//默认JScrollPane
	private JScrollPane scroll = null;
	
	// 工具栏
	JPanel toolPanel = createToolPanel();
	
	// 颜色面板
	JPanel colorPanel = createColorPanel();
	
	//信息文本
	JPanel infoPanel = createInfoPanel();
	
	//鼠标的起始坐标和终点坐标
	private int startX, startY, endX, endY;
	
	//线宽
	private int linewidth = 1;
	
	//背景颜色
	private Color backgroundColor = Color.WHITE;
	
	//构造函数
	public PaintingFrame() 
	{
		super();
		
		this.setTitle("画图");
		service.initDrawSpace(this);
		tool = ToolKeyValueSet.getToolInstance(this, Tool.PENCIL_TOOL);
		MouseMotionListener motionListener = new MouseMotionAdapter() 
		{
			public void mouseDragged(MouseEvent e) 
			{
				tool.mouseDragged(e);
				endX = e.getX();
				endY = e.getY();

			}
			
			public void mouseMoved(MouseEvent e) {
				tool.mouseMoved(e);
			}
		};
		MouseListener mouseListener = new MouseAdapter() 
		{
			public void mouseReleased(MouseEvent e) {
				tool.mouseReleased(e);
			}
			public void mousePressed(MouseEvent e) {
				tool.mousePressed(e);
				startX = e.getX();
				startY = e.getY();
			}

		};
		drawSpace.addMouseMotionListener(motionListener);
		drawSpace.addMouseListener(mouseListener);
		createMenuBar();
		scroll = new JScrollPane(drawSpace);
		PaintingOption.setViewport(scroll, drawSpace, bufferedImage.getWidth(),bufferedImage.getHeight());
		this.add(scroll, BorderLayout.CENTER);
		this.add(toolPanel, BorderLayout.EAST);
		this.add(colorPanel, BorderLayout.NORTH);
		this.add(infoPanel, BorderLayout.SOUTH);
		this.pack();
	}


	
	//获得当前颜色选择器
	public JColorChooser getColorChooser() 
	{
		if (colorChooser == null) {
			colorChooser = new JColorChooser();
		}
		return colorChooser;
	}
	
	//菜单
	private void createMenuBar() 
	{
		JMenuBar menuBar = new JMenuBar();
		String[] menuArr = { "文件", "背景色", "帮助(H)" };
		String[][] menuItemArr = { { "清空", "打开文件", "另存为", "-", "退出" },
								   { "设置背景颜色" }, { "帮助", "关于" } };
		for (int i = 0; i < menuArr.length; i++) 
		{
			JMenu menu = new JMenu(menuArr[i]);
			for (int j = 0; j < menuItemArr[i].length; j++) 
			{
				if (menuItemArr[i][j].equals("-")) 
				{
					menu.addSeparator();
				}
				else 
				{
					JMenuItem menuItem = new JMenuItem(menuItemArr[i][j]);
					menuItem.addActionListener(menuListener);
					menu.add(menuItem);
				}
			}
			menuBar.add(menu);
		}
		this.setJMenuBar(menuBar);
	}
	

	//颜色和线宽选择面板
	public JPanel createColorPanel() 
	{
		JPanel panel = new JPanel();
		
		JPanel panelset1 = new JPanel();	//用于整合颜色板组件的panel
		JPanel panelset2 = new JPanel();	//用于整合拖动条组件的panel
		
		panel.setLayout(new BorderLayout());
		
		JToolBar toolBar = new JToolBar("颜色");
		toolBar.setFloatable(false);
		toolBar.setMargin(new Insets(2, 2, 2, 2));
		toolBar.setLayout(new GridLayout(2, 5, 4, 2));
		
		JLabel label = new JLabel("画笔宽度:  ");
		label.setFont(new Font("黑体", Font.PLAIN, 12));
		JButton colorbutton = new JButton("颜色选择器");
		
		colorbutton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						// 获取颜色
						Color color = JColorChooser.showDialog(getColorChooser(), "编辑颜色", Color.BLACK);
						color = color == null ? AbstractTool.color : color;
						// 设置工具的颜色
						AbstractTool.color = color;
						// 设置目前显示的颜色
						getCurrentColorPanel().setBackground(color);
					}
				}
				);
		
		
		JSlider slider;
		slider = new JSlider(SwingConstants.HORIZONTAL, 0, 10, 1);	
		slider.setMajorTickSpacing(1);	//设置主刻度标记间隔
		slider.setPaintLabels(true);		//设置开启刻度数值
		slider.setPaintTicks(true);		//设置开启滑块的刻度标记
		
		slider.addChangeListener(
				new ChangeListener()
				{
					public void stateChanged(ChangeEvent event)
					{
						linewidth = slider.getValue();		//将panel中绘制的矩形的长设置为slider拖动的数值
						AbstractTool.linewidth = linewidth;
					}
				}
				);
		Color[] colorArr = { Color.BLACK, Color.GRAY, Color.BLUE,  Color.RED, Color.ORANGE, 
							 Color.WHITE, Color.GREEN, Color.CYAN, Color.PINK, Color.YELLOW };
		JButton[] panelArr = new JButton[colorArr.length];
		currentColorPanel = new JPanel();
		currentColorPanel.setBackground(Color.BLACK);
		currentColorPanel.setPreferredSize(new Dimension(20, 20));
		for (int i = 0; i < panelArr.length; i++) {
			panelArr[i] = new JButton(new MyActionListener(colorArr[i],currentColorPanel));
			panelArr[i].setBackground(colorArr[i]);
			toolBar.add(panelArr[i]);
		}
		panelset1.add(currentColorPanel);
		panelset1.add(toolBar);
		panelset1.add(colorbutton);
		
		panelset2.add(label);
		panelset2.add(slider);
		
		panel.add(panelset1, BorderLayout.WEST);
		panel.add(panelset2, BorderLayout.EAST);
		return panel;
	}

	//创建工具栏
	private JPanel createToolPanel() 
	{
		JPanel panel = new JPanel();
		JToolBar toolBar = new JToolBar("工具栏");

		toolBar.setFloatable(true);
		toolBar.setMargin(new Insets(2, 2, 2, 2));
		toolBar.setLayout(new GridLayout(7, 1, 2, 2));
		String[] toolarr = { Tool.PENCIL_TOOL, Tool.COLORPICKED_TOOL, Tool.ATOMIZER_TOOL, 
				Tool.ERASER_TOOL, Tool.LINE_TOOL, Tool.RECT_TOOL, Tool.ROUND_TOOL};
		for (int i = 0; i < toolarr.length; i++) 
		{
			MyActionListener action = new MyActionListener(new ImageIcon("pics/" + toolarr[i] + ".png"), toolarr[i], this);
			JButton button = new JButton(action);
			toolBar.add(button);
		}
		panel.add(toolBar);
		return panel;
	}
	
	//创建信息文本面板
	private JPanel createInfoPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JLabel label1 = new JLabel("Designed by William Zhang,  Software Engineering,  Xiamen University.");
		JLabel label2 = new JLabel("Version 1.5   ");
		
		label1.setFont(new Font("微软雅黑", Font.PLAIN, 10));
		label2.setFont(new Font("微软雅黑", Font.PLAIN, 10));
		
		panel.add(label1, BorderLayout.WEST);
		panel.add(label2,BorderLayout.EAST);
		return panel;
	}
	
	//创建绘画区域
	private JPanel createDrawSpace() 
	{
		JPanel drawSpace = new DrawSpace();
		// 设置drawSpace的大小
		drawSpace.setPreferredSize(new Dimension((int) screenSize.getWidth(),
				(int) screenSize.getHeight()));
		drawSpace.setBackground(backgroundColor);
		return drawSpace;
	}
	
	//获取绘画区域的Panel
	public JPanel getDrawSpace() 
	{
		return this.drawSpace;
	}

	//设置bufferedImage
	public void setBufferedImage(CreateNewImage bufferedImage) 
	{
		this.bufferedImage = bufferedImage;
	}

	//获取当前图片
	public CreateNewImage getBufferedImage() 
	{
		return this.bufferedImage;
	}
	
	//获取滑动面板
	public JScrollPane getScroll() 
	{
		return this.scroll;
	}

	//获取当前颜色显示
	public JPanel getCurrentColorPanel() 
	{
		return this.currentColorPanel;
	}
	
	//获取工具栏
	public JPanel getToolPanel() 
	{
		return this.toolPanel;
	}
	
	//设置工具
	public void setTool(Tool tool) 
	{
		this.tool = tool;
	}
	
	//获取工具
	public Tool getTool() 
	{
		return this.tool;
	}
	
	//获取颜色面板
	public JPanel getColorPanel() 
	{
		return this.colorPanel;
	}
	
	//获取当前背景颜色
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	//设置当前背景颜色
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	//获取当前屏幕大小
	public Dimension getScreenSize() 
	{
		return this.screenSize;
	}
	
	// 画图区域
	public class DrawSpace extends JPanel 
	{
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) 
		{
			Graphics2D g2d = (Graphics2D)g;			//初始化g2d对象

			service.repaint(g, bufferedImage);		//先把之前保存的image画上
			
			g2d.setStroke(new BasicStroke(AbstractTool.linewidth));		//设置线宽
			g2d.setColor(AbstractTool.color);			//设置颜色
			
			if(tool.toString().indexOf("Line")!=-1)
				g2d.drawLine(startX, startY, endX, endY);
			
			else if(tool.toString().indexOf("Rectangle")!=-1)
				g2d.drawRect(startX, startY, endX-startX, endY-startY);
			
			else if(tool.toString().indexOf("Round")!=-1)
				g2d.drawOval(startX, startY, endX-startX, endY-startY);
		}
	}
	
}
