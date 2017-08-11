package cn.com.unilever.www.unileverapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.activity.FunctionActivity;
import cn.com.unilever.www.unileverapp.activity.MainActivity;
import cn.com.unilever.www.unileverapp.adapter.MobileSoftAdapter;
import cn.com.unilever.www.unileverapp.info.ObInfo;
import cn.com.unilever.www.unileverapp.config.MyConfig;
import cn.com.unilever.www.unileverapp.data.Area_json;
import cn.com.unilever.www.unileverapp.data.ErrorType_json;
import okhttp3.Call;

/**
 * @class 观察
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/7/25 19:19
 */
public class ObservationFragment extends Fragment implements View.OnTouchListener {
    public static int num;
    public static int num1;
    private SMATFragment fragment3;
    private ManagerclassifyFragment afragment;
    private View view;
    private MobileSoftAdapter adapter;
    private ErrorFragment errorFragment;
    private TextView tv_name;
    private ListView rv_history;
    private Context context;
    private ArrayList<String> categorylist;
    private ArrayList<String> namelist;
    private ArrayList<ObInfo> contentlist;
    private ArrayList<String> keylist;
    private ProgressDialog progressDialog;
    private ManagerclassifyFragment managerclassifyFragment;
    private SMATFragment smat;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
//                MyConfig.num++;
                if (errorFragment == null) {
                    errorFragment = new ErrorFragment();
                }
                ((FunctionActivity) getActivity()).changFragment(errorFragment);
            }
            if (msg.what == 1) {
                OkHttpUtils
                        .get()
                        .url(MyConfig.url + "/elistAndroid.sp?method=getErrorCheckMenuByName&menu_name=" + MyConfig.area)
                        .build()
                        .connTimeOut(30000)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                                if (managerclassifyFragment == null) {
                                    managerclassifyFragment = new ManagerclassifyFragment();
                                }
                                ((FunctionActivity) getActivity()).changFragment(managerclassifyFragment);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Gson gson = new Gson();
                                namelist = new ArrayList<>();
                                ArrayList<ErrorType_json> errorType_json = gson.fromJson(response, new TypeToken<ArrayList<ErrorType_json>>() {
                                }.getType());
                                for (int i = 0; i < errorType_json.size(); i++) {
                                    namelist.add(errorType_json.get(i).getName());
                                }
                                keylist = new ArrayList<>();
                                for (int i = 0; i < errorType_json.size(); i++) {
                                    keylist.add(errorType_json.get(i).getKeyId());
                                }
                                if (namelist.size() > 0 && keylist.size() >= num1 + 1) {
                                    getquestion(keylist.get(num1));
                                    tv_name.setText(namelist.get(num1));
                                }
                            }
                        });
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        this.context = context;
        adapter = new MobileSoftAdapter(getActivity());
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_observation, null);
        view.setOnTouchListener(this);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.mToolbar);
        toolbar.setTitle("观察中");
        progressDialog = new ProgressDialog(getActivity(), 4);
        progressDialog.setMessage("正在获取列表");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        initview();
        initlogin();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String tv_name = namelist.get(num1) + "(" + categorylist.get(num) + ")";
        ArrayList<ObInfo> infos = contentlist;
        outState.putString("tv_name", tv_name);
        outState.putParcelableArrayList("infos", infos);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.getString("tv_name", "");
            adapter = new MobileSoftAdapter(getActivity());
            savedInstanceState.getParcelableArrayList("infos");
            adapter.addDatasToFirst(savedInstanceState.<ObInfo>getParcelableArrayList("infos"));
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    private void initview() {
        rv_history = (ListView) view.findViewById(R.id.lv_content);
        rv_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(context)
                        .setTitle("是否上报问题")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                MyConfig.num++;
                                if (errorFragment == null) {
                                    errorFragment = new ErrorFragment();
                                }
                                ((FunctionActivity) context).changFragment(errorFragment);
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
        tv_name = (TextView) view.findViewById(R.id.tv_Name);
        Button shang = (Button) view.findViewById(R.id.shang);
        Button xia = (Button) view.findViewById(R.id.xia);
        shang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        });
        xia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentlist != null && contentlist.size() > 0) {
                    if (categorylist.size() == num + 1) {
                        num1++;
                        num = 0;
                        initlogin();
                    } else {
                        num++;
                        initlogin();
                    }
                } else {
                    Toast.makeText(context, "列表加载中...", Toast.LENGTH_SHORT).show();
                }
                if (keylist != null && keylist.size() < num1 + 1) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("本次发现异常" + MyConfig.num + "次")
                            .setPositiveButton("继续观察", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (fragment3 == null) {
                                        fragment3 = new SMATFragment();
                                    }
                                    ((FunctionActivity) getActivity()).changFragment(fragment3);
                                }
                            })

                            .setNegativeButton("结束观察", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (afragment == null) {
                                        afragment = new ManagerclassifyFragment();
                                    }
                                    ((FunctionActivity) getActivity()).changFragment(afragment);
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(context, "列表加载中...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getquestion(String s) {
        try {
            OkHttpUtils
                    .get()
                    .url(MyConfig.url + "/elistAndroid.sp?method=getCheckListByPid&keyId=" + URLEncoder.encode(s, "utf-8"))
                    .build()
                    .connTimeOut(30000)
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                            if (managerclassifyFragment == null) {
                                managerclassifyFragment = new ManagerclassifyFragment();
                            }
                            ((FunctionActivity) getActivity()).changFragment(managerclassifyFragment);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Gson gson = new Gson();
                            ArrayList<Area_json> errorType_json = gson.fromJson(response, new TypeToken<ArrayList<Area_json>>() {
                            }.getType());
                            categorylist = new ArrayList<>();
                            HashSet<String> set = new HashSet<>();
                            for (int i = 0; i < errorType_json.size(); i++) {
                                set.add(errorType_json.get(i).getCategory());
                            }
                            categorylist.addAll(set);
                            if (categorylist.size() == 0) {
                                progressDialog.dismiss();
                            }
                            if (categorylist.size() != 0 && categorylist.size() > num) {
                                tv_name.setText(namelist.get(num1) + "(" + categorylist.get(num) + ")");
                            }
                            contentlist = new ArrayList<>();
                            int j = 1;
                            for (int i = 0; i < errorType_json.size(); i++) {
                                if (categorylist.get(num).equals(errorType_json.get(i).getCategory())) {
                                    ObInfo info = new ObInfo();
                                    info.tv_or = j + "." + errorType_json.get(i).getContent();
                                    //                                info.cb_or = false;
                                    contentlist.add(info);
                                    j++;
                                }
                            }
                            if (contentlist.size()==0){
                                if (smat == null) {
                                    smat = new SMATFragment();
                                }
                                ((FunctionActivity) getActivity()).changFragment(smat);
                            }
                            adapter.clearDatas();
                            adapter.addDatasToFirst(contentlist);
                            adapter.notifyDataSetChanged();
                            rv_history.setAdapter(adapter);
                            progressDialog.dismiss();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}