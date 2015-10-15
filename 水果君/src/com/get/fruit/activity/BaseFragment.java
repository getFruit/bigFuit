package com.get.fruit.activity;


import java.io.Serializable;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;
import c.system;
import cn.bmob.im.BmobUserManager;

import com.get.fruit.App;
import com.get.fruit.R;
import com.get.fruit.bean.User;
import com.get.fruit.util.CommonUtils;
import com.get.fruit.util.TimeUtil;
import com.get.fruit.view.HeaderLayout;

/**
 * Fragmenet ����
 */
public abstract class BaseFragment extends Fragment {

	protected View contentView;

	public LayoutInflater mInflater;

	private Handler handler = new Handler();
	public Runnable LAZYLOAD;
	protected BmobUserManager userManager;
	protected User me;
	public void runOnWorkThread(Runnable action) {
		new Thread(action).start();
	}

	public void runOnUiThread(Runnable action) {
		handler.post(action);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		userManager = BmobUserManager.getInstance(getActivity());
		me=App.mInstance.getCurrentUser();
		mInflater = LayoutInflater.from(getActivity());
	}

	public BaseFragment() {

	}

	public interface FragmentCallBack{
		public HeaderLayout getHeaderLayout();
	}

	
	public View findViewById(int paramInt) {
		return getView().findViewById(paramInt);
	}
	
	Toast mToast;

	public void ShowToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}

	public void ShowToast(int text) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}

	public boolean isNetConnected() {
		boolean isNetConnected = CommonUtils.isNetworkAvailable(getActivity());
		return isNetConnected;
	}

	public void startAnimActivityWithData(Class<?> cla,String key, Serializable value) {
		Intent intent=new Intent(getActivity(), cla);
		intent.putExtra(key, value);
		this.startActivity(intent);
	}

	public void startAnimActivity(Class<?> cla) {
		this.startActivity(new Intent(getActivity(), cla));
	}
	public void startAnimActivity(Intent intent) {
		this.startActivity(intent);
	}

	// ��ťģ����������
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

				// ʵ��������View
				imageView.startAnimation(animationSet);
			}
		});

		// ʵ��������View
		imageView.startAnimation(animationSet);
	}

	/**
	 * ��Log ShowLog
	 * 
	 * @return void
	 * @throws
	 */
	public void ShowLog(String msg) {
		Log.i("fruit", msg);
	}
	
	public void popSelection(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("��δ��¼������զ�Σ�").setIcon(R.drawable.person_me);
        builder.setNegativeButton("����", null);
        builder.setPositiveButton("��¼",new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				startAnimActivity(LoginActivity.class);
				getActivity().finish();
			}});
		builder.show();
	}
	
	
	
	
	 public View getContentView() {
		return contentView;
	}

	public void setContentView(View contentView) {
		this.contentView = contentView;
	}

	public Runnable getLAZYLOAD() {
		if (null==this.LAZYLOAD) {
			LAZYLOAD=new Runnable() {
				
				@Override
				public void run() {
					lazyLoad();
				}
			};
		}
		return LAZYLOAD;
	}

	/** Fragment��ǰ״̬�Ƿ�ɼ� */
	
    protected boolean isVisible;
     
     
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(getUserVisibleHint()) {
            isVisible = true;
            onInvisible();
        } else {
            isVisible = false;
            onVisible();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
     
     
    /**
     * �ɼ�
     */
    long post;
    protected void onVisible() {
    	post=new Date().getTime();
    	ShowLog("onVisible"+post);
    	handler.postDelayed(getLAZYLOAD(), 800);
    }
     
     

	/**
     * ���ɼ�
     */
    long cancel;
    protected void onInvisible() {
    	if (null!=LAZYLOAD) {
    		cancel=new Date().getTime();
    		handler.removeCallbacks(getLAZYLOAD());
    		ShowLog(cancel-post+"handler.removeCallbacks"+cancel);
		}
    }

     
    /** 
     * �ӳټ���
     * ���������д�˷���
     */
    protected abstract void lazyLoad();


}
