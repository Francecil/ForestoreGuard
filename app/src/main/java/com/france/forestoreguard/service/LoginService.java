package com.france.forestoreguard.service;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

/**
 * Created by Administrator on 2016/3/13.
 */
public class LoginService {
    private void login(){
        FinalHttp fh = new FinalHttp();
        fh.get("http://www.yangfuhai.com", new AjaxCallBack(){

            @Override
            public void onLoading(long count, long current) { //每1秒钟自动被回调一次
            }


            @Override
            public void onStart() {
                //开始http请求的时候回调
            }

        });
    }
}
