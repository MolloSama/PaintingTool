package tools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import filechooser.CreateNewImage;
import paintimage.PaintingFrame;
import paintimage.PaintingOption;

//工具Tool的抽象类
public abstract class AbstractTool implements Tool {
	
	// 定义ImageFrame
	private PaintingFrame frame = null;
	
	// 定义画板的宽与高
	public static int drawWidth = 0;
	public static int drawHeight = 0;
	
	// 定义默认鼠标指针
	private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	
	// 按下鼠标的坐标
	private int pressX = -1;
	private int pressY = -1;
	
	// 拖拽鼠标的坐标
	private int endX = -1;
	private int endY = -1;
	
	// 颜色
	public static Color color = Color.BLACK;
	
	// 线宽
	public static int linewidth = 1;
	
	public AbstractTool(PaintingFrame frame) 
	{
		this.frame = frame;
		AbstractTool.drawWidth = frame.getBufferedImage().getWidth();
		AbstractTool.drawHeight = frame.getBufferedImage().getHeight();
	}
	
	public AbstractTool(PaintingFrame frame, String path) 
	{
		this(frame);
		this.defaultCursor = PaintingOption.createCursor(path);
	}

	//获取当前的绘图窗体
	PaintingFrame getFrame() {
		return this.frame;
	}
	
	//获取鼠标指针
	public Cursor getDefaultCursor() {
		return this.defaultCursor;
	}
	
	//设置默认鼠标指针
	public void setDefaultCursor(Cursor cursor) {
		this.defaultCursor = cursor;
	}

	//设置点击时的X坐标
	public void setPressX(int x) {
		this.pressX = x;
	}

	//设置点击时的Y坐标
	public void setPressY(int y) {
		this.pressY = y;
	}

	//获取点击的X坐标
	public int getPressX() {
		return this.pressX;
	}

	//获取点击的Y坐标
	public int getPressY() {
		return this.pressY;
	}

	//获取拖拽终点的X坐标
	public int getEndX() {
		return endX;
	}

	//获取拖拽中点的Y坐标
	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}

	//重写鼠标拖拽方法
	public void mouseDragged(MouseEvent e) 
	{
		dragBorder(e);// 拖动图形边界
		Graphics g = getFrame().getDrawSpace().getGraphics();
		createShape(e, g);// 画图
	}
	
	//位置常量
	protected static final boolean MID = false;
	protected static final boolean LOW = true;
	
	//判断鼠标是否在拖动原点上
	protected boolean mouseOn(int x,int y,boolean locx,boolean locy) 
	{
		if (locx==MID&&!(x>(int)AbstractTool.drawWidth/2-4&&x<(int)AbstractTool.drawWidth/2+4))
			return false;
		if (locx==LOW&&!(x>AbstractTool.drawWidth-4&&x<AbstractTool.drawWidth+4))
			return false;
		if (locy==MID&&!(y>(int)AbstractTool.drawHeight/2-4&&y<(int)AbstractTool.drawHeight/2+4))
			return false;
		if (locy==LOW&&!(y>AbstractTool.drawHeight-4&&y<AbstractTool.drawHeight+4))
			return false;
		return true;
	}
	
	//判断鼠标是否在指定矩形内
	protected boolean mouseOn(int x,int y,int dx,int dy) 
	{
		if (x > 0 && x < dx && y > 0 && y < dy) return true;
		return false;
	}
	
	//重写鼠标移动方法
	public void mouseMoved(MouseEvent e) 
	{
		int x = e.getX();// 获取鼠标现在的坐标
		int y = e.getY();
		Cursor cursor = getDefaultCursor();// 获取默认鼠标指针
		if (mouseOn(x,y,LOW,LOW)) {// 如果鼠标指针在右下
			cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);// 将鼠标指针改变为右下拖动形状
		}
		if (mouseOn(x,y,LOW,MID)) {// 如果鼠标指针在右中
			cursor = new Cursor(Cursor.W_RESIZE_CURSOR);// 将鼠标指针改变为右拖动形状
		}
		if (mouseOn(x,y,MID,LOW)) {// 如果鼠标指针在中下
			cursor = new Cursor(Cursor.S_RESIZE_CURSOR);// 将鼠标指针改变为下拖动形状
		}
		getFrame().getDrawSpace().setCursor(cursor);// 更新鼠标指针
	}

	//重写鼠标释放方法
	public void mouseReleased(MouseEvent e) {
		Graphics g = getFrame().getBufferedImage().getGraphics();
		createShape(e, g);// 画图
		setPressX(-1);
		setPressY(-1);
		getFrame().getDrawSpace().repaint();// 重绘
	}
	
	//重写鼠标按下方法
	public void mousePressed(MouseEvent e) {
		int x = e.getX();// 如果位置在图片范围内，设置按下的坐标
		int y = e.getY();
		if (mouseOn(x,y,AbstractTool.drawWidth,AbstractTool.drawHeight)) {
			setPressX(x);
			setPressY(y);
		}
	}
	
	
	//拖动
	private void dragBorder(MouseEvent e) 
	{
		getFrame().getBufferedImage().setIsSaved(false);
		// 获取鼠标现在的x与y坐标
		int cursorType = getFrame().getDrawSpace().getCursor().getType();
		int x = cursorType == Cursor.S_RESIZE_CURSOR ? AbstractTool.drawWidth : e.getX();
		int y = cursorType == Cursor.W_RESIZE_CURSOR ? AbstractTool.drawHeight : e.getY();
		CreateNewImage img = null;
		// 如果鼠标指针是拖动状态
		if ((cursorType == Cursor.NW_RESIZE_CURSOR
				|| cursorType == Cursor.W_RESIZE_CURSOR || cursorType == Cursor.S_RESIZE_CURSOR)
				&& (x > 0 && y > 0)) {
			// 改变图像大小
			img = new CreateNewImage(x, y, BufferedImage.TYPE_INT_RGB);
			Graphics g = img.getGraphics();
			g.setColor(Color.WHITE);
			g.drawImage(getFrame().getBufferedImage(),0,0,AbstractTool.drawWidth,AbstractTool.drawHeight,null);
			getFrame().setBufferedImage(img);
			// 设置画布的大小
			AbstractTool.drawWidth = x;
			AbstractTool.drawHeight = y;
			// 设置viewport
			PaintingOption.setViewport(frame.getScroll(), frame.getDrawSpace(), x, y);
		}
	}
	
	//创建当前图形
	private void createShape(MouseEvent e, Graphics g) 
	{
		// 如果位置在画布内
		if (getPressX() > 0 && getPressY() > 0 
				&& mouseOn(e.getX(),e.getY(),AbstractTool.drawWidth,AbstractTool.drawHeight)) {
			// 将整张图片重画
			g.drawImage(getFrame().getBufferedImage(),0,0,AbstractTool.drawWidth,AbstractTool.drawHeight,null);
			// 设置颜色
			g.setColor(AbstractTool.color);
			getFrame().getBufferedImage().setIsSaved(false);
			// 画图形
			draw(g, getPressX(), getPressY(), e.getX(), e.getY());
		}
	}
	
	//画图
	private void draw(Graphics g, int x1, int y1, int x2, int y2) 
	{
		
	}

}
