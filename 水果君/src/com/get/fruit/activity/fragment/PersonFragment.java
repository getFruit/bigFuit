package com.get.fruit.activity.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.get.fruit.activity.AddFruitActivity;
import com.get.fruit.activity.BaseFragment;
import com.get.fruit.activity.CollectionActivity;
import com.get.fruit.activity.HarvestActivity;
import com.get.fruit.activity.LocationActivity;
import com.get.fruit.activity.MessengerActivity;
import com.get.fruit.activity.OrderListActivity;
import com.get.fruit.bean.User;
import com.get.fruit.util.ImageLoadOptions;
import com.get.fruit.util.PhotoUtil;
import com.get.fruit.util.StringUtils;
import com.get.fruit.view.RoundAngleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PersonFragment extends BaseFragment implements OnClickListener{

	private List<TextView> views;
	private TextView nickname;
	private RoundAngleImageView avatar;
	
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
		
		for (int i = 0; i <views.size(); i++) {
			views.get(i).setOnClickListener(this);
		}
		
		nickname=((TextView) findViewById(R.id.textView11));
		avatar=(RoundAngleImageView)findViewById(R.id.roundAngleImageView1);
		avatar.setOnClickListener(this);

	}

	@Override
	protected void onVisible() {
		// TODO Auto-generated method stub
		super.onVisible();
		nickname.setText(me.getUsername());
		setAvatar();
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
			startAnimActivity(AddFruitActivity.class);
			break;
		case R.id.textView2:
			startAnimActivity(MessengerActivity.class);
			break;
		case R.id.textView3:
			startAnimActivity(OrderListActivity.class);
			break;
		case R.id.textView4:
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
			ShowLog("�ݲ�֧��");
			break;
	}
		
	}

	
	private void inputNickNameDialog() {

        final EditText inputServer = new EditText(getActivity());
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("�����Ƹ�ߵ������").setIcon(R.drawable.person_me).setView(inputServer);
        builder.setNegativeButton("ȡ��", null);
        builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String inputName = inputServer.getText().toString();
				if (StringUtils.isEmpty(inputName)) {
        			ShowToast("����Ϊ��");
					return;
				}
				
				if (StringUtils.getWordCountRegex(inputName)<3) {
					ShowToast("����̫��Ŷ");
					return;
				}
				User user=new User();
				user.setUsername(inputName);
				updateUserData(user, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						nickname.setText(me.getUsername());
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ShowToast("����ʧ��");
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
        builder.setTitle("��������").setIcon(R.drawable.person_me).setView(inputServer);
        builder.setNegativeButton("ȡ��", null);
        builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String inputName = inputServer.getText().toString();
				if (StringUtils.isEmail(inputName)) {
        			ShowToast("������û��");
					return;
				}
				User user=new User();
				user.setEmail(inputName);
				updateUserData(user, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						ShowToast("�޸ĳɹ�");
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ShowToast("����ʧ��");
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
		builder.setTitle("��������").setIcon(R.drawable.person_email).setView(view);
		builder.setNegativeButton("ȡ��", null);
		builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String psdString = psd.getText().toString();
				String psdString2 = email.getText().toString();
				if (StringUtils.isEmpty(psdString)||StringUtils.isEmpty(psdString2)) {
					ShowToast("����Ϊ��");
					return;
				}
				User user=new User();
				updateUserData(user, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						nickname.setText(me.getUsername());
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ShowToast("����ʧ��");
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
				// ԭͼ
				File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date()));
				filePath = file.getAbsolutePath();// ��ȡ��Ƭ�ı���·��
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
		// ����Ч�� �ӵײ�����
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
	boolean isFromCamera = false;// ��������
	int degree = 0;//��ת

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		ShowLog("Fragment >> request: "+requestCode+"   result: "+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case BmobConstants.REQUESTCODE_TAKE_CAMERA://�������
			if (resultCode == getActivity().RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					ShowToast("SD������");
					return;
				}
				isFromCamera = true;
				File file = new File(filePath);
				degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
				Log.i("life", "���պ�ĽǶȣ�" + degree);
				startImageAction(Uri.fromFile(file), 200, 200,
						BmobConstants.REQUESTCODE_PICTURE_CROP, true);
			}
			break;
		case BmobConstants.REQUESTCODE_TAKE_LOCAL:// ������᷵��
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
					ShowToast("SD������");
					return;
				}
				isFromCamera = false;
				uri = data.getData();
				startImageAction(uri, 200, 200,
						BmobConstants.REQUESTCODE_PICTURE_CROP, true);
			} else {
				ShowToast("��Ƭ��ȡʧ��");
			}

			break;
		case BmobConstants.REQUESTCODE_PICTURE_CROP:// �ü�ͷ�񷵻�
			// TODO sent to crop
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			if (data == null) {
				//ȡ��ѡ��
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
	 * ����ü����ͼ��
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
				ShowToast("ͷ���ϴ�ʧ�ܣ�" + arg1);
			}
			
			@Override
			public void onSuccess(String arg0, String arg1, BmobFile arg2) {
				// TODO Auto-generated method stub
				updateUserAvatar(arg2.getFileUrl(getActivity()));
				ShowToast("ͷ���ϴ��ɹ�");
				ShowLog("ͷ��    0: "+arg0+"   1:"+arg1+"   bf:"+arg2.getFilename()+"    getFileUrl: "+arg2.getFileUrl(getActivity())+"   url: "+arg2.getUrl());
				
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
				ShowToast("ͷ����³ɹ���");
				//setAvatar();
			}
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				ShowToast("ͷ�����ʧ�ܣ�" + msg);
				setAvatar();
			}
		});
	}

	private void updateUserData(User user, UpdateListener listener) {
		user.setObjectId(me.getObjectId());
		user.update(getActivity(), listener);
	}

	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
}
