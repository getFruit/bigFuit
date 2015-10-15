package com.get.fruit.big.bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Fruit extends BmobObject {

	/**
	 * 
	 */

	private String number,name,describe,origin;//编号,描述,产地
	private Category category;//种类名称
	private Color color;//颜色
	private Season season;//季节
	private FruitShop shop;//所属商店
	private Double price;//价格
	private double count;//数量
	private Integer paynum;//付款人数（直接写入实体，减少查询）
	private Integer likesNumber;//收藏人数
	private BmobRelation likes;//收藏的用户
	private BmobFile picture;//主图
	private String[] pictures;//附图（可选）(url)
	private Boolean overdue;//是否过期
	
	
	

	

	
	public Fruit(FruitShop shop) {
		this.shop = shop;
	}



	
	/**
	 * 
	 */
	public Fruit() {
		// TODO Auto-generated constructor stub
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getNumber() {
		return number;
	}



	public void setNumber(String number) {
		this.number = number;
	}



	public Category getCategory() {
		return category;
	}



	public void setCategory(Category category) {
		this.category = category;
	}



	public Color getColor() {
		return color;
	}



	public void setColor(Color color) {
		this.color = color;
	}



	public Season getSeason() {
		return season;
	}



	public void setSeason(Season season) {
		this.season = season;
	}



	public String getOrigin() {
		return origin;
	}



	public void setOrigin(String origin) {
		this.origin = origin;
	}



	public String getDescribe() {
		return describe;
	}



	public void setDescribe(String describe) {
		this.describe = describe;
	}




	public FruitShop getShop() {
		return shop;
	}



	public void setShop(FruitShop shop) {
		this.shop = shop;
	}



	public Double getPrice() {
		return price;
	}



	public void setPrice(Double price) {
		this.price = price;
	}



	public double getCount() {
		return count;
	}



	public void setCount(double count) {
		this.count = count;
	}



	


	public Integer getPaynum() {
		return paynum;
	}




	public void setPaynum(Integer paynum) {
		this.paynum = paynum;
	}




	public BmobRelation getLikes() {
		return likes;
	}



	public void setLikes(BmobRelation likes) {
		this.likes = likes;
	}

	
	public BmobFile getPicture() {
		return picture;
	}



	public void setPicture(BmobFile picture) {
		this.picture = picture;
	}



	public String[] getPictures() {
		return pictures;
	}



	public void setPictures(String[] pictures) {
		this.pictures = pictures;
	}

	
	
	
	
	
	
	
	
	
	
	
	public Integer getLikesNumber() {
		return likesNumber;
	}




	public void setLikesNumber(Integer likesNumber) {
		this.likesNumber = likesNumber;
	}




	public Boolean getOverdue() {
		return overdue;
	}




	public void setOverdue(Boolean overdue) {
		this.overdue = overdue;
	}













	public enum Season{
		
		春天, 夏天, 秋天, 冬天;
		public String categoryBy(){
			return "季节";
		}
	}
	
	public enum Color{
		
		红色, 黄色, 蓝色, 绿色, 黑色, 白色, 紫色, 橙色;
		public String categoryBy(){
			return "颜色";
		}
	}
	
	public enum Origin{
		南方 ,北方 ,西部 ,进口;
		public String categoryBy(){
			return "产地";
		}
	}

}
