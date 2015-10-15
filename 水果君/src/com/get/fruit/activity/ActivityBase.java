package com.get.fruit.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import cn.bmob.im.BmobUserManager;


/**
 * ����½ע��ͻ�ӭҳ����̳еĻ���-���ڼ���Ƿ��������豸��¼��ͬһ�˺�
 * 
 * @ClassName: ActivityBase
 * @Description: TODO
 * @author smile
 * @date 2014-6-13 ����5:18:24
 */
public abstract class ActivityBase extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �Զ���½״̬�¼���Ƿ��������豸��½
		checkLogin();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ����״̬�µļ��
		checkLogin();
	}

	public void checkLogin() {
		BmobUserManager userManager = BmobUserManager.getInstance(this);
		if (userManager.getCurrentUser() == null) {
			ShowToast("�����˺����������豸�ϵ�¼!");
			startAnimActivity(LoginActivity.class);
			finish();
		}
	}
	
}
