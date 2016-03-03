package tk.eqpic.util;

import java.io.File;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import tk.eqpic.action.FeedbackAction;
import tk.eqpic.entity.Feedback;
import tk.eqpic.entity.FeedbackFile;
/**
 * 用来存储短暂对象的缓存类，内部设定一个定时器，给定时间进行更新
 * @author xys
 *
 * @param <K>
 * @param <V>
 */
public class CacheMap<K, V> extends AbstractMap<K, V> {

	private static final int UPDATE_TIME = 600000;		// 10分钟更新一次缓存
	private static final int TIMEOUT = 30000;			// 当未获取到数据时，30秒更新一次
	public static final String THUMB_DIR = "image/thumb/";
	private static CacheMap<String, FeedbackFile> defaultInstance;
	
	public static synchronized final CacheMap<String, FeedbackFile> getDefaultCache() {
		if (defaultInstance == null) {
			defaultInstance = new CacheMap<String, FeedbackFile>(UPDATE_TIME);
		}
		return defaultInstance;
	}
	
	private class CacheEntry implements Entry<K, V> {

		private long time;
		private V value;
		private K key;
		
		public CacheEntry(K key, V value) {
			// TODO Auto-generated constructor stub
			super();
			this.key = key;
			this.value = value;
			this.time = System.currentTimeMillis();
		}
		
		public long GetTime() {
			return this.time;
		}
		
		@Override
		public K getKey() {
			// TODO Auto-generated method stub
			return this.key;
		}

		@Override
		public V getValue() {
			// TODO Auto-generated method stub
			return this.value;
		}

		@Override
		public V setValue(V value) {
			// TODO Auto-generated method stub
			return this.value = value;
		}
	}
	
	private long cacheTimeout;
	private Map<K, CacheEntry> map = new HashMap<K, CacheEntry>();
	private boolean fetchFlag = false;
	private class UpdateThread extends Thread {
		public UpdateThread() {
			// TODO Auto-generated constructor stub
			this.setName("给定间隔,更新缓存线程");
		}
		
		public void run() {
			while (true) {
				try {
					long now = System.currentTimeMillis();
					Object[] keys = map.keySet().toArray();
					if (keys == null || keys.length == 0) {
						// 首次加载的时候
						synchronized (map) {
							fetchFlag = false;
							fetchFlag = updateWork();
						}
					} else {
						for (Object key : keys) {
							CacheEntry entry = map.get(key);
							if (now - entry.GetTime() >= cacheTimeout) {
								// 开始更新线程
								synchronized (map) {
									fetchFlag = false;
									fetchFlag = updateWork();
								}
								break;		// 只要其中一个key满足条件，则整个缓存会被更新
							}
						}
					}
					
					keys = map.keySet().toArray();
					if (keys == null || keys.length == 0) {
						// 还未获取到数据
						Thread.sleep(TIMEOUT);   // 10s后再次请求
					} else {
						Thread.sleep(cacheTimeout);	// 10m后更新缓存
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	private boolean updateWork() {
		// 先清空原来的缓存数据
		map.clear();
		
		// 获取image路径
		URL resource = this.getClass().getClassLoader().getResource("/");
		if (resource != null) {
			String classPath = resource.getPath();
			String thumbDir = classPath.substring(0, classPath.indexOf("WEB-INF")) + "/" + THUMB_DIR;
			File thumbDirFile = new File(thumbDir);
			if (!thumbDirFile.exists()) {
				thumbDirFile.mkdirs();
			} else {
				// 存在的话先删除，再创建
				thumbDirFile.delete();
				thumbDirFile.mkdirs();
			}
			// 生成缩略图的线程
			ExecutorService excutor = Executors.newCachedThreadPool();
			
			// 1.根据初始url取出返回json的url
			String feedStr = HttpInvoker.readContentByGet(FeedbackAction.FEEDBACK_URL);
			String realUrl = feedStr.substring(feedStr.lastIndexOf("}")+1);
			String feedJson = HttpInvoker.readContentByGet(realUrl);
			
			// 2.解析json串
			JSONObject jsonObject = JSONObject.parseObject(feedJson);
			System.out.println(feedJson);
			JSONArray feedArray = jsonObject.getJSONObject("data").getJSONArray("feedbacks");
			
			//int realSize = 0;
			//List<String> keyList = new ArrayList<String>();
			
			for (int i=0; i<feedArray.size(); i++) {
				
				JSONObject curJsonObject = feedArray.getJSONObject(i);
				JSONObject detailJsonObject = curJsonObject.getJSONObject("detail");
				//JSONObject picJsonObject = detailJsonObject.getJSONObject("key3");
				String picPath = detailJsonObject.getString("key3");//picJsonObject.getString("path");
				if (picPath != null && !picPath.equals("")) {
					// 根据图片url得到上传地点
					String picHttp = picPath + "?exif";	// 图片属性http请求
					String picJson = HttpInvoker.readContentByGet(picHttp);
					if (picJson != null && !picJson.equals("")) {
						String address = detailJsonObject.getString("key5");
						boolean isaddre = false;
						
						JSONObject picObject = JSONObject.parseObject(picJson);
						JSONObject latObj = picObject.getJSONObject("GPSLatitude");
						
						String dateStr = null;
						float lat = 0.0f;
						float lon = 0.0f;
						
						if (latObj != null) {
							
							JSONObject lonObj = picObject.getJSONObject("GPSLongitude");
							JSONObject dateObj = picObject.getJSONObject("GPSDateStamp");
							String latStr = latObj.getString("val");
							String lonStr = lonObj.getString("val");
							dateStr = dateObj.getString("val");
							//String lat = latStr.substring(0, latStr.lastIndexOf(",")).replaceAll(", ", ".");
							//String lon = lonStr.substring(0, lonStr.lastIndexOf(",")).replaceAll(", ", ".");
							String[] lats = latStr.split(", ");
							String[] lons = lonStr.split(", ");
							lat = Float.valueOf(lats[0]) + (Float.valueOf(lats[1]) + Float.valueOf(lats[2])/60)/60 ;//+ (float)0.006;
							lon = Float.valueOf(lons[0]) + (Float.valueOf(lons[1]) + Float.valueOf(lons[2])/60)/60 ;//+ (float)0.012;
							/*String latitude = String.valueOf(lat);
							String longitude = String.valueOf(lon);
							
							keyList.add(id);
							realSize++;
							if (realSize == )*/
							isaddre = true;
							
						} else {
							// 为空的话，用“key5”字段的经纬度
							if (address.contains("[")) {
								String str1 = address.substring(address.indexOf("["), address.indexOf("]"));
								String lonStr = str1.substring(str1.indexOf(":"), str1.indexOf(","));
								String latStr = str1.substring(str1.lastIndexOf(":"), str1.indexOf("]"));
								lat = Float.valueOf(latStr);
								lon = Float.valueOf(lonStr);
								
								JSONObject dateObj = picObject.getJSONObject("DateTime");
								dateStr = dateObj.getString("val");
								isaddre = true;
							}
							
						}
						
						if (isaddre) {
							Feedback tempFeed = new Feedback();
							String id = curJsonObject.getString("id");
							String ip = curJsonObject.getString("ip");
							String time = curJsonObject.getString("create_time");
							tempFeed.setId(id);
							tempFeed.setIp(ip);
							tempFeed.setUploadTime(time);;
							
							String username = detailJsonObject.getString("key1");
							String phone = detailJsonObject.getString("key2");
							String text = detailJsonObject.getString("key4");
							
							tempFeed.setUsername(username);
							tempFeed.setPhone(phone);
							tempFeed.setText(text);
							tempFeed.setAddress(address);
							
							String picName = picPath.substring(picPath.lastIndexOf("/")+1);//picJsonObject.getString("name");
							tempFeed.setPicTitle(picName);
							tempFeed.setPicture(picPath);
							
							tempFeed.setLatitude(String.valueOf(lat));
							tempFeed.setLongitude(String.valueOf(lon));
							tempFeed.setPhotoTime(dateStr);
							
							String fileName = picPath.substring(picPath.lastIndexOf("/")+1);
							tempFeed.setThumb(THUMB_DIR + fileName);
							
							getDefaultCache().put(id, tempFeed);
							
							String outFilepath = thumbDir + fileName;
							excutor.execute(new GenerateThumbThread(
									picPath, GenerateThumbThread.URL_TYPE, outFilepath));
						}
						
					}
				}
			}
			
			excutor.shutdown();
			while (!excutor.isTerminated()) {
				// 等待缩略图线程结束
			}
			
			String coordsStr = new String();
			@SuppressWarnings("rawtypes")
			List caches = (List) getDefaultCache().values();
			List<Integer> keyList = new ArrayList<Integer>();
			for (int i=0; i<caches.size(); i++) {
				Feedback tempFeed = (Feedback) caches.get(i);
				keyList.add(i);
				coordsStr += tempFeed.getLongitude();
				if (i == caches.size()-1) {
					// 缓存结束
					coordsStr += ",";
					coordsStr += tempFeed.getLatitude();
					//System.out.println(coordsStr);
					List<String> coordsList = convertToBDCoord(coordsStr);
					for (int j=0; j<coordsList.size(); j+=2) {
						int index = keyList.get(j/2);
						Feedback temp = (Feedback) caches.get(index);
						temp.setLongitude(coordsList.get(j));
						temp.setLatitude(coordsList.get(j+1));
						getDefaultCache().put(temp.getId(), temp);
					}
				} else if ((i+1) % FeedbackAction.COORD_SIZE_PER_REQUEST == 0) {
					// 每次处理100个
					coordsStr += ",";
					coordsStr += tempFeed.getLatitude();
					
					List<String> coordsList = convertToBDCoord(coordsStr);
					for (int j=0; j<coordsList.size(); j+=2) {
						int index = keyList.get(j/2);
						Feedback temp = (Feedback) caches.get(index);
						temp.setLongitude(coordsList.get(j));
						temp.setLatitude(coordsList.get(j+1));
						getDefaultCache().put(temp.getId(), temp);
					}
					
					coordsStr = "";
					keyList.clear();
				} else {
					coordsStr += ",";
					coordsStr += tempFeed.getLatitude();
					coordsStr += ";";
				}
			}
			return true;
		}
		return false;
	}
	
	// 每次请求100个坐标,或者达到当前缓存大小时，发送坐标转换请求
	private List<String> convertToBDCoord(String coords) {
		List<String> baiduCoords = new ArrayList<String>();
		String httpString = FeedbackAction.GEOCODE_URL + "&coords=" + coords;
		// 1.根据初始url取出返回json
		String feedJson = HttpInvoker.readContentByGet(httpString);
		
		// 2.解析json串
		JSONObject jsonObject = JSONObject.parseObject(feedJson);
		JSONArray feedArray = jsonObject.getJSONArray("result");
		for (int i=0; i<feedArray.size(); i++) {
			JSONObject curFeed = feedArray.getJSONObject(i);
			String tempLon = curFeed.getString("x");
			String tempLat = curFeed.getString("y");
			baiduCoords.add(tempLon);
			baiduCoords.add(tempLat);
		}
		
		return baiduCoords;
	}
	
	public CacheMap(long timeout) {
		// TODO Auto-generated constructor stub
		this.cacheTimeout = timeout;
		new UpdateThread().start();
	}
	@Override 
	public Set<Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		Set<Entry<K, V>> entrySet = new HashSet<Map.Entry<K,V>>();
		Set<Entry<K, CacheEntry>> wrapEntrySet = map.entrySet();
		for (Entry<K, CacheEntry> entry: wrapEntrySet) {
			entrySet.add(entry.getValue());
		}
		return entrySet;
	}

	@Override
	public V get(Object key) {
		// TODO Auto-generated method stub
		CacheEntry entry = map.get(key);
		return entry==null? null: entry.value;
	}
	@Override
	public V put(K key, V value) {
		CacheEntry entry = new CacheEntry(key, value);
		synchronized (map) {
			map.put(key, entry);
		}
		return value;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		if (!fetchFlag) {
			return 0;
		}
		return map.size();
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		List<V> resList = new ArrayList<V>();
		Object[] keys = map.keySet().toArray();
		for (Object key: keys) {
			CacheEntry entry = map.get(key);
			resList.add(entry.getValue());
		}
		return resList;
	}
	
}
