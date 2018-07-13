package tools;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import paintimage.PaintingFrame;

//����
public class Pencil extends AbstractTool {
	private static Tool tool = null;

	private Pencil(PaintingFrame frame) {
		super(frame, "pics/pencilcursor.png");
	}

	public static Tool getInstance(PaintingFrame frame) {
		if (tool == null) {
			tool = new Pencil(frame);
		}
		return tool;
	}

	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
		// ��ȡͼƬ��Graphics����
		Graphics g = getFrame().getBufferedImage().getGraphics();
		Graphics2D g2d = (Graphics2D)g;			//��ʼ��g2d����
		
		g2d.setColor(AbstractTool.color);
		
		if (getPressX() > 0 && getPressY() > 0) 
		{
			g2d.setColor(AbstractTool.color);
			g2d.setStroke(new BasicStroke(AbstractTool.linewidth));		//�����߿�
			g2d.drawLine(getPressX(), getPressY(), e.getX(), e.getY());
			setPressX(e.getX());
			setPressY(e.getY());
			getFrame().getDrawSpace().repaint();
		}
	}

}
