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
			sb.append("�� ;");
			sum+=item.getCount()*item.getFruit().getPrice();
		}
		contentString=sb.toString();
		content.setText(contentString);
		price.setText(sum+"");	
		name.setText(me.getRealName());
		phone.setText(me.getMobilePhoneNumber());
		address.setText(me.getSchool());//��ʱʹ��School��������ַ
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
	


	private final  static String[] method_item={"�ͻ�����","��ȡ","��ȡ","���"};
	
	//�ͻ���ʽ����
	public void send_method(View view)
	{
		new AlertDialog.Builder(this)
	 	.setTitle("��ѡ��")
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
	 	.setNegativeButton("ȡ��", null)
	 	.show();
	}
	
	public void order_name(View view)
	{
		final EditText edit_text=new EditText(this);
		edit_text.setHint("�������������");
		edit_text.setMaxWidth(12);
		new AlertDialog.Builder(this)
		.setTitle("�������ͷ����")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setView(edit_text)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {                

			@Override  
			public void onClick(DialogInterface dialog, int which) {  
				// TODO Auto-generated method stub 
				name.setText(edit_text.getText().toString());
			}})  
			.setNegativeButton("ȡ��", null)
			.show();
	}
	public void order_phone(View view)
	{
		final EditText edit_text=new EditText(this);
		edit_text.setHint("���������");
		new AlertDialog.Builder(this)
		.setTitle("������绰")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setView(edit_text)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {                
			
			@Override  
			public void onClick(DialogInterface dialog, int which) {  
				// TODO Auto-generated method stub 
				if (!StringUtils.isPhone(edit_text.getText().toString())) {
					ShowToast("�¹��ش󣬲�������");
					return;
				}
				phone.setText(edit_text.getText().toString());
			}})  
			.setNegativeButton("ȡ��", null)
			.show();
	}
	public void order_address(View view)
	{
		final EditText edit_text=new EditText(this);
		edit_text.setHint("ѧУ-ѧԺ-����¥-���Һ�");
		new AlertDialog.Builder(this)
	 	.setTitle("�������ͻ���ַ")
	 	.setIcon(android.R.drawable.ic_dialog_info)
	 	.setView(edit_text)
	 	.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {                

			@Override  
	 	    public void onClick(DialogInterface dialog, int which) {  
	 	        // TODO Auto-generated method stub  
	 	        address.setText(edit_text.getText().toString());
	 	    }})  
	 	.setNegativeButton("ȡ��", null)
	 	.show();
	}
	public void order_words(View view)
	{
		final EditText edit_text=new EditText(this);
		new AlertDialog.Builder(this)
	 	.setTitle("�����붩������")
	 	.setIcon(android.R.drawable.ic_dialog_info)
	 	.setView(edit_text)
	 	.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {    
	 	    @Override  
	 	    public void onClick(DialogInterface dialog, int which) {  
	 	        // TODO Auto-generated method stub  
	 	    	messengerString=edit_text.getText().toString();
	 	        messenger.setText(messengerString);
	 	    }})  
	 	.setNegativeButton("ȡ��", null)
	 	.show();
	}
	
	
	/*
	//���ڲ��رնԻ���
	try { 
	Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing"); 
	field.setAccessible(true); 
	field.set(dialog, false);
	 
	} catch (Exception e) { 
	e.printStackTrace(); 
	}
	������������Ϳ���ʹdialog�޷��رգ�������Ҫ�رյĵط�����ӣ�
	//�رնԻ���
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
		progressDialog.setMessage("�����µ�");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
		saveOrder();
	}


	//��װorder���ݣ����浽�ƶ�
	public void saveOrder() {
		saveList=new ArrayList<BmobObject>();
		for(Order order:orders){
			order.setPay(false);
			order.setName(nameString);
			order.setPhone(phoneString);
			order.setAddress(addressString);
			order.setMessenger(messengerString);
			order.setSendway(SendWay.�ͻ�����);
			order.setState(State.�ȴ�֧��);
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
				ShowToast("�µ�ʧ�ܣ��Ժ�����");
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
			ShowToast("���ܲ��ܺú��ջ��ˣ��������ͷ����");
			return false;
		}
		phoneString=(String) phone.getText();
		if(!StringUtils.isPhone(phoneString)){
			ShowToast("��д�绰��Ĭ�ϴ�911���°�������ˮ��");
			return false;
		}
		addressString=(String) address.getText();
		if (StringUtils.isEmpty(addressString)) {
			ShowToast("�͵�����ȥ���ҿɲ��ܣ���ַ����д");
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
			tempOrder.setState(State.�ȴ�����);
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
				ShowToast("�µ�ʧ�ܣ��Ժ�����");
			}
		});
	}
	/** 
	 * @Title: updateFruit 
	 * @Description: ����������һ
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
				ShowToast("����ʧ��");
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
			bmobPay=new BmobPay(OrderEditActivity.this);
			bmobPay.pay(sum, contentString, messengerString, new PayListener() {

				// ��Ϊ�����ԭ��,֧�����δ֪(С�����¼�),���ڱ�������Ժ��ֶ���ѯ
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

				// ���۳ɹ����,���ض�����
				@Override
				public void orderId(String orderId) {
					// �˴�Ӧ�ñ��涩����,���籣������ݿ��,�Ա��Ժ��ѯ
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

			// ���۳ɹ����,���ض�����
			@Override
			public void orderId(String orderId) {
				// �˴�Ӧ�ñ��涩����,���籣������ݿ��,�Ա��Ժ��ѯ
				showDialog("��ȡ�����ɹ�!��ȴ���ת��֧��ҳ��~");
			}

			@Override
			public void fail(int code, String reason) {

				// ��codeΪ-2,��ζ���û��ж��˲���
				// codeΪ-3��ζ��û�а�װBmobPlugin���
				hideDialog();
				if (code == -3) {
					new AlertDialog.Builder(OrderEditActivity.this)
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
