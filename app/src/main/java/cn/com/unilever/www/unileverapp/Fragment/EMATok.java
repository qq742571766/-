package cn.com.unilever.www.unileverapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.activity.FunctionActivity;
import cn.com.unilever.www.unileverapp.config.MyConfig;

/**
 * @class 答题
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/6/13 10:14
 */
public class EMATok extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private int i = 0;
    private View view;
    private WebView webView;
    private Button button;
    private Context context;
    private Timer timer = new Timer();
    private Toolbar toolbar;
    private String s;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    button.performClick();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
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
        view = inflater.inflate(R.layout.fragment_ematok, null, false);
        view.setOnTouchListener(this);
        return view;
    }

    private void initview() {
        webView = (WebView) view.findViewById(R.id.wv_emat_ok);
        WebSettings webSettings = webView.getSettings();
        //设置支持javaScript脚步语言
        webSettings.setJavaScriptEnabled(true);
        //支持缩放按钮-前提是页面要支持才显示
        webSettings.setBuiltInZoomControls(true);
        //设置客户端-不跳转到默认浏览器中
        webView.setWebViewClient(new WebViewClient());
        //加载网络资源
        webView.loadUrl("file:///android_asset/H50B7ECBA/www/EMATok.html");
        //支持屏幕缩放
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webView.addJavascriptInterface(new AndroidAndJSInterface(), "Android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String[] urls = url.split("=");
                SharedPreferences sp = context.getSharedPreferences("grade", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if (urls.length == 1) {
                } else {
                    //通过sp储存答案
                    if (urls[1] != null) {
                        editor.putString(i + "", urls[1]);
                    }
                    editor.apply();
                }
                if (MyConfig.sourceStrArray.size() == i) {
                    GradeFragment grade = new GradeFragment();
                    ((FunctionActivity) getActivity()).changFragment(grade);
                }
                toolbar.setTitle("开始答题" + (i + 1) + "/" + MyConfig.sourceStrArray.size());
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        button = (Button) view.findViewById(R.id.btn_emat_ok);
        timer.schedule(task, 0, 10);
        button.setOnClickListener(this);
        button.setVisibility(View.GONE);
        toolbar = (Toolbar) getActivity().findViewById(R.id.mToolbar);
        toolbar.setTitle("开始答题" + (i + 1) + "/" + MyConfig.sourceStrArray.size());
    }

    @Override
    public void onClick(View v) {
        if (MyConfig.problem != null && MyConfig.sourceStrArray.size() > i) {
            if (i < 0) {
                Toast.makeText(context, "无上一题", Toast.LENGTH_SHORT).show();
                i = 0;
            } else {
                s = MyConfig.problem.get(MyConfig.sourceStrArray.get(i));
                webView.loadUrl("javascript:javaCallJs(" + "'" + s + "'" + ")");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initview();
        button.performClick();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    private class AndroidAndJSInterface {
        @JavascriptInterface
        public void next() {
            if (MyConfig.sourceStrArray.size() > i) {
                i++;
            }
        }

        @JavascriptInterface
        public void last() {
            if (MyConfig.sourceStrArray.size() > i) {
                if (i < 0) {
                    Toast.makeText(context, "无上一题", Toast.LENGTH_SHORT).show();
                    i = 0;
                } else {
                    i--;
                }
            }
        }
    }
}