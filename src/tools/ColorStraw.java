package tools;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.MouseEvent;

import paintimage.PaintingFrame;

//���ܹ���
public class ColorStraw extends AbstractTool {
	private static Tool tool = null;

	private ColorStraw(PaintingFrame frame) {
		super(frame, "pics/colorcursor.png");
	}

	public static Tool getInstance(PaintingFrame frame) {
		if (tool == null) {
			tool = new ColorStraw(frame);
		}
		return tool;
	}

	//��갴�·���
	public void mousePressed(MouseEvent e) 
	{
		if (e.getX() > 0 && e.getY() > 0) {
			if (e.getX() < AbstractTool.drawWidth
					&& e.getY() < AbstractTool.drawHeight) {
				setPressX(e.getX());
				setPressY(e.getY());
				/**
				 * ������ɫ getRGB()����Ĭ�� sRGB ColorModel �б�ʾ��ɫ�� RGB ֵ 24-31 λ��ʾ
				 * alpha��16-23 λ��ʾ��ɫ�� 8-15 λ��ʾ��ɫ��0-7 λ��ʾ��ɫ
				 */
				int rgb = getFrame().getBufferedImage().getRGB(e.getX(),
						e.getY());
				// ǰ8λ
				int int8 = (int) Math.pow(2, 8);
				// ǰ16λ
				int int16 = (int) Math.pow(2, 16);
				// ǰ24λ
				int int24 = (int) Math.pow(2, 24);
				// �ֱ�ȡ0-7λ,8-15λ,16-23λ
				int r = (rgb & (int24 - int16)) >> 16;
				int g = (rgb & (int16 - int8)) >> 8;
				int b = (rgb & (int8 - 1));
				// ������ɫ
				AbstractTool.color = new Color(r, g, b);
			} 
			else {
				try {
					AbstractTool.color = new Robot().getPixelColor(e.getX(), e.getY());
				}
				catch (AWTException ae) {
					ae.printStackTrace();
				}
			}
			// ����Ŀǰ��ʾ����ɫ
			getFrame().getCurrentColorPanel().setBackground(AbstractTool.color);
		}
	}
}
