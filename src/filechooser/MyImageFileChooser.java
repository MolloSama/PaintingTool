package filechooser;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

//�ļ�ѡ�����͹�����
public class MyImageFileChooser extends JFileChooser 
{
	//����һ��ͼƬѡ����
	public MyImageFileChooser() {
		super();
		setAcceptAllFileFilterUsed(false);
		
		//���ù�����
		this.addChoosableFileFilter(new MyFileFilter(new String[] { ".PNG" }, "PNG (*.png)"));
		this.addChoosableFileFilter(new MyFileFilter(new String[] { ".BMP" }, "BMP (*.bmp)"));
		this.addChoosableFileFilter(new MyFileFilter(new String[] { ".JPG",".JPEG" },"JPEG (*.jpg;*.jpeg;)"));
		this.addChoosableFileFilter(new MyFileFilter(new String[] { ".GIF" }, "GIF (*.gif)"));
	}



	//��ȡ��׺��
	public String getSuf() 
	{
		// ��ȡ�ļ����˶���
		FileFilter fileFilter = this.getFileFilter();
		String desc = fileFilter.getDescription();
		String[] sufarr = desc.split(" ");
		String suf = sufarr[0].equals("����ͼ���ļ�") ? "" : sufarr[0];
		return suf.toLowerCase();
	}


	//�Զ�����ļ���������
	class MyFileFilter extends FileFilter 
	{
		// ��׺������
		String[] suffarr;
		// ����
		String decription;
		
		public MyFileFilter() {
			super();
		}

		//����һ���Զ����ļ�������
		public MyFileFilter(String[] suffarr, String decription) 
		{
			super();
			this.suffarr = suffarr;
			this.decription = decription;
		}

		//��дaccept����
		public boolean accept(File f) {
			// ����ļ��ĺ�׺���Ϸ�������true
			for (String s : suffarr) {
				if (f.getName().toUpperCase().endsWith(s)) {
					return true;
				}
			}
			// �����Ŀ¼������true,���߷���false
			return f.isDirectory();
		}

		//��ȡ��׺������
		public String getDescription() {
			return this.decription;
		}
	}
}
