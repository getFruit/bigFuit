package com.get.fruit.activity;

import com.get.fruit.R;
import com.get.fruit.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PayResultActivity extends BaseActivity{

	private TextView payresultTextView;
	private ImageView payresultImageView;
	private Button ordercenter;
	private String result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_result);
		
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

		result=getIntent().getStringExtra("result");
		if ("success".equals(result)) {
			payresultImageView.setImageResource(R.drawable.result_success);
		}else {
			payresultImageView.setImageResource(R.drawable.result_faild);
		}
		payresultTextView.setText(result);
		
		ordercenter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				startAnimActivity(OrderListActivity.class);
				finish();
			}
		});
	}


	/** 
	* @Title: initView 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void initView() {
		initTopBarForOnlyTitle("Ö§¸¶½á¹û");
		payresultTextView=(TextView) findViewById(R.id.payresulttext);
		payresultImageView=(ImageView) findViewById(R.id.payresultimageView);
		ordercenter=(Button) findViewById(R.id.ordercenter);
	}


	
}
