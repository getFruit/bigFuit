package com.get.fruit.big.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
public class StringUtils {

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	
	public static boolean inputValidate(EditText view,String regString){
		String input=view.getText().toString();
		if( isEmpty(input)) return false;
		return matches(input, regString);
		
	}
	/**
	 * 规范内容长度
	 * 
	 * @param s
	 *            输入的字符
	 * @return
	 */
 	public static int getWordCountRegex(String s) {
		s = s.replaceAll("[^\\x00-\\xff]", "**");
		int length = s.length();
		return length;
	}
	
	/**
	 * 校验整数
	 * @param text
	 * @return
	 */
	protected static boolean isNumeric(String text) {
		return TextUtils.isDigitsOnly(text);
	}
	
	protected static boolean isAlphaNumeric(String text) {
		return matches(text, "[a-zA-Z0-9 \\./-]*");
	}
	
	
	protected static boolean isDomain(String text) {
		return matches(text, Build.VERSION.SDK_INT>=8?Patterns.DOMAIN_NAME:Pattern.compile(".*"));
	}
	
	protected static boolean isIpAddress(String text) {
		return matches(text, Build.VERSION.SDK_INT>=8?Patterns.DOMAIN_NAME:Pattern.compile(
	            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
	            + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
	            + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
	            + "|[1-9][0-9]|[0-9]))"));
	}
	
	protected static boolean isWebUrl(String text) {
		//TODO: Fix the pattern for api level < 8
		return matches(text, Build.VERSION.SDK_INT>=8?Patterns.WEB_URL:Pattern.compile(".*"));
	}
	
	
	protected static boolean find(String text,String regex) {
		return Pattern.compile(regex).matcher(text).find();
	}
	
	protected static boolean matches(String text,String regex) {
		Pattern pattern = Pattern.compile(".*");
		return pattern.matcher(text).matches();
	}
	
	protected static boolean matches(String text,Pattern pattern) {
		return pattern.matcher(text).matches();
	}





	    private final static Pattern emailer = Pattern
	            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	    private final static Pattern phone = Pattern
	            .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

	    /**
	     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	     */
	    public static boolean isEmpty(CharSequence input) {
	        if (input == null || "".equals(input))
	            return true;

	        for (int i = 0; i < input.length(); i++) {
	            char c = input.charAt(i);
	            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
	                return false;
	            }
	        }
	        return true;
	    }
	    public static boolean isEmptys(CharSequence[] inputs) {
	    	for(CharSequence input:inputs){
	    		if(StringUtils.isEmpty(input))
	    			return true;
	    	}
	    	return false;
	    }

	    /**
	     * 判断是不是一个合法的电子邮件地址
	     */
	    public static boolean isEmail(CharSequence email) {
	        if (isEmpty(email))
	            return false;
	        return emailer.matcher(email).matches();
	    }

	    /**
	     * 判断是不是一个合法的手机号码
	     */
	    public static boolean isPhone(CharSequence phoneNum) {
	        if (isEmpty(phoneNum))
	            return false;
	        return phone.matcher(phoneNum).matches();
	    }

	    /**
	     * 返回当前系统时间
	     */
	    public static String getDataTime(String format) {
	        SimpleDateFormat df = new SimpleDateFormat(format);
	        return df.format(new Date());
	    }

	    /**
	     * 字符串转整数
	     * 
	     * @param str
	     * @param defValue
	     * @return
	     */
	    public static int toInt(String str, int defValue) {
	        try {
	            return Integer.parseInt(str);
	        } catch (Exception e) {
	        }
	        return defValue;
	    }

	    /**
	     * 对象转整
	     * 
	     * @param obj
	     * @return 转换异常返回 0
	     */
	    public static int toInt(Object obj) {
	        if (obj == null)
	            return 0;
	        return toInt(obj.toString(), 0);
	    }

	    /**
	     * String转long
	     * 
	     * @param obj
	     * @return 转换异常返回 0
	     */
	    public static long toLong(String obj) {
	        try {
	            return Long.parseLong(obj);
	        } catch (Exception e) {
	        }
	        return 0;
	    }

	    /**
	     * String转double
	     * 
	     * @param obj
	     * @return 转换异常返回 0
	     */
	    public static double toDouble(String obj) {
	        try {
	            return Double.parseDouble(obj);
	        } catch (Exception e) {
	        }
	        return 0D;
	    }

	    /**
	     * 字符串转布尔
	     * 
	     * @param b
	     * @return 转换异常返回 false
	     */
	    public static boolean toBool(String b) {
	        try {
	            return Boolean.parseBoolean(b);
	        } catch (Exception e) {
	        }
	        return false;
	    }

	    /**
	     * 判断一个字符串是不是数字
	     */
	    public static boolean isNumber(CharSequence str) {
	        try {
	            Integer.parseInt(str.toString());
	        } catch (Exception e) {
	            return false;
	        }
	        return true;
	    }

	    /**
	     * byte[]数组转换为16进制的字符串。
	     * 
	     * @param data
	     *            要转换的字节数组。
	     * @return 转换后的结果。
	     */
	    public static final String byteArrayToHexString(byte[] data) {
	        StringBuilder sb = new StringBuilder(data.length * 2);
	        for (byte b : data) {
	            int v = b & 0xff;
	            if (v < 16) {
	                sb.append('0');
	            }
	            sb.append(Integer.toHexString(v));
	        }
	        return sb.toString().toUpperCase(Locale.getDefault());
	    }

	    /**
	     * 16进制表示的字符串转换为字节数组。
	     * 
	     * @param s
	     *            16进制表示的字符串
	     * @return byte[] 字节数组
	     */
	    public static byte[] hexStringToByteArray(String s) {
	        int len = s.length();
	        byte[] d = new byte[len / 2];
	        for (int i = 0; i < len; i += 2) {
	            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
	            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
	                    .digit(s.charAt(i + 1), 16));
	        }
	        return d;
	    }

	    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
	        @Override
	        protected SimpleDateFormat initialValue() {
	            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        }
	    };

	    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
	        @Override
	        protected SimpleDateFormat initialValue() {
	            return new SimpleDateFormat("yyyy-MM-dd");
	        }
	    };

	    /**
	     * 以友好的方式显示时间
	     * 
	     * @param sdate
	     * @return
	     */
	    public static String friendlyTime(String sdate) {
	        Date time = null;

	        if (isInEasternEightZones()) {
	            time = toDate(sdate);
	        } else {
	            time = transformTime(toDate(sdate), TimeZone.getTimeZone("GMT+08"),
	                    TimeZone.getDefault());
	        }

	        if (time == null) {
	            return "Unknown";
	        }
	        String ftime = "";
	        Calendar cal = Calendar.getInstance();

	        // 判断是否是同一天
	        String curDate = dateFormater2.get().format(cal.getTime());
	        String paramDate = dateFormater2.get().format(time);
	        if (curDate.equals(paramDate)) {
	            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
	            if (hour == 0)
	                ftime = Math.max(
	                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
	                        + "分钟前";
	            else
	                ftime = hour + "小时前";
	            return ftime;
	        }

	        long lt = time.getTime() / 86400000;
	        long ct = cal.getTimeInMillis() / 86400000;
	        int days = (int) (ct - lt);
	        if (days == 0) {
	            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
	            if (hour == 0)
	                ftime = Math.max(
	                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
	                        + "分钟前";
	            else
	                ftime = hour + "小时前";
	        } else if (days == 1) {
	            ftime = "昨天";
	        } else if (days == 2) {
	            ftime = "前天 ";
	        } else if (days > 2 && days < 31) {
	            ftime = days + "天前";
	        } else if (days >= 31 && days <= 2 * 31) {
	            ftime = "一个月前";
	        } else if (days > 2 * 31 && days <= 3 * 31) {
	            ftime = "2个月前";
	        } else if (days > 3 * 31 && days <= 4 * 31) {
	            ftime = "3个月前";
	        } else {
	            ftime = dateFormater2.get().format(time);
	        }
	        return ftime;
	    }

	    /**
	     * 将字符串转位日期类型
	     * 
	     * @param sdate
	     * @return
	     */
	    public static Date toDate(String sdate) {
	        return toDate(sdate, dateFormater.get());
	    }

	    public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
	        try {
	            return dateFormater.parse(sdate);
	        } catch (ParseException e) {
	            return null;
	        }
	    }

	    /**
	     * 判断用户的设备时区是否为东八区（中国） 2014年7月31日
	     * 
	     * @return
	     */
	    public static boolean isInEasternEightZones() {
	        boolean defaultVaule = true;
	        if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08"))
	            defaultVaule = true;
	        else
	            defaultVaule = false;
	        return defaultVaule;
	    }

	    /**
	     * 根据不同时区，转换时间 2014年7月31日
	     * 
	     * @param time
	     * @return
	     */
	    public static Date transformTime(Date date, TimeZone oldZone,
	            TimeZone newZone) {
	        Date finalDate = null;
	        if (date != null) {
	            int timeOffset = oldZone.getOffset(date.getTime())
	                    - newZone.getOffset(date.getTime());
	            finalDate = new Date(date.getTime() - timeOffset);
	        }
	        return finalDate;
	    }

}
