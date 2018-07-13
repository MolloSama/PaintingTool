package tools;

import java.awt.event.MouseEvent;

//工具Tool的接口
public interface Tool {
	
	//工具类型
	public static final String ARROW_TOOL = "ArrowTool";
	public static final String PENCIL_TOOL = "Pencil";
	public static final String ERASER_TOOL = "Eraser";
	public static final String LINE_TOOL = "Line";
	public static final String RECT_TOOL = "Rectangle";
	public static final String ROUND_TOOL = "Round";
	public static final String ATOMIZER_TOOL = "Spray";
	public static final String COLORPICKED_TOOL = "ColorStraw";

	//鼠标按下方法
	public void mousePressed(MouseEvent e);
	
	//鼠标拖拽方法
	public void mouseDragged(MouseEvent e);

	//鼠标释放方法
	public void mouseReleased(MouseEvent e);
	
	//鼠标移动方法
	public void mouseMoved(MouseEvent e);

}
