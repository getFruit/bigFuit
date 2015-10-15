package com.get.fruit.activity;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.get.fruit.R;
import com.get.fruit.adapter.util.BaseAdapterHelper;
import com.get.fruit.adapter.util.QuickAdapter;
import com.get.fruit.bean.Order;
import com.get.fruit.util.CollectionUtils;
import com.get.fruit.util.StringUtils;
import com.get.fruit.view.PagerSlidingTabStrip;
import com.get.fruit.view.listview.XListView;
import com.get.fruit.view.listview.XListView.IXListViewListener;


public class OrderListActivity extends BaseActivity {

	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private static int current=0;
	private static int tabcount=3;
	private static String[] tabNames={"待付款","待收货","所有"};
	private List<PagerSlidingTabStripFragment> fragments;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);

		initTopBarForLeft("我的订单");
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setPageMargin(6);
		initPageAdapter();
		mPagerSlidingTabStrip.setViewPager(mViewPager);
		fragments=new ArrayList<>();
		for (int i = 0; i < tabcount; i++) {
			fragments.add(PagerSlidingTabStripFragment.newInstance(i));
		}
	}

	/** 初始化Adapter */
	private void initPageAdapter() {
		mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

			@Override
			public Fragment getItem(int position) {
				return fragments.get(position);
			}

			@Override
			public int getCount() {
				return tabcount;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return tabNames[position];
			}
			
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {            
				//防止销毁
			}

		});
	}
	
	
	

	public static final class PagerSlidingTabStripFragment extends BaseFragment {

		public static PagerSlidingTabStripFragment newInstance(int position) {
			PagerSlidingTabStripFragment fragment = new PagerSlidingTabStripFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			fragment.setArguments(bundle);
			return fragment;
		}

		private TextView emptyView;
		private XListView mListView;
		private QuickAdapter<Order> mQuickAdapter;
		List<Order> allItems;
		private boolean loaded=false;
		private boolean inited=false;
		private int position;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.order_fragment, container, false);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			position=getArguments().getInt("position",0);
			initView();
			inited=true;
			if (position==0) {
				query();
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
			emptyView=(TextView) findViewById(R.id.listview_empty);
			mListView=(XListView) findViewById(R.id.order_listview);
			mQuickAdapter=new QuickAdapter<Order>(getActivity(),R.layout.item_order_list) {

				@Override
				protected void convert(final BaseAdapterHelper helper, final Order item) {
					// TODO Auto-generated method stub
					helper.setText(R.id.list_item_name, item.getFruit().getName());
					helper.setText(R.id.list_item_functions, item.getCreatedAt());
					helper.setText(R.id.list_item_price, "￥ "+item.getFruit().getPrice());
					if (item.getFruit().getPicture()!=null) {
						helper.setImageBitmapFromBmobFile(R.id.list_item_image, item.getFruit().getPicture());
					}
					final Button addto=helper.getView(R.id.list_addto);
					switch (item.getState()) {
					case 等待支付:
						addto.setText("马上支付");
						addto.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								startAnimActivityWithData(PayActivity.class,"order",item);
							}
						});
						break;
					case 等待发货:
						if (item.getPay()) {
							addto.setText("确认收货");
							Order order=new Order();
							order.setState(Order.State.交易完成);
							order.update(getActivity(), item.getObjectId(), new UpdateListener() {
								
								@Override
								public void onSuccess() {
									mQuickAdapter.remove(item);
								}
								
								@Override
								public void onFailure(int arg0, String arg1) {
									ShowLog("稍后再试："+arg1);
								}
							});
							
						}else {
							addto.setText("在线支付");
							addto.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									startAnimActivityWithData(PayActivity.class,"order",item);
								}
							});
						}
						break;

					default:
						addto.setText("删除订单");
						item.delete(getActivity(),new DeleteListener() {
							
							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								mQuickAdapter.remove(item);
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO Auto-generated method stub
								ShowToast("删除失败："+arg1);
								
							}
						});
						break;
					}
					
					
					
					helper.setOnClickListener(R.id.list_item_image, new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(getActivity(),DetailActivity.class);	
							intent.putExtra("fruit", item.getFruit());
							startAnimActivity(intent);
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
					setEmptyView(null);
					query();
				}
				
				@Override
				public void onLoadMore() {
					// TODO Auto-generated method stub
					ShowLog("onLoadMore...");
					setEmptyView(null);
					query();
				}

				
			});

		}
		
		/** 
		* @Title: setEmptyView 
		* @Description: TODO
		* @param @param string
		* @return void
		* @throws 
		*/
		private void setEmptyView(String string) {
			// TODO Auto-generated method stub
			if (StringUtils.isEmpty(string)) {
				emptyView.setVisibility(View.GONE);
			}else {
				emptyView.setVisibility(View.VISIBLE);
				emptyView.setText(string);
			}
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

		
		
		private void query() {
			BmobQuery<Order> query=new BmobQuery<Order>();
			query.addWhereEqualTo("user", me.getObjectId());
			query.include("fruit");
			query.order("createdAt");
			switch (position) {
			case 0:
				query.addWhereEqualTo("state", Order.State.等待支付.toString());
				break;
			case 1:
				query.addWhereEqualTo("state", Order.State.等待发货.toString());
				break;
			default:
				break;
			}
			
			query.findObjects(getActivity(), new FindListener<Order>() {
				
				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					setEmptyView("数据获取错误" +arg0+ arg1);
					stopRefresh();
				}

				@Override
				public void onSuccess(List<Order> arg0) {
					// TODO Auto-generated method stub
					if(CollectionUtils.isNotNull(arg0)){
						setEmptyView("");
						allItems=arg0;
						mQuickAdapter.replaceAll(allItems);
					}else {
						setEmptyView("毛线都没有买");
					}
					stopRefresh();
				}
			});
			loaded=true;
		}

		@Override
		protected void lazyLoad() {
			ShowLog(String.valueOf(loaded)+String.valueOf(inited));
			if (!loaded&&inited) {
				query();
				emptyView.setVisibility(View.GONE);
				mListView.pullRefreshing();
				
			}
		}
		
	}
}
