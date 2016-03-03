package tk.eqpic.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.coobird.thumbnailator.Thumbnails;

public class GenerateThumbThread implements Runnable {

	public final static int FILE_TYPE = 20; // 文件类型
	public final static int URL_TYPE = 21;  // url类型
	public final static int THUMB_WIDTH = 100;		// 缩略图宽度
	public final static int THUMB_HEIGHT = 100;     // 缩略图高度
	private String orginStr;
	private int strType;
	private String outFilepath;
	
	public GenerateThumbThread(String orginStr, int strType, String outFilepath) {
		// TODO Auto-generated constructor stub
		super();
		this.orginStr = orginStr;
		this.strType = strType;
		this.outFilepath = outFilepath;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		switch (this.strType) {
		case FILE_TYPE:
			
			break;

		default:
			// 默认为url类型
			try {
				URL url = new URL(this.orginStr);
				Thumbnails.of(url)
					.size(THUMB_WIDTH, THUMB_HEIGHT)
					.keepAspectRatio(false)
					.toFile(this.outFilepath);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			break;
		}
	}

}
