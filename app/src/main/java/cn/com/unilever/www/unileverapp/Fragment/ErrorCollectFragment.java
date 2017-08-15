package cn.com.unilever.www.unileverapp.Fragment;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.ImageView;

import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.code.Y;
import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.style.TextStyle;

import java.text.NumberFormat;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.activity.FunctionActivity;
import cn.com.unilever.www.unileverapp.config.MyConfig;

/**
 * @class 异常管理
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/5/22 13:08
 */
public class ErrorCollectFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private DisposeFragment disposeFragment;
    private View view;
    private HistoryFragment historyFragment;
    private ManagerclassifyFragment managerclassifyFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_collect, null, false);
        }
        view.setOnTouchListener(this);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.mToolbar);
        toolbar.setTitle("异常管理");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        initlogin();
        initWidget();
    }

    private void initWidget() {
        WebView mWebView = (WebView) view.findViewById(R.id.layout_arcview_main);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.addJavascriptInterface(new WebAppInterface(getActivity()), "Android");
        Button btn_dispose = (Button) view.findViewById(R.id.btn_dispose);
        ImageView btn_error = (ImageView) view.findViewById(R.id.btn_error);
        Button btn_history = (Button) view.findViewById(R.id.btn_history);
        btn_dispose.setOnClickListener(this);
        btn_error.setOnClickListener(this);
        btn_history.setOnClickListener(this);
        WebSettings webSettings = mWebView.getSettings();
        //设置支持javaScript脚步语言
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //支持缩放按钮-前提是页面要支持才显示
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        mWebView.loadUrl("file:///android_asset/H50B7ECBA/www/echart.html");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dispose:
                if (disposeFragment == null) {
                    disposeFragment = new DisposeFragment();
                }
                ((FunctionActivity) getActivity()).changFragment(disposeFragment);
                break;
            case R.id.btn_error:
                if (managerclassifyFragment == null) {
                    managerclassifyFragment = new ManagerclassifyFragment();
                }
                ((FunctionActivity) getActivity()).changFragment(managerclassifyFragment);
                break;
            case R.id.btn_history:
                if (historyFragment == null) {
                    historyFragment = new HistoryFragment();
                }
                ((FunctionActivity) getActivity()).changFragment(historyFragment);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    private class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public String getPieChartOptions(int type) {
            GsonOption option = new GsonOption();
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            String result = numberFormat.format((float) MyConfig.on / ((float) MyConfig.ok + (float) MyConfig.on) * 100);
            option.title(new Title()
                    .text(result + "%")
                    .textStyle(new TextStyle().color("#FFD85E").fontSize(30))
                    .subtext("解决率")
                    .subtextStyle(new TextStyle().color("#FFFFFF").fontSize(15))
                    .x(X.center).y(Y.center));
            option.calculable(true);
            Pie pie1 = new Pie("异常统计");
            pie1.type(SeriesType.pie).radius("50%", "55%");
            Data data1 = new Data("未解决", MyConfig.ok);
            data1.itemStyle().normal().color("#8C17BF");
            Data data2 = new Data("已解决", MyConfig.on);
            data2.itemStyle().normal().color("#FFD85E");
            pie1.data(data1, data2);
            option.series(pie1);
            return option.toString();
        }
    }
}