package com.get.fruit;

import android.annotation.SuppressLint;
import android.os.Environment;


/** 
  * @ClassName: BmobConstants
  * @Description: TODO
  * @author smile
  * @date 2014-6-19 ����2:48:33
  */
@SuppressLint("SdCardPath")
public class BmobConstants {

	public static String MyAvatarDir = "/sdcard/ˮ����/me/";
	public static String MyFruitDir = "/sdcard/ˮ����/fruit/";
	public static String MyTempDir = "/sdcard/ˮ����/ad/";
	/**
	 * ���ջص�
	 */
	
	public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;//����
	public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//����ͼƬ
	public static final int REQUESTCODE_PICTURE_CROP =0x000003;//ϵͳ�ü�ͷ��
	
	public static final int REQUESTCODE_TAKE_LOCATION = 0x000004;//λ��ѡ��
	
	
	public static final int REQUESTCODE_FROM_ADDFRUIT_FORADDRESS = 0x000006;//��Ʒ�ϴ�λ��ѡ�����
	public static final int REQUESTCODE_FROM_ADDFRUIT_FORCATEGORY = 0x000007;//��Ʒ�ϴ��������
	public static final int REQUESTCODE_FROM_MAINACTIVITY_FORADDRESS = 0x000008;//
	public static final int REQUESTCODE_FROM_FRAGMENT_FORADDRESS = 0x000009;//
	
	public static final String EXTRA_STRING = "extra_string";
	
	
	public static final String ACTION_REGISTER_SUCCESS_FINISH ="register.success.finish";//ע��ɹ�֮���½ҳ���˳�
}
