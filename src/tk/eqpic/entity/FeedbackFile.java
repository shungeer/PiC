package tk.eqpic.entity;

public class FeedbackFile {

	protected String id;
	protected String thumb;
	protected String picture;
	protected String picTitle;
	protected String text;
	protected String address;
	protected String longitude;
	protected String latitude;
	
	public FeedbackFile() {
		
	}

	public FeedbackFile(String id, String thumb, String picture, String picTitle,
			String text, String address, String longitude, String latitude) {
		this.thumb = thumb;
		this.id = id;
		this.picture = picture;
		this.picTitle = picTitle;
		this.text = text;
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getPicTitle() {
		return picTitle;
	}

	public void setPicTitle(String picTitle) {
		this.picTitle = picTitle;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
