package com.get.fruit.activity.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.UpdateListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.get.fruit.BmobConstants;
import com.get.fruit.R;
import com.get.fruit.activity.BaseFragment;
import com.get.fruit.activity.CollectionActivity;
import com.get.fruit.activity.HarvestActivity;
import com.get.fruit.activity.LocationActivity;
import com.get.fruit.activity.LoginActivity;
import com.get.fruit.activity.MessengerActivity;
import com.get.fruit.activity.OrderListActivity;
import com.get.fruit.activity.SettingsActivity;
import com.get.fruit.bean.User;
import com.get.fruit.util.ImageLoadOptions;
import com.get.fruit.util.PhotoUtil;
import com.get.fruit.util.StringUtils;
import com.get.fruit.view.HeaderLayout.onRightImageButtonClickListener;
import com.get.fruit.view.RoundAngleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PersonFragment extends BaseFragment implements OnClickListener{

	private List<TextView> views;
	private TextView nickname;
	private RoundAngleImageView avatar;
	private onRightImageButtonClickListener mRightImageButtonClickListener=null;
	private FragmentCallBack callBack;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_person, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	//设置删除按钮
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		ShowLog("attach.....cart");
		callBack=(FragmentCallBack) activity;
		super.onAttach(activity);
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
					me.logOut(getActivity());
					startAnimActivity(LoginActivity.class);
					getActivity().finish();
				}
			};
		}
		callBack.getHeaderLayout().setTitleAndRightButton("个人中心",-1,"注销", mRightImageButtonClickListener);
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
		views=new ArrayList<>();
		
		views.add((TextView) findViewById(R.id.textView1));
		views.add((TextView) findViewById(R.id.textView2));
		views.add((TextView) findViewById(R.id.textView3));
		views.add((TextView) findViewById(R.id.textView4));
		views.add((TextView) findViewById(R.id.textView5));
		views.add((TextView) findViewById(R.id.textView6));
		views.add((TextView) findViewById(R.id.textView7));
		views.add((TextView) findViewById(R.id.textView8));
		views.add((TextView) findViewById(R.id.textView9));
		views.add((TextView) findViewById(R.id.textView10));
		
	}

	/** 
	* @Title: initEvent 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void initEvent() {
		for (int i = 0; i <views.size(); i++) {
			views.get(i).setOnClickListener(this);
		}
		
		nickname=((TextView) findViewById(R.id.textView11));
		avatar=(RoundAngleImageView)findViewById(R.id.roundAngleImageView1);
		avatar.setOnClickListener(this);
	}

	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		if (null==me) {
			popSelection();
		}else {
			setRightButtonListener();
			initEvent();
			nickname.setText(me.getNick());
			setAvatar();
		}
	}
	
	public void setAvatar() {
		if (StringUtils.isEmpty(me.getAvatar())) {
			avatar.setImageResource(R.drawable.person_icon);
		}else if (isNetConnected()) {
			ShowLog("ImageLoader ....."+me.getAvatar());
			ImageLoader.getInstance().displayImage(me.getAvatar(), avatar,ImageLoadOptions.getOptions());
		} else{
			ImageLoader.getInstance().displayImage("file://"+path, avatar,ImageLoadOptions.getOptions());
		}
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.roundAngleImageView1:
			showAvatarPop();
			break;
		case R.id.textView1:
			startAnimActivity(SettingsActivity.class);
			break;
		case R.id.textView2:
			ShowToast("稍后开通");
			startAnimActivity(MessengerActivity.class);
			break;
		case R.id.textView3:
			startAnimActivity(OrderListActivity.class);
			break;
		case R.id.textView4:
			ShowToast("稍后开通");
			startAnimActivity(HarvestActivity.class);
			break;
		case R.id.textView5:
			startAnimActivity(CollectionActivity.class);
			break;
		case R.id.textView6:
			inputNickNameDialog();
			break;
		case R.id.textView7:
			inputEmailDialog();
			break;
		case R.id.textView9:
			Intent intent=new Intent(getActivity(),LocationActivity.class);
			getActivity().startActivityForResult(intent, BmobConstants.REQUESTCODE_FROM_FRAGMENT_FORADDRESS);
			break;
		default:
			ShowLog("暂不支持");
			break;
	}
		
	}

	
	private void inputNickNameDialog() {

        final EditText inputServer = new EditText(getActivity());
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("换个逼格高点的名儿").setIcon(R.drawable.person_me).setView(inputServer);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				final String inputName = inputServer.getText().toString();
				if (StringUtils.isEmpty(inputName)) {
        			ShowToast("不能为空");
					return;
				}
				
				if (StringUtils.getWordCountRegex(inputName)<3) {
					ShowToast("不能太短哦");
					return;
				}
				User user=new User();
				user.setNick(inputName);
				updateUserData(user, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						nickname.setText(me.getNick());
						ShowToast(inputName);
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ShowToast("更新失败");
					}
				});
			}
		});
        builder.show();
    }
	
	private void inputEmailDialog() {
		
		final EditText inputServer = new EditText(getActivity());
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("换个邮箱").setIcon(R.drawable.person_me).setView(inputServer);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String inputName = inputServer.getText().toString();
				if (StringUtils.isEmail(inputName)) {
        			ShowToast("你相信没错咯");
					return;
				}
				User user=new User();
				user.setEmail(inputName);
				updateUserData(user, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						ShowToast("修改成功");
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ShowToast("更新失败");
					}
				});
			}
		});
        builder.show();
    }
	
	
	private void inputPasswordDialog() {
		
		final View view = LayoutInflater.from(getActivity()).inflate(R.layout.include_modify_password,
				null);
		view.setFocusable(true);
		final EditText psd=(EditText) view.findViewById(R.id.editText_password);
		final EditText email=(EditText) findViewById(R.id.editText_password2);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("换个邮箱").setIcon(R.drawable.person_email).setView(view);
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String psdString = psd.getText().toString();
				String psdString2 = email.getText().toString();
				if (StringUtils.isEmpty(psdString)||StringUtils.isEmpty(psdString2)) {
					ShowToast("不能为空");
					return;
				}
				User user=new User();
				updateUserData(user, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						nickname.setText(me.getNick());
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ShowToast("更新失败");
					}
				});
			}
		});
		builder.show();
	}
	
	
	RelativeLayout layout_choose;
	RelativeLayout layout_photo;
	PopupWindow avatorPop;
	public String filePath = "";
	private View layout_all;
	private void showAvatarPop() {
		layout_all=findViewById(R.id.all_view);
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.include_pop_showavator,
				null);
		layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
		layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		layout_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				avatorPop.dismiss();
				// TODO Auto-generated method stub
				layout_choose.setBackgroundColor(getResources().getColor(
						R.color.base_color_white));
				layout_photo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				File dir = new File(BmobConstants.MyFruitDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 原图
				File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date()));
				filePath = file.getAbsolutePath();// 获取相片的保存路径
				Uri imageUri = Uri.fromFile(file);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				getActivity().startActivityForResult(intent,
						BmobConstants.REQUESTCODE_TAKE_CAMERA);
			}
		});
		layout_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				avatorPop.dismiss();
				layout_photo.setBackgroundColor(getResources().getColor(
						R.color.base_color_white));
				layout_choose.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				/*Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				*/
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		        intent.setType("image/*");
				getActivity().startActivityForResult(intent,
						BmobConstants.REQUESTCODE_TAKE_LOCAL);
			}
		});

		//here
		avatorPop = new PopupWindow(view);
		avatorPop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					avatorPop.dismiss();
					return true;
				}
				return false;
			}
		});

		avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		avatorPop.setTouchable(true);
		avatorPop.setFocusable(true);
		avatorPop.setOutsideTouchable(true);
		avatorPop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从底部弹起
		avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
		avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
	}
	
	/**
	 * @Title: startImageAction
	 * @return void
	 * @throws
	 */
	private void startImageAction(Uri uri, int outputX, int outputY,
			int requestCode, boolean isCrop) {
		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		getActivity().startActivityForResult(intent, requestCode);
	}

	Bitmap newBitmap;
	boolean isFromCamera = false;// 区分拍照
	int degree = 0;//旋转

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		ShowLog("Fragment >> request: "+requestCode+"   result: "+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case BmobConstants.REQUESTCODE_TAKE_CAMERA://相机返回
			if (resultCode == getActivity().RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					ShowToast("SD不可用");
					return;
				}
				isFromCamera = true;
				File file = new File(filePath);
				degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
				Log.i("life", "拍照后的角度：" + degree);
				startImageAction(Uri.fromFile(file), 200, 200,
						BmobConstants.REQUESTCODE_PICTURE_CROP, true);
			}
			break;
		case BmobConstants.REQUESTCODE_TAKE_LOCAL:// 本地相册返回
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			Uri uri = null;
			if (data == null) {
				return;
			}
			if (resultCode == getActivity().RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					ShowToast("SD不可用");
					return;
				}
				isFromCamera = false;
				uri = data.getData();
				startImageAction(uri, 200, 200,
						BmobConstants.REQUESTCODE_PICTURE_CROP, true);
			} else {
				ShowToast("照片获取失败");
			}

			break;
		case BmobConstants.REQUESTCODE_PICTURE_CROP:// 裁剪头像返回
			// TODO sent to crop
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			if (data == null) {
				//取消选择
				return;
			} else {
				saveCropedPicturer(data);
			}
			break;
			
		default:
			break;

		}
	}
	
	/**
	 * 处理裁剪后的图像
	 * 
	 * @param data
	 */
	String path;
	private void saveCropedPicturer(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			if (bitmap != null) {
				String filename = "myavatar.jpg";
				PhotoUtil.saveBitmap(BmobConstants.MyTempDir, filename,bitmap, true);
				ShowLog(BmobConstants.MyTempDir+filename);
				path=BmobConstants.MyTempDir+filename;
				uploadAvatar();
				if (isFromCamera && degree != 0) {
					bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
				}
				BmobRelation bmobRelation=new BmobRelation();
				avatar.setImageBitmap(bitmap);
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
	}
	
	private void uploadAvatar() {
		BmobProFile.getInstance(getActivity()).upload(path, new UploadListener() {
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("头像上传失败：" + arg1);
			}
			
			@Override
			public void onSuccess(String arg0, String arg1, BmobFile arg2) {
				// TODO Auto-generated method stub
				updateUserAvatar(arg2.getFileUrl(getActivity()));
				ShowToast("头像上传成功");
				ShowLog("头像    0: "+arg0+"   1:"+arg1+"   bf:"+arg2.getFilename()+"    getFileUrl: "+arg2.getFileUrl(getActivity())+"   url: "+arg2.getUrl());
				
			}
			
			@Override
			public void onProgress(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void updateUserAvatar(final String url) {
		User u = new User();
		u.setAvatar(url);
		updateUserData(u, new UpdateListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ShowToast("头像更新成功！");
				//setAvatar();
			}
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				ShowToast("头像更新失败：" + msg);
				setAvatar();
			}
		});
	}

	private void updateUserData(User user, UpdateListener listener) {
		user.setObjectId(me.getObjectId());
		user.update(getActivity(), listener);
	}

	
}
