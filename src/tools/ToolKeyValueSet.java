package tools;

import java.util.HashMap;
import java.util.Map;

import paintimage.PaintingFrame;


public class ToolKeyValueSet {

	private static Map<String, Tool> toolMap = null;

	//用一个map来存对应工具的键-值对关系
	public static Tool getToolInstance(PaintingFrame frame, String type) {
		if (toolMap == null) {
			toolMap = new HashMap<String, Tool>();
			toolMap.put(Tool.PENCIL_TOOL, Pencil.getInstance(frame));
			toolMap.put(Tool.ERASER_TOOL, Eraser.getInstance(frame));
			toolMap.put(Tool.LINE_TOOL, Line.getInstance(frame));
			toolMap.put(Tool.RECT_TOOL, Rectangle.getInstance(frame));
			toolMap.put(Tool.ROUND_TOOL, Round.getInstance(frame));
			toolMap.put(Tool.ATOMIZER_TOOL, Spray.getInstance(frame));
			toolMap.put(Tool.COLORPICKED_TOOL, ColorStraw.getInstance(frame));
		}
		return (Tool)toolMap.get(type);
	}
}
