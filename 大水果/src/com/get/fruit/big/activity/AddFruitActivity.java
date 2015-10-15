package com.get.fruit.big.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.get.fruit.big.App;
import com.get.fruit.big.BmobConstants;
import com.get.fruit.big.R;
import com.get.fruit.big.adapter.util.BaseAdapterHelper;
import com.get.fruit.big.adapter.util.QuickAdapter;
import com.get.fruit.big.bean.Category;
import com.get.fruit.big.bean.Fruit;
import com.get.fruit.big.bean.Fruit.Color;
import com.get.fruit.big.bean.Fruit.Season;
import com.get.fruit.big.bean.FruitShop;
import com.get.fruit.big.utils.PhotoUtil;
import com.get.fruit.big.utils.StringUtils;
import com.google.gson.Gson;

public class AddFruitActivity extends BaseActivity implements OnClickListener {

	//private TextView addCategoryTextView,addSeasonTextView,addOriginTextView,addColorTextView;
	//private ImageButton addPicChoose,addCategoryChoose,addSeasonChoose,addOriginCHoose,addColorChoose;
	private TextView addTextViews[]=new TextView[4];
	private EditText addPriceEditText,addCounEditText,addDescribeEditText,addNameEditText;
	private ImageButton chooseButtons[]=new ImageButton[4];
	private Button addCommit;
	private GridView mGridView;
	private QuickAdapter<Bitmap> mQuickAdapter;
	private List<String> pics=new LinkedList();
	private ScrollView parentScroll;
	private ScrollView childScroll;
	public static int currentClickedItem=0;
	private static Intent intent;
	private Fruit fruit;
	private static FruitShop shop=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfruit);
		
		initView();
		initData();
		
	}
	/** 
	* @Title: initData 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void initData() {
		
		fruit=new Fruit(shop);
	}
	/** 
	* @Title: initView 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	private void initView() {
		layout_all = (LinearLayout) findViewById(R.id.layout_all);
		
		addCommit=(Button) findViewById(R.id.add_commit);
		addCommit.setOnClickListener(this);
		addCounEditText=(EditText) findViewById(R.id.add_count);
		addPriceEditText=(EditText) findViewById(R.id.add_price);
		addDescribeEditText=(EditText) findViewById(R.id.add_describe);
		addNameEditText=(EditText) findViewById(R.id.add_name);
		chooseButtons[0]=(ImageButton) findViewById(R.id.add_category_choose);
		chooseButtons[1]=(ImageButton) findViewById(R.id.add_season_choose);
		chooseButtons[2]=(ImageButton) findViewById(R.id.add_origin_choose);
		chooseButtons[3]=(ImageButton) findViewById(R.id.add_color_choose);
		
		addTextViews[0]=(TextView) findViewById(R.id.add_category);
		addTextViews[1]=(TextView) findViewById(R.id.add_season);
		addTextViews[2]=(TextView) findViewById(R.id.add_origin);
		addTextViews[3]=(TextView) findViewById(R.id.add_color);
		for(View v:chooseButtons){
			v.setOnClickListener(this);
		}
		
		mGridView =(GridView) findViewById(R.id.add_gridView);
		mGridView.setAdapter(mQuickAdapter=new QuickAdapter<Bitmap>(AddFruitActivity.this,R.layout.item_add_gridview ) {

			@Override
			protected void convert(final BaseAdapterHelper helper, final Bitmap item) {
				helper.setImageBitmap(R.id.item_image, item);

				//添加按钮
				helper.setOnClickListener(R.id.item_image, new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						currentClickedItem=helper.getPosition();
						showAvatarPop();
					}
				});
				
				//长按事件
				helper.setOnLongClickListener(R.id.item_image, new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View arg0) {
						if (helper.getPosition()==mQuickAdapter.getCount()-1) {
							return false;
						}
						mQuickAdapter.remove(item);
						pics.remove(helper.getPosition());
						return false;
					}
					
					
					
					
				});
				
			
			}
		});
		mQuickAdapter.add(BitmapFactory.decodeResource(getResources(), R.id.item_image));
	}
	
	
	
	
	RelativeLayout layout_choose;
	RelativeLayout layout_photo;
	PopupWindow avatorPop;
	public String filePath = "";
	private View layout_all;
	private void showAvatarPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.include_pop_showavator,
				null);
		layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
		layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		layout_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowLog("点击拍照");
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
				startActivityForResult(intent,
						BmobConstants.REQUESTCODE_TAKE_CAMERA);
			}
		});
		layout_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ShowLog("点击相册");
				layout_photo.setBackgroundColor(getResources().getColor(
						R.color.base_color_white));
				layout_choose.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent,
						BmobConstants.REQUESTCODE_TAKE_LOCAL);
			}
		});

		avatorPop = new PopupWindow(view, mScreenWidth, 600);
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
		startActivityForResult(intent, requestCode);
	}

	Bitmap newBitmap;
	boolean isFromCamera = false;// 区分拍照
	int degree = 0;//旋转

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		ShowLog("MainAc >> request: "+requestCode+"   result: "+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case BmobConstants.REQUESTCODE_TAKE_CAMERA://相机返回
			if (resultCode == RESULT_OK) {
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
			if (resultCode == RESULT_OK) {
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
			
		case BmobConstants.REQUESTCODE_FROM_ADDFRUIT_FORCATEGORY:
			if (resultCode == RESULT_OK) {
				if (data==null) {
					break;
				}
				
				switch (data.getIntExtra("witch",-1)) {
				case 0:
					addTextViews[0].setText(data.getStringExtra("value"));
					fruit.setCategory((Category) data.getSerializableExtra("value"));
					break;
				case 1:
					addTextViews[1].setText(data.getStringExtra("value"));
					fruit.setSeason((Season) data.getSerializableExtra("value"));
					break;
				case 3:
					addTextViews[3].setText(data.getStringExtra("value"));
					fruit.setColor((Color) data.getSerializableExtra("value"));
					break;

				default:
					break;
				}
			}
			
			break;
		case BmobConstants.REQUESTCODE_FROM_ADDFRUIT_FORADDRESS:
			if (resultCode == RESULT_OK) {
				if (data==null) {
					break;
				}
				addTextViews[2].setText(data.getStringExtra("address"));
				fruit.setOrigin((String) data.getStringExtra("fullAddress"));
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
			Log.i("life", "avatar - bitmap = " + bitmap);
			if (bitmap != null) {
				String filename = new SimpleDateFormat("yyMMddHHmmss").format(new Date())+".jpg";
				PhotoUtil.saveBitmap(BmobConstants.MyTempDir, filename,bitmap, true);
				ShowLog(BmobConstants.MyTempDir+filename);
				path=BmobConstants.MyTempDir+filename;
				pics.add(currentClickedItem,path);
				if (isFromCamera && degree != 0) {
					bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
				}
				if (currentClickedItem!=mQuickAdapter.getCount()-1) {
					
					mQuickAdapter.addTo(currentClickedItem, bitmap);
					mQuickAdapter.remove(currentClickedItem+1);
				}else {
					mQuickAdapter.addTo(mQuickAdapter.getCount()-1, bitmap);
				}
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		intent=new Intent(AddFruitActivity.this,CategorySelectActivity.class);
		switch (arg0.getId()) {
		case R.id.add_origin_choose:
			startActivityForResult(new Intent(AddFruitActivity.this,LocationActivity.class), BmobConstants.REQUESTCODE_FROM_ADDFRUIT_FORADDRESS);
			return;
		case R.id.add_category_choose:
			intent.putExtra("for", "种类");
			startActivityForResult(intent, BmobConstants.REQUESTCODE_FROM_ADDFRUIT_FORCATEGORY);
			break;
		case R.id.add_season_choose:
			intent.putExtra("for", "季节");
			startActivityForResult(intent, BmobConstants.REQUESTCODE_FROM_ADDFRUIT_FORCATEGORY);
			break;
		case R.id.add_color_choose:
			intent.putExtra("for", "颜色");
			startActivityForResult(intent, BmobConstants.REQUESTCODE_FROM_ADDFRUIT_FORCATEGORY);
			break;

		case R.id.add_commit:
			commt();
		default:
			break;
		}
	}
	/** 
	* @Title: commt 
	* @Description: TODO
	* @param 
	* @return void
	* @throws 
	*/
	int i=0;
	private void commt() {
		uploadSuccessed=1;
		if (fruit.getCategory()==null) {
			ShowToast("请选择种类");
			return;
		}
		
		try {
			Double price=Double.valueOf(addPriceEditText.getText().toString());
			double count =Double.valueOf(addCounEditText.getText().toString());
			fruit.setPrice(price);
			fruit.setCount(count);
		} catch (Exception e) {
			ShowToast("价格和数量输入不合法");
			return;
		}
		
		String[] ssStrings=new String[]{fruit.getColor().toString(),fruit.getSeason().toString(),fruit.getOrigin()};
		if(StringUtils.isEmptys(ssStrings)){
			ShowToast("请完善信息");
			return;
		}
		
		String name=addNameEditText.getText().toString();
		fruit.setName(StringUtils.isEmpty(name)?fruit.getCategory().getCategoryName():name);
		fruit.setDescribe(addDescribeEditText.getText().toString());
		
		
		if (pics.size()<=0) {
			ShowToast("至少添加一张主图");
			return;
		}
		addCommit.setClickable(false);
		//上传
		uploadPics();
		
		
	}
	public void saveFruit() {
		ShowLog("save....");
		if ((shop=App.getMyshop())==null) {
			ShowToast("商店未初始化");
			return;
		}
		fruit.setShop(shop);
		ShowLog("fruit:"+new Gson().toJson(fruit, Fruit.class));
		fruit.save(this, new SaveListener() {
		    @Override
		    public void onSuccess() {
		        // TODO Auto-generated method stub
		    	ShowToast("添加成功");
		    	ShowLog("添加成功");
		    	//重置视图，数据
				resetViewAndData(true);
		    }
		    @Override
		    public void onFailure(int code, String msg) {
		        // TODO Auto-generated method stub
		    	ShowLog("添加失败:"+msg);
		    	ShowToast("添加失败:"+msg);
		    	//重置视图，数据
				resetViewAndData(false);
		    }
		});
	}
	public void resetViewAndData(boolean successed) {
		if(!successed)
			return;
		fruit=new Fruit(shop);
		addCommit.setClickable(true);
		mQuickAdapter.clear();
		mQuickAdapter.add(BitmapFactory.decodeResource(getResources(), R.drawable.add_upload));
		pics.clear();
		
		addCounEditText.setText("");
		addPriceEditText.setText("");
		addNameEditText.setText("");
		
	}
	
	
	final  ProgressDialog progress= new ProgressDialog(AddFruitActivity.this);;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			progress.setMessage(msg.what+" %");
		};
	};
	static int uploadSuccessed=0;
	private void uploadPics() {
		ShowLog(" 正在上传...");
		progress.setMessage("0%");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		BmobProFile.getInstance(this).uploadBatch(pics.toArray(new String[pics.size()]), new UploadBatchListener() {

            @SuppressLint("NewApi")
			@Override
            public void onSuccess(boolean isFinish,String[] fileNames,String[] urls,BmobFile[] files) {
            	fruit.setPicture(files[0]);
            	fruit.setPictures(fileNames);
            	
            	uploadSuccessed=101;
            	ShowLog("批量上传成功");
            	progress.dismiss();
            	saveFruit();
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
            	uploadSuccessed=totalPercent;
            	handler.sendEmptyMessage(uploadSuccessed);
                ShowLog("onProgress :"+curIndex+" / "+total+"----"+totalPercent+"%");
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
            	progress.dismiss();
            	uploadSuccessed=-1;
            	ShowLog("批量上传出错："+statuscode+"--"+errormsg);
            	//重置视图，数据
        		resetViewAndData(false);
            }
        });
		
		

    }

}
