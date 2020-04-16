package com.nachtgeistw.impurebird;

import android.content.Intent;
import android.os.Bundle;

import com.nachtgeistw.impurebird.Util.BaseAppCompatActivity;

public class SplashActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
//                    sleep(1000);//make app sleep 1s
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}

/*
Android 启动页-解决图片被拉伸和压缩问题，适配虚拟导航栏：https://blog.csdn.net/u010218170/article/details/92437667
Android启动页开发：https://www.jianshu.com/p/96f0b9a971ac
Android全面屏启动页适配的一些坑：https://www.jianshu.com/p/ab0344bda165
 */