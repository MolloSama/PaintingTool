package tools;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Random;

import paintimage.PaintingFrame;

//�����
public class Spray extends AbstractTool {
	private static Tool tool = null;

	private Spray(PaintingFrame frame) {
		super(frame, "pics/atomizercursor.png");
	}

	public static Tool getInstance(PaintingFrame frame) {
		if (tool == null) {
			tool = new Spray(frame);
		}
		return tool;
	}

	//���������
	public void mousePressed(MouseEvent e) {
		int x = e.getX();// ���λ����ͼƬ��Χ�ڣ����ð��µ�����
		int y = e.getY();
		if (super.mouseOn(x,y,AbstractTool.drawWidth,AbstractTool.drawHeight)) 
		{
			setPressX(x);
			setPressY(y);
			Graphics g = getFrame().getBufferedImage().getGraphics();
			draw(e, g);
		}
	}

	//�����ק����
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
		Graphics g = getFrame().getBufferedImage().getGraphics();
		draw(e, g);
	}


	public void draw(MouseEvent e, Graphics g) 
	{
		int x = 0;
		int y = 0;
		// ��ǹ��С
		int size = 15;
		// ��ǹ����
		int count = 10;
		if (getPressX() > 0 && getPressY() > 0&&e.getX() < AbstractTool.drawWidth&& e.getY() < AbstractTool.drawHeight) 
		{
			g.setColor(AbstractTool.color);
			for (int i = 0; i < count; i++) 
			{
				x = new Random().nextInt(size) + 1;
				y = new Random().nextInt(size) + 1;
				g.fillRect(e.getX() + x, e.getY() + y, 1, 1);
			}
			setPressX(e.getX());
			setPressY(e.getY());
			getFrame().getDrawSpace().repaint();
		}
	}
}
