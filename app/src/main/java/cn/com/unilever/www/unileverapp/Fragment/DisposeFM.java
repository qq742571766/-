package cn.com.unilever.www.unileverapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.activity.FunctionActivity;
import cn.com.unilever.www.unileverapp.config.MyConfig;
import cn.com.unilever.www.unileverapp.data.list_josn;
import cn.com.unilever.www.unileverapp.utils.SystemTimeUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * @class 异常列表
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/5/22 14:15
 */
public class DisposeFM extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private View view;
    private TextView xiangqing1;
    private FrameLayout rl;
    private TextView xiangqing2;
    private RelativeLayout rl_title;
    private Button button;
    private ErrorCollectFragment dm;
    private list_josn josn;
    private boolean on_off = true;
    private EditText et;
    private String url;
    //    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 1 && on_off) {
//                errorclose();
//                on_off = false;
//            }
//            super.handleMessage(msg);
//        }
//    };
    private ProgressDialog progressDialog;

    private void errorclose() {
        SharedPreferences sp = getActivity().getSharedPreferences("logininformation", Context.MODE_PRIVATE);
        String usernames = sp.getString("username", null);
        if (et.getText().toString().length() > 0) {
            url = MyConfig.url + "/ErrorController.sp?method=errorclose&closeUserId="
                    + usernames + "&errorRole=1&key=" + josn.getKey() + "&closeTime=" + SystemTimeUtil.getErrorDate()
                    + "&error_close_idea=" + et.getText().toString();
//            try {
//                url = URLDecoder.decode(URLDecoder.decode(url, "UTF-8"));
//                Log.d("errors", url);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            OkHttpUtils
                    .get()
                    .url(url)
                    .build()
                    .connTimeOut(3000)
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d("errors", response);
                            if (dm == null) {
                                dm = new ErrorCollectFragment();
                            }
                            ((FunctionActivity) getActivity()).changFragment(dm);
                            progressDialog.dismiss();
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "请输入解决方案", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAttach(Context context) {
        josn = getArguments().getParcelable("josn");
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_history, null);
        view.setOnTouchListener(this);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.mToolbar);
        toolbar.setTitle("异常处理");
        initWidget();
        return view;
    }

    private void initWidget() {
        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.imageView_f);
        Picasso.with(getActivity())
                .load(MyConfig.url + "/errorImg/" + josn.getUrl())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imageView);
        et = (EditText) view.findViewById(R.id.et);
        SpannableString ss = new SpannableString("解决方案");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        et.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
        TextView textView_title = (TextView) view.findViewById(R.id.textView_title);
        textView_title.setText(josn.getErrorContent());
        TextView f_date = (TextView) view.findViewById(R.id.f_date);
        f_date.setText(josn.getErrorTime().split(" ")[0]);
        TextView textView_author_name = (TextView) view.findViewById(R.id.textView_author_name);
        textView_author_name.setText(josn.getCreateUserId());
        Log.d("AAA", josn.getCreateUserId() + "\t" + josn.getErrorDate());
        TextView tv_errorType = (TextView) view.findViewById(R.id.tv_errorType);
        tv_errorType.setText(josn.getErrorType());
        TextView tv_area = (TextView) view.findViewById(R.id.tv_area);
        tv_area.setText(josn.getArea());
        TextView tv_factory = (TextView) view.findViewById(R.id.tv_factory);
        tv_factory.setText(josn.getBaseFactory());
        TextView tv_errorTime = (TextView) view.findViewById(R.id.tv_errorTime);
        tv_errorTime.setText(josn.getErrorTime());
        TextView tv_errorContent = (TextView) view.findViewById(R.id.tv_errorContent);
        tv_errorContent.setText(josn.getErrorContent());
        xiangqing1 = (TextView) view.findViewById(R.id.xiangqing1);
        xiangqing2 = (TextView) view.findViewById(R.id.xiangqing2);
        rl = (FrameLayout) view.findViewById(R.id.rl);
        rl_title = (RelativeLayout) view.findViewById(R.id.rl_title);
        rl_title.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_title:
                if (xiangqing2.getVisibility() == View.GONE && rl.getVisibility() == View.GONE) {
                    xiangqing1.setVisibility(View.GONE);
                    xiangqing2.setVisibility(View.VISIBLE);
                    rl.setVisibility(View.VISIBLE);
                } else {
                    xiangqing1.setVisibility(View.VISIBLE);
                    xiangqing2.setVisibility(View.GONE);
                    rl.setVisibility(View.GONE);
                }
                break;
            case R.id.button:
                progressDialog = new ProgressDialog(getActivity(), 4);
                progressDialog.setMessage("正在关闭中");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                initlogin();
                break;
        }
    }

    private void initlogin() {
        OkHttpUtils
                .get()
                .url(MyConfig.loginurl)
                .build()
                .connTimeOut(3000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        errorclose();
//                        Message msg = new Message();
//                        msg.what = 1;
//                        msg.obj = response;
//                        handler.sendMessage(msg);
                    }
                });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
