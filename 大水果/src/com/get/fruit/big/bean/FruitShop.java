package com.get.fruit.big.bean;
/**   
* @Title: Fruiterer.java
* @Package com.get.fruit.bean 
* @Description: TODO
* @author LiQinglin 
* @date 2015-8-1 上午4:19:17 
* @version V1.0   
*/


import java.io.Serializable;

import cn.bmob.im.util.BmobUtils;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

/** 
 * @ClassName: Fruiterer 
 * @Description: TODO
 * @author LiQinglin
 * @date 2015-8-1 上午4:19:17 
 *  
 */
public class FruitShop extends BmobObject implements Serializable{
	
	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 7028325953020366321L;
	
	private String name,address;//店名，地址（省-市-县）
	private User owner;//老板
	private Integer rank=1 ; //等级
	private double  sale=0.0;  //销量
	private BmobGeoPoint location;//具体坐标
	
	
	
	public FruitShop(String name, String address, User owner) {
		super();
		this.name = name;
		this.address = address;
		this.owner = owner;
	}
	
	
	public FruitShop(String name, User owner, String address, Integer rank,
			double sale) {
		super();
		this.name = name;
		this.owner = owner;
		this.address = address;
		this.rank = rank;
		this.sale = sale;
	}


	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getSale() {
		return sale;
	}
	public void setSale(double sale) {
		this.sale = sale;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public BmobGeoPoint getLocation() {
		return location;
	}
	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
