package actionlisten;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import paintimage.PaintingFrame;
import tools.AbstractTool;
import tools.Tool;
import tools.ToolKeyValueSet;


//�����¼�������

public class MyActionListener extends AbstractAction {
	
	private String name = "";
	private PaintingFrame frame = null;
	private Color color = null;
	private Tool tool = null;
	private JPanel colorPanel = null;
	
	//�����ɫ�������actionlistener
	public MyActionListener(Color color, JPanel colorPanel) 
	{
		// ���ø�������
		super();
		this.color = color;
		this.colorPanel = colorPanel;
	}

	//��Թ����������actionlistener
	public MyActionListener(ImageIcon icon, String name, PaintingFrame frame) {
		// ���ø�������
		super("", icon);
		this.name = name;
		this.frame = frame;
	}
	
	//��д�������ּ�������
	public void actionPerformed(ActionEvent e) 
	{
		tool = name != "" ? ToolKeyValueSet.getToolInstance(frame, name) : tool;// ����tool
		if (tool != null) {
			frame.setTool(tool);// ��������ʹ�õ�tool
		}
		if (color != null) {
			AbstractTool.color = color;// ��������ʹ�õ���ɫ
			colorPanel.setBackground(color);
		}

	}

}
