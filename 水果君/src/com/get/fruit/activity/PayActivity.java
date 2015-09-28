package com.get.fruit.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.get.fruit.Config;
import com.get.fruit.R;
import com.get.fruit.bean.Order;

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
		order=(Order) getIntent().getSerializableExtra("order");
		BmobPay.init(this,Config.applicationId);
		initView();
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
		case R.id.pay3:
			
			break;
		case R.id.cancel:
			
			break;

		default:
			break;
		}
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
	
	
	
	// 调用支付宝支付
	private void payByAli() {
			showDialog("正在获取订单...");
			bmobPay=new BmobPay(PayActivity.this);
			bmobPay.pay(0.3, "测试", "我的毛爷爷啊", new PayListener() {

				// 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
				@Override
				public void unknow() {
					hideDialog();
					ShowLog("unlnow: ");
					//startAnimActivityWithData(PayResultActivity.class, "result", "unknow");
				}

				@Override
				public void succeed() {
					hideDialog();
					startAnimActivityWithData(PayResultActivity.class, "result", "success");
				}

				// 无论成功与否,返回订单号
				@Override
				public void orderId(String orderId) {
					// 此处应该保存订单号,比如保存进数据库等,以便以后查询
					order.setObjectId(orderId);
					showDialog("获取订单成功!请等待跳转到支付页面~"+orderId);
					ShowLog("获取订单成功!请等待跳转到支付页面~"+orderId);
				}

				// 支付失败,原因可能是用户中断支付操作,也可能是网络原因
				@Override
				public void fail(int code, String reason) {
					hideDialog();
					ShowLog("fail "+code+reason);
					//startAnimActivityWithData(PayResultActivity.class, "result", "fail:"+code+":"+reason);
				}
			});
		}

	// 调用微信支付
	private void payByWeiXin() {
		showDialog("正在获取订单...");
		bmobPay.payByWX(order.getSum(), order.getFruit().getName(), order.getFruit().getDescribe(), new PayListener() {

			@Override
			public void unknow() {
				hideDialog();
				startAnimActivityWithData(PayResultActivity.class, "result", "unknow");
			}

			@Override
			public void succeed() {
				hideDialog();
				startAnimActivityWithData(PayResultActivity.class, "result", "success");
			}

			// 无论成功与否,返回订单号
			@Override
			public void orderId(String orderId) {
				// 此处应该保存订单号,比如保存进数据库等,以便以后查询
				order.setObjectId(orderId);
				showDialog("获取订单成功!请等待跳转到支付页面~");
			}

			@Override
			public void fail(int code, String reason) {

				// 当code为-2,意味着用户中断了操作
				// code为-3意味着没有安装BmobPlugin插件
				hideDialog();
				if (code == -3) {
					new AlertDialog.Builder(PayActivity.this)
							.setMessage(
									"监测到你尚未安装支付插件,无法进行微信支付,请选择安装插件(无流量消耗),还是用支付宝支付?")
							.setPositiveButton("安装",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											installBmobPayPlugin("BmobPayPlugin.apk");
										}
									})
							.setNegativeButton("支付宝支付",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											payByAli();
										}
									}).create().show();
				} else if (code == -2) {
					ShowToast("您取消了支付");
				}
				startAnimActivityWithData(PayResultActivity.class, "result", "fail:"+code+":"+reason);
			}
		});
	}

	//安装微信支付插件
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
