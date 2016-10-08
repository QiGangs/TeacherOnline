package com.example.qi.teacheronline.view;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qi.teacheronline.R;
import com.example.qi.teacheronline.controller.Deletemytz;
import com.example.qi.teacheronline.controller.Getdetail;
import com.example.qi.teacheronline.controller.Getinfoof;
import com.example.qi.teacheronline.controller.Reporttiezi;
import com.example.qi.teacheronline.controller.Setsc;
import com.example.qi.teacheronline.model.Userinfo;
//import com.example.qi.teacheronline.util.Myappliction;
import com.example.qi.teacheronline.util.Noscrollview;
import com.example.qi.teacheronline.util.Settitle;
import com.example.qi.teacheronline.view.Maths;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qi on 2016/2/9.
 */
public class Tiezi extends AppCompatActivity{

    private int flagnum = 0;

    private ListView listView;
    private TextView header1,header2;
    private List<Map<String,Object>> list;
    private Getdetail getdetail;
    private String resultcopy1,resultcopy2;
    private SimpleAdapter simpleAdapter;
    private ArrayList<String> urls= new ArrayList<String>();
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refresh"))
            {
                list.clear();
                list.addAll(getdetail.getgzinfo(getIntent().getStringExtra("tzid"), resultcopy1, resultcopy2));
                simpleAdapter.notifyDataSetChanged();
            }
        }
    };

    private ViewGroup viewGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiezi);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().
                detectDiskWrites().detectNetwork().penaltyLog().build());

        getdetail = new Getdetail();
        final String[] result = getdetail.getftzinfo(getIntent().getStringExtra("tzid"));
        resultcopy1 = result[1];
        resultcopy2 = result[2];

        Toolbar toolbar = (Toolbar) findViewById(R.id.tiezitoolbar);
        toolbar.setTitle(result[0]);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View headerview = getLayoutInflater().inflate(R.layout.listviewheader, null);
        header1 = (TextView)headerview.findViewById(R.id.header1);              //该布局还未初始化
        header2 = (TextView)headerview.findViewById(R.id.header2);
        header1.setText(result[1]);
        header2.setText("作者："+result[2]);
        final int picnum = new Integer(result[3]);
        if( picnum > 0){
            viewGroup = (ViewGroup)headerview.findViewById(R.id.tieziheaderview);
            for(int i=0;i<picnum;i++){
                String url = "http://115.159.92.239:8080/image/"+getIntent().getStringExtra("tzid")+"/"+i+".jpg";
                urls.add(url);
            }
            for(int i=0;i<picnum;i++) {
                final int ii = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = getHttpBitmap(urls.get(ii));
                        Message message = new Message();
                        message.obj = bitmap;
                        message.what=picnum;
                        handler1.sendMessage(message);
                    }
                }).start();


            }
        }

        View footview = getLayoutInflater().inflate(R.layout.footview, null);

        list = getdetail.getgzinfo(getIntent().getStringExtra("tzid"), result[1], result[2]);
        listView = (Noscrollview)findViewById(R.id.tiezilistView);
        listView.addHeaderView(headerview, null, false);
        listView.addFooterView(footview,null,false);
        simpleAdapter = new SimpleAdapter
                (this,list , R.layout.listitem2, new String[]{"text", "name"}, new int[]{R.id.listviews1, R.id.listviews2});
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        //Toast.makeText(Tiezi.this, getdetail.getgzinfo(getIntent().getStringExtra("tzid"), result[1], result[2]).toString() ,Toast.LENGTH_SHORT).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tiezifab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "你确定要回复么？", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Tiezi.this, Huifu.class);
                                intent.putExtra("tzid", getIntent().getStringExtra("tzid"));

                                startActivity(intent);
                            }
                        }).show();
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.tiezifab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "你确定要收藏么？", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Setsc setsc = new Setsc();
                                SharedPreferences preferences = getSharedPreferences("itke", MODE_PRIVATE);
                                String account = preferences.getString("account",null).trim().toString();
                                if (setsc.issc(account, getIntent().getStringExtra("tzid")).equals("1")) {
                                    Toast.makeText(Tiezi.this,"该帖子已收藏",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                setsc.setsc(account, getIntent().getStringExtra("tzid"), result[0]);
                            }
                        }).show();
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refresh");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);


    }

    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 2){
            Intent intent = new Intent();     //发送广播刷新前一个界面
            intent.setAction("action.refreshsc");
            sendBroadcast(intent);}
            else if(msg.what == 1){
                Intent intent = new Intent();     //发送广播刷新前一个界面
                intent.setAction("action.refreshtz");
                sendBroadcast(intent);
            }

            finish();
        }
    };

    private Handler handler1 = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            ImageView imageView1 = new ImageView(Tiezi.this);
            imageView1.setId(flagnum%msg.what);
            imageView1.setImageBitmap((Bitmap) msg.obj);
            imageView1.setPadding(5, 5, 5, 5);
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertimg(v.getId());
                }
            });
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(666,666);
            viewGroup.addView(imageView1,lp);
            flagnum++;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settingx, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //APPBAR 的监听事件
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            new  AlertDialog.Builder(this)
                    .setTitle("删除确认" )
                    .setMessage("确定删除该帖子么" )
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (getIntent().getStringExtra("flag") == null) {
                                Toast.makeText(Tiezi.this,"请从我的帖子或我的收藏执行此操作",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Deletemytz deletemytz = new Deletemytz();
                                    deletemytz.delete(getIntent().getStringExtra("tzid"), getIntent().getStringExtra("flag"));
                                    if(getIntent().getStringExtra("flag").equals("1")){
                                        handler.sendEmptyMessage(1);
                                    }
                                    else if(getIntent().getStringExtra("flag").equals("2")){
                                        handler.sendEmptyMessage(2);
                                    }
                                }
                            }).start();
                        }
                    })
                    .setNegativeButton("否", null)
                    .show();
            return true;
        }else if(id == R.id.action_report){
            new  AlertDialog.Builder(this)
                    .setTitle("举报确认" )
                    .setMessage("确定举报该帖子么" )
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String mainid = getIntent().getStringExtra("tzid");
                            SharedPreferences preferences = getSharedPreferences("itke", MODE_PRIVATE);
                            final String reporter = preferences.getString("name",null).trim().toString();
                            final String reportacc = preferences.getString("account",null).trim().toString();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Reporttiezi reporttiezi = new Reporttiezi();
                                    if (1 == reporttiezi.isreport(mainid)) {
                                        return;
                                    }
                                    reporttiezi.report(mainid,reporter,reportacc);
                                    handler4.sendEmptyMessage(0);
                                }
                            }).start();
                        }
                    })
                    .setNegativeButton("否", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void alertimg(final int x){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getHttpBitmap(urls.get(x));
                Message message = new Message();
                message.obj = bitmap;
                handler3.sendMessage(message);

            }
        }).start();
    }

    Handler handler3 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            LayoutInflater inflater = LayoutInflater.from(Tiezi.this);
            View imgEntryView = inflater.inflate(R.layout.showphoto, null); // 加载自定义的布局文件
            final AlertDialog dialog = new AlertDialog.Builder(Tiezi.this).create();
            ImageView img = (ImageView)imgEntryView.findViewById(R.id.bigimage);
            imgEntryView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    dialog.cancel();
                }
            });

            img.setImageBitmap((Bitmap) msg.obj);
            dialog.setView(imgEntryView); // 自定义dialog
            dialog.show();
        }
    };

    Handler handler4 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(Tiezi.this,"举报成功",Toast.LENGTH_SHORT).show();
        }
    };

}
