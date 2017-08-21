package cn.com.unilever.www.unileverapp.base;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.yanzhenjie.permission.AndPermission;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @class 创建
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/5/17 11:32
 */
public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA, Manifest.permission.WAKE_LOCK,
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_LOGS,
                        Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.WRITE_SETTINGS, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                .start();
    }
}