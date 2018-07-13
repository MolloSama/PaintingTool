package filechooser;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

//�̳���BufferedImage��ͼƬ��
public class CreateNewImage extends BufferedImage {

	//�����ǣ��ж��Ƿ��Ѿ�����
	private boolean isSaved = true;
	
	//�����µ�ͼƬ
	public CreateNewImage(int width, int height, int type) 
	{
		super(width, height, type);
		this.getGraphics().fillRect(0, 0, width, height);
	}
	
	//���ñ�����
	public void setIsSaved(boolean b) {
		this.isSaved = b;
	}

	//��ȡ������
	public boolean isSaved() {
		return this.isSaved;
	}
}
