package tk.eqpic.entity;

public class Feedback extends FeedbackFile{

	private String username;
	private String phone;
	private String ip;
	private String uploadTime;
	private String photoTime;
	
	public Feedback() {
		super();
	}

	public Feedback(String id, String username, String phone, String thumb, String picture,
			String picTitle, String text, String address, String ip, String uploadTime, 
			String photoTime, String longitude, String latitude) {
		super(id, thumb, picture, picTitle, text, address, longitude, latitude);
		this.username = username;
		this.phone = phone;
		this.address = address;
		this.ip = ip;
		this.uploadTime = uploadTime;
		this.photoTime = photoTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getPhotoTime() {
		return photoTime;
	}

	public void setPhotoTime(String photoTime) {
		this.photoTime = photoTime;
	}
}
