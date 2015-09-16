package com.get.fruit.activity;

import java.io.Serializable;
import java.util.Map;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.util.BmobLog;

import com.get.fruit.App;
import com.get.fruit.R;
import com.get.fruit.bean.User;
import com.get.fruit.util.CommonUtils;
import com.get.fruit.view.HeaderLayout;
import com.get.fruit.view.HeaderLayout.HeaderStyle;
import com.get.fruit.view.HeaderLayout.onLeftImageButtonClickListener;
import com.get.fruit.view.HeaderLayout.onRightImageButtonClickListener;



/** 基类
  * @ClassName: BaseActivity
  * @Description: TODO
  * @author smile
  * @date 2014-6-13 下午5:05:38
  */
public class BaseActivity extends FragmentActivity {

	BmobUserManager userManager;
	BmobChatManager manager;
	User me;
	App mApplication;
	protected HeaderLayout mHeaderLayout;
	
	protected int mScreenWidth;
	protected int mScreenHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userManager = BmobUserManager.getInstance(this);
		manager = BmobChatManager.getInstance(this);
		mApplication = App.getInstance();
		me=mApplication.getCurrentUser();
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
	}

	Toast mToast;

	public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mToast == null) {
						mToast = Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_LONG);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});
			
		}
	}

	public void ShowToast(final int resId) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mToast == null) {
					mToast = Toast.makeText(BaseActivity.this.getApplicationContext(), resId,
							Toast.LENGTH_LONG);
				} else {
					mToast.setText(resId);
				}
				mToast.show();
			}
		});
	}

	/** 打Log
	  * ShowLog
	  * @return void
	  * @throws
	  */
	public void ShowLog(String msg){
		BmobLog.i(msg);
	}
	
	/**
	 * 只有title initTopBarLayoutByTitle
	 * @Title: initTopBarLayoutByTitle
	 * @throws
	 */
	
	
	
	public void setTopBarTitle(String titleName) {
		mHeaderLayout.setDefaultTitle(titleName);
	}
	
	
	public void initTopBarForOnlyTitle(String titleName) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle(titleName);
	}
	
	/** 
	* @Title: initTopBarForBoth
	* @Description: direction 1234->left top right buttom
	* @param 
	* @return void
	* @throws 
	*/
	public void initTopBarForBoth(String titleName, int leftDrawableId,CharSequence leftText,onLeftImageButtonClickListener leftlistener,int rightDrawableId,String text,
			onRightImageButtonClickListener rightlistener,int direct) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName, leftDrawableId,leftText,leftlistener,direct);
		mHeaderLayout.setTitleAndRightButton(titleName, rightDrawableId,text,rightlistener);
	}
	/**
	 * 初始化标题栏-带搜索框布局
	 * @return void
	 * @throws
	 */
	public void initTopBarForMiddleView(String titleName, int leftDrawableId,CharSequence leftText,onLeftImageButtonClickListener leftlistener,int rightDrawableId,String text,
			onRightImageButtonClickListener rightlistener,int direct) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.LEFT_MIDDLE_RIGHT);
		mHeaderLayout.setTitleAndLeftImageButton(titleName, leftDrawableId,leftText,leftlistener,direct);
		mHeaderLayout.setTitleAndRightButton(titleName, rightDrawableId,text,rightlistener);
	}
	

	/**
	 * 只有左边按钮和Title initTopBarLayout
	 * 
	 * @throws
	 */
	public void initTopBarForLeft(String titleName) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_LIFT_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				null,
				new OnLeftClickListenerFinishMe(),
				1);
	}
	
	
	// 左边按钮的点击事件
	public class OnLeftClickListenerFinishMe implements
			onLeftImageButtonClickListener {

		@Override
		public void onClick() {
			finish();
		}
	}
	
	public void startAnimActivityForResult(Intent intent) {
		this.startActivityForResult(intent, 0);
	}
	
	public void startAnimActivity(Class<?> cla) {
		this.startActivity(new Intent(this, cla));
	}
	
	public void startAnimActivity(Intent intent) {
		this.startActivity(intent);
	}

	public void startAnimActivityWithData(Class<?> cla,String key, Serializable value) {
		Intent intent=new Intent(this, cla);
		intent.putExtra(key, value);
		this.startActivity(intent);
	}
	public void startAnimActivityToFragment(Class<?> cla,int to) {
		Intent intent=new Intent(this, cla);
		intent.putExtra("to", to);
		this.startActivity(intent);
	}
	
	public void changeFonts(ViewGroup root,Activity activity){
		ShowLog("set changeFonts()");
		Typeface tf=Typeface.createFromAsset(activity.getAssets(), "fonts/DroidSansFallback.ttf");
		for(int i=0;i<root.getChildCount();i++){
			View view=root.getChildAt(i);
			if(view instanceof TextView){
				((TextView) view).setTypeface(tf);
			}else if (view instanceof Button) {
				((Button) view).setTypeface(tf);
			}else if (view instanceof EditText) {
				((EditText) view).setTypeface(tf);
			}else if (view instanceof ViewGroup) {
				changeFonts((ViewGroup)view, activity);
				
			}
		}
	}
	
	public boolean isNetConnected() {
		boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
		return isNetConnected;
	}
	
	
	// 按钮模拟心脏跳动
	public void playHeartbeatAnimation(final View imageView) {
			AnimationSet animationSet = new AnimationSet(true);
			animationSet.addAnimation(new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
					0.5f));
			animationSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));

			animationSet.setDuration(200);
			animationSet.setInterpolator(new AccelerateInterpolator());
			animationSet.setFillAfter(true);

			animationSet.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					AnimationSet animationSet = new AnimationSet(true);
					animationSet.addAnimation(new ScaleAnimation(0.5f, 1.0f, 0.5f,
							1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f));
					animationSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));

					animationSet.setDuration(600);
					animationSet.setInterpolator(new DecelerateInterpolator());
					animationSet.setFillAfter(false);

					// 实现心跳的View
					imageView.startAnimation(animationSet);
				}
			});

			// 实现心跳的View
			imageView.startAnimation(animationSet);
		}

	
	
	
	
	
	
	
	
	
	
/*	// 按钮模拟心脏跳动
	public void playHeartbeatAnimation(final View imageView) {
			if (animationSet==null) {
				 initAnimation();
			}
			// 实现心跳的View
			imageView.startAnimation(animationSet);
		}

	AnimationSet animationSet =null;
	public AnimationSet initAnimation() {
		animationSet = new AnimationSet(true);
		animationSet.addAnimation(new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f));
		animationSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));

		animationSet.setDuration(200);
		animationSet.setInterpolator(new AccelerateInterpolator());
		animationSet.setFillAfter(true);

		animationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				AnimationSet animationSet = new AnimationSet(true);
				animationSet.addAnimation(new ScaleAnimation(0.5f, 1.0f, 0.5f,
						1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f));
				animationSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));

				animationSet.setDuration(600);
				animationSet.setInterpolator(new DecelerateInterpolator());
				animationSet.setFillAfter(false);

				// 实现心跳的View
				//imageView.startAnimation(animationSet);
			}
		});
		return animationSet;
	}*/
	
}
