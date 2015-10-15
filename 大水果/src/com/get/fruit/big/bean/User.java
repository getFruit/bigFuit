package com.get.fruit.big.bean;


import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;

import com.bmob.BmobProFile;

public class User extends BmobChatUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8520852L;

	/**
	 * //性别-true-男
	 */
	private boolean sex;
	
	private BmobDate birthday;
	
	private String school;//针对学生
	
	private String qqNumber;
	
	private String realName;
	
	private BmobGeoPoint location;

	
	public BmobGeoPoint getLocation() {
		return location;
	}

	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public BmobDate getBirthday() {
		return birthday;
	}

	public void setBirthday(BmobDate birthday) {
		this.birthday = birthday;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getQqNumber() {
		return qqNumber;
	}

	public void setQqNumber(String qqNumber) {
		this.qqNumber = qqNumber;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
	
	
	
	
}
