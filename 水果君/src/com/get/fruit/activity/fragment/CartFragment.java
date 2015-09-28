package com.get.fruit.activity.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.get.fruit.R;
import com.get.fruit.activity.BaseFragment;
import com.get.fruit.activity.DetailActivity;
import com.get.fruit.activity.OrderEditActivity;
import com.get.fruit.adapter.util.BaseAdapterHelper;
import com.get.fruit.adapter.util.QuickAdapter;
import com.get.fruit.bean.CartItem;
import com.get.fruit.bean.Fruit;
import com.get.fruit.bean.Order;
import com.get.fruit.util.CollectionUtils;
import com.get.fruit.view.HeaderLayout;
import com.get.fruit.view.HeaderLayout.onRightImageButtonClickListener;
import com.get.fruit.view.listview.XListView;
import com.get.fruit.view.listview.XListView.IXListViewListener;

public class CartFragment extends BaseFragment {
	static boolean inited=false;
	static boolean loaded=false;
	private CartCallBack callBack;
	private Button gopay;
	private CheckBox checkAll;
	private TextView totalPrice;
	private onRightImageButtonClickListener mRightImageButtonClickListener=null;
	private XListView mListView;
	private QuickAdapter<CartItem> mQuickAdapter;
	private TextView emptyView;
	List<Integer> checkedItems=new LinkedList<Integer>();
	List<CartItem> allItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_cart, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
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
		ShowLog("initView.....cart");
		// TODO Auto-generated method stub
		initListView();
		initButtomView();
		inited=true;
		/*
		if (!loaded) {
			loadData();
		}
		*/
	}

	/** 
	* @Title: setRightButtonListener 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void setRightButtonListener() {
		ShowLog("setRightButtonListener......");
		if (mRightImageButtonClickListener==null) {
			mRightImageButtonClickListener=new onRightImageButtonClickListener() {
				@Override
				public void onClick() {
					// TODO Auto-generated method stub
					if (checkedItems.size()<=0) {
						ShowToast("ÇëÑ¡Ôñ£¡");
						return;
					}
					final List deleteItems=new ArrayList<CartItem>();
					for (int i = 0; i < checkedItems.size(); i++) {
						
						deleteItems.add(allItems.get(checkedItems.get(i)));
					}
					
					new CartItem().deleteBatch(getActivity(), deleteItems, new DeleteListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							allItems.removeAll(deleteItems);
							/*for (int i = 0; i < checkedItems.size(); i++) {
								mQuickAdapter.remove(checkedItems.get(i));
							}*/
							mQuickAdapter.removeAll(deleteItems);
							checkedItems.clear();
							deleteItems.clear();
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							deleteItems.clear();
							ShowToast("É¾³ýÊ§°Ü£¬ÉÔºóÔÙÊÔ");
						}
					} );
				}
			};
		}
		callBack.getHeaderLayout().setTitleAndRightButton("¹ºÎï³µ",-1,"É¾³ý", mRightImageButtonClickListener);
	}

	
	
	/** 
	* @Title: loadData 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void loadData() {
		// TODO Auto-generated method stub
		ShowLog("loadData........cart");
		BmobQuery<CartItem> query=new BmobQuery<CartItem>();
		query.include("fruit");
		query.addWhereEqualTo("mine",me);
		query.findObjects(getActivity(), new FindListener<CartItem>() {
			
			@Override
			public void onSuccess(List<CartItem> arg0) {
				// TODO Auto-generated method stub
				if(CollectionUtils.isNotNull(arg0)){
					emptyView.setVisibility(View.GONE);
					allItems.clear();
					allItems=arg0;
					mQuickAdapter.clear();
					mQuickAdapter.addAll(allItems);
					loaded=true;
					
				}else {
					emptyView.setVisibility(View.VISIBLE);
					emptyView.setText("¹ºÎï³µ¿Õ¿ÕµÄ");
				}
				arg0.clear();
				stopRefresh();
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				emptyView.setVisibility(View.VISIBLE);
				emptyView.setText("Êý¾Ý»ñÈ¡´íÎó" +arg0+ arg1);
				ShowLog("Êý¾Ý»ñÈ¡´íÎó" +arg0+ arg1);
			}
		});
	}

	/** 
	* @Title: initButtomView 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	public void initButtomView() {
		// TODO Auto-generated method stub
		checkAll=(CheckBox) findViewById(R.id.cart_all_check);
		gopay=(Button) findViewById(R.id.cart_gopay);
		totalPrice=(TextView) findViewById(R.id.cart_totalprice);
		
		checkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				checkedItems.clear();
				if (isChecked) {
					for (int i = 0; i < allItems.size(); i++) {
						checkedItems.add(i);
					}
				}
				mQuickAdapter.notifyDataSetChanged();
				setTotal();
			}

			
		});
		
		
		gopay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				List<Order> items=new ArrayList<>();
				List<CartItem> cartItems=new ArrayList<>();
				for (Integer i:checkedItems) {
					CartItem c=allItems.remove((int)i);
					cartItems.add(c);
					Order order=new Order(me,c.getFruit(),c.getCount());
					items.add(order);
				}
				checkedItems.clear();
				allItems.removeAll(cartItems);
				// TODO Auto-generated method stub
				startAnimActivityWithData(OrderEditActivity.class, "orders", (Serializable) items);
			}
		});
		
		
		
	}

	public void setTotal() {
		ShowLog("setTotal");
		float total = 0;
		if (checkedItems.size()!=0) {
			CartItem item;
			for(int i=0; i<checkedItems.size();i++){
				item=allItems.get(checkedItems.get(i));
				total+=(item.getFruit().getPrice()*item.getCount());
			}
		}
		totalPrice.setText(total+"");
	}
	
	public void initListView() {
		mListView=(XListView) findViewById(R.id.cart_listview);
		emptyView=(TextView) findViewById(R.id.listview_empty);
		mQuickAdapter=new QuickAdapter<CartItem>(getActivity(),R.layout.item_cart_list) {
			
			@Override
			protected void convert(final BaseAdapterHelper helper, final CartItem item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.cart_item_price, ""+(item.getFruit().getPrice()*item.getCount()));
				helper.setText(R.id.cart_item_name, item.getFruit().getName());
				helper.setText(R.id.cart_item_describe, item.getFruit().getDescribe());
				helper.setText(R.id.cart_item_count, item.getCount()+"");
				helper.setChecked(R.id.cart_item_check,checkedItems.contains((Integer)helper.getPosition()));
				final ImageView imageView=helper.getView(R.id.cart_item_image);
				helper.setImageBitmapFromBmobFile(R.id.cart_item_image, item.getFruit().getPicture());
				helper.setOnClickListener(R.id.cart_item_image, new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						startAnimActivityWithData(DetailActivity.class, "fruit", item.getFruit());
					}
				});
				helper.setOnClickListener(R.id.cart_item_add, new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						final int count=Integer.valueOf(((EditText)helper.getView(R.id.cart_item_count)).getText().toString())+1;
						if (count>=item.getFruit().getCount()) {
							ShowToast("¿â´æ²»¹»£¡ "+item.getFruit().getCount());
							return;
						}
						item.increment("count");
						item.update(getActivity(), new UpdateListener() {
							
							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								helper.setText(R.id.cart_item_count,count+"");
								item.setCount(count);
								helper.setText(R.id.cart_item_price, ""+(item.getFruit().getPrice()*item.getCount()));
								setTotal();
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO Auto-generated method stub
								ShowToast("Ìí¼ÓÊ§°Ü");
							}
						});
					}
				});
				helper.setOnClickListener(R.id.cart_item_dec, new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						final int count=Integer.valueOf(((EditText)helper.getView(R.id.cart_item_count)).getText().toString())-1;
						if (count<=0) {
							return;
						}
						item.increment("count", -1);
						item.update(getActivity(), new UpdateListener() {
							
							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								helper.setText(R.id.cart_item_count,count+"");
								item.setCount(count);
								helper.setText(R.id.cart_item_price, ""+(item.getFruit().getPrice()*item.getCount()));
								setTotal();
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO Auto-generated method stub
								ShowToast("É¾³ýÊ§°Ü£¬ÉÔºóÔÙÊÔ");
							}
						});
					}
				});
				final CheckBox checkBox=helper.getView(R.id.cart_item_check);
				helper.setOnClickListener(R.id.cart_item_check, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (checkBox.isChecked()) {
							checkedItems.add((Integer)helper.getPosition());
						}else {
							checkedItems.remove((Integer)helper.getPosition());
						}
						setTotal();
						//mQuickAdapter.notifyDataSetChanged();
					}
				});
			}
		};
		
		mListView.setAdapter(mQuickAdapter);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				ShowLog("onRefresh...");
				loadData();
			}
			
			@Override
			public void onLoadMore() {
				return;
			}
		});

	}
	//Í£Ö¹Ë¢ÐÂ
	private void stopRefresh() {
		if (mListView.getPullRefreshing()) {
			mListView.stopRefresh();
		}
	}
	//ÉèÖÃÉ¾³ý°´Å¥
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		ShowLog("attach.....cart");
		callBack=(CartCallBack) activity;
		super.onAttach(activity);
	}
	
	
	public interface CartCallBack{
		public HeaderLayout getHeaderLayout();
	}


	
	
	static boolean isVisible=false;
	@Override
	protected void onVisible() {
		isVisible=true;
		ShowLog("onVisible....cart");

		setRightButtonListener();
		if (inited && !loaded) {
			mListView.pullRefreshing();
			if (!isNetConnected()) {
				emptyView.setText(R.string.network_tips);
				stopRefresh();
			}else {
				loadData();
			}
		}
	}
	
	@Override
	protected void onInvisible() {
		isVisible=false;
	}

	
	/*static boolean first=true;
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		ShowLog("onstart.....");
		if (loaded&&!first) {
			
			ShowLog("onstart.....setRightButtonListener");
			setRightButtonListener();
		}
		first=false;
		super.onStart();
		
	}*/
	

	
}
