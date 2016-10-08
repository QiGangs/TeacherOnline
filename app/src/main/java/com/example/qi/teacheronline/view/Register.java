package com.example.qi.teacheronline.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qi.teacheronline.MainActivity;
import com.example.qi.teacheronline.R;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.qi.teacheronline.controller.Isaccount;
import com.example.qi.teacheronline.util.CustomHttpClient;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * Created by qi on 2016/1/17.
 */
public class Register extends AppCompatActivity{
    private Button bt,bty;
    private TextView account,name,password,password1;
    private TextView reginfo;
    private TextView yanzhen;
    private static String APPKEY = "f3d1189823d0";
    private static String APPSECRET = "9d60291f4f7cfb9e1b3d4b007021bd5d";
    public String phString;
     public int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()        //是主线程中可以调用网络
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarreg);
        toolbar.setTitle("新用户注册");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {      //用于给导航按钮田间监听事件
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bty = (Button)findViewById(R.id.button);
        yanzhen = (TextView)findViewById(R.id.yanzhen);
        reginfo = (TextView)findViewById(R.id.reginfo);
        account = (TextView)findViewById(R.id.account);
        name = (TextView)findViewById(R.id.name);
        password = (TextView)findViewById(R.id.password);
        password1 = (TextView)findViewById(R.id.password1);

        bty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSSDK.getVerificationCode("86",account.getText().toString());
                phString=account.getText().toString();
            }
        });


        bt = (Button)findViewById(R.id.zuchebt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Isaccount isaccount = new Isaccount();
                String flag = isaccount.isaccount(account.getText().toString());
                if(flag.equals("1")){
                    Toast.makeText(getApplicationContext(), "该账号已被注册" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if (check() == 0){
                    return;
                }
                //先判断短信是否成功验证
                SMSSDK.submitVerificationCode("86", phString, yanzhen.getText().toString());

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
            Log.e("event", "event="+event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result"+event);
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    postParameters.add(new BasicNameValuePair("account", account.getText().toString()));
                    postParameters.add(new BasicNameValuePair("username", name.getText().toString()));
                    postParameters.add(new BasicNameValuePair("password", password.getText().toString()));

                    String response = null;
                    try {
                        response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/register.jsp", postParameters);  //Enetr Your remote PHP,ASP, Servlet file link
                        String res = response.toString();
                        //res = res.replaceAll("\\s+", "");
                        res = res.trim();
                        if (res.equals("1")) {
                            reginfo.setText("success");
                            finish();
                        } else {
                            reginfo.setText("error");
                        }
                    } catch (HttpHostConnectException e) {

                    } catch (Exception e) {
                        reginfo.setText(e.toString());
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
                        Toast.makeText(Register.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }
        };
    };

    Handler mHandler1 = new Handler() {
        public void handleMessage(Message msg) {

        }
        };

    public int check(){
        if(name.getText().toString().length()>10 || name.getText().toString().length()<2){
            Toast.makeText(this,"呢称限制在2—10个字符",Toast.LENGTH_SHORT);
            return 0;
        }
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
