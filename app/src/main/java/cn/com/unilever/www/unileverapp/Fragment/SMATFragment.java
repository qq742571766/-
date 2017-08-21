package cn.com.unilever.www.unileverapp.Fragment;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airsaid.pickerviewlibrary.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import cn.com.unilever.www.unileverapp.view.MyEdittext;
import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.activity.FunctionActivity;
import cn.com.unilever.www.unileverapp.config.MyConfig;
import cn.com.unilever.www.unileverapp.data.ErrorType_json;
import okhttp3.Call;

/**
 * @class SMAT观察
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/7/21 11:10
 */
public class SMATFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private View view;
    private TextView tv_area;
    private TextView tv_class;
    private MyEdittext et_person;
    private ArrayList<String> dataList;
    private ObservationFragment fragment;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                OkHttpUtils
                        .get()
                        .url(MyConfig.url + "/elistAndroid.sp?method=getErrorCheckMenuByName&menu_name=" + MyConfig.type)
                        .build()
                        .connTimeOut(30000)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Gson gson = new Gson();
                                dataList = new ArrayList<>();
                                ArrayList<ErrorType_json> errorType_json = gson.fromJson(response, new TypeToken<ArrayList<ErrorType_json>>() {
                                }.getType());
                                for (int i = 0; i < errorType_json.size(); i++) {
                                    dataList.add(errorType_json.get(i).getName());
                                }
                                if (dataList.size() > 0 && dataList != null) {
                                    tv_area.setText(dataList.get(0));
                                    MyConfig.area = dataList.get(0);
                                }
                            }
                        });
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_smat, null);
        view.setOnTouchListener(this);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.mToolbar);
        toolbar.setTitle(MyConfig.type);
        ObservationFragment.num = 0;
        ObservationFragment.num1 = 0;
        MyConfig.num = 0;
        initview();
        initlogin();
        return view;
    }

    private void initlogin() {
        OkHttpUtils
                .get()
                .url(MyConfig.loginurl)
                .build()
                .connTimeOut(30000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = response;
                        handler.sendMessage(msg);
                    }
                });
    }

    private void initview() {
        LinearLayout ll_area = (LinearLayout) view.findViewById(R.id.ll_area);
        LinearLayout ll_class = (LinearLayout) view.findViewById(R.id.ll_class);
        et_person = (MyEdittext) view.findViewById(R.id.et_observe_person);
        tv_area = (TextView) view.findViewById(R.id.tv_areas);
        tv_class = (TextView) view.findViewById(R.id.tv_classs);
        Button btn_begin = (Button) view.findViewById(R.id.btn_begin);
        tv_class.setText("早班A");
        ll_area.setOnClickListener(this);
        ll_class.setOnClickListener(this);
        btn_begin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_area:
                OptionsPickerView<String> ll_area = new OptionsPickerView<>(getActivity());
                // 设置数据
                if (dataList != null) {
                    ll_area.setPicker(dataList);
                    // 设置选项单位
                    ll_area.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int option1, int option2, int option3) {
                            tv_area.setText(dataList.get(option1));
                            MyConfig.area = dataList.get(option1);
                        }
                    });
                    ll_area.show();
                } else {
                    Snackbar.make(view, "区域信息加载中", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.ll_class:
                OptionsPickerView<String> ll_class = new OptionsPickerView<>(getActivity());
                final ArrayList<String> g = new ArrayList<>();
                g.add("早班");
                g.add("晚班");
                final ArrayList<String> c = new ArrayList<>();
                c.add("A");
                c.add("B");
                c.add("C");
                c.add("D");
                ArrayList<ArrayList<String>> c2 = new ArrayList<>();
                c2.add(0, c);
                c2.add(1, c);
                // 设置数据
                ll_class.setPicker(g, c2, false);
                // 设置选项单位
                ll_class.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int option1, int option2, int option3) {
                        tv_class.setText(g.get(option1) + "" + c.get(option2));
                    }
                });
                ll_class.show();
                break;
            case R.id.btn_begin:
                if (tv_area.getText().toString().length() > 0 && tv_class.getText().toString().length() > 0 && et_person.getText().toString().length() > 0) {
                    if (fragment == null) {
                        fragment = new ObservationFragment();
                    }
                    ((FunctionActivity) getActivity()).changFragment(fragment);
                } else {
                    Snackbar.make(view, "信息收集不全", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}