package tk.eqpic.test;

import java.util.HashMap;
import java.util.Map;

public class Test {

	public static void main(String[] str) {
		String path = "E:/eclipseM9/workspace/tree/WEB-INF/classes/";
		System.out.println(path.substring(0, path.indexOf("WEB-INF")));
		String latString = "115, 58, 18.54";
		String[] strs = latString.split(", ");
		System.out.println(strs.length);
		
		Map<String, String> testMap = new HashMap<String, String>();
		testMap.put("1", "test1");
		testMap.put("2", "test2");
		testMap.put("1", "test");
		
		for (Map.Entry<String, String> entry: testMap.entrySet()) {
			System.out.println(entry.getKey()+":"+entry.getValue());
		}
		
		String regex = "\\[(.+)\\]";
		String testString = "江西省九江市庐山区 [经度: 116.022224, 纬度: 29.65207]";
		if (testString.contains("[")) {
			System.out.println(1);
		} else {
			System.out.println(0);
		}
	}
}
