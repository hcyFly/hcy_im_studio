package com.stay4it.im.untils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.stay4it.im.IMApplication;
import com.stay4it.im.entities.Attachment.AttachmentType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String TAG = "PushDemoActivity";
    public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";

    public static String logStringCache = "";

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    // 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
    public static boolean hasBind(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String flag = sp.getString("bind_flag", "");
        if ("ok".equalsIgnoreCase(flag)) {
            return true;
        }
        return false;
    }

    public static void setBind(Context context, boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("bind_flag", flagStr);
        editor.commit();
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.commit();
    }
    
    public static void showImage(String file_url, ImageView image, boolean entire) {
		showImage(file_url, image, entire, false, null);
	}

	public static void showImage(String file_url, ImageView image, boolean entire, boolean isPicture) {
		showImage(file_url, image, entire, isPicture, null);
	}

	public static void showImage(String file_url, ImageView image) {
		showImage(file_url, image, false, false, null);
	}

	public static void showImage(String file_url, ImageView image, boolean entire, boolean isPicture, ImageLoadingListener imageLoadListener) {
		if (!TextUtil.isValidate(file_url)) {
			ImageLoader.getInstance().displayImage(null, image, isPicture ? IMApplication.mPictureOptions : IMApplication.mAvatarOptions,
					imageLoadListener);
		} else {
			if (file_url.startsWith("/media")) {
				ImageLoader.getInstance().displayImage(UrlHelper.loadImg(file_url, entire), image,
						isPicture ? IMApplication.mPictureOptions : IMApplication.mAvatarOptions, imageLoadListener);
			} else {
				ImageLoader.getInstance().displayImage(Uri.fromFile(new File(file_url)).toString(), image,
						isPicture ? IMApplication.mPictureOptions : IMApplication.mAvatarOptions, imageLoadListener);
			}
		}
	}

	public static boolean isImage(String file_type) {
		if (AttachmentType.BMP.fetchMimeType().equalsIgnoreCase(file_type) || AttachmentType.PNG.fetchMimeType().equalsIgnoreCase(file_type)
				|| AttachmentType.JPEG.fetchMimeType().equalsIgnoreCase(file_type) || AttachmentType.JPG.fetchMimeType().equalsIgnoreCase(file_type)) {
			return true;
		}
		return false;
	}

}
