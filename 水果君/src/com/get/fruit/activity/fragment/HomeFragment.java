package com.get.fruit.activity.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;

import com.get.fruit.App;
import com.get.fruit.BmobConstants;
import com.get.fruit.R;
import com.get.fruit.activity.BaseFragment;
import com.get.fruit.activity.DetailActivity;
import com.get.fruit.adapter.util.BaseAdapterHelper;
import com.get.fruit.adapter.util.QuickAdapter;
import com.get.fruit.bean.HomeAD;
import com.get.fruit.util.CollectionUtils;
import com.get.fruit.util.TimeUtil;
import com.get.fruit.view.MyGridView;
import com.get.fruit.view.MyImageSwitcher;
import com.get.fruit.view.Rotate3D;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class HomeFragment extends BaseFragment{
	static int adnum=4;//轮播光告数
	static int ad2num=6;//首页广告数
	static  long casheage=TimeUtil.DAY*7;
	
	List<HomeAD> ads=new ArrayList<HomeAD>();
	MyImageSwitcher imswitcher;
	GestureDetector mGestureDetector;
	int i=0;
	private MyGridView mGridView;
	private QuickAdapter<HomeAD> mAdapter;
	
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
	}


	/** 
	* @Title: initView 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void initView() {
		initGridView();
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
		query.setMaxCacheAge(casheage);
		if (isNetConnected()) {
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		}else if (query.hasCachedResult(getActivity(),HomeAD.class)) {
			query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		}else {
			ShowToast("無網絡鏈接");
			return;
		}
		query.findObjects(getActivity(), new FindListener<HomeAD>() {
			
			@Override
			public void onSuccess(List<HomeAD> arg0) {
				// TODO Auto-generated method stub
				if(!(CollectionUtils.isNotNull(arg0)&&arg0.size()==4))
					return;
				ads=arg0;
				initBanner();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowLog("广告1查询失败： "+arg1);
			}
		});
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
		}else {
			return;
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
	}
	

	/** 
	* @Title: initBanner 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void initBanner() {
		// TODO Auto-generated method stub
		initImageLoader();
		initAdView();
	}
	private ViewPager adViewPager;
	private List<ImageView> imageViews;// 滑动的图片集合
	private List<View> dots; // 图片标题正文的那些点
	private List<View> dotList;
	private TextView tv_date;
	private TextView tv_title;
	private TextView tv_topic_from;
	private TextView tv_topic;
	private int currentItem = 0; // 当前图片的索引号
	// 定义的五个指示点
	private View dot0;
	private View dot1;
	private View dot2;
	private View dot3;
	private View dot4;

	private ScheduledExecutorService scheduledExecutorService;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private PagerAdapter mViewPagerAdapter;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adViewPager.setCurrentItem(currentItem);
		};
	};

	private void initImageLoader() {
		File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils
				.getOwnCacheDirectory(App.getInstance(),
						BmobConstants.MyTempDir);

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity()).defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new LruMemoryCache(12 * 1024 * 1024))
				.memoryCacheSize(12 * 1024 * 1024)
				.discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoader.getInstance().init(config);
		
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.top_banner_android)
				.showImageForEmptyUri(R.drawable.top_banner_android)
				.showImageOnFail(R.drawable.top_banner_android)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();
	}

	private void initAdView() {
		imageViews = new ArrayList<ImageView>();

		dots = new ArrayList<View>();
		dotList = new ArrayList<View>();
		dot0 = findViewById(R.id.v_dot0);
		dot1 = findViewById(R.id.v_dot1);
		dot2 = findViewById(R.id.v_dot2);
		dot3 = findViewById(R.id.v_dot3);
		dot4 = findViewById(R.id.v_dot4);
		dots.add(dot0);
		dots.add(dot1);
		dots.add(dot2);
		dots.add(dot3);
		dots.add(dot4);
		
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_topic_from = (TextView) findViewById(R.id.tv_topic_from);
		tv_topic = (TextView) findViewById(R.id.tv_topic);
		
		adViewPager = (ViewPager) findViewById(R.id.vp);
		mViewPagerAdapter=new MyAdapter();
		adViewPager.setAdapter(mViewPagerAdapter);
		adViewPager.setOnPageChangeListener(new MyPageChangeListener());
		addDynamicView();
	}

	private void addDynamicView() {
		for (int i = 0; i < ads.size(); i++) {
			ImageView imageView = new ImageView(getActivity());
			mImageLoader.displayImage(ads.get(i).getPic().getFileUrl(getActivity()), imageView,options);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
			dots.get(i).setVisibility(View.VISIBLE);
			dotList.add(dots.get(i));
		}
		startAd();
	}

	private void startAd() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
	}

	private class ScrollTask implements Runnable {

		@Override
		public void run() {
			synchronized (adViewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget();
			}
		}
	}
	
	private class MyPageChangeListener implements OnPageChangeListener {

		private int oldPosition = 0;
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			HomeAD adDomain = ads.get(position);
			tv_title.setText(adDomain.getName());
			tv_date.setText(adDomain.getUpdatedAt().substring(0, adDomain.getUpdatedAt().indexOf(" ")));
			tv_topic_from.setText("特价");
			tv_topic.setText(adDomain.getPrice()+"");
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}
	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return ads.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			ImageView iv = imageViews.get(position);
			((ViewPager) container).addView(iv);
			final HomeAD adDomain = ads.get(position);
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startAnimActivityWithData(DetailActivity.class, "fruit", ads.get(position).getFruit());
				}
			});
			return iv;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}

	}
	
	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		loadData();
		loadData2();
	}

	@Override
	protected void onInvisible() {
		super.onInvisible();
		if (null!=scheduledExecutorService) {
			scheduledExecutorService.shutdown();
		}
	}

}
