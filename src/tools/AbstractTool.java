package tools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import filechooser.CreateNewImage;
import paintimage.PaintingFrame;
import paintimage.PaintingOption;

//����Tool�ĳ�����
public abstract class AbstractTool implements Tool {
	
	// ����ImageFrame
	private PaintingFrame frame = null;
	
	// ���廭��Ŀ����
	public static int drawWidth = 0;
	public static int drawHeight = 0;
	
	// ����Ĭ�����ָ��
	private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	
	// ������������
	private int pressX = -1;
	private int pressY = -1;
	
	// ��ק��������
	private int endX = -1;
	private int endY = -1;
	
	// ��ɫ
	public static Color color = Color.BLACK;
	
	// �߿�
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

	//��ȡ��ǰ�Ļ�ͼ����
	PaintingFrame getFrame() {
		return this.frame;
	}
	
	//��ȡ���ָ��
	public Cursor getDefaultCursor() {
		return this.defaultCursor;
	}
	
	//����Ĭ�����ָ��
	public void setDefaultCursor(Cursor cursor) {
		this.defaultCursor = cursor;
	}

	//���õ��ʱ��X����
	public void setPressX(int x) {
		this.pressX = x;
	}

	//���õ��ʱ��Y����
	public void setPressY(int y) {
		this.pressY = y;
	}

	//��ȡ�����X����
	public int getPressX() {
		return this.pressX;
	}

	//��ȡ�����Y����
	public int getPressY() {
		return this.pressY;
	}

	//��ȡ��ק�յ��X����
	public int getEndX() {
		return endX;
	}

	//��ȡ��ק�е��Y����
	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}

	//��д�����ק����
	public void mouseDragged(MouseEvent e) 
	{
		dragBorder(e);// �϶�ͼ�α߽�
		Graphics g = getFrame().getDrawSpace().getGraphics();
		createShape(e, g);// ��ͼ
	}
	
	//λ�ó���
	protected static final boolean MID = false;
	protected static final boolean LOW = true;
	
	//�ж�����Ƿ����϶�ԭ����
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
	
	//�ж�����Ƿ���ָ��������
	protected boolean mouseOn(int x,int y,int dx,int dy) 
	{
		if (x > 0 && x < dx && y > 0 && y < dy) return true;
		return false;
	}
	
	//��д����ƶ�����
	public void mouseMoved(MouseEvent e) 
	{
		int x = e.getX();// ��ȡ������ڵ�����
		int y = e.getY();
		Cursor cursor = getDefaultCursor();// ��ȡĬ�����ָ��
		if (mouseOn(x,y,LOW,LOW)) {// ������ָ��������
			cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);// �����ָ��ı�Ϊ�����϶���״
		}
		if (mouseOn(x,y,LOW,MID)) {// ������ָ��������
			cursor = new Cursor(Cursor.W_RESIZE_CURSOR);// �����ָ��ı�Ϊ���϶���״
		}
		if (mouseOn(x,y,MID,LOW)) {// ������ָ��������
			cursor = new Cursor(Cursor.S_RESIZE_CURSOR);// �����ָ��ı�Ϊ���϶���״
		}
		getFrame().getDrawSpace().setCursor(cursor);// �������ָ��
	}

	//��д����ͷŷ���
	public void mouseReleased(MouseEvent e) {
		Graphics g = getFrame().getBufferedImage().getGraphics();
		createShape(e, g);// ��ͼ
		setPressX(-1);
		setPressY(-1);
		getFrame().getDrawSpace().repaint();// �ػ�
	}
	
	//��д��갴�·���
	public void mousePressed(MouseEvent e) {
		int x = e.getX();// ���λ����ͼƬ��Χ�ڣ����ð��µ�����
		int y = e.getY();
		if (mouseOn(x,y,AbstractTool.drawWidth,AbstractTool.drawHeight)) {
			setPressX(x);
			setPressY(y);
		}
	}
	
	
	//�϶�
	private void dragBorder(MouseEvent e) 
	{
		getFrame().getBufferedImage().setIsSaved(false);
		// ��ȡ������ڵ�x��y����
		int cursorType = getFrame().getDrawSpace().getCursor().getType();
		int x = cursorType == Cursor.S_RESIZE_CURSOR ? AbstractTool.drawWidth : e.getX();
		int y = cursorType == Cursor.W_RESIZE_CURSOR ? AbstractTool.drawHeight : e.getY();
		CreateNewImage img = null;
		// ������ָ�����϶�״̬
		if ((cursorType == Cursor.NW_RESIZE_CURSOR
				|| cursorType == Cursor.W_RESIZE_CURSOR || cursorType == Cursor.S_RESIZE_CURSOR)
				&& (x > 0 && y > 0)) {
			// �ı�ͼ���С
			img = new CreateNewImage(x, y, BufferedImage.TYPE_INT_RGB);
			Graphics g = img.getGraphics();
			g.setColor(Color.WHITE);
			g.drawImage(getFrame().getBufferedImage(),0,0,AbstractTool.drawWidth,AbstractTool.drawHeight,null);
			getFrame().setBufferedImage(img);
			// ���û����Ĵ�С
			AbstractTool.drawWidth = x;
			AbstractTool.drawHeight = y;
			// ����viewport
			PaintingOption.setViewport(frame.getScroll(), frame.getDrawSpace(), x, y);
		}
	}
	
	//������ǰͼ��
	private void createShape(MouseEvent e, Graphics g) 
	{
		// ���λ���ڻ�����
		if (getPressX() > 0 && getPressY() > 0 
				&& mouseOn(e.getX(),e.getY(),AbstractTool.drawWidth,AbstractTool.drawHeight)) {
			// ������ͼƬ�ػ�
			g.drawImage(getFrame().getBufferedImage(),0,0,AbstractTool.drawWidth,AbstractTool.drawHeight,null);
			// ������ɫ
			g.setColor(AbstractTool.color);
			getFrame().getBufferedImage().setIsSaved(false);
			// ��ͼ��
			draw(g, getPressX(), getPressY(), e.getX(), e.getY());
		}
	}
	
	//��ͼ
	private void draw(Graphics g, int x1, int y1, int x2, int y2) 
	{
		
	}

}
