/**   
* @Title: UserAdress.java
* @Package com.get.fruit.bean 
* @Description: TODO
* @author LiQinglin 
* @date 2015-9-17 ����11:30:23 
* @version V1.0   
*/
package com.get.fruit.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

/** 
 * @ClassName: UserAdress 
 * @Description: TODO
 * @author LiQinglin
 * @date 2015-9-17 ����11:30:23 
 *  
 */
public class UserAdress extends BmobObject {

	private User mine;
	private BmobGeoPoint location;
	//�������绰�����õ绰����ϸ��ַ������5�����������ʱ�
	private String name,phone,phone2,detail,region,postcode;
	
}
