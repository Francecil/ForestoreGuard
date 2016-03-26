package com.france.forestoreguard.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.france.forestoreguard.R;
import com.france.forestoreguard.ui.BaseActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends BaseActivity {
    ImageView login_btn;
    EditText loginname,loginpsw;
    private ProgressDialog progress;
    MyHandler myHandler;
    private SharedPreferences tokenSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_btn=(ImageView)findViewById(R.id.login_btn);
        loginname=(EditText)findViewById(R.id.loginname);
        loginpsw=(EditText)findViewById(R.id.loginpwd);
        progress=new ProgressDialog(LoginActivity.this);
        myHandler=new MyHandler();
        tokenSharedPreferences=this.getSharedPreferences("token",MODE_PRIVATE);
        String token=tokenSharedPreferences.getString("token","");
        if(token!=null&&token.length()>0){
            startAnimActivity(MainActivity.class);
            LoginActivity.this.finish();
        }
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startAnimActivity(MainActivity.class);
//                LoginActivity.this.finish();
                MThread mThread=new MThread();
                mThread.run();
            }
        });
//
    }
    //连接定位接口并得到json
    public void GetJSONString() throws Exception {
        AjaxParams params = new AjaxParams();
        params.put("guard_name", loginname.getText().toString());
        params.put("guard_psw", loginpsw.getText().toString());

        FinalHttp fh = new FinalHttp();
        fh.post("http://120.24.251.94/3w/jalan/forestore/module/guard/login/login.php", params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
            }


            @Override
            public void onStart() {
                super.onStart();
                myHandler.sendEmptyMessage(2);
            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                ShowLog(o.toString());
                try {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    boolean status=(boolean)jsonObject.get("status");
                    ShowLog(status+"");
                    if(status){
                        JSONObject dataObject=(JSONObject)jsonObject.get("data");
                        String token=(String)dataObject.get("token");
                        ShowLog("token:"+token);
                        tokenSharedPreferences=getApplicationContext().getSharedPreferences("token",MODE_PRIVATE);
                        tokenSharedPreferences.edit().putString("token",token).commit();
                        myHandler.sendEmptyMessage(1);
                    }
                    else myHandler.sendEmptyMessage(0);
                }catch (JSONException e){
                    myHandler.sendEmptyMessage(0);
                    e.printStackTrace();
                }

                }

                @Override
                public void onFailure (Throwable t,int errorNo, String strMsg){
                    super.onFailure(t, errorNo, strMsg);
                    myHandler.sendEmptyMessage(-1);
                }
            }

            );
        }
        class MThread implements Runnable{
        @Override
        public void run() {
            try {
                GetJSONString();
            }catch (Exception e){
                Log.e("zjx",e.getStackTrace()+"");
            }

        }
    }
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:{
                    ShowToast(R.string.login_failed_net);
                    progress.cancel();
                    break;
                }
                case 0:{
                    ShowToast(R.string.login_failed);
                    progress.cancel();
                    break;
                }
                case 1:{
                    progress.cancel();
                    startAnimActivity(MainActivity.class);
                    LoginActivity.this.finish();
                    break;
                }
                case 2:{
                    progress.setMessage("loading");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
                    break;
                }
            }
        }
    }
}
