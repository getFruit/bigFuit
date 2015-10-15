package com.get.fruit.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.EditText;
import android.widget.TextView;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.platform.comapi.map.u;
import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.get.fruit.Config;
import com.get.fruit.R;
import com.get.fruit.bean.Order;
import com.get.fruit.bean.User;
import com.get.fruit.bean.Order.SendWay;
import com.get.fruit.bean.Order.State;
import com.get.fruit.bean.UserAdress;
import com.get.fruit.util.StringUtils;

public class OrderEditActivity extends ActivityBase implements OnClickListener {

	private View orderView,payView;
	private TextView content,price,sendway,messenger,address,name,phone;
	private List<Order> orders;
	
	private double sum=0;
	private String contentString;
	private String phoneString;
	private String addressString;
	private String nameString;
	private String messengerString="";
	private String sendWayString=method_item[0];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_edit);
		orders=(List<Order>) getIntent().getSerializableExtra("orders");
		initView();
		setView();
	}
	
	
	/** 
	* @Title: setView 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	
	private void setView() {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		for(Order item:orders){
			sb.append(item.getFruit().getName());
			sb.append("/");
			sb.append(item.getCount());
			sb.append("斤 ;");
			sum+=item.getCount()*item.getFruit().getPrice();
		}
		contentString=sb.toString();
		content.setText(contentString);
		price.setText(sum+"");	
		name.setText(me.getRealName());
		phone.setText(me.getMobilePhoneNumber());
		address.setText(me.getSchool());//暂时使用School属性做地址
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
		orderView=findViewById(R.id.order);
		payView=findViewById(R.id.pay);
		content=(TextView) findViewById(R.id.order_edit_content);
		price=(TextView) findViewById(R.id.order_edit_price);
		sendway=(TextView) findViewById(R.id.order_edit_method);
		messenger=(TextView) findViewById(R.id.order_edit_words);
		address=(TextView) findViewById(R.id.order_edit_address);
		name=(TextView) findViewById(R.id.order_edit_name);
		phone=(TextView) findViewById(R.id.order_edit_phone);
	}
	


	private final  static String[] method_item={"送货上门","自取","代取","快递"};
	
	//送货方式函数
	public void send_method(View view)
	{
		new AlertDialog.Builder(this)
	 	.setTitle("请选择")
	 	.setIcon(android.R.drawable.ic_dialog_info)                
	 	.setSingleChoiceItems(method_item, 0, 
	 	  new DialogInterface.OnClickListener() {
	 	                              
	 	     public void onClick(DialogInterface dialog, int which) {
	 	    	TextView method_view=(TextView) findViewById(R.id.order_edit_method);
	 	    	sendWayString=method_item[which];
	 	    	method_view.setText(sendWayString);
	 	    	dialog.cancel();
	 	     }
	 	  }
	 	)
	 	.setNegativeButton("取消", null)
	 	.show();
	}
	
	public void order_name(View view)
	{
		final EditText edit_text=new EditText(this);
		edit_text.setHint("可以是你的名字");
		edit_text.setMaxWidth(12);
		new AlertDialog.Builder(this)
		.setTitle("请输入接头暗号")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setView(edit_text)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {                

			@Override  
			public void onClick(DialogInterface dialog, int which) {  
				// TODO Auto-generated method stub 
				name.setText(edit_text.getText().toString());
			}})  
			.setNegativeButton("取消", null)
			.show();
	}
	public void order_phone(View view)
	{
		final EditText edit_text=new EditText(this);
		edit_text.setHint("不能输错了");
		new AlertDialog.Builder(this)
		.setTitle("请输入电话")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setView(edit_text)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {                
			
			@Override  
			public void onClick(DialogInterface dialog, int which) {  
				// TODO Auto-generated method stub 
				if (!StringUtils.isPhone(edit_text.getText().toString())) {
					ShowToast("事关重大，不能乱来");
					return;
				}
				phone.setText(edit_text.getText().toString());
			}})  
			.setNegativeButton("取消", null)
			.show();
	}
	public void order_address(View view)
	{
		final EditText edit_text=new EditText(this);
		edit_text.setHint("学校-学院-宿舍楼-寝室号");
		new AlertDialog.Builder(this)
	 	.setTitle("请输入送货地址")
	 	.setIcon(android.R.drawable.ic_dialog_info)
	 	.setView(edit_text)
	 	.setPositiveButton("确定", new DialogInterface.OnClickListener() {                

			@Override  
	 	    public void onClick(DialogInterface dialog, int which) {  
	 	        // TODO Auto-generated method stub  
	 	        address.setText(edit_text.getText().toString());
	 	    }})  
	 	.setNegativeButton("取消", null)
	 	.show();
	}
	public void order_words(View view)
	{
		final EditText edit_text=new EditText(this);
		new AlertDialog.Builder(this)
	 	.setTitle("请输入订单留言")
	 	.setIcon(android.R.drawable.ic_dialog_info)
	 	.setView(edit_text)
	 	.setPositiveButton("确定", new DialogInterface.OnClickListener() {    
	 	    @Override  
	 	    public void onClick(DialogInterface dialog, int which) {  
	 	        // TODO Auto-generated method stub  
	 	    	messengerString=edit_text.getText().toString();
	 	        messenger.setText(messengerString);
	 	    }})  
	 	.setNegativeButton("取消", null)
	 	.show();
	}
	
	
	/*
	//用于不关闭对话框
	try { 
	Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing"); 
	field.setAccessible(true); 
	field.set(dialog, false);
	 
	} catch (Exception e) { 
	e.printStackTrace(); 
	}
	添加上述代码后就可以使dialog无法关闭，在你需要关闭的地方，添加：
	//关闭对话框
	try {
	Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
	field.setAccessible(true);
	field.set(dialog, true);
	} catch (Exception e) {
	e.printStackTrace();
	}
	*/
	
	ProgressDialog progressDialog;
	public void submit_order(View view)
	{
		if (!inputValidate()) {
			return;
		}
		progressDialog=new ProgressDialog(this);
		progressDialog.setMessage("正在下单");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
		saveOrder();
	}


	//封装order数据，保存到云端
	public void saveOrder() {
		saveList=new ArrayList<BmobObject>();
		for(Order order:orders){
			order.setPay(false);
			order.setName(nameString);
			order.setPhone(phoneString);
			order.setAddress(addressString);
			order.setMessenger(messengerString);
			order.setSendway(SendWay.送货上门);
			order.setState(State.等待支付);
			saveList.add(order);
		}
		new Order().insertBatch(this, saveList, new SaveListener() {
			
			@Override
			public void onSuccess() {
				saveList.clear();
				progressDialog.dismiss();
				orderView.setVisibility(View.GONE);
				payView.setVisibility(View.VISIBLE);
				initView2();
				savaUserInfo();
			}
			
			private void savaUserInfo() {
				// TODO Auto-generated method stub
				User user =new User();
				user.setRealName(nameString);
				user.setSchool(addressString);
				user.update(OrderEditActivity.this, me.getObjectId(), null);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				progressDialog.dismiss();
				saveList.clear();
				ShowToast("下单失败，稍后再试");
			}
		});
	}
	
	
	/** 
	* @Title: inputValidate 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private boolean inputValidate() {
		// TODO Auto-generated method stub
		nameString=(String) name.getText();
		if (StringUtils.isEmpty(nameString)) {
			ShowToast("还能不能好好收货了，请输入接头暗号");
			return false;
		}
		phoneString=(String) phone.getText();
		if(!StringUtils.isPhone(phoneString)){
			ShowToast("不写电话的默认打911，奥巴马来查水表咯");
			return false;
		}
		addressString=(String) address.getText();
		if (StringUtils.isEmpty(addressString)) {
			ShowToast("送到火星去了我可不管，地址都不写");
			return false;
		}
		return true;
	}



	private TextView pay1,pay2,pay3,payprice;
	private Button cancel;
	ProgressDialog dialog;
	BmobPay bmobPay;
	/** 
	* @Title: initView2 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void initView2() {
		BmobPay.init(this,Config.applicationId);
		
		pay1=(TextView) findViewById(R.id.pay1);
		pay2=(TextView) findViewById(R.id.pay2);
		pay3=(TextView) findViewById(R.id.pay3);
		cancel=(Button) findViewById(R.id.cancel);
		payprice=(TextView) findViewById(R.id.price);
		pay1.setOnClickListener(this);
		pay2.setOnClickListener(this);
		pay3.setOnClickListener(this);
		cancel.setOnClickListener(this);
		payprice.setText(sum+"");
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
			updateOrders();
			break;
		case R.id.cancel:
			deleteOrders();
			break;

		default:
			break;
		}
	}
	
	
	/** 
	* @Title: updateOrders 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private ArrayList<BmobObject> saveList;
	private void updateOrders() {
		saveList=new ArrayList<BmobObject>();
		for(Order order:orders){
			Order tempOrder =new Order();
			tempOrder.setState(State.等待发货);
			saveList.add(tempOrder);
		}
		new Order().updateBatch(this, saveList, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				saveList.clear(); 
				updateFruit();
				startAnimActivityWithData(PayResultActivity.class, "result", "success");
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				progressDialog.dismiss();
				saveList.clear();
				ShowToast("下单失败，稍后再试");
			}
		});
	}
	/** 
	 * @Title: updateFruit 
	 * @Description: 购买人数加一
	 * @param 
	 * @return void
	 * @throws 
	 */
	private void updateFruit() {
		for(Order order:orders){
			order.getFruit().increment("paynum");
		}
	}


	/** 
	* @Title: deleteOrder 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void deleteOrders() {
		// TODO Auto-generated method stub
		new BmobObject().deleteBatch(OrderEditActivity.this, saveList, new DeleteListener() {
			
			@Override
			public void onSuccess() {
				payView.setVisibility(View.GONE);
				orderView.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("操作失败");
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
	

	// 调用支付宝支付
	private void payByAli() {
			showDialog("正在获取订单...");
			bmobPay=new BmobPay(OrderEditActivity.this);
			bmobPay.pay(sum, contentString, messengerString, new PayListener() {

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
					ShowLog("succeed: ");
					startAnimActivityWithData(PayResultActivity.class, "result", "success");
				}

				// 无论成功与否,返回订单号
				@Override
				public void orderId(String orderId) {
					// 此处应该保存订单号,比如保存进数据库等,以便以后查询
					showDialog("获取订单成功!请等待跳转到支付页面~"+orderId);
					ShowLog("获取订单成功!请等待跳转到支付页面~"+orderId);
				}

				// 支付失败,原因可能是用户中断支付操作,也可能是网络原因
				@Override
				public void fail(int code, String reason) {
					hideDialog();
					ShowLog("fail "+code+reason);
					startAnimActivityWithData(PayResultActivity.class, "result", "fail:"+code+":"+reason);
				}
			});
		}

	// 调用微信支付
	private void payByWeiXin() {
		bmobPay.payByWX(sum, contentString, messengerString, new PayListener() {

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
				showDialog("获取订单成功!请等待跳转到支付页面~");
			}

			@Override
			public void fail(int code, String reason) {

				// 当code为-2,意味着用户中断了操作
				// code为-3意味着没有安装BmobPlugin插件
				hideDialog();
				if (code == -3) {
					new AlertDialog.Builder(OrderEditActivity.this)
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
