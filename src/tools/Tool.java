package tools;

import java.awt.event.MouseEvent;

//����Tool�Ľӿ�
public interface Tool {
	
	//��������
	public static final String ARROW_TOOL = "ArrowTool";
	public static final String PENCIL_TOOL = "Pencil";
	public static final String ERASER_TOOL = "Eraser";
	public static final String LINE_TOOL = "Line";
	public static final String RECT_TOOL = "Rectangle";
	public static final String ROUND_TOOL = "Round";
	public static final String ATOMIZER_TOOL = "Spray";
	public static final String COLORPICKED_TOOL = "ColorStraw";

	//��갴�·���
	public void mousePressed(MouseEvent e);
	
	//�����ק����
	public void mouseDragged(MouseEvent e);

	//����ͷŷ���
	public void mouseReleased(MouseEvent e);
	
	//����ƶ�����
	public void mouseMoved(MouseEvent e);

}
