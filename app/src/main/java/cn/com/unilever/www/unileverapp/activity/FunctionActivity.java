package cn.com.unilever.www.unileverapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.unilever.www.unileverapp.Fragment.AnswerFragment;
import cn.com.unilever.www.unileverapp.Fragment.DisposeFM;
import cn.com.unilever.www.unileverapp.Fragment.EMATAccomplish;
import cn.com.unilever.www.unileverapp.Fragment.EMATok;
import cn.com.unilever.www.unileverapp.Fragment.ErrorCollectFragment;
import cn.com.unilever.www.unileverapp.Fragment.GradeFragment;
import cn.com.unilever.www.unileverapp.Fragment.HistoryFM;
import cn.com.unilever.www.unileverapp.Fragment.ManagerclassifyFragment;
import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.base.BaseFragmentActiviy;
import cn.com.unilever.www.unileverapp.config.MyConfig;
import cn.com.unilever.www.unileverapp.utils.CameraAlbumUtil;
import okhttp3.Call;

/**
 * @class 功能界面
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/5/17 11:32
 */
public class FunctionActivity extends BaseFragmentActiviy {
    View headerLayout;
    private Toolbar mToolbar;
    private NavigationView navigationView;
    private DrawerLayout activity_function;
    private AnswerFragment answerfragment;
    private ErrorCollectFragment errorcollectfragment;
    private OnButtonClickListener listener = null;
    private OnButtonClickListeners listeners = null;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        //控件
        initView();
        if (errorcollectfragment == null) {
            errorcollectfragment = new ErrorCollectFragment();
        }
        changFragment(errorcollectfragment);
        //头布局
        itinToolbar();
        //进入时显示界面
        //切换
        getWindow().addFlags(WindowManager.LayoutParams.
                FLAG_KEEP_SCREEN_ON);
        initNavigation();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        navigationView = (NavigationView) findViewById(R.id.nav);
        activity_function = (DrawerLayout) findViewById(R.id.activity_function);
    }

    private void itinToolbar() {
        mToolbar.setTitle("标题");//设置文本为空的
        setSupportActionBar(mToolbar);//调用此方法之后可以使用actionbar的所有功能
        ActionBar actionBar = getSupportActionBar();//通过上面设置的actionbar获取出来
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//Display：展示显示 HomeAsUp：左边的按钮 此条属性就是设置左边按钮能否被显示
            actionBar.setHomeAsUpIndicator(R.drawable.assistor);//设置HomeAsUp的图片
        }
        headerLayout = navigationView.getHeaderView(0);
        SharedPreferences sp = getSharedPreferences("logininformation", Context.MODE_PRIVATE);
        String username = sp.getString("username", null);
        TextView tv_name = (TextView) headerLayout.findViewById(R.id.tv_name);
        tv_name.setText(username);
    }

    private void initNavigation() {
        //头条设置的灰色，一旦进去之后自己就默认为灰色
        navigationView.setCheckedItem(R.id.error);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.error:
//                        if (errorcollectfragment == null) {
                        errorcollectfragment = new ErrorCollectFragment();
//                        }
                        changFragment(errorcollectfragment);
                        activity_function.closeDrawers();
                        break;
                    case R.id.answer:
                        if (answerfragment == null) {
                            answerfragment = new AnswerFragment();
                        }
                        changFragment(answerfragment);
                        activity_function.closeDrawers();
                        break;
                }
                return true;
            }
        });
    }

    //当选项菜单具体某个item被选中的时候
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //android下面的键的名字，一直是这个名字，但是要用android点
            case android.R.id.home:
                //打开抽屉布局,因为xml是gravity属性，所以需要设置Gravity下面的属性
                activity_function.openDrawer(Gravity.LEFT);
                break;
        }
        return true;
    }

    //跳转Fragment
    public void changFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (!(fragment instanceof EMATok) && !(fragment instanceof EMATAccomplish) && !(fragment instanceof GradeFragment) && !(fragment instanceof ErrorCollectFragment) && !(fragment instanceof AnswerFragment) && !(fragment instanceof ManagerclassifyFragment)) {

            transaction.addToBackStack(null);
        }
        if (fragment != null) {
            if (fragment instanceof DisposeFM || fragment instanceof HistoryFM || fragment instanceof AnswerFragment) {
                transaction.replace(R.id.fl_commcontent_main, fragment);
            } else {
                transaction.add(R.id.fl_commcontent_main, fragment);
            }
        }
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = CameraAlbumUtil.onActivityResult(requestCode, resultCode, data);
        if (CameraAlbumUtil.outputImage.length() > 0) {
            listener.OnButtonClick(CameraAlbumUtil.outputImage.getAbsolutePath());
            initBitmap();
        }
    }

    private void initBitmap() {
        OkHttpUtils
                .post()
                .url(MyConfig.url + "/ErrorController.sp?method=saveImage")
                .addFile("file", "image.png", CameraAlbumUtil.outputImage)
                .build()
                .connTimeOut(30000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            listeners.OnButtonClicks(jsonObject.getString("path"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void setOnButtonClickListener(OnButtonClickListener l) {
        this.listener = l;
    }

    public void setOnButtonClickListeners(OnButtonClickListeners ls) {
        this.listeners = ls;
    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock = ((PowerManager) getSystemService(POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.ON_AFTER_RELEASE, "TAG");
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    public interface OnButtonClickListener {
        void OnButtonClick(String file);
    }

    public interface OnButtonClickListeners {
        void OnButtonClicks(String file);
    }
}