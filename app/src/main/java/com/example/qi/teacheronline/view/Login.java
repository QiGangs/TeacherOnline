package com.example.qi.teacheronline.view;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qi.teacheronline.MainActivity;
import com.example.qi.teacheronline.R;
import com.example.qi.teacheronline.controller.Getuserinfo;
import com.example.qi.teacheronline.model.Userinfo;
import com.example.qi.teacheronline.util.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by qi on 2016/1/17.
 */
public class Login extends AppCompatActivity{
    private Button registerbt,loginx;
    private TextView accountx,passwordz,tv;
    private TextView wjmm;
    private View loginpro,loginlinear;
    private int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        zddenglu();

        loginpro = (View)findViewById(R.id.login_progress);
        loginlinear = (View)findViewById(R.id.login_form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.logintoolbar);
        toolbar.setTitle(" IT客");
        setSupportActionBar(toolbar);

        registerbt = (Button)findViewById(R.id.registerbt);
        registerbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        loginx = (Button)findViewById(R.id.loginbt);
        accountx = (TextView)findViewById(R.id.accountz);
        passwordz = (TextView)findViewById(R.id.passwordz);
        wjmm = (TextView)findViewById(R.id.wjmm);
        tv = (TextView)findViewById(R.id.testinfo);

        wjmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Login.this,Missmm.class);
                startActivity(intent);
            }
        });

        loginx.setOnClickListener(new View.OnClickListener() {    //成功就跳转不成功就提示错误
            @Override
            public void onClick(View v) {
               showprogress();
                if(!check()){
                    try {
                        Thread.sleep(1000);
                        noshowpro();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                new Thread( new Runnable() {
                    public void run() {
                        connetjsp(0);
                    }
                }).start();

            }
        });
    }

    @Override
    public void finish() {     //用来关闭该activity
        super.finish();
    }

    public void showprogress(){
       loginpro.setVisibility(View.VISIBLE);
        loginlinear.setVisibility(View.INVISIBLE);
    }

    public void noshowpro(){
        loginpro.setVisibility(View.INVISIBLE);
        loginlinear.setVisibility(View.VISIBLE);
    }

    public void toast(int i){
            switch (i) {
                case 1:
                    Toast.makeText(Login.this, "网络错误，请检查网络", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(Login.this, "账号密码不匹配,请重新输入", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(Login.this, "注意账号只能为手机号码", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(Login.this, "密码长度太短", Toast.LENGTH_SHORT).show();
                    break;
            }
    }

    public boolean check(){
        int length = accountx.getText().toString().length();
        if(length != 11){
            toast(3);
            return false;
        }

        int length1 = passwordz.getText().toString().length();
        if(length1<=6 || length1>=20){
            toast(4);
            return false;
        }
        return true;
    }

    public void jldenglu(String account,String psword,String name){
        SharedPreferences preferences = getSharedPreferences("itke",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("account",account);
        editor.putString("psword",psword);
        editor.putString("name",name);
        editor.commit();
    }

    public void zddenglu(){
        SharedPreferences preferences = getSharedPreferences("itke", MODE_PRIVATE);
        //SharedPreferences.Editor editor = preferences.edit();
        if(preferences.getString("account","1").equals("1")&&preferences.getString("psword","1").equals("1")){
            //Toast.makeText(Login.this, preferences.getString("psword",null).trim().toString() , Toast.LENGTH_SHORT).show();
            return;
        }
        connetjsp(1);
    }



    private Handler handler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what){
                case 0:
                    toast(2);break;
                case 1:
                    toast(1);break;
            }
            noshowpro();
            //shuxinview();
        }
    };


//    public void shuxinview(){
//        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.login_meun);
//        relativeLayout.postInvalidate();
//    }

    public void connetjsp(final int flag){
        new Thread( new Runnable() {
            public void run() {
                String psword=null;
                String account=null;
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                if(flag == 0) {
                    account = accountx.getText().toString();
                    psword = passwordz.getText().toString();
                    //Userinfo.setPhone(account);
                }else if(flag == 1){
                    SharedPreferences preferences = getSharedPreferences("itke", MODE_PRIVATE);
                    account = preferences.getString("account",null).trim().toString();
                    psword = preferences.getString("psword",null).trim().toString();
                    //Userinfo.setPhone(account);
                }
                postParameters.add(new BasicNameValuePair("username", account));
                postParameters.add(new BasicNameValuePair("password", psword));
                String response = null;
                try {
                    response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/denglu.jsp", postParameters);  //Enetr Your remote PHP,ASP, Servlet file link
                    String res = response.toString();
                    res = res.trim();
                    if (res.equals("1")) {
                        Getuserinfo getusetinfo = new Getuserinfo(account);
                        String name = getusetinfo.getinfo();
                        jldenglu(account,psword,name);
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        //intent.putExtra("name", accountx.getText().toString());
                        startActivity(intent);
                        finish();
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                } catch (NetworkOnMainThreadException e) {
                    e.printStackTrace();
                    Log.e("qweewq", e.toString());
                } catch (HttpHostConnectException e){
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    Log.e("qweasd", e.toString());
                }
            }
        }).start();
    }

}
