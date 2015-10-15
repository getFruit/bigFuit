package com.get.fruit.big.utils;


import cn.bmob.im.util.BmobLog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


@SuppressLint("CommitPrefEdits")
public class SharePreferenceUtil {
	private SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor editor;

	public SharePreferenceUtil(Context context, String name) {
		mSharedPreferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
	}

	private String SHARED_KEY_NOTIFY = "shared_key_notify";
	private String SHARED_KEY_VOICE = "shared_key_sound";
	private String SHARED_KEY_VIBRATE = "shared_key_vibrate";
	private String SHARED_KEY_AUTOUPDATE = "shared_key_autoupdate";

	
	public boolean saveValue(String key, String value) {
		return editor.putString(key, value).commit();
	}

	public String getValue(String key) {
		return mSharedPreferences.getString(key, "");
	}
	public static void ShowLog(String msg){
		BmobLog.i(msg);
	}
	
	public boolean isAllowPushNotify() {
		return mSharedPreferences.getBoolean(SHARED_KEY_NOTIFY, true);
	}

	public void setPushNotifyEnable(boolean isChecked) {
		editor.putBoolean(SHARED_KEY_NOTIFY, isChecked);
		editor.commit();
	}

	public boolean isAllowVoice() {
		return mSharedPreferences.getBoolean(SHARED_KEY_VOICE, true);
	}

	public void setAllowAutoUpdateEnable(boolean isChecked) {
		editor.putBoolean(SHARED_KEY_AUTOUPDATE, isChecked);
		editor.commit();
	}
	public boolean isAllowAutoUpdate() {
		return mSharedPreferences.getBoolean(SHARED_KEY_AUTOUPDATE, true);
	}
	
	public void setAllowVoiceEnable(boolean isChecked) {
		editor.putBoolean(SHARED_KEY_VOICE, isChecked);
		editor.commit();
	}

	public boolean isAllowVibrate() {
		return mSharedPreferences.getBoolean(SHARED_KEY_VIBRATE, true);
	}

	public void setAllowVibrateEnable(boolean isChecked) {
		editor.putBoolean(SHARED_KEY_VIBRATE, isChecked);
		editor.commit();
	}

	
}
