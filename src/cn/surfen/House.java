package cn.surfen;

public class House {
	private String information;// 成交信息
	private String imageUrl;// 图片信息
	private String Community;// 小区信息
	private String date;// 日期信息
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getCommunity() {
		return Community;
	}
	public void setCommunity(String community) {
		Community = community;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public House(String information, String imageUrl, String community, String date) {
		super();
		this.information = information;
		this.imageUrl = imageUrl;
		Community = community;
		this.date = date;
	}
	@Override
	public String toString() {
		return "小区信息" + Community + ", 成交价格" + information + "万, 图片地址" + imageUrl + ", 成交日期"
				+ date + "";
	}

	
}
