package com.get.fruit.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.get.fruit.Config;
import com.get.fruit.R;
import com.get.fruit.bean.Order;
import com.get.fruit.bean.Order.State;

public class PayActivity extends BaseActivity implements OnClickListener {

	private TextView pay1,pay2,pay3,price;
	private Button cancel;
	ProgressDialog dialog;
	BmobPay bmobPay;
	Order order;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		BmobPay.init(this,Config.applicationId);
		initView();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		order=(Order) getIntent().getSerializableExtra("order");
		if (null==price) {
			price=(TextView) findViewById(R.id.price);
		}
		if (null!=order) {
			price.setText(order.getSum()+"");
		}
	}
	
	/** 
	* @Title: initView 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void initView() {
		// TODO Auto-generated method stub
		pay1=(TextView) findViewById(R.id.pay1);
		pay2=(TextView) findViewById(R.id.pay2);
		pay3=(TextView) findViewById(R.id.pay3);
		cancel=(Button) findViewById(R.id.cancel);
		pay1.setOnClickListener(this);
		pay2.setOnClickListener(this);
		pay3.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		/*if (null==order) {
			return;
		}*/
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pay1:
			payByAli();
			break;
		case R.id.pay2:
			payByWeiXin();
			break;
		case R.id.cancel:
			deleteOrder();
			break;

		default:
			break;
		}
	}
	
	


	/** 
	* @Title: deleteOrder 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void deleteOrder() {
		// TODO Auto-generated method stub
		Order temOrder =new Order();
		temOrder.setObjectId(order.getObjectId());
		temOrder.delete(PayActivity.this, new DeleteListener() {
			
			@Override
			public void onSuccess() {
				ShowToast("��ɾ������ˢ��");
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast("����ʧ��:"+arg1);
			}
		});
	}
	
	public  void showDialog(String message) {
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCancelable(false);
		}
		dialog.setMessage(message);
		dialog.show();
	}

	public void hideDialog() {
		if (dialog != null && dialog.isShowing())
			try {
				dialog.dismiss();
			} catch (Exception e) {
			}
	}
	
	// ����֧����֧��
	private void payByAli() {
			showDialog("���ڻ�ȡ����...");
			bmobPay=new BmobPay(PayActivity.this);
			bmobPay.pay(order.getSum(), order.getFruit().getName(), order.getFruit().getDescribe(), new PayListener() {

				// ��Ϊ�����ԭ��,֧�����δ֪(С�����¼�),���ڱ�������Ժ��ֶ���ѯ
				@Override
				public void unknow() {
					hideDialog();
					ShowLog("unlnow: ");
					startAnimActivityWithData(PayResultActivity.class, "result", "unknow");
				}

				@Override
				public void succeed() {
					hideDialog();
					Order temOrder=new Order();
					temOrder.setOrderid(order.getOrderid());
					temOrder.setPay(true);
					new BmobObject().update(PayActivity.this, order.getObjectId(),null);
					order.getFruit().increment("paynum");
					startAnimActivityWithData(PayResultActivity.class, "result", "success");
				}

				// ���۳ɹ����,���ض�����
				@Override
				public void orderId(String orderId) {
					// �˴�Ӧ�ñ��涩����,���籣������ݿ��,�Ա��Ժ��ѯ
					order.setOrderid(orderId);
					showDialog("��ȡ�����ɹ�!��ȴ���ת��֧��ҳ��~"+orderId);
					ShowLog("��ȡ�����ɹ�!��ȴ���ת��֧��ҳ��~"+orderId);
				}

				// ֧��ʧ��,ԭ��������û��ж�֧������,Ҳ����������ԭ��
				@Override
				public void fail(int code, String reason) {
					hideDialog();
					ShowLog("fail "+code+reason);
					startAnimActivityWithData(PayResultActivity.class, "result", "fail:"+code+":"+reason);
				}
			});
		}

	// ����΢��֧��
	private void payByWeiXin() {
		showDialog("���ڻ�ȡ����...");
		bmobPay.payByWX(order.getSum(), order.getFruit().getName(), order.getFruit().getDescribe(), new PayListener() {

			@Override
			public void unknow() {
				hideDialog();
				startAnimActivityWithData(PayResultActivity.class, "result", "unknow");
			}

			@Override
			public void succeed() {
				hideDialog();
				Order temOrder=new Order();
				temOrder.setPay(true);
				temOrder.setOrderid(order.getOrderid());
				new BmobObject().update(PayActivity.this, order.getObjectId(),null);
				order.getFruit().increment("paynum");
				startAnimActivityWithData(PayResultActivity.class, "result", "success");
			}

			// ���۳ɹ����,���ض�����
			@Override
			public void orderId(String orderId) {
				// �˴�Ӧ�ñ��涩����,���籣������ݿ��,�Ա��Ժ��ѯ
				order.setOrderid(orderId);
				showDialog("��ȡ�����ɹ�!��ȴ���ת��֧��ҳ��~");
			}

			@Override
			public void fail(int code, String reason) {

				// ��codeΪ-2,��ζ���û��ж��˲���
				// codeΪ-3��ζ��û�а�װBmobPlugin���
				hideDialog();
				if (code == -3) {
					new AlertDialog.Builder(PayActivity.this)
							.setMessage(
									"��⵽����δ��װ֧�����,�޷�����΢��֧��,��ѡ��װ���(����������),������֧����֧��?")
							.setPositiveButton("��װ",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											installBmobPayPlugin("BmobPayPlugin.apk");
										}
									})
							.setNegativeButton("֧����֧��",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											payByAli();
										}
									}).create().show();
				} else if (code == -2) {
					ShowToast("��ȡ����֧��");
				}
				startAnimActivityWithData(PayResultActivity.class, "result", "fail:"+code+":"+reason);
			}
		});
	}

	//��װ΢��֧�����
	private void installBmobPayPlugin(String fileName) {
		try {
			InputStream is = getAssets().open(fileName);
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + fileName);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close(); 

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + file),
					"application/vnd.android.package-archive");
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
