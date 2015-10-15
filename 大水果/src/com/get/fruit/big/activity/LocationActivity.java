package com.get.fruit.big.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.get.fruit.big.R;
import com.get.fruit.big.view.citypicker.CityPicker;
import com.get.fruit.big.view.citypicker.CityPicker.OnSelectingListener;

public class LocationActivity extends BaseActivity {
	private CityPicker mCityPicker;
	private TextView selectedAddress;
	private String fullAddress="";
	private String address="";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        selectedAddress=(TextView) findViewById(R.id.selected_address);
        intent= this.getIntent();
        //城市选择
        mCityPicker=(CityPicker) findViewById(R.id.citypicker);
        mCityPicker.setOnSelectingListener(new OnSelectingListener() {
			
			@Override
			public void selected(boolean selected) {
				// TODO Auto-generated method stub
				selectedAddress.setText(fullAddress=mCityPicker.getCity_string());
			}
		});
        
    }
    
    public void onClick() {
		// TODO Auto-generated method stub
		if (fullAddress!="") {
			address=fullAddress.split("-")[2];
		}
		if(address.equals("市辖区")||address.equals("县")||address.equals(fullAddress.split("-")[1])){
			ShowToast("请选择准确地址");
			return;
		}
		if(intent!=null){
			ShowToast(address);
			intent.putExtra("fullAddress",fullAddress);
			intent.putExtra("address",address);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
//热门城市事件 没有返回
    public void hot_city(View v){
    	switch (v.getId()) {
		case R.id.SH:
			mCityPicker.getProvincePicker().setDefault(21);
			mCityPicker.getOnProvinceSelectListener().endSelect(21, "上海市");
			mCityPicker.getOnCitySelectListener().endSelect(0, "市辖区");
			mCityPicker.getCityPicker().setDefault(0);
			break;
		case R.id.GZ:
			mCityPicker.getProvincePicker().setDefault(5);
			mCityPicker.getOnProvinceSelectListener().endSelect(5, "广东省");
			mCityPicker.getCityPicker().setDefault(0);
			break;
		case R.id.CD:
			mCityPicker.getProvincePicker().setDefault(25);
			mCityPicker.getOnProvinceSelectListener().endSelect(25, "四川省");
			mCityPicker.getOnCitySelectListener().endSelect(0, "成都市");
			mCityPicker.getCityPicker().setDefault(0);
			break;
		case R.id.SZ:
			mCityPicker.getProvincePicker().setDefault(5);
			mCityPicker.getOnProvinceSelectListener().endSelect(5, "广东省");
			mCityPicker.getCityPicker().setDefault(2);
			mCityPicker.getOnCitySelectListener().endSelect(2, "深圳市");
			break;

		default:
			break;
		}
    }
    
    }
