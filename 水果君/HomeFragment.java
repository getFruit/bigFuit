package com.get.fruit.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.DownloadListener;
import com.get.fruit.R;
import com.get.fruit.activity.BaseFragment;
import com.get.fruit.activity.DetailActivity;
import com.get.fruit.activity.MainActivity;
import com.get.fruit.adapter.util.BaseAdapterHelper;
import com.get.fruit.adapter.util.QuickAdapter;
import com.get.fruit.bean.HomeAD;
import com.get.fruit.util.CollectionUtils;
import com.get.fruit.util.ImageLoadOptions;
import com.get.fruit.util.TimeUtil;
import com.get.fruit.view.MyGridView;
import com.get.fruit.view.MyImageSwitcher;
import com.get.fruit.view.Rotate3D;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeFragment extends BaseFragment{
	static int adnum=4;//轮播光告数
	static int ad2num=6;//首页广告数
	static  long casheage=TimeUtil.DAY*3;
	
	List<Drawable> adpics=new ArrayList<Drawable>();
	/*
	ImageView[] views=new ImageView[]{
			(ImageView) findViewById(R.id.home_imageButton1),
			(ImageView) findViewById(R.id.home_imageButton1),
			(ImageView) findViewById(R.id.home_imageButton1),
			(ImageView) findViewById(R.id.home_imageButton1)};
	
	*/
	ImageView[] views = new ImageView[4];
	List<HomeAD> ads=new ArrayList<HomeAD>();
	MyImageSwitcher imswitcher;
	GestureDetector mGestureDetector;
	int i=0;
	Runnable r;
	
	private MyGridView mGridView;
	private QuickAdapter<HomeAD> mAdapter;
	
	private HomeAD[] dataAds;
	List<String> urls=new ArrayList<>();
	int[] res = new int[]{R.drawable.a,R.drawable.bb,R.drawable.cc,R.drawable.aa};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		loadData();
		loadData2();
	}

	

	/** 
	* @Title: initView 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void initView() {
		initImageSwitcher();
		
		initGridView();
		
		initButtons();
	}

	/** 
	* @Title: initButtons 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void initButtons() {
		// TODO Auto-generated method stub
		//按钮
				ImageView v1 = (ImageView) findViewById(R.id.View1);
				ImageView v2 = (ImageView) findViewById(R.id.View2);
				ImageView v3 = (ImageView) findViewById(R.id.View3);
				ImageView v4 = (ImageView) findViewById(R.id.View4);
				views[0]=v1;
				views[1]=v2;
				views[2]=v3;
				views[3]=v4;
				views[0].setSelected(true);
	}

	public void initGridView() {
		//gridview
		mGridView=(MyGridView) findViewById(R.id.home_gridView);
		mAdapter=new QuickAdapter<HomeAD>(getActivity(), R.layout.item_home_gridview) {

			@Override
			protected void convert(BaseAdapterHelper helper, final HomeAD item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.home_title, item.getName());
				helper.setText(R.id.home_price, String.valueOf(item.getPrice()));
				helper.setImageBitmapFromBmobFile(R.id.home_pic, item.getPic());
				helper.setOnClickListener(R.id.home_pic , new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						startAnimActivityWithData(DetailActivity.class,"fruit",item.getFruit());
					}
				});
			}
		};
		mGridView.setAdapter(mAdapter);
	}

	public void initImageSwitcher() {
		//轮播图
		imswitcher = (MyImageSwitcher) findViewById(R.id.detail_imageSwitcher1);
		imswitcher.setFactory(new ViewFactory()
		{
			@Override
			public View makeView()
			{
				ImageView imageView = new ImageView(getActivity());
				imageView.setBackgroundColor(0xff0000);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				return imageView;
			}
		});
		imswitcher.setImageResource(R.drawable.a);
		mGestureDetector = new GestureDetector(getActivity(), new MyGestureListener());

		
		//启动轮播事件监听
		imswitcher.setOnTouchListener(new OnTouchListener() {
		
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				imswitcher.getParent().requestDisallowInterceptTouchEvent(true);
				 mGestureDetector.onTouchEvent(event);  
				return true;
			}
		});
		//启动轮播
		//DownloadTask dTask = new DownloadTask();   
		//dTask.execute(100);
	
		
	}
	
	
	/** 
	* @Title: loadData 
	* @Description: TODO下载广告1
	* @param 
	* @return void
	* @throws 
	*/
	private void loadData() {
		// TODO Auto-generated method stub
		BmobQuery< HomeAD> query=new BmobQuery<HomeAD>();
		query.addWhereEqualTo("top", true);
		query.setLimit(adnum);
		//query.include("fruit");
		query.setMaxCacheAge(casheage);
		if (isNetConnected()) {
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		}else if (query.hasCachedResult(getActivity(),HomeAD.class)) {
			query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		}
		query.findObjects(getActivity(), new FindListener<HomeAD>() {
			
			@Override
			public void onSuccess(List<HomeAD> arg0) {
				// TODO Auto-generated method stub
				if(!(CollectionUtils.isNotNull(arg0)&&arg0.size()==4))
					return;
				ads=arg0;
				downloadPics();
				//loadImage();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowLog("广告查询失败： "+arg1);
			}
		});
	}
	
	private void loadImage() {
		// TODO Auto-generated method stub
		ShowLog("load....");
		for(int i = 0;i<ads.size();i++){
			ads.get(i).getPic().loadImage(getActivity(), views[i]);
		}
	}
	
	//下载轮播图片
	public void downloadPics(){
		ImageLoader.getInstance().displayImage(ads.get(0).getPic().getFileUrl(getActivity()), (ImageView)findViewById(R.id.test),ImageLoadOptions.getOptions());
		for (int i = 0; i < ads.size(); i++) {
			ShowLog("down>>"+"    getFileUrl: "+ads.get(i).getPic().getFileUrl(getActivity())+"   url: "+ads.get(i).getPic().getUrl());
			ShowLog(ads.get(i).getPic().getUrl());
			ShowLog(ads.get(i).getPic().getFileUrl(getActivity()));
			ShowLog(ads.get(i).getPic().getGroup());
			
			BmobProFile.getInstance(getActivity()).download(ads.get(i).getPic().getFilename(), new DownloadListener() {
	
		        @Override
		        public void onSuccess(String fullPath) {
		            // TODO Auto-generated method stub
		        	adpics.add(Drawable.createFromPath(fullPath));
		            ShowLog("下载成功："+fullPath);
		        }
	
		        @Override
		        public void onProgress(String localPath, int percent) {
		            // TODO Auto-generated method stub
		        	ShowLog("download-->onProgress :"+percent);
		        }
	
		        @Override
		        public void onError(int statuscode, String errormsg) {
		            // TODO Auto-generated method stub
		        	ShowLog("下载出错："+statuscode +"--"+errormsg);
		        }
		    });
			
		}
	}
	
	
	/** 
	 * @Title: loadData2 
	 * @Description: TODO下载广告2
	 * @param 
	 * @return void
	 * @throws 
	 */
	private void loadData2() {
		// TODO Auto-generated method stub
		BmobQuery< HomeAD> query=new BmobQuery<HomeAD>();
		query.setMaxCacheAge(casheage);
		query.addWhereNotEqualTo("top", true);
		query.setLimit(ad2num);
		if (isNetConnected()) {
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		}else if (query.hasCachedResult(getActivity(),HomeAD.class)) {
			query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		}
		
		query.findObjects(getActivity(), new FindListener<HomeAD>() {
			
			@Override
			public void onSuccess(List<HomeAD> arg0) {
				// TODO Auto-generated method stub
				if(CollectionUtils.isNotNull(arg0))
					mAdapter.addAll(arg0);
				}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowLog("广告2查询失败： "+arg0+arg1);
			}
		});
	}
	
	
	//改变圆点颜色
	public void	setDotState(int m)
	{
	
	  for(int i=0;i<views.length;i++)
	  {
		  if(i==m)
		  {
			  views[i].setSelected(true);
		  }
		  else
		  {
			  views[i].setSelected(false);
			  
		  }
			  
	  }
	
	}
	
	//轮播动作
	@SuppressLint("NewApi")
	public void setImageSwitcherState(int direction) {
		float halfWidth=imswitcher.getWidth()/2.0f;  
		 float halfHeight=imswitcher.getHeight()/2.0f;  
		 int duration=500;  
		 int depthz=0;
		
		 Rotate3D rdin = new Rotate3D(direction*75,0,0,halfWidth,halfHeight);
		 rdin.setDuration(duration);    
		 rdin.setFillAfter(true);
		 imswitcher.setInAnimation(rdin);   
		 Rotate3D rdout = new Rotate3D(direction*(-15),direction*(-90),0,halfWidth,halfHeight);
		 rdout.setDuration(duration);    
		 rdout.setFillAfter(true);
		 imswitcher.setOutAnimation(rdout);
		 
		 
		 i=(i+direction);
		 
		int p= i%4;
		if(p>=0)
		{
			setDotState(p);
			if (res.length>p) {
			     imswitcher.setImageURI(Uri.parse(ads.get(p).getPic().getFileUrl(getActivity())));
				//imswitcher.setImageDrawable(adpics.get(p));
				//imswitcher.setImageFromBmobFile(ads.get(p).getPic());
				//imswitcher.setImageResource(res[p]);
			}
		
		}else
		{
			
		int	k=4+p;
		setDotState(k);
		if(res.length>k){
			//imswitcher.setImageDrawable(adpics.get(k));
			//imswitcher.setImageFromBmobFile(ads.get(k).getPic());
			imswitcher.setImageResource(res[k]);
		}
			
		}
	}
	
	//收拾滑动
	private class MyGestureListener implements GestureDetector.OnGestureListener
	{

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			setImageSwitcherState(velocityX >0?-1:1);  
			return true;
		}

		

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			
          int p= i%4;
          
          try {
			if (null==ads.get(p)||null==ads.get(p).getFruit()) {
				  return true;
			  }
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return true;
		}
			
			if(p>=0)
			{
				startAnimActivityWithData(DetailActivity.class, "fruit", ads.get(p).getFruit());
			}else
			{
				int k =4+p;
				startAnimActivityWithData(DetailActivity.class, "fruit", ads.get(k).getFruit());
			}
			return true;
		}

		
		
	}
	
	//自动轮播
	class DownloadTask extends AsyncTask
	 {

		@SuppressWarnings("unchecked")
		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			
			for(int i=0;i<Integer.MAX_VALUE;i++)
			{
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				 publishProgress(null);
				
			}
			
			
			return null;
		}
		
		
		@Override
		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			setImageSwitcherState(1);  
			
		 
		}
		
	 }
	
	
}
