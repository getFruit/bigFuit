package com.get.fruit.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import b.Android;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;

import com.get.fruit.App;
import com.get.fruit.BmobConstants;
import com.get.fruit.R;
import com.get.fruit.activity.BaseFragment.FragmentCallBack;
import com.get.fruit.activity.fragment.CartFragment;
import com.get.fruit.activity.fragment.CategoryFragment;
import com.get.fruit.activity.fragment.GardenFragment;
import com.get.fruit.activity.fragment.HomeFragment;
import com.get.fruit.activity.fragment.PersonFragment;
import com.get.fruit.adapter.ZoomOutPageTransformer;
import com.get.fruit.adapter.util.BaseAdapterHelper;
import com.get.fruit.adapter.util.QuickAdapter;
import com.get.fruit.bean.Order;
import com.get.fruit.util.StringUtils;
import com.get.fruit.view.HeaderLayout;
import com.get.fruit.view.HeaderLayout.onLeftImageButtonClickListener;
import com.get.fruit.view.HeaderLayout.onRightImageButtonClickListener;
import com.get.fruit.view.listview.XListView;
import com.get.fruit.view.listview.XListView.IXListViewListener;

public class MainActivity extends BaseActivity implements OnClickListener,
		 FragmentCallBack {

	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private ImageButton[] mButtons = new ImageButton[5];
	private BaseFragment fHome, fCategory, fPerson, fCart, fGarden;
	private BaseFragment[] mFragments;
	private static int currentSelect;
	private CharSequence address = "���";
	private int to = 0;// ��Ҫǰ����fragment

	private onLeftImageButtonClickListener homeLeftListener = new onLeftImageButtonClickListener() {
		@SuppressLint("NewApi")
		@Override
		public void onClick() {
			// TODO Auto-generated method stub
			ShowToast("��ʱֻ֧������������������Ժ�֧�֣�");
			/*Intent intent = new Intent(MainActivity.this,
					LocationActivity.class);
			startAnimActivityForResult(intent,BmobConstants.REQUESTCODE_FROM_MAINACTIVITY_FORADDRESS);*/
		}
	};
	private onLeftImageButtonClickListener baseLeftListener = new onLeftImageButtonClickListener() {
		@Override
		public void onClick() {
			// TODO Auto-generated method stub
			changePage(currentSelect);
		}
	};
	private onRightImageButtonClickListener mRightButtonSaerch=new onRightImageButtonClickListener() {

		@Override
		public void onClick() {
			// ���search��ť �¼���Ӧ
			startAnimActivity(ListFruitsActivity.class);
		}
	};
	private onRightImageButtonClickListener deleteListener=null;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ShowLog("MainAc >> request: "+requestCode+"   result: "+resultCode);
		switch (requestCode) {
		case BmobConstants.REQUESTCODE_FROM_MAINACTIVITY_FORADDRESS:
			if (data==null) {
				break;
			}
			address=data.getStringExtra("address");
			if (StringUtils.isEmpty(address)) {
				break;
			}
			mHeaderLayout.setLeftText(address);
			break;
			
		default:
			ShowLog("default  onActivityResult");
			fPerson.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	// here
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			Intent intent = getIntent();
			ShowLog(intent.toString());
			to = intent.getIntExtra("to", 0);
			
			ShowLog("onResume>>   to..." + to);
			if (to!=0) {
				this.onClick(mButtons[to]);
			}
			super.onResume();
		}
		
		//���onResume�����л�ȡ����extraֵ������
		protected void onNewIntent(Intent intent) {
	        super.onNewIntent(intent);
	        setIntent(intent);
	    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initEvent();
		if (App.mInstance.getSpUtil().isAllowAutoUpdate()) {
			BmobUpdateAgent.update(this);
		}
	}

	/**
	 * @Title: initEvent
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */

	public void initView() {

		initTopBarForBoth("ˮ����", R.drawable.base_action_bar_addrees_selector,
				address, homeLeftListener,

				R.drawable.base_action_bar_search_selector, null,
				mRightButtonSaerch, 4);

		mButtons[0] = (ImageButton) findViewById(R.id.ib_home);
		mButtons[1] = (ImageButton) findViewById(R.id.ib_category);
		mButtons[2] = (ImageButton) findViewById(R.id.ib_person);
		mButtons[3] = (ImageButton) findViewById(R.id.ib_cart);
		mButtons[4] = (ImageButton) findViewById(R.id.ib_garden);
		mButtons[0].setSelected(true);
		fHome = new HomeFragment();
		fCategory = new CategoryFragment();
		fPerson = new PersonFragment();
		fCart = new CartFragment();
		fGarden = new GardenFragment();

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mFragments = new BaseFragment[] { fHome, fCategory, fPerson, fCart, fGarden };
		mViewPager.setOffscreenPageLimit(4);
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return mFragments[position];
			}

			@Override
			public int getCount() {
				return mFragments.length;
			}
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {            
				//��ֹ����
			}
		};
		mViewPager.setAdapter(mAdapter);
		mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

		// changeFonts(mHeaderLayout, this);
	}

	private void initEvent() {
		// �ײ���ť�¼�
		for (ImageButton b : mButtons) {
			b.setOnClickListener(this);
		}

		// viewPager�����¼�
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
					}

					@Override
					public void onPageScrollStateChanged(int state) {
					}

					@Override
					public void onPageSelected(int position) {
						ShowLog("onPageSelected  " + position);
						/*for (int i = 0; i < mFragments.length; i++) {
							if (position!=i) {
								//mFragments[i];
							}
						}*/
						setSelect(position);
					}

					
				});
	}

	public void setSelect(int position) {
		for (ImageButton b : mButtons) {
			b.setSelected(false);
		}
		mButtons[position].setSelected(true);
		
		switch (position) {
		case 0:
			mHeaderLayout.setTitleAndLeftImageButton("ˮ����",
					R.drawable.base_action_bar_addrees_selector, address,
					homeLeftListener, 4);
			mHeaderLayout.setRightButtonAndText(R.drawable.base_action_bar_search_selector, "");
			mHeaderLayout.setOnRightImageButtonClickListener(mRightButtonSaerch);
			
			break;
		case 1:
			mHeaderLayout.setTitleAndLeftImageButton("����",
					R.drawable.base_action_bar_back_bg_selector, null,
					baseLeftListener, 1);
			mHeaderLayout.setRightButtonAndText(R.drawable.base_action_bar_search_selector, "");
			mHeaderLayout.setOnRightImageButtonClickListener(mRightButtonSaerch);
			break;
		case 2:
			mHeaderLayout.setTitleAndLeftImageButton("��������",
					R.drawable.base_action_bar_back_bg_selector, null,
					baseLeftListener, 1);
			break;
		case 3:
			mHeaderLayout.setTitleAndLeftImageButton("���ﳵ",
					R.drawable.base_action_bar_back_bg_selector, null,
					baseLeftListener, 1);
			break;
		case 4:
			mHeaderLayout.setTitleAndLeftImageButton("��԰",
					R.drawable.base_action_bar_back_bg_selector, null,
					baseLeftListener, 1);
			mHeaderLayout.setRightButtonAndText(R.drawable.base_action_bar_search_selector, "");
			mHeaderLayout.setOnRightImageButtonClickListener(mRightButtonSaerch);
			break;
		default:
			break;
		}
	}
	public void changePage(int currentItem) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(currentItem,true);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.ib_home:
			changePage(0);
			break;
		case R.id.ib_category:
			changePage(1);
			break;
		case R.id.ib_person:
			changePage(2);
			break;
		case R.id.ib_cart:
			changePage(3);
			break;
		case R.id.ib_garden:
			changePage(4);
			break;

		default:
			break;
		}

	}

	// �ٰ�һ���˳�
	private long mPressedTime = 0;
	private int currentTabIndex;

	@Override
	public void onBackPressed() {

		long mNowTime = System.currentTimeMillis();// ��ȡ��һ�ΰ���ʱ��
		if ((mNowTime - mPressedTime) > 2000) {// �Ƚ����ΰ���ʱ���
			mPressedTime = mNowTime;
			ShowToast("�ٰ�һ���˳�����");
		} else {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
			System.exit(0);
		}
	}


	// categoryFragment button����¼�
	public void iconClick(final View v) {
		playHeartbeatAnimation(v);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String categoryBy = (String) v.getTag();
		if (!StringUtils.isEmpty(categoryBy)) {
			startAnimActivityWithData(CategorySelectActivity.class,"categoryBy", categoryBy);
		}
	}
	/*

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO �Զ����ɵķ������
		int Action = event.getAction();
		if (Action == MotionEvent.ACTION_DOWN) {
			playHeartbeatAnimation(v);
		}
		return false;
	}
*/
	
	@SuppressLint("NewApi")
	public class DepthPageTransformer implements ViewPager.PageTransformer {
		private static final float MIN_SCALE = 0.75f;

		@Override
		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();

			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(0);

			} else if (position <= 0) { // [-1,0]
				// Use the default slide transition when moving to the left page
				view.setAlpha(1);
				view.setTranslationX(0);
				view.setScaleX(1);
				view.setScaleY(1);

			} else if (position <= 1) { // (0,1]
				// Fade the page out.
				view.setAlpha(1 - position);

				// Counteract the default slide transition
				view.setTranslationX(pageWidth * -position);

				// Scale the page down (between MIN_SCALE and 1)
				float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
						* (1 - Math.abs(position));
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(0);
			}
		}
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.get.fruit.activity.fragment.CartFragment.CartCallBack#getRightButton
	 * ()
	 */
	@Override
	public HeaderLayout getHeaderLayout() {
		// TODO Auto-generated method stub
		return mHeaderLayout;
	}
}
