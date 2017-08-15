package cn.com.unilever.www.unileverapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.activity.FunctionActivity;
import cn.com.unilever.www.unileverapp.config.MyConfig;
import cn.com.unilever.www.unileverapp.utils.CameraAlbumUtil;

/**
 * @class 异常填写
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/5/17 14:24
 */
public class ErrorFragment extends Fragment implements View.OnTouchListener {
    private final static String url = "file:///android_asset/H50B7ECBA/www/index.html";
    private final static String staff_url = "file:///android_asset/H50B7ECBA/www/staff-index.html";
    private View view;
    private CameraAlbumUtil util;
    private TextView textView;
    private Context context;
    private WebView webview;
    private boolean on_off = true;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (on_off) {
                    webview.post(new Runnable() {
                        @Override
                        public void run() {
                            webview.loadUrl(staff_url);
                        }
                    });
                }
                if (MyConfig.type.equals("STAFFTAG")) {
                    webview.loadUrl(staff_url);
                }
                on_off = false;
            } else if (msg.what == 2) {
                util.takePhoto();
            } else if (msg.what == 3) {
                util.openAlbum();
            }
        }
    };
    private ManagerclassifyFragment fragment;
    private ObservationFragment fragment1;
    private DCAObservationFragment dca;
    private ErrorFragment errorFragment;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_error, null);
        view.setOnTouchListener(this);
        //获取数据
        textView = (TextView) view.findViewById(R.id.error_tx);
        webview = (WebView) view.findViewById(R.id.wv_error);
        WebSettings webSettings = webview.getSettings();
        //设置支持javaScript脚本语言
        webSettings.setJavaScriptEnabled(true);
        //支持缩放按钮-前提是页面要支持才显示
        webSettings.setBuiltInZoomControls(true);
        //设置客户端-不跳转到默认浏览器中
        webview.setWebViewClient(new WebViewClient());
        //加载网络资源
        webview.loadUrl(MyConfig.loginurl);
        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        //设置支持js调用java
        webview.addJavascriptInterface(new AndroidAndJSInterface(), "Android");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        on_off = true;
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.mToolbar);
        toolbar.setTitle(MyConfig.type);

        initWidget();
    }

    private void initWidget() {
        //页面监听
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                if (!url.equals(MyConfig.loginurl)) {
                    textView.setVisibility(View.GONE);
                }
            }
        });
        SharedPreferences sp = context.getSharedPreferences("logininformation", Context.MODE_PRIVATE);
        final String s = sp.getString("username", null);
        final String a = MyConfig.type;
        webview.post(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl("javascript:javaCallJs('" + s + "','" + a + "','" + null + "')");
            }
        });
        ((FunctionActivity) getActivity()).setOnButtonClickListeners(new FunctionActivity.OnButtonClickListeners() {
            @Override
            public void OnButtonClicks(final String file) {
                webview.post(new Runnable() {
                    @Override
                    public void run() {
                        webview.loadUrl("javascript:javaCallJs('" + s + "','" + a + "','" + file + "')");
                    }
                });
            }
        });
        ((FunctionActivity) getActivity()).setOnButtonClickListener(new FunctionActivity.OnButtonClickListener() {
            @Override
            public void OnButtonClick(final String file) {
                webview.post(new Runnable() {
                    @Override
                    public void run() {
                        webview.loadUrl("JavaScript:setImag('" + file + "')");
                    }
                });
            }
        });
    }

    private void chooseDagilog() {
        util = new CameraAlbumUtil(getActivity());
        new AlertDialog.Builder(getActivity())
                .setTitle("选择相片")
                .setPositiveButton("相机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                })

                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }
                })
                .show();
    }

    @Override
    public void onDestroyView() {
        on_off = true;
        super.onDestroyView();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    private class AndroidAndJSInterface {
        @JavascriptInterface
        public void picture() {
            chooseDagilog();
        }

        @JavascriptInterface
        public void upload() {
//            Toast.makeText(context, "上报中...", Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void submit(String submit) {
            try {
                JSONObject object = new JSONObject(submit);
                if (!object.getString("errorUserId").equals("") &&
                        !object.getString("otherUserId").equals("") &&
                        !object.getString("errorContent").equals("") &&
                        !object.getString("errorType").equals("") &&
                        !object.getString("area").equals("") &&
                        !object.getString("factory").equals("") &&
                        !object.getString("workerClass").equals("") &&
                        !object.getString("errorTime").equals("")) {
                    Snackbar.make(view, "上报中,请稍后", Snackbar.LENGTH_LONG).show();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (MyConfig.type.equals("DCA")) {
                        MyConfig.num++;
                        if (dca == null) {
                            dca = new DCAObservationFragment();
                        }
                        ((FunctionActivity) getActivity()).changFragment(dca);
                    } else if (MyConfig.type.equals("SMAT")) {
                        MyConfig.num++;
                        if (fragment1 == null) {
                            fragment1 = new ObservationFragment();
                        }
                        ((FunctionActivity) getActivity()).changFragment(fragment1);
                    } else if (MyConfig.type.equals("审计") || MyConfig.type.equals("TAG")) {
                        if (fragment == null) {
                            fragment = new ManagerclassifyFragment();
                        }
                        ((FunctionActivity) getActivity()).changFragment(fragment);
                    }
                } else {
                    Toast.makeText(context, "表单填写不完整,请重新填写", Toast.LENGTH_LONG).show();
                    if (errorFragment == null) {
                        errorFragment = new ErrorFragment();
                    }
                    ((FunctionActivity) getActivity()).changFragment(errorFragment);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}