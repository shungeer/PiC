package tk.eqpic.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.coobird.thumbnailator.Thumbnails;

public class ThumbTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		URL url;
		try {
			url = new URL("http://file.lxi.me/page/2015/1218/d11b4d10751a848ca69f5be98a0d4e8d14123949.jpg");
			Thumbnails.of(url)
				.size(100, 100)
				.keepAspectRatio(false)
				.toFile("C:/Users/xys/Desktop/test.png");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
