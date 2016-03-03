package tk.eqpic.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tk.eqpic.entity.FeedbackFile;
import tk.eqpic.util.CacheMap;

public class FeedbackAction extends IBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8247230537326710628L;
	public static final String FEEDBACK_URL = "http://1.cenc321.sinaapp.com/get.php";
	public static final String GEOCODE_URL = "http://api.map.baidu.com/geoconv/v1/?output=json&ak=M5hW5B104Scn5iL2PBQAGV2w"; // 转换为百度坐标系的http请求
	public static final int COORD_SIZE_PER_REQUEST = 100;  // 目前标度api一次支持100个坐标同时请求
	private List<List<FeedbackFile>> feedList;			// 存储全部详细信息
	private List<Map<String, String>> feedAddressList;  // 存储全部地址详细信息
	static CacheMap<String, FeedbackFile> cache = CacheMap.getDefaultCache();
	private String pageHtml = "";
	private boolean success = false;
	
	public String loadData() throws UnsupportedEncodingException {
		
		// 取得服务端根目录
		/*String rootPath = this.request.getSession().getServletContext().getRealPath("/");
		System.out.println(rootPath);
		File indexJsp = new File(rootPath+"/css/style.css");
		System.out.println(indexJsp.exists());
		System.out.println(indexJsp);*/
		
		this.feedList = new ArrayList<List<FeedbackFile>>();	// 地址对应包含的项
		this.feedAddressList = new ArrayList<Map<String,String>>();
		List<String> addressList = new ArrayList<String>();	// 地址列表

		long now = System.currentTimeMillis();
		while (true) {
			if (cache.size() != 0) {
				// 缓存有数据，则退出
				break;
			}
			if ((System.currentTimeMillis() - now) >= 10000) {
				// 10s后还未有数据，则退出
				break;
			}
		}
		
		// 缓存存在数据后
		if (cache.size() != 0) {
			List<FeedbackFile> feedbackFiles = (List<FeedbackFile>) cache.values();
			for (int i=0; i<feedbackFiles.size(); i++) {
				FeedbackFile tempFeed = feedbackFiles.get(i);
				String pos = tempFeed.getLongitude() + "," + tempFeed.getLatitude();
				int index = -1; 
				for (int j=0; j<addressList.size(); j++) {
					if (addressList.get(j).equals(pos)) {
						index = j;
						break;
					}
				}
				if (index != -1) {
					feedList.get(index).add(tempFeed);
				} else { 
					addressList.add(pos);
					List<FeedbackFile> newFeeds = new ArrayList<FeedbackFile>();
					newFeeds.add(tempFeed);
					feedList.add(newFeeds);
				}
			}
		}
		for (int i=0; i<addressList.size(); i++) {		
			String[] pos = addressList.get(i).split(",");
			Map<String, String> tempAddress = new HashMap<String, String>();
			tempAddress.put("mid", String.valueOf(i));
			tempAddress.put("lng", pos[0]);
			tempAddress.put("lat", pos[1]);
			this.feedAddressList.add(tempAddress);
		}

		this.success = true;
		return SUCCESS;
	}

	public List<List<FeedbackFile>> getFeedList() {
		return feedList;
	}

	public List<Map<String, String>> getFeedAddressList() {
		return feedAddressList;
	}

	public String getPageHtml() {
		return pageHtml;
	}

	public boolean isSuccess() {
		return success;
	}
}
