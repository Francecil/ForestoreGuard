package com.france.forestoreguard.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.france.forestoreguard.R;
import com.france.forestoreguard.ui.BaseActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;


public class LoginActivity extends BaseActivity {
    ImageView login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_btn=(ImageView)findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimActivity(MainActivity.class);
                LoginActivity.this.finish();
            }
        });
//        MThread mThread=new MThread();
//        mThread.run();
    }
    //连接定位接口并得到json
    public void GetJSONString() throws Exception {
        AjaxParams params = new AjaxParams();
        params.put("user_name", "Amy");
        params.put("user_psw", "950601");

        FinalHttp fh = new FinalHttp();
        fh.post("http://120.24.251.94/3w/jalan/forestore/module/user/login/login.php", params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
            }


            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                ShowLog(o.toString());
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
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
}
