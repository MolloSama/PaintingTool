package tools;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import paintimage.PaintingFrame;

//直线
public class Line extends AbstractTool {
	private static Tool tool = null;

	private Line(PaintingFrame frame) {
		super(frame);
	}

	public static Tool getInstance(PaintingFrame frame) {
		if (tool == null) {
			tool = new Line(frame);
		}
		return tool;
	}
	
	//鼠标按下方法
	public void mousePressed(MouseEvent e) 
	{
		int x = e.getX();// 如果位置在图片范围内，设置按下的坐标
		int y = e.getY();
		if (super.mouseOn(x,y,AbstractTool.drawWidth,AbstractTool.drawHeight)) {
			setPressX(x);
			setPressY(y);
		}
	}
	
	public void mouseDragged(MouseEvent e) 
	{
		super.mouseDragged(e);
		setEndX(e.getX());			//获取鼠标拖拽的终点X和Y
		setEndY(e.getY());
		
		getFrame().getDrawSpace().repaint();// 重绘
	}
	
	//鼠标释放方法
	public void mouseReleased(MouseEvent e) 
	{
		Graphics g = getFrame().getBufferedImage().getGraphics();
		draw(g, getPressX(), getPressY(), getEndX(), getEndY());
		
		setPressX(-1);
		setPressY(-1);
		getFrame().getDrawSpace().repaint();// 重绘
	}

	public void draw(Graphics g, int x1, int y1, int x2, int y2) 
	{
		Graphics2D g2d = (Graphics2D)g;			//初始化g2d对象
		
		g2d.setColor(AbstractTool.color);
		g2d.setStroke(new BasicStroke(AbstractTool.linewidth));		//设置线宽
		g2d.drawLine(x1, y1, x2, y2);
	}
}
