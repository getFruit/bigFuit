package com.get.fruit.big.bean;
/**   
* @Title: Category.java
* @Package com.get.fruit.bean 
* @Description: TODO
* @author LiQinglin 
* @date 2015-8-13 ����9:48:38 
* @version V1.0   
*/


import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/** 
 * @ClassName: Category 
 * @Description: TODO
 * @author LiQinglin
 * @date 2015-8-13 ����9:48:38 
 *  
 */

public class Category  extends BmobObject implements Serializable{
	
	private static final long serialVersionUID = 85208520L;
	
	private String categoryName;
	private String[] functions;
	private Taste taste;
	
	
	
	
	public Taste getTaste() {
		return taste;
	}
	public void setTaste(Taste taste) {
		this.taste = taste;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String[] getFunctions() {
		return functions;
	}
	public void setFunctions(String[] functions) {
		this.functions = functions;
	}
	public Category(String categoryName, String[] functions,Taste taste) {
		super();
		this.categoryName = categoryName;
		this.functions = functions;
		this.taste=taste;
	}
	
	public Category() {
		super();
	}
	
	
	
	public Category(String categoryName) {
		super();
		this.categoryName = categoryName;
	}



	public enum Taste{
		�����,�����,С����,�ؿ�ζ;
	}
	
	public enum Function{
		��θ,����,��Ѫ��,Ӫ��,��Ѫ,����,����,����,׳��,���
	}
	
	public enum CategoryName{
		����,ƻ��,����,����,⨺���,ӣ��,����,��ݮ,����,���ܹ�,����,����,��֦,��ݮ,����,����,����,������,ʯ��,�㽶,ľ��,��,��ݮ,��,â��,��,ɽ��
	}

}
