package cn.com.unilever.www.unileverapp.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.activity.FunctionActivity;
import cn.com.unilever.www.unileverapp.adapter.HistoryAdapter;
import cn.com.unilever.www.unileverapp.config.MyConfig;
import cn.com.unilever.www.unileverapp.data.list_josn;
import okhttp3.Call;

/**
 * @class 异常历史
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/5/22 13:29
 */
public class HistoryFragment extends Fragment implements View.OnTouchListener {
    private ErrorCollectFragment fragment;
    private View view;
    private HistoryAdapter adapter;
    private List<list_josn> list;
    private long exitTime = 0;
    private boolean on_off = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                initadapter();
            }
            super.handleMessage(msg);
        }
    };
    private HistoryFM fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dispose, null);
        view.setOnTouchListener(this);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.mToolbar);
        toolbar.setTitle("异常列表");
        //适配器
        adapter = new HistoryAdapter();
        initlogin();
        //测试数据
        initadapter();
        //初始化
        initWidget();
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

    //数据
    private void initadapter() {
        OkHttpUtils
                .get()
                .url(MyConfig.url + "/ErrorController.sp?method=AndroiderrorList")
                .build()
                .connTimeOut(30000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        ArrayList<list_josn> list_josn = gson.fromJson(response, new TypeToken<ArrayList<list_josn>>() {
                        }.getType());
                        list = new ArrayList<>();
                        list_josn josn = null;
                        for (int i = 0; i < list_josn.size(); i++) {
                            josn = new list_josn();
                            if (list_josn.get(i).getIsSolve().equals("2")) {
//                                josn.error_question_type = list_josn.get(i).getError_question_type();
                                josn.createUserId = list_josn.get(i).getCreateUserId();
                                josn.errorTime = list_josn.get(i).getErrorTime();
                                josn.errorType = list_josn.get(i).getErrorType();
                                josn.area = list_josn.get(i).getArea();
                                josn.baseFactory = list_josn.get(i).getBaseFactory();
                                josn.errorDate = list_josn.get(i).getErrorDate();
                                josn.errorContent = list_josn.get(i).getErrorContent();
                                josn.key = list_josn.get(i).getKey();
                                josn.closeUserId = list_josn.get(i).getCloseUserId();
                                josn.errorRole = list_josn.get(i).getErrorRole();
                                josn.error_close_idea = list_josn.get(i).getError_close_idea();
                                josn.url = list_josn.get(i).getUrl();
                                list.add(josn);
                            }
                        }
                        if (list.size() > 0) {
                            //将数据添加至适配器
                            adapter.adds(list);
                            adapter.notifyDataSetChanged();
                        } else if (list.size() == 0 && on_off) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("无待历史异常")
                                    .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (fragment == null) {
                                                fragment = new ErrorCollectFragment();
                                            }
                                            ((FunctionActivity) getActivity()).changFragment(fragment);
                                        }
                                    }).show();
                            on_off = false;
                        }
                    }
                });
    }

    private void initWidget() {
        //控件
        RecyclerView rv_history = (RecyclerView) view.findViewById(R.id.rv_history);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_history.setLayoutManager(manager);
        //添加适配器
        rv_history.setAdapter(adapter);
        //点击事件
        adapter.setOnButtonClickListener(new HistoryAdapter.OnButtonClickListener() {
            @Override
            public void OnButtonClick(list_josn info) {
                if (System.currentTimeMillis() - exitTime > 1000) {
                    if (fm == null) {
                        fm = new HistoryFM();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("josn", info);
                    fm.setArguments(bundle);
                    ((FunctionActivity) getActivity()).changFragment(fm);
                }
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}