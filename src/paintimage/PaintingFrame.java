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

//����
public class PaintingFrame extends JFrame 
{
	
	//ҵ���߼�
	private PaintingOption service = new PaintingOption();
	
	//��ʼ����Ļ�ĳߴ�
	private Dimension screenSize = service.getScreenSize();
	
	//����Ĭ�ϻ���
	private JPanel drawSpace = createDrawSpace();
	
	//���û���ͼƬ
	private CreateNewImage bufferedImage = new CreateNewImage((int) screenSize.getWidth() / 2,
												(int) screenSize.getHeight() / 2,
												BufferedImage.TYPE_INT_RGB);
	
	//���õ�ǰʹ�õĹ���
	private Tool tool = null;
	
	//���û�ͼ����
	Graphics g = bufferedImage.getGraphics();
	
	//��ɫ��ʾ���
	private JPanel currentColorPanel = null;
	
	//��ɫѡ����
	private JColorChooser colorChooser = getColorChooser();
	
	//�˵����¼�������
	ActionListener menuListener = new ActionListener() 
	{
		
		public void actionPerformed(ActionEvent e) 
		{
			service.menuDo(PaintingFrame.this, e.getActionCommand());
		}
	};
	
	//Ĭ��JScrollPane
	private JScrollPane scroll = null;
	
	// ������
	JPanel toolPanel = createToolPanel();
	
	// ��ɫ���
	JPanel colorPanel = createColorPanel();
	
	//��Ϣ�ı�
	JPanel infoPanel = createInfoPanel();
	
	//������ʼ������յ�����
	private int startX, startY, endX, endY;
	
	//�߿�
	private int linewidth = 1;
	
	//������ɫ
	private Color backgroundColor = Color.WHITE;
	
	//���캯��
	public PaintingFrame() 
	{
		super();
		
		this.setTitle("��ͼ");
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


	
	//��õ�ǰ��ɫѡ����
	public JColorChooser getColorChooser() 
	{
		if (colorChooser == null) {
			colorChooser = new JColorChooser();
		}
		return colorChooser;
	}
	
	//�˵�
	private void createMenuBar() 
	{
		JMenuBar menuBar = new JMenuBar();
		String[] menuArr = { "�ļ�", "����ɫ", "����(H)" };
		String[][] menuItemArr = { { "���", "���ļ�", "���Ϊ", "-", "�˳�" },
								   { "���ñ�����ɫ" }, { "����", "����" } };
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
	

	//��ɫ���߿�ѡ�����
	public JPanel createColorPanel() 
	{
		JPanel panel = new JPanel();
		
		JPanel panelset1 = new JPanel();	//����������ɫ�������panel
		JPanel panelset2 = new JPanel();	//���������϶��������panel
		
		panel.setLayout(new BorderLayout());
		
		JToolBar toolBar = new JToolBar("��ɫ");
		toolBar.setFloatable(false);
		toolBar.setMargin(new Insets(2, 2, 2, 2));
		toolBar.setLayout(new GridLayout(2, 5, 4, 2));
		
		JLabel label = new JLabel("���ʿ��:  ");
		label.setFont(new Font("����", Font.PLAIN, 12));
		JButton colorbutton = new JButton("��ɫѡ����");
		
		colorbutton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						// ��ȡ��ɫ
						Color color = JColorChooser.showDialog(getColorChooser(), "�༭��ɫ", Color.BLACK);
						color = color == null ? AbstractTool.color : color;
						// ���ù��ߵ���ɫ
						AbstractTool.color = color;
						// ����Ŀǰ��ʾ����ɫ
						getCurrentColorPanel().setBackground(color);
					}
				}
				);
		
		
		JSlider slider;
		slider = new JSlider(SwingConstants.HORIZONTAL, 0, 10, 1);	
		slider.setMajorTickSpacing(1);	//�������̶ȱ�Ǽ��
		slider.setPaintLabels(true);		//���ÿ����̶���ֵ
		slider.setPaintTicks(true);		//���ÿ�������Ŀ̶ȱ��
		
		slider.addChangeListener(
				new ChangeListener()
				{
					public void stateChanged(ChangeEvent event)
					{
						linewidth = slider.getValue();		//��panel�л��Ƶľ��εĳ�����Ϊslider�϶�����ֵ
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

	//����������
	private JPanel createToolPanel() 
	{
		JPanel panel = new JPanel();
		JToolBar toolBar = new JToolBar("������");

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
	
	//������Ϣ�ı����
	private JPanel createInfoPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JLabel label1 = new JLabel("Designed by William Zhang,  Software Engineering,  Xiamen University.");
		JLabel label2 = new JLabel("Version 1.5   ");
		
		label1.setFont(new Font("΢���ź�", Font.PLAIN, 10));
		label2.setFont(new Font("΢���ź�", Font.PLAIN, 10));
		
		panel.add(label1, BorderLayout.WEST);
		panel.add(label2,BorderLayout.EAST);
		return panel;
	}
	
	//�����滭����
	private JPanel createDrawSpace() 
	{
		JPanel drawSpace = new DrawSpace();
		// ����drawSpace�Ĵ�С
		drawSpace.setPreferredSize(new Dimension((int) screenSize.getWidth(),
				(int) screenSize.getHeight()));
		drawSpace.setBackground(backgroundColor);
		return drawSpace;
	}
	
	//��ȡ�滭�����Panel
	public JPanel getDrawSpace() 
	{
		return this.drawSpace;
	}

	//����bufferedImage
	public void setBufferedImage(CreateNewImage bufferedImage) 
	{
		this.bufferedImage = bufferedImage;
	}

	//��ȡ��ǰͼƬ
	public CreateNewImage getBufferedImage() 
	{
		return this.bufferedImage;
	}
	
	//��ȡ�������
	public JScrollPane getScroll() 
	{
		return this.scroll;
	}

	//��ȡ��ǰ��ɫ��ʾ
	public JPanel getCurrentColorPanel() 
	{
		return this.currentColorPanel;
	}
	
	//��ȡ������
	public JPanel getToolPanel() 
	{
		return this.toolPanel;
	}
	
	//���ù���
	public void setTool(Tool tool) 
	{
		this.tool = tool;
	}
	
	//��ȡ����
	public Tool getTool() 
	{
		return this.tool;
	}
	
	//��ȡ��ɫ���
	public JPanel getColorPanel() 
	{
		return this.colorPanel;
	}
	
	//��ȡ��ǰ������ɫ
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	//���õ�ǰ������ɫ
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	//��ȡ��ǰ��Ļ��С
	public Dimension getScreenSize() 
	{
		return this.screenSize;
	}
	
	// ��ͼ����
	public class DrawSpace extends JPanel 
	{
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) 
		{
			Graphics2D g2d = (Graphics2D)g;			//��ʼ��g2d����

			service.repaint(g, bufferedImage);		//�Ȱ�֮ǰ�����image����
			
			g2d.setStroke(new BasicStroke(AbstractTool.linewidth));		//�����߿�
			g2d.setColor(AbstractTool.color);			//������ɫ
			
			if(tool.toString().indexOf("Line")!=-1)
				g2d.drawLine(startX, startY, endX, endY);
			
			else if(tool.toString().indexOf("Rectangle")!=-1)
				g2d.drawRect(startX, startY, endX-startX, endY-startY);
			
			else if(tool.toString().indexOf("Round")!=-1)
				g2d.drawOval(startX, startY, endX-startX, endY-startY);
		}
	}
	
}
