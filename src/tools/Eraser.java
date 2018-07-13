package tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import paintimage.PaintingFrame;

//橡皮擦
public class Eraser extends AbstractTool {
	private static Tool tool = null;
	private static Color bkColor = Color.WHITE;
	
	private Eraser(PaintingFrame frame) {
		super(frame, "pics/erasercursor.png");
	}

	public static Tool getInstance(PaintingFrame frame) {
		if (tool == null) {
			tool = new Eraser(frame);
		}
		return tool;
	}

	public void mouseDragged(MouseEvent e) 
	{
		super.mouseDragged(e);
		// 获取图片的Graphics对象
		Graphics g = getFrame().getBufferedImage().getGraphics();
		Graphics2D g2d = (Graphics2D)g;			//初始化g2d对象
		bkColor = getFrame().getBackgroundColor();
		g2d.setColor(bkColor);
		
		if (getPressX() > 0 && getPressY() > 0) 
		{
			g2d.setStroke(new BasicStroke(15));		//设置线宽
			g2d.drawLine(getPressX(), getPressY(), e.getX(), e.getY());
			setPressX(e.getX());
			setPressY(e.getY());
			getFrame().getDrawSpace().repaint();
		}
	}
}
