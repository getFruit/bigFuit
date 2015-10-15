package com.get.fruit;

import android.annotation.SuppressLint;
import android.os.Environment;


/** 
  * @ClassName: BmobConstants
  * @Description: TODO
  * @author smile
  * @date 2014-6-19 下午2:48:33
  */
@SuppressLint("SdCardPath")
public class BmobConstants {

	public static String MyAvatarDir = "/sdcard/水果君/me/";
	public static String MyFruitDir = "/sdcard/水果君/fruit/";
	public static String MyTempDir = "/sdcard/水果君/ad/";
	/**
	 * 拍照回调
	 */
	
	public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;//拍照
	public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//本地图片
	public static final int REQUESTCODE_PICTURE_CROP =0x000003;//系统裁剪头像
	
	public static final int REQUESTCODE_TAKE_LOCATION = 0x000004;//位置选择
	
	
	public static final int REQUESTCODE_FROM_ADDFRUIT_FORADDRESS = 0x000006;//商品上传位置选择调用
	public static final int REQUESTCODE_FROM_ADDFRUIT_FORCATEGORY = 0x000007;//商品上传分类调用
	public static final int REQUESTCODE_FROM_MAINACTIVITY_FORADDRESS = 0x000008;//
	public static final int REQUESTCODE_FROM_FRAGMENT_FORADDRESS = 0x000009;//
	
	public static final String EXTRA_STRING = "extra_string";
	
	
	public static final String ACTION_REGISTER_SUCCESS_FINISH ="register.success.finish";//注册成功之后登陆页面退出
}
