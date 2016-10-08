package com.example.qi.teacheronline.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qi.teacheronline.R;
import com.example.qi.teacheronline.controller.Sethfinfo;
import com.example.qi.teacheronline.controller.Setinfo;
import com.example.qi.teacheronline.model.Userinfo;
import com.example.qi.teacheronline.util.Checkflag;

/**
 * Created by qi on 2016/2/13.
 */
public class Huifu extends AppCompatActivity{
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huifu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.huifutoolbar);
        toolbar.setTitle("回复");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView = (TextView)findViewById(R.id.huifuedit);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.huifufab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, " 确认？", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(check() == 0){
                                    return;
                                }
                                Checkflag checkflag = new Checkflag();
                                if (checkflag.check(textView.getText().toString())) {
                                    Toast.makeText(Huifu.this, "不可包含字符'<cont>'或'<conts>'", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                //Toast.makeText(Huifu.this,textView.getText().toString(),Toast.LENGTH_SHORT).show();
                                final String mainid = getIntent().getStringExtra("tzid");
                                final String cont = textView.getText().toString();
                                SharedPreferences preferences = getSharedPreferences("itke", MODE_PRIVATE);
                                final String account = preferences.getString("account",null).trim().toString();
                                final String name = preferences.getString("name",null).trim().toString();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Sethfinfo sethfinfo = new Sethfinfo();
                                        sethfinfo.sethfinfo(mainid, name, account, cont);
                                        handler.sendEmptyMessage(0);
                                    }
                                }).start();
                            }
                             }).show();
                            }
                        });


    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();     //发送广播刷新前一个界面
            intent.setAction("action.refresh");
            sendBroadcast(intent);
            finish();
        }
    };

    public int check(){
        if(textView.getText().toString().length()<1){
            Toast.makeText(this,"请输入内容",Toast.LENGTH_SHORT).show();
            return 0;
        }
        return 1;
    }



}
