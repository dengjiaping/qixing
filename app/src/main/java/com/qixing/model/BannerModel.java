package com.qixing.model;

import java.util.List;

public class BannerModel {

	
	public List<String> imgs;
	public List<String> images;
	public List<String> banner_url;
	public  List<String> url;
	public String number;


	public List<Integer> banner_id;

	public List<Integer> getBanner_id() {
		return banner_id;
	}

	public void setBanner_id(List<Integer> banner_id) {
		this.banner_id = banner_id;
	}

	public List<String> getImgs() {
		return imgs;
	}


	public List<String> getBanner_url() {
		return banner_url;
	}

	public void setBanner_url(List<String> banner_url) {
		this.banner_url = banner_url;
	}


	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
