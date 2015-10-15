package com.get.fruit.activity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.location.f;
import com.get.fruit.R;
import com.get.fruit.R.layout;
import com.get.fruit.adapter.util.BaseAdapterHelper;
import com.get.fruit.adapter.util.QuickAdapter;
import com.get.fruit.bean.Fruit;
import com.get.fruit.util.CollectionUtils;
import com.get.fruit.util.StringUtils;
import com.get.fruit.view.listview.XListView;
import com.get.fruit.view.listview.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CollectionActivity extends BaseActivity {
	
	private TextView emptyView;
	private XListView mListView;
	private QuickAdapter<Fruit> mQuickAdapter;
	List<Fruit> allItems;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		initTopBarForLeft("我的收藏");
		initView();
		if (!isNetConnected()) {
			setEmptyView("断网咯");
			return;
		}
		mListView.pullRefreshing();
		setEmptyView(null);
		query();
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
		mListView=(XListView) findViewById(R.id.collection_listview);
		mQuickAdapter=new QuickAdapter<Fruit>(this,R.layout.item_collection_list) {

			@Override
			protected void convert(final BaseAdapterHelper helper, final Fruit item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.list_item_name, item.getName());
				helper.setText(R.id.list_item_describe, item.getDescribe());
				helper.setText(R.id.list_item_price, "￥ "+item.getPrice());
				if (item.getPicture()!=null) {
					helper.setImageBitmapFromBmobFile(R.id.list_item_image, item.getPicture());
				}
				
				helper.setOnClickListener(R.id.list_addto, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Fruit fruit=new Fruit();
						fruit.setObjectId(item.getObjectId());
						BmobRelation relation = new BmobRelation();
						relation.remove(me);
						fruit.setLikes(relation);
						fruit.update(CollectionActivity.this, new UpdateListener() {
							
							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								mQuickAdapter.remove(item);
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO Auto-generated method stub
								ShowLog("稍后再试："+arg1);
							}
						});
					}
				});
				
				helper.setOnClickListener(R.id.list_item_image, new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						startAnimActivityWithData(DetailActivity.class, "fruit", item);
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

		mListView.pullRefreshing();
		if (!isNetConnected()) {
			setEmptyView("断网咯");
			stopRefresh();
			return;
		}

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
		//here
		BmobQuery<Fruit> query=new BmobQuery<Fruit>();
		query.addWhereRelatedTo("likes", new BmobPointer(me));
		query.findObjects(this, new FindListener<Fruit>() {
			
			@Override
			public void onError(int arg0, String arg1) {
				setEmptyView("数据获取错误" +arg0+ arg1);
				stopRefresh();
			}

			@Override
			public void onSuccess(List<Fruit> arg0) {
				stopRefresh();
				if(CollectionUtils.isNotNull(arg0)){
					setEmptyView("");
					allItems=arg0;
					mQuickAdapter.replaceAll(allItems);
				}else {
					setEmptyView("毛线都没有收藏");
				}
			}
		});
		
	}
}
