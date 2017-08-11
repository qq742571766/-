package cn.com.unilever.www.unileverapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.config.MyConfig;
import cn.com.unilever.www.unileverapp.data.list_josn;
import okhttp3.Call;
import okhttp3.Request;

/**
 * @class 登录界面
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/5/17 11:32
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername;
    private EditText etPassword;
    private CheckBox cbMemory;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    private boolean islogin;
    private String response;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                response = (String) msg.obj;
            }
            if (msg.what == 2) {
                try {
                    //json解析
                    JSONObject jsonObject = new JSONObject(response);
                    boolean result = jsonObject.getBoolean("result");
                    JSONObject user = jsonObject.getJSONObject("user");
                    //通过sp储存登录信息
                    SharedPreferences sp = getSharedPreferences("logininformation", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username", user.getString("username"));
                    editor.putString("userKey", user.getString("userKey"));
                    editor.putString("name", user.getString("name"));
                    editor.apply();
                    if (result) {
                        //跳转
                        Intent intent = new Intent(MainActivity.this, FunctionActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                        progressDialog.dismiss();
                    } else {
                        Snackbar.make(etUsername, "登录失败,请检查登录信息", Snackbar.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("islogin", Context.MODE_PRIVATE);
        islogin = sp.getBoolean("isChecked", false);
        if (islogin) {
            String usernames = sp.getString("username", null);
            String passwords = sp.getString("password", null);
            if (usernames != null && passwords != null) {
                Login(usernames, passwords);
            }
        }
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.
                FLAG_KEEP_SCREEN_ON);
        initView();
    }

    private void initView() {
        TextInputLayout til_username = (TextInputLayout) findViewById(R.id.til_username);
        til_username.setCounterEnabled(true);
        til_username.setCounterMaxLength(20);
        TextInputLayout til_password = (TextInputLayout) findViewById(R.id.til_password);
        til_password.setCounterEnabled(true);
        til_password.setCounterMaxLength(20);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        cbMemory = (CheckBox) findViewById(R.id.cb_memory);
        cbMemory.setChecked(islogin);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (v.getId() == R.id.btn_login) {
            boolean isChecked = cbMemory.isChecked();
            if (isChecked) {
                //通过sp储存用户
                SharedPreferences sp = getSharedPreferences("islogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isChecked", true);
                editor.putString("username", username);
                editor.putString("password", password);
                editor.apply();
                Login(username, password);
            } else {
                SharedPreferences sp = getSharedPreferences("islogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Login(username, password);
            }
        }
    }

    public void Login(String username, String password) {
        if (username.length() > 0 && password.length() > 0) {
            String login = MyConfig.url + "/login.sp?method=appLogin&loginName=" + username + "&password=" + password;
            OkHttpUtils
                    .post()
                    //http://192.168.10.23:8080/HiperMES/login.sp?method=appLogin&loginName=admin&password=admin
                    .url(login)
                    .build()
                    .connTimeOut(30000)
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressDialog.dismiss();
                            Snackbar.make(btnLogin, "登录失败请检查网络" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onBefore(Request request, int id) {
                            progressDialog = new ProgressDialog(MainActivity.this, 4);
                            progressDialog.setMessage("正在登陆中");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            super.onBefore(request, id);
                        }

                        @Override
                        public void onAfter(int id) {
                            super.onAfter(id);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            initadapter();
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = response;
                            handler.sendMessage(msg);
                        }
                    });
        } else {
            Snackbar.make(btnLogin, "请输入账号密码", Snackbar.LENGTH_LONG).show();
        }
    }

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
                        Log.d("ERRORs", response);
                        Gson gson = new Gson();
                        ArrayList<list_josn> list_josn = gson.fromJson(response, new TypeToken<ArrayList<list_josn>>() {
                        }.getType());
                        MyConfig.ok = 0;
                        MyConfig.on = 0;
                        for (int i = 0; i < list_josn.size(); i++) {
                            if (list_josn.get(i).getIsSolve().equals("1")) {
                                MyConfig.ok++;
                            } else {
                                MyConfig.on++;
                            }
                        }
                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                });
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
}