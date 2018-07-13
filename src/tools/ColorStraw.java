package tools;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.MouseEvent;

import paintimage.PaintingFrame;

//吸管工具
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

	//鼠标按下方法
	public void mousePressed(MouseEvent e) 
	{
		if (e.getX() > 0 && e.getY() > 0) {
			if (e.getX() < AbstractTool.drawWidth
					&& e.getY() < AbstractTool.drawHeight) {
				setPressX(e.getX());
				setPressY(e.getY());
				/**
				 * 设置颜色 getRGB()返回默认 sRGB ColorModel 中表示颜色的 RGB 值 24-31 位表示
				 * alpha，16-23 位表示红色， 8-15 位表示绿色，0-7 位表示蓝色
				 */
				int rgb = getFrame().getBufferedImage().getRGB(e.getX(),
						e.getY());
				// 前8位
				int int8 = (int) Math.pow(2, 8);
				// 前16位
				int int16 = (int) Math.pow(2, 16);
				// 前24位
				int int24 = (int) Math.pow(2, 24);
				// 分别取0-7位,8-15位,16-23位
				int r = (rgb & (int24 - int16)) >> 16;
				int g = (rgb & (int16 - int8)) >> 8;
				int b = (rgb & (int8 - 1));
				// 设置颜色
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
			// 设置目前显示的颜色
			getFrame().getCurrentColorPanel().setBackground(AbstractTool.color);
		}
	}
}
