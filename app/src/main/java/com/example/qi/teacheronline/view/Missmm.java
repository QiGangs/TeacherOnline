package com.example.qi.teacheronline.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qi.teacheronline.R;
import com.example.qi.teacheronline.controller.Isaccount;
import com.example.qi.teacheronline.util.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * Created by qi on 2016/3/1.
 */
public class Missmm extends AppCompatActivity{
    private Button bt,bty;
    private TextView account,password,password1,yanzhen,testtv321;
    private static String APPKEY = "f3d1189823d0";
    private static String APPSECRET = "9d60291f4f7cfb9e1b3d4b007021bd5d";
    public String phString1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missmm);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()        //是主线程中可以调用网络
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarwjmm);
        toolbar.setTitle("密码重置");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {      //用于给导航按钮田间监听事件
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt = (Button)findViewById(R.id.wjmmbutton);
        bty = (Button)findViewById(R.id.findbt);
        account = (TextView)findViewById(R.id.wjmmaccount);
        yanzhen = (TextView)findViewById(R.id.wjmmyanzhen);
        password = (TextView)findViewById(R.id.wjmmpassword);
        password1 = (TextView)findViewById(R.id.wjmmpassword1);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSSDK.getVerificationCode("86", account.getText().toString());
                phString1=account.getText().toString();
            }
        });
        bty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Isaccount isaccount = new Isaccount();
                String flag = isaccount.isaccount(account.getText().toString());
                //Toast.makeText(getApplicationContext(), password.getText().toString()+"qwe" , Toast.LENGTH_SHORT).show();
                if(!flag.equals("1")){
                    Toast.makeText(getApplicationContext(), "该账号未注册" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if (check() == 0){
                    return;
                }
                //先判断短信是否成功验证
                SMSSDK.submitVerificationCode("86", phString1, yanzhen.getText().toString());

//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if(flag != 10){
//                    Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                //进行http通信

            }
        });


        try {
            SMSSDK.initSDK(this, APPKEY, APPSECRET, true);
            EventHandler eh = new EventHandler() {

                @Override
                public void afterEvent(int event, int result, Object data) {
                    Message msg = new Message();
                    msg.arg1 = event;
                    msg.arg2 = result;
                    msg.obj = data;
                    mHandler.sendMessage(msg);
                }

            };
            SMSSDK.registerEventHandler(eh);
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result"+event);
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                    //flag = 10;
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    postParameters.add(new BasicNameValuePair("account", account.getText().toString()));
                    postParameters.add(new BasicNameValuePair("password", password.getText().toString()));

                    String response = null;
                    try {
                        response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/resetmm.jsp", postParameters);  //Enetr Your remote PHP,ASP, Servlet file link
                        String res = response.toString();
                        //res = res.replaceAll("\\s+", "");
                        res = res.trim();
                        if (res.equals("1")) {
                            Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (HttpHostConnectException e) {

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //已经验证
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();


                }

            } else {
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(Missmm.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }
        };
    };

    public int check(){
        if(!password.getText().toString().equals(password1.getText().toString())){
            Toast.makeText(this,"密码不一致",Toast.LENGTH_SHORT);
            return 0;
        }
        if(password.getText().toString().length()>20 || password.getText().toString().length()>20){
            Toast.makeText(this,"密码限制在6—20个字符",Toast.LENGTH_SHORT);
            return 0;
        }
        return 1;
    }
}

