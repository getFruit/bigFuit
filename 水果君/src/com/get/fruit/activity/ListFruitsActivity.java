package com.get.fruit.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.DownloadManager.Query;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.get.fruit.App;
import com.get.fruit.R;
import com.get.fruit.adapter.util.BaseAdapterHelper;
import com.get.fruit.adapter.util.QuickAdapter;
import com.get.fruit.bean.CartItem;
import com.get.fruit.bean.Fruit;
import com.get.fruit.util.CitycodeUtil;
import com.get.fruit.util.CollectionUtils;
import com.get.fruit.util.FileUtil;
import com.get.fruit.util.PixelUtil;
import com.get.fruit.util.StringUtils;
import com.get.fruit.view.HeaderLayout.onRightImageButtonClickListener;
import com.get.fruit.view.citypicker.Cityinfo;
import com.get.fruit.view.citypicker.JSONParser;
import com.get.fruit.view.listview.XListView;
import com.get.fruit.view.listview.XListView.IXListViewListener;

public class ListFruitsActivity extends BaseActivity {
	
	static CachePolicy policy=CachePolicy.CACHE_ELSE_NETWORK;//缓存策略
	static int limit=8;//每页条数
	static int currentPage=0;//当前页
	static String provinceString="";//产地省份
	static String cityString="";//产地城市
	static String searchBy;//搜索方式
	static String searchValue;//搜索条件
	static String keyWord;//搜索关键字
	
	private SearchView keyWordSearch;
	private XListView mListView;
	private QuickAdapter<Fruit> mqQuickAdapter;
	private TextView[] tabs;
	private Intent intent;
	private PopupWindow popupwindow;
	private List<CartItem> myyCartItems=new ArrayList<CartItem>();
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		intent=getIntent();
		ShowLog(intent.toString());
		searchBy=intent.getStringExtra("searchBy");
		searchValue=intent.getStringExtra("searchValue");
		keyWord=intent.getStringExtra("keyWord");
		ShowLog("searchBy："+searchBy+"   searchValue： "+searchValue+"   keyWord： "+keyWord);
		mHeaderLayout.setLeftText(searchValue);
		super.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listfruit);
		
		initView();
	}

	/** 
	* @Title: initView 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	@SuppressLint("NewApi")
	private void initView() {
		// TODO Auto-generated method stub
		initTopBarForMiddleView(null, R.drawable.base_action_bar_back_bg_selector, searchValue, new OnLeftClickListenerFinishMe(), R.drawable.base_action_bar_person_selector, null, new onRightImageButtonClickListener() {
		
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				Intent intent =new Intent(ListFruitsActivity.this, MainActivity.class);
				intent.putExtra("to", 2);
				ShowLog("intent.putExtra('to', 2)....");
				startActivity(intent);
			}
		},1);
	
		keyWordSearch=mHeaderLayout.getmEditText();
		keyWordSearch.setSubmitButtonEnabled(true);
		keyWordSearch.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String arg0) {
				// TODO Auto-generated method stub
				keyWord=arg0;
				query();
				//隐藏输入法
				if(keyWordSearch!=null){
					InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					if (inputMethodManager!=null) {
						inputMethodManager.hideSoftInputFromWindow(keyWordSearch.getWindowToken(), 0);
					}
					keyWordSearch.clearFocus();
				}
				
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				keyWord=arg0;
				return false;
			}
		});
		/*keyWordSearch.setSuggestionsAdapter(new CursorAdapter(ListFruitsActivity.this, new Cursor, true) {
			
			@Override
			public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void bindView(View arg0, Context arg1, Cursor arg2) {
				// TODO Auto-generated method stub
				
			}
		});*/
		
		tabs=new TextView[]{
			(TextView) findViewById(R.id.textView0),(TextView) findViewById(R.id.textView1),(TextView) findViewById(R.id.textView2),(TextView) findViewById(R.id.textView3)	};
		
		initListView();
		
		tabsClick(tabs[0]);
	}

	/** 
	* @Title: initListView 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	public void initListView() {
		mListView=(XListView) findViewById(R.id.xListView1);
		mqQuickAdapter=new QuickAdapter<Fruit>(ListFruitsActivity.this,R.layout.item_listfruit){
			
			@SuppressLint("ResourceAsColor")
			@Override
			protected void convert(final BaseAdapterHelper helper, final Fruit item) {
				// TODO Auto-generated method stub
				ShowLog("convert    "+item.getName());
				helper.setText(R.id.list_item_name, item.getName());
				helper.setText(R.id.list_item_address, item.getOrigin());
				if(item.getCategory().getFunctions()!=null){
					
					StringBuilder stringBuilder=new StringBuilder();
					for (String string:item.getCategory().getFunctions()) {
						stringBuilder.append(string);
						stringBuilder.append("  ");
					}
					helper.setText(R.id.list_item_functions, stringBuilder.toString());
				}
				helper.setText(R.id.list_item_price, "￥ "+item.getPrice());
				helper.setText(R.id.list_item_buynum,item.getPaynum()+"");
				if (item.getPicture()!=null) {
					helper.setImageBitmapFromBmobFile(R.id.list_item_image, item.getPicture());
				}
				
				if (myyCartItems.contains(item)) {
					helper.setText(R.id.list_addto, "查看购物车");
					helper.setBackgroundColor(R.id.list_addto, R.color.red_button_disable);
				}
				final Button addto=helper.getView(R.id.list_addto);
				helper.setOnClickListener(R.id.list_addto, new OnClickListener() {
						
					@Override
					public void onClick(final View arg0) {
						// TODO Auto-generated method stub
						if (null==me) {
							ShowToast("你尚未登录");
							return;
						}
						if (addto.getText().equals("查看购物车")) {
							startAnimActivityToFragment(MainActivity.class, 3);
						}else{
							final CartItem cartItem=new CartItem();
							BmobQuery<CartItem> query=new BmobQuery<CartItem>();
							
							cartItem.setMine(me);
							cartItem.setCount(1);
							cartItem.setFruit(item);
							cartItem.save(ListFruitsActivity.this, new SaveListener() {
								
								@Override
								public void onSuccess() {
	
									//helper.
									myyCartItems.add(cartItem);
									addto.setText("查看购物车");								}
								
								@Override
								public void onFailure(int arg0, String arg1) {
									// TODO Auto-generated method stub
									ShowToast("添加失败");
								}
							});
						}
					}
	
				});
					
				helper.setOnClickListener(R.id.list_item_image, new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(ListFruitsActivity.this,DetailActivity.class);	
						intent.putExtra("fruit", item);
						startActivity(intent);
					}
				});
			}
			
		};
		mListView.setAdapter(mqQuickAdapter);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				ShowLog("onRefresh...");
				currentPage=0;
				query();
			}
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				ShowLog("onLoadMore...");
				query();
			}
		});

		mListView.pullRefreshing();
		if (!isNetConnected()) {
			ShowToast(R.string.network_tips);
			stopRefresh();
			return;
		}
		query();

	}
	
	//挺止刷新，加载
	private void stopLoadMore() {
		if (mListView.getPullLoading()) {
			mListView.stopLoadMore();
		}
	}
	private void stopRefresh() {
		if (mListView.getPullRefreshing()) {
			mListView.stopRefresh();
		}
	}
	
	
	//列表查询
	public void query(){
		ShowLog("query:>> order: "+order+" order: "+order);
		
		BmobQuery<Fruit> query = new BmobQuery<Fruit>();
		query.setCachePolicy(policy); 
		query.include("category");
		query.setLimit(limit);
		query.setSkip(limit*currentPage);
		query.order(order);
		
		if(!(StringUtils.isEmpty(searchBy)&&StringUtils.isEmpty(searchValue)))
			query.addWhereEqualTo(searchBy, searchValue);
		/*if(!StringUtils.isEmpty(keyWordSearch.getQuery().toString()))
			query.addWhereContains("name",keyWordSearch.getQuery().toString() );
		*/
		if(!StringUtils.isEmpty(keyWord))
			query.addWhereContains("name", keyWord);
		if(lower!=null&&!StringUtils.isEmpty(lower.getText().toString()))
			query.addWhereGreaterThanOrEqualTo("price", Float.valueOf(lower.getText().toString()));
		if(higher!=null&&!StringUtils.isEmpty(higher.getText().toString()))
			query.addWhereLessThanOrEqualTo("price", Float.valueOf(higher.getText().toString()));
		if (!StringUtils.isEmpty(provinceString)) 
			query.addWhereStartsWith("origin", provinceString+"-"+cityString);
		
		query.findObjects(ListFruitsActivity.this, new FindListener<Fruit>() {
			
			@Override
			public void onSuccess(List<Fruit> arg0) {
				// TODO Auto-generated method stub
				ShowLog("query onSuccess："+arg0.size());
				
				if (CollectionUtils.isNotNull(arg0)) {
					if(currentPage==0){
						mqQuickAdapter.clear();
					}
					
					mqQuickAdapter.addAll(arg0);
					ShowLog("arg0.size() >=limit  "+String.valueOf(arg0.size() >=limit));
					mListView.setPullLoadEnable(arg0.size() >=limit);
					currentPage+=1;
					arg0.clear();
					
				}else {
					
					ShowToast("暂无数据!");
				}
				
				stopRefresh();
				stopLoadMore();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowLog("数据获取错误" +arg0+ arg1);
				mListView.setPullLoadEnable(false);
				stopLoadMore();
				stopRefresh();
			}
		});
		
	}
	
	public void query2(){
		BmobQuery<CartItem> query=new BmobQuery<CartItem>();
		query.include("fruit");
		query.addWhereEqualTo("mine",me);
		query.findObjects(this, new FindListener<CartItem>() {
			
			@Override
			public void onSuccess(List<CartItem> arg0) {
				// TODO Auto-generated method stub
				if(CollectionUtils.isNotNull(arg0)){
					myyCartItems.clear();
					myyCartItems.addAll(arg0);
					arg0.clear();
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowLog("数据获取错误" +arg0+ arg1);
			}
		});
	}
		
	
	static int current=3;
	static String[] reversed=new String[]{"-","-",""}; 
	static String order="";
	//排序方式选择按钮点击事件
	@SuppressLint({ "ResourceAsColor", "NewApi" })
	public void tabsClick(View view){
		currentPage=0;
		try {
			mListView.removeAllViews();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(TextView textView:tabs){
			textView.setTextColor(getResources().getColor(R.color.text_color_gray));
		}
		((TextView) view).setTextColor(getResources().getColor(R.color.text_color_green));
		
		if(current<3&&view.getId()==tabs[current].getId()){
			if(reversed[current].equals("")){
				tabs[current].setSelected(false);
				reversed[current]="-";
			}else {
				tabs[current].setSelected(true);
				reversed[current]="";
			}
		}
		
		
		switch (view.getId()) {
		
		case R.id.textView3:
			current=3;
			if (popupwindow != null&&popupwindow.isShowing()) {
				popupwindow.dismiss();
				return;
			} else {
				if(popupwindow==null){
					initShareMenu();
				}
				openPopuWindow(view);
			}
			break;

		case R.id.textView0:
			current=0;
			order=reversed[0]+"paynum,"+reversed[0]+"liksNumber";
			query();
			break;
			
		case R.id.textView1:
			current=1;
			order=reversed[1]+"paynum";
			query();
			break;
		case R.id.textView2:
			current=2;
			order=reversed[2]+"price";
			query();
			break;
		
		default:
			break;
		}
		
	}
	

	//onActivityResult回调
	@Override
 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		   case RESULT_OK:
		    Bundle b=data.getExtras();
		    keyWord=b.getString("keyWord");
		    mHeaderLayout.setLeftText(keyWord);
		    break;
		default:
		    break;
		    }
		}
	
	
	
	
	
	
	
	//initMenu
	private Spinner province,city;
	private EditText lower;
	private EditText higher;
	private ArrayList<String> list=new ArrayList<String>();//城市
	private ArrayList<String> clist=new ArrayList<String>();//子城市
	private List<Cityinfo> province_list;
	private HashMap<String, List<Cityinfo>> city_map;
	private ArrayAdapter<String> cAdapter;
	
	public void initShareMenu(){

		//menu
		View menu=getLayoutInflater().inflate(R.layout.include_pop_conditionmenu, null,false);
		popupwindow=new PopupWindow(menu,PixelUtil.dp2px(180),PixelUtil.dp2px(200));
		popupwindow.setAnimationStyle(R.style.AnimationFade);
		popupwindow.setFocusable(true);  
		popupwindow.setOutsideTouchable(true);  
		popupwindow.setBackgroundDrawable(new BitmapDrawable());  
		menu.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(popupwindow!=null && popupwindow.isShowing()){
					popupwindow.dismiss();
				}
				return false;
			}
		});
		popupwindow.setOnDismissListener(new OnDismissListener() {
					
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				WindowManager.LayoutParams params=ListFruitsActivity.this.getWindow().getAttributes();  
			    params.alpha=1f;  
			    ListFruitsActivity.this.getWindow().setAttributes(params); 
			}
		});
		//产地
		//menu.findViewById()....
		province=(Spinner) menu.findViewById(R.id.spinner1);
		initSpinnerData();
		ArrayAdapter<String> pAdapter=new ArrayAdapter<String>(ListFruitsActivity.this, android.R.layout.simple_spinner_item,list);
		pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		province.setAdapter(pAdapter);
		province.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				ShowLog("position:"+position+"   size:"+list.size());
				provinceString=position==0?"":list.get(position-1);
				clist.clear();
				clist.add(0,"不限");
				if (position!=0) {
					clist.addAll(CitycodeUtil.getSingleton().getCity(city_map, province_list.get(position-1).getId()));
				}
				city.setSelection(0, true);
				cAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		city=(Spinner) menu.findViewById(R.id.spinner2);
		cAdapter=new ArrayAdapter<String>
		(ListFruitsActivity.this, android.R.layout.simple_spinner_item, clist);
		cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		city.setAdapter(cAdapter);
	    city.setOnItemSelectedListener(new OnItemSelectedListener() {

	    	@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				cityString=position==0?"" :clist.get(position-1);					}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    province.setSelection(0, true);
		
		//价格区间
		lower=(EditText) menu.findViewById(R.id.low);
		higher=(EditText) menu.findViewById(R.id.high);
		
		TextView textView=(TextView) menu.findViewById(R.id.clear);
		textView.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View arg0) {
				province.setSelection(0, true);
				city.setSelection(0, true);
				lower.setText("");
				higher.setText("");
			}
		});
		
	}

	
	/** 
	* @Title: initSpinnerData 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void initSpinnerData() {
		// TODO Auto-generated method stub
		// 获取城市信息
		JSONParser parser = new JSONParser();
		String area_str = FileUtil.readAssets(ListFruitsActivity.this, "area.json");
		province_list = parser.getJSONParserResult(area_str, "area0");
		city_map = parser.getJSONParserResultArray(area_str, "area1");
		list.add(0,"不限");
		list.addAll(CitycodeUtil.getSingleton().getProvince(province_list));
	}


	/** 
	* @Title: openPopuWindow 
	* @Description: TODO
	* @param View 
	* @return void
	* @throws 
	*/
	private void openPopuWindow(View view) {
		// TODO Auto-generated method stub
		popupwindow.showAsDropDown(view);
		WindowManager.LayoutParams params=ListFruitsActivity.this.getWindow().getAttributes();  
	    params.alpha=0.5f;  
	    ListFruitsActivity.this.getWindow().setAttributes(params); 
	}

	
}
