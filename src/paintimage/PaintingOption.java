package paintimage;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import filechooser.CreateNewImage;
import filechooser.MyImageFileChooser;
import helper.HelpMessage;
import tools.AbstractTool;

//��ͼ���ߴ����߼���(�ǹ���)
public class PaintingOption {
	
	private MyImageFileChooser fileChooser = new MyImageFileChooser();

	//��ȡ��Ļ��С
	public Dimension getScreenSize() {
		Toolkit dt = Toolkit.getDefaultToolkit();
		return dt.getScreenSize();
	}
	
	//��ʼ����ͼ����
	public void initDrawSpace(PaintingFrame frame) 
	{
		Graphics g = frame.getBufferedImage().getGraphics();// ��ȡ��ͼ����
		Dimension d = frame.getDrawSpace().getPreferredSize();// ��ȡ�����Ĵ�С
		int drawWidth = (int) d.getWidth();// ��ȡ��
		int drawHeight = (int) d.getHeight();// ��ȡ��
		g.setColor(frame.getBackgroundColor());// �滭��
		g.fillRect(0, 0, drawWidth, drawHeight);
	}
	
	//����һ���Զ�������
	public static Cursor createCursor(String path) 
	{
		java.awt.Image cursorImage = Toolkit.getDefaultToolkit().createImage(path);
		return Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(10, 10), "mycursor");
	}
	
	//����JScroll�����viewport
	public static void setViewport(JScrollPane scroll, JPanel panel, int width, int height) 
	{
		// �½�һ��JViewport
		JViewport viewport = new JViewport();
		// ����viewport�Ĵ�С
		panel.setPreferredSize(new Dimension(width, height));
		// ����viewport
		viewport.setView(panel);
		scroll.setViewport(viewport);	
	}


	//�˵������Ӧִ��
	public void menuDo(PaintingFrame frame, String cmd) 
	{
		if (cmd.equals("���ñ�����ɫ")) {
			changeBackgroundColor(frame);
		}

		if (cmd.equals("���")) {
			createGraphics(frame);
		}

		if (cmd.equals("���ļ�")) {
			open(frame);
		}

		if (cmd.equals("���Ϊ")) {
			save(true, frame);
		}

		if (cmd.equals("�˳�")) {
			exit(frame);
		}
		
		if (cmd.equals("����")) {
			help();
		}
		
	}
	
	//����
	public void save(boolean b, PaintingFrame frame) 
	{
		if (b) {
			// ���ѡ�񱣴�
			if (fileChooser.showSaveDialog(frame) == MyImageFileChooser.APPROVE_OPTION) {
				// ��ȡ��ǰ·��
				File currentDirectory = fileChooser.getCurrentDirectory();
				// ��ȡ�ļ���
				String fileName = fileChooser.getSelectedFile().getName();
				// ��ȡ��׺��
				String suf = fileChooser.getSuf();
				// ��ϱ���·��
				String savePath = currentDirectory + "\\" + fileName + "."
						+ suf;
				try {
					// ��ͼƬд������·��
					ImageIO.write(frame.getBufferedImage(), suf, new File(
							savePath));
				} catch (java.io.IOException ie) {
					ie.printStackTrace();
				}
				// ���ñ����Ĵ��ڱ���
				frame.setTitle(fileName + "." + suf + " - ��ͼ");
				// �ѱ���
				frame.getBufferedImage().setIsSaved(true);
			}
		} else if (!frame.getBufferedImage().isSaved()) {
			// �½�һ���Ի���
			JOptionPane option = new JOptionPane();
			// ��ʾȷ�ϱ���Ի���YES_NO_OPTION
			int checked = option.showConfirmDialog(frame, "����Ķ�?", "��ͼ", option.YES_NO_OPTION, option.WARNING_MESSAGE);
			// ���ѡ����
			if (checked == option.YES_OPTION) {
				// ����ͼƬ
				save(true, frame);
			}
		}
	}

	//��
	public void open(PaintingFrame frame) 
	{
		save(false, frame);
		// �����һ���ļ�
		if (fileChooser.showOpenDialog(frame) == MyImageFileChooser.APPROVE_OPTION) {
			// ��ȡѡ����ļ�
			File file = fileChooser.getSelectedFile();
			// ���õ�ǰ�ļ���
			fileChooser.setCurrentDirectory(file);
			BufferedImage image = null;
			try {
				// ���ļ���ȡͼƬ
				image = ImageIO.read(file);
			} catch (java.io.IOException e) {
				e.printStackTrace();
				return;
			}
			// ����
			int width = image.getWidth();
			int height = image.getHeight();
			AbstractTool.drawWidth = width;
			AbstractTool.drawHeight = height;
			// ����һ��MyImage
			CreateNewImage myImage = new CreateNewImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			// �Ѷ�ȡ����ͼƬ����myImage����
			myImage.getGraphics().drawImage(image, 0, 0, width, height, null);
			frame.setBufferedImage(myImage);
			// repaint
			frame.getDrawSpace().repaint();
			// ��������viewport
			PaintingOption.setViewport(frame.getScroll(), frame.getDrawSpace(),
					width, height);
			// ���ñ����Ĵ��ڱ���
			frame.setTitle(fileChooser.getSelectedFile().getName() + " - ��ͼ");
		}
	}

	//��գ����´�һ���հ׻�ͼ����
	public void createGraphics(PaintingFrame frame) 
	{
		save(false, frame);
		// ����
		int width = (int) getScreenSize().getWidth() / 2;
		int height = (int) getScreenSize().getHeight() / 2;
		AbstractTool.drawWidth = width;
		AbstractTool.drawHeight = height;
		// ����һ��MyImage
		CreateNewImage myImage = new CreateNewImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = myImage.getGraphics();
		g.setColor(frame.getBackgroundColor());
		g.fillRect(0, 0, width, height);
		frame.setBufferedImage(myImage);
		// repaint
		frame.getDrawSpace().repaint();
		// ��������viewport
		PaintingOption.setViewport(frame.getScroll(), frame.getDrawSpace(),
				width, height);
		// ���ñ����Ĵ��ڱ���
		frame.setTitle("δ����_����");
	}

	//���ñ�����ɫ
	public void changeBackgroundColor(PaintingFrame frame) 
	{
		// ��ȡ��ɫ
		Color color = JColorChooser.showDialog(frame.getColorChooser(), "�༭��ɫ", Color.BLACK);

		frame.setBackgroundColor(color);
	}

	//�˳�����
	public void exit(PaintingFrame frame) 
	{
		save(false, frame);		//�ȱ���
		System.exit(0);
	}
	
	//�򿪰����ĵ�
	public void help()
	{
		HelpMessage help = new HelpMessage("help\\README.txt");
		help.OpenHelpMessage();
	}
	
	//�д��ڴ�С�仯ʱ���ػ����
	public void repaint(Graphics g, CreateNewImage bufferedImage) 
	{
		int drawWidth = bufferedImage.getWidth();
		int drawHeight = bufferedImage.getHeight();
		Dimension screenSize = getScreenSize();
		// ���÷ǻ滭������ɫ
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, (int) screenSize.getWidth() * 10, (int) screenSize.getHeight() * 10);

		// �ѻ����ͼƬ�滭����
		g.drawImage(bufferedImage, 0, 0, drawWidth, drawHeight, null);
	}

}
