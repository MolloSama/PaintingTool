package filechooser;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

//继承自BufferedImage的图片类
public class CreateNewImage extends BufferedImage {

	//保存标记：判断是否已经保存
	private boolean isSaved = true;
	
	//创建新的图片
	public CreateNewImage(int width, int height, int type) 
	{
		super(width, height, type);
		this.getGraphics().fillRect(0, 0, width, height);
	}
	
	//设置保存标记
	public void setIsSaved(boolean b) {
		this.isSaved = b;
	}

	//获取保存标记
	public boolean isSaved() {
		return this.isSaved;
	}
}
