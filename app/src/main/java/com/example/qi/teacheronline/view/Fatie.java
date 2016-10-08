package com.example.qi.teacheronline.view;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qi.teacheronline.R;
import com.example.qi.teacheronline.controller.Setinfo;
import com.example.qi.teacheronline.controller.Setpic;
import com.example.qi.teacheronline.model.Userinfo;
import com.example.qi.teacheronline.util.Checkflag;
import com.example.qi.teacheronline.util.Settitle;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qi on 2016/2/2.
 */
public class Fatie extends AppCompatActivity{
    private Button button;
    private TextView textView,tittlettv,contenttv;
    private int picnum = 0;
    private ArrayList<String> uris = new ArrayList<String>();
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fatie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.fatietoolbar);
        Settitle settitle = new Settitle();
        toolbar.setTitle(settitle.set(getIntent().getStringExtra("view")));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tittlettv = (TextView)findViewById(R.id.fatie1);
        contenttv = (TextView)findViewById(R.id.fatie2);
        textView = (TextView)findViewById(R.id.testpic);
        button = (Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String IMAGE_TYPE = "image/*";
                final int IMAGE_CODE = 0;
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType(IMAGE_TYPE);
                startActivityForResult(getAlbum, IMAGE_CODE);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fatiefab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "你确定要发帖么？", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //ProgressDialog.show(Fatie.this, "登录中", "      正在登录...", false, true);
                                if (check() == 0) {
                                    return;
                                }
                                Checkflag checkflag = new Checkflag();
                                if (checkflag.check(tittlettv.getText().toString()) || checkflag.check(contenttv.getText().toString())) {
                                    Toast.makeText(Fatie.this, "不可包含字符'<cont>'或'<conts>'", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                SharedPreferences preferences = getSharedPreferences("itke", MODE_PRIVATE);

                                final String account = preferences.getString("account",null).trim().toString();
                                final String name = preferences.getString("name",null).trim().toString();
                                final String family = getIntent().getStringExtra("view");
                                final String tittle = tittlettv.getText().toString();
                                final String content = contenttv.getText().toString();
                                final Setinfo setinfo = new Setinfo();
                                final int mun = picnum;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        id = setinfo.setinfo(account, name, family, tittle, content, mun);
                                        for (int i = 0; i < uris.size(); i++) {
                                            postpic(uris.get(i), id, i);
                                        }
                                        handler.sendEmptyMessage(0);
                                    }
                                }).start();
                            }
                        }).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量

            Log.e("qwe", "ActivityResult resultCode error");
            return;

        }

        Bitmap bm = null;

        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口

        ContentResolver resolver = getContentResolver();
        //此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == 0) {

            try {

                Uri originalUri = data.getData();        //获得图片的uri
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);        //显得到bitmap图片

                ViewGroup viewGroup = (ViewGroup)findViewById(R.id.fatieaddimage);
                ImageView imageView1 = new ImageView(this);
                imageView1.setImageBitmap(bm);
                imageView1.setPadding(5,5,5,5);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(666,666);
                viewGroup.addView(imageView1,lp);

                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                //获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                //将光标移至开头 ，否则容易引起越界
                cursor.moveToFirst();
                //根据索引值获取图片路径
                String qwe = cursor.getString(column_index);
                //textView.setText(qwe);

                uris.add(qwe);
                picnum++;

            }catch (IOException e) {

                Log.e("qwe",e.toString());

            }

        }

    }


    private void postpic(String files,String id,int no){

        String gid = id;
        File file = new File(files);
        String httpUrl = "http://115.159.92.239:8080/setinfo.jsp" + "?gid=" + gid+"&no="+no;
        HttpPost request = new HttpPost(httpUrl);
        HttpClient httpClient = new DefaultHttpClient();
        FileEntity entity = new FileEntity(file, "binary/octet-stream");
        HttpResponse response;
        try {
            request.setEntity(entity);
            entity.setContentEncoding("binary/octet-stream");
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

               // textView.setText("success");
            } else {
                //textView.setText(response.getStatusLine().getStatusCode()+"@qwe@"+HttpStatus.SC_OK);
            }
        } catch (Exception e) {
            textView.setText("error"+e.toString());
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();     //发送广播刷新前一个界面
            intent.setAction("action.refreshlist");
            sendBroadcast(intent);
            finish();
        }
    };

    public int check(){
        if(tittlettv.getText().toString().length()>10 || tittlettv.getText().toString().length()<1){
            Toast.makeText(this,"控制标题的长度",Toast.LENGTH_SHORT).show();
            return 0;
        }
        return 1;
    }
}
