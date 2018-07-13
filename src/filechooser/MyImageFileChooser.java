package filechooser;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

//文件选择器和过滤器
public class MyImageFileChooser extends JFileChooser 
{
	//创建一个图片选择器
	public MyImageFileChooser() {
		super();
		setAcceptAllFileFilterUsed(false);
		
		//设置过滤器
		this.addChoosableFileFilter(new MyFileFilter(new String[] { ".PNG" }, "PNG (*.png)"));
		this.addChoosableFileFilter(new MyFileFilter(new String[] { ".BMP" }, "BMP (*.bmp)"));
		this.addChoosableFileFilter(new MyFileFilter(new String[] { ".JPG",".JPEG" },"JPEG (*.jpg;*.jpeg;)"));
		this.addChoosableFileFilter(new MyFileFilter(new String[] { ".GIF" }, "GIF (*.gif)"));
	}



	//获取后缀名
	public String getSuf() 
	{
		// 获取文件过滤对象
		FileFilter fileFilter = this.getFileFilter();
		String desc = fileFilter.getDescription();
		String[] sufarr = desc.split(" ");
		String suf = sufarr[0].equals("所有图形文件") ? "" : sufarr[0];
		return suf.toLowerCase();
	}


	//自定义的文件过滤器类
	class MyFileFilter extends FileFilter 
	{
		// 后缀名数组
		String[] suffarr;
		// 描述
		String decription;
		
		public MyFileFilter() {
			super();
		}

		//创建一个自定义文件过滤器
		public MyFileFilter(String[] suffarr, String decription) 
		{
			super();
			this.suffarr = suffarr;
			this.decription = decription;
		}

		//重写accept方法
		public boolean accept(File f) {
			// 如果文件的后缀名合法，返回true
			for (String s : suffarr) {
				if (f.getName().toUpperCase().endsWith(s)) {
					return true;
				}
			}
			// 如果是目录，返回true,或者返回false
			return f.isDirectory();
		}

		//获取后缀名描述
		public String getDescription() {
			return this.decription;
		}
	}
}
