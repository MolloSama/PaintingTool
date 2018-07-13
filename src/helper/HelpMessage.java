package helper;

import java.io.IOException;

public class HelpMessage {
	
	private String path = "C:\\WINDOWS\\system32\\notepad.exe E:\\JAVA\\eclipse\\workspace\\Painting-Tool\\";
	
	public HelpMessage(String path)
	{
		this.path = String.format("%s%s", this.path, path);
	}
	
	public void OpenHelpMessage()
	{
		try {
			Runtime.getRuntime().exec(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
