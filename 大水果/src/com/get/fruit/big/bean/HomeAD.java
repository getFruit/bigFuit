package com.get.fruit.big.bean;
/**   
* @Title: HomeAD.java
* @Package com.get.fruit.bean 
* @Description: TODO
* @author LiQinglin 
* @date 2015-8-16 下午5:10:09 
* @version V1.0   
*/


import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/** 
 * @ClassName: HomeAD 
 * @Description: 首页广告，轮播广告
 * @author LiQinglin
 * @date 2015-8-16 下午5:10:09 
 *  
 */
public class HomeAD extends BmobObject implements Serializable {
	
	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 7028325953020366322L;
	private String name;
	private float price;
	private BmobFile pic;
	private Boolean top;
	private Fruit fruit;


	public HomeAD() {
		super();
	}
	
	
	public Boolean getTop() {
		return top;
	}
	public void setTop(Boolean top) {
		this.top = top;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public BmobFile getPic() {
		return pic;
	}
	public void setPic(BmobFile pic) {
		this.pic = pic;
	}
	public Fruit getFruit() {
		return fruit;
	}
	public void setFruit(Fruit fruit) {
		this.fruit = fruit;
	}
	
	
}
