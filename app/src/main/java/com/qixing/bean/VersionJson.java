package com.qixing.bean;

import java.io.Serializable;

/**
 * @params version 版本号
 * @params url 下载地址
 * */

public class VersionJson implements Serializable{
	private String result;
	private String message;
	private String sysname;//sysname（系统）
	private String versioncode;//versioncode（版本id）
	private String versionname;//versionname（版本号）
	private String downpath;//downpath（下载路径）

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	public String getVersioncode() {
		return versioncode;
	}

	public void setVersioncode(String versioncode) {
		this.versioncode = versioncode;
	}

	public String getVersionname() {
		return versionname;
	}

	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}

	public String getDownpath() {
		return downpath;
	}

	public void setDownpath(String downpath) {
		this.downpath = downpath;
	}
}
