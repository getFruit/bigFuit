package com.get.fruit.big.bean;
/**   
* @Title: Category.java
* @Package com.get.fruit.bean 
* @Description: TODO
* @author LiQinglin 
* @date 2015-8-13 ÏÂÎç9:48:38 
* @version V1.0   
*/


import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/** 
 * @ClassName: Category 
 * @Description: TODO
 * @author LiQinglin
 * @date 2015-8-13 ÏÂÎç9:48:38 
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
		ËáËáµÄ,ÌğÌğµÄ,Ğ¡ÇåĞÂ,ÖØ¿ÚÎ¶;
	}
	
	public enum Function{
		µ÷Î¸,ÃÀÈİ,½µÑªÌÇ,ÓªÑø,²¹Ñª,Àû·Î,ÀûÄò,¼õ·Ê,×³Ñô,½â¾Æ
	}
	
	public enum CategoryName{
		Î÷¹Ï,Æ»¹û,ÄûÃÊ,¸ÌéÙ,â¨ºïÌÒ,Ó£ÌÒ,ÆÏÌÑ,²İİ®,²¤ÂÜ,¹şÃÜ¹Ï,¸ÊÕá,³È×Ó,ÀóÖ¦,À¶İ®,ÁñÁ«,èÖ×Ó,ÑîÌÒ,»ğÁú¹û,Ê¯Áñ,Ïã½¶,Ä¾¹Ï,Àæ,Ê÷İ®,ĞÓ,Ã¢¹û,ÌÒ,É½Öñ
	}

}
