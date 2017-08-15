package cn.com.unilever.www.unileverapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.activity.FunctionActivity;
import cn.com.unilever.www.unileverapp.config.MyConfig;
import cn.com.unilever.www.unileverapp.utils.SystemTimeUtil;
import okhttp3.Call;

/**
 * @class EMAT提交
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/7/10 15:18
 */
public class EMATAccomplish extends Fragment implements View.OnClickListener, View.OnTouchListener {
    public View view;
    public String pass = "2";
    public Context context;
    private WebView webView;
    private String userKey;
    private AnswerFragment fragment;
    private boolean islogin = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 3 && islogin) {
                String url = MyConfig.url + "/ematAndroid.sp?method=addAndroidSave&answer_user=" + MyConfig.name +
                        "&answer_user_id=" + MyConfig.id + "&ask_uesr_id=" + userKey + "&question_num=" + MyConfig.sourceStrArray.size() +
                        "&test_time=" + SystemTimeUtil.getErrorDate() + "&test_result=" + pass;
                try {
                    url = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                webView.loadUrl(url);
                Toast.makeText(context, "上传完成，等待跳转", Toast.LENGTH_SHORT).show();
                MyConfig.sourceStrArray = null;
                MyConfig.sourceStrArray = new ArrayList<>();
                MyConfig.ExcellentNumber = 0;
                MyConfig.FineNumber = 0;
                MyConfig.DadNumber = 0;
                SharedPreferences sp = context.getSharedPreferences("grade", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (fragment == null) {
                    fragment = new AnswerFragment();
                }
                ((FunctionActivity) getActivity()).changFragment(fragment);
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_accomplish, null, false);
        view.setOnTouchListener(this);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.mToolbar);
        toolbar.setTitle("评分");
        return view;
    }

    @Override
    public void onStart() {
        initview();
        initdata();
        super.onStart();
    }

    private void initview() {
        Button btn_no = (Button) view.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(this);
        Button btn_yes = (Button) view.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(this);
        TextView textView6 = (TextView) view.findViewById(R.id.textView6);
        textView6.setText(MyConfig.name);
        TextView textView8 = (TextView) view.findViewById(R.id.textView8);
        textView8.setText(MyConfig.sourceStrArray.size() + "");
        TextView textView10 = (TextView) view.findViewById(R.id.textView10);
        textView10.setText(MyConfig.ExcellentNumber + "");
        TextView textView12 = (TextView) view.findViewById(R.id.textView12);
        textView12.setText(MyConfig.FineNumber + "");
        TextView textView14 = (TextView) view.findViewById(R.id.textView14);
        textView14.setText(MyConfig.DadNumber + "");
        webView = (WebView) view.findViewById(R.id.wwv);
        WebSettings webSettings = webView.getSettings();
        //设置支持javaScript脚步语言
        webSettings.setJavaScriptEnabled(true);
        //支持缩放按钮-前提是页面要支持才显示
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        //设置客户端-不跳转到默认浏览器中
        webView.setWebViewClient(new WebViewClient());
        //加载网络资源
        webView.loadUrl(MyConfig.url + "/login.sp?method=appLogin&loginName=admin&password=admin");
        //支持屏幕缩放
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //不通过
            case R.id.btn_no:
                pass = "2";
                onEMATSubmit();
                break;
            //通过
            case R.id.btn_yes:
                pass = "1";
                onEMATSubmit();
                break;
        }
    }

    private void onEMATSubmit() {
        SharedPreferences sp = context.getSharedPreferences("logininformation", Context.MODE_PRIVATE);
        userKey = sp.getString("userKey", null);
        Message msg = new Message();
        msg.what = 3;
        handler.sendMessage(msg);
    }

    private void initdata() {
        OkHttpUtils
                .post()
                .url(MyConfig.url + "/login.sp?method=appLogin&loginName=admin&password=admin")
                .build()
                .connTimeOut(30000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Snackbar.make(view, "数据获取失败请检查网络..." + e.toString(), Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        islogin = true;
                    }
                });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}