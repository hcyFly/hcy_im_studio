package com.stay4it.im;

import android.content.Context;

import com.baidu.frontia.FrontiaApplication;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.stay4it.im.entities.Profile;
import com.stay4it.im.untils.Constants;
import com.stay4it.im.untils.FileUtil;
import com.stay4it.im.untils.PrefsAccessor;

import java.io.File;

/**
 * @author Stay
 * @version create time：Mar 8, 2015 6:36:08 PM
 */
public class IMApplication extends FrontiaApplication {
    public static IMApplication gContext;
    private static Profile profile;
    public static String selfId = null;
    public static int mAppState = -1;
    public static DisplayImageOptions mAvatarOptions;
    public static DisplayImageOptions mPictureOptions;
    @Override
    public void onCreate() {
        super.onCreate();
        gContext = this;
        mAppState = -1;
        initImageLoader(this);
    }

    public static void setProfile(Profile tmp) {
        profile = tmp;
        selfId = profile.getUserId();
        Gson gson = new Gson();
        PrefsAccessor.getInstance(gContext).saveString(Constants.KEY_PROFILE, gson.toJson(profile));
    }
    public static Profile getProfile() {
        return profile;
    }

    public static String getToken() {
        if (profile == null) {
            return null;
        }
        return profile.getAccess_token();
    }

    public static void initializeProfile() {
        String json = PrefsAccessor.getInstance(gContext).getString(Constants.KEY_PROFILE);
        Gson gson = new Gson();
        Profile profile = gson.fromJson(json, Profile.class);
        IMApplication.profile = profile;
        selfId = profile.getUserId();
    }
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        File cacheDir = new File(FileUtil.getImgDir());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().discCache(new LimitedAgeDiskCache(cacheDir,5000)) // default
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        mAvatarOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.mini_avatar).showImageForEmptyUri(R.drawable.mini_avatar)
                .showImageOnFail(R.drawable.mini_avatar).cacheInMemory().cacheOnDisc().build();
        mPictureOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.image_placeholder)
                .showImageForEmptyUri(R.drawable.image_placeholder).showImageOnFail(R.drawable.image_placeholder).cacheInMemory().cacheOnDisc()
                .build();
    }
}
