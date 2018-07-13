package paintimage;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import filechooser.CreateNewImage;
import filechooser.MyImageFileChooser;
import helper.HelpMessage;
import tools.AbstractTool;

//画图工具处理逻辑类(非工具)
public class PaintingOption {
	
	private MyImageFileChooser fileChooser = new MyImageFileChooser();

	//获取屏幕大小
	public Dimension getScreenSize() {
		Toolkit dt = Toolkit.getDefaultToolkit();
		return dt.getScreenSize();
	}
	
	//初始化绘图区域
	public void initDrawSpace(PaintingFrame frame) 
	{
		Graphics g = frame.getBufferedImage().getGraphics();// 获取画图对象
		Dimension d = frame.getDrawSpace().getPreferredSize();// 获取画布的大小
		int drawWidth = (int) d.getWidth();// 获取宽
		int drawHeight = (int) d.getHeight();// 获取高
		g.setColor(frame.getBackgroundColor());// 绘画区
		g.fillRect(0, 0, drawWidth, drawHeight);
	}
	
	//创建一个自定义的鼠标
	public static Cursor createCursor(String path) 
	{
		java.awt.Image cursorImage = Toolkit.getDefaultToolkit().createImage(path);
		return Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(10, 10), "mycursor");
	}
	
	//设置JScroll组件的viewport
	public static void setViewport(JScrollPane scroll, JPanel panel, int width, int height) 
	{
		// 新建一个JViewport
		JViewport viewport = new JViewport();
		// 设置viewport的大小
		panel.setPreferredSize(new Dimension(width, height));
		// 设置viewport
		viewport.setView(panel);
		scroll.setViewport(viewport);	
	}


	//菜单点击对应执行
	public void menuDo(PaintingFrame frame, String cmd) 
	{
		if (cmd.equals("设置背景颜色")) {
			changeBackgroundColor(frame);
		}

		if (cmd.equals("清空")) {
			createGraphics(frame);
		}

		if (cmd.equals("打开文件")) {
			open(frame);
		}

		if (cmd.equals("另存为")) {
			save(true, frame);
		}

		if (cmd.equals("退出")) {
			exit(frame);
		}
		
		if (cmd.equals("帮助")) {
			help();
		}
		
	}
	
	//保存
	public void save(boolean b, PaintingFrame frame) 
	{
		if (b) {
			// 如果选择保存
			if (fileChooser.showSaveDialog(frame) == MyImageFileChooser.APPROVE_OPTION) {
				// 获取当前路径
				File currentDirectory = fileChooser.getCurrentDirectory();
				// 获取文件名
				String fileName = fileChooser.getSelectedFile().getName();
				// 获取后缀名
				String suf = fileChooser.getSuf();
				// 组合保存路径
				String savePath = currentDirectory + "\\" + fileName + "."
						+ suf;
				try {
					// 将图片写到保存路径
					ImageIO.write(frame.getBufferedImage(), suf, new File(
							savePath));
				} catch (java.io.IOException ie) {
					ie.printStackTrace();
				}
				// 设置保存后的窗口标题
				frame.setTitle(fileName + "." + suf + " - 画图");
				// 已保存
				frame.getBufferedImage().setIsSaved(true);
			}
		} else if (!frame.getBufferedImage().isSaved()) {
			// 新建一个对话框
			JOptionPane option = new JOptionPane();
			// 显示确认保存对话框YES_NO_OPTION
			int checked = option.showConfirmDialog(frame, "保存改动?", "画图", option.YES_NO_OPTION, option.WARNING_MESSAGE);
			// 如果选择是
			if (checked == option.YES_OPTION) {
				// 保存图片
				save(true, frame);
			}
		}
	}

	//打开
	public void open(PaintingFrame frame) 
	{
		save(false, frame);
		// 如果打开一个文件
		if (fileChooser.showOpenDialog(frame) == MyImageFileChooser.APPROVE_OPTION) {
			// 获取选择的文件
			File file = fileChooser.getSelectedFile();
			// 设置当前文件夹
			fileChooser.setCurrentDirectory(file);
			BufferedImage image = null;
			try {
				// 从文件读取图片
				image = ImageIO.read(file);
			} catch (java.io.IOException e) {
				e.printStackTrace();
				return;
			}
			// 宽，高
			int width = image.getWidth();
			int height = image.getHeight();
			AbstractTool.drawWidth = width;
			AbstractTool.drawHeight = height;
			// 创建一个MyImage
			CreateNewImage myImage = new CreateNewImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			// 把读取到的图片画到myImage上面
			myImage.getGraphics().drawImage(image, 0, 0, width, height, null);
			frame.setBufferedImage(myImage);
			// repaint
			frame.getDrawSpace().repaint();
			// 重新设置viewport
			PaintingOption.setViewport(frame.getScroll(), frame.getDrawSpace(),
					width, height);
			// 设置保存后的窗口标题
			frame.setTitle(fileChooser.getSelectedFile().getName() + " - 画图");
		}
	}

	//清空（重新打开一个空白绘图区域）
	public void createGraphics(PaintingFrame frame) 
	{
		save(false, frame);
		// 宽，高
		int width = (int) getScreenSize().getWidth() / 2;
		int height = (int) getScreenSize().getHeight() / 2;
		AbstractTool.drawWidth = width;
		AbstractTool.drawHeight = height;
		// 创建一个MyImage
		CreateNewImage myImage = new CreateNewImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = myImage.getGraphics();
		g.setColor(frame.getBackgroundColor());
		g.fillRect(0, 0, width, height);
		frame.setBufferedImage(myImage);
		// repaint
		frame.getDrawSpace().repaint();
		// 重新设置viewport
		PaintingOption.setViewport(frame.getScroll(), frame.getDrawSpace(),
				width, height);
		// 设置保存后的窗口标题
		frame.setTitle("未命名_画板");
	}

	//设置背景颜色
	public void changeBackgroundColor(PaintingFrame frame) 
	{
		// 获取颜色
		Color color = JColorChooser.showDialog(frame.getColorChooser(), "编辑颜色", Color.BLACK);

		frame.setBackgroundColor(color);
	}

	//退出程序
	public void exit(PaintingFrame frame) 
	{
		save(false, frame);		//先保存
		System.exit(0);
	}
	
	//打开帮助文档
	public void help()
	{
		HelpMessage help = new HelpMessage("help\\README.txt");
		help.OpenHelpMessage();
	}
	
	//有窗口大小变化时的重绘操作
	public void repaint(Graphics g, CreateNewImage bufferedImage) 
	{
		int drawWidth = bufferedImage.getWidth();
		int drawHeight = bufferedImage.getHeight();
		Dimension screenSize = getScreenSize();
		// 设置非绘画区的颜色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, (int) screenSize.getWidth() * 10, (int) screenSize.getHeight() * 10);

		// 把缓冲的图片绘画出来
		g.drawImage(bufferedImage, 0, 0, drawWidth, drawHeight, null);
	}

}
