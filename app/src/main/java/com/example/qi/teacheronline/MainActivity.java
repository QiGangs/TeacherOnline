package com.example.qi.teacheronline;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.qi.teacheronline.controller.Getuserinfo;
import com.example.qi.teacheronline.controller.testzw;
import com.example.qi.teacheronline.model.Userinfo;
import com.example.qi.teacheronline.util.CustomHttpClient;
import com.example.qi.teacheronline.view.Maths;
import com.example.qi.teacheronline.view.Missmm;
import com.example.qi.teacheronline.view.Mysc;
import com.example.qi.teacheronline.view.Mytiezi;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private TextView textView,textView1;
    //private ListView listView;
    private TextView tvmat,tvbcyy,tvsjjg,tvydkf,tvjavaee,tvczxt,tvwljs,tvfzyx;
    private NavigationView navigationView;

    private SharedPreferences preferences=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("IT客");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //首先需要获取抽屉布局才能获取其中的控件

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        textView = (TextView)headerLayout.findViewById(R.id.loname);
        textView1 = (TextView)headerLayout.findViewById(R.id.loaccount);
        preferences = getSharedPreferences("itke", MODE_PRIVATE);
        preferences.getString("account",null).trim().toString();
        textView.setText("昵称: "+preferences.getString("name",null).trim().toString());
        textView1.setText("账号: "+preferences.getString("account",null).trim().toString());

        tvmat = (TextView)findViewById(R.id.textView);
        tvmat.setOnClickListener(this);
        tvbcyy = (TextView)findViewById(R.id.textView1);
        tvbcyy.setOnClickListener(this);
        tvsjjg = (TextView)findViewById(R.id.textView2);
        tvsjjg.setOnClickListener(this);
        tvwljs = (TextView)findViewById(R.id.textView3);
        tvwljs.setOnClickListener(this);
        tvydkf = (TextView)findViewById(R.id.textView4);
        tvydkf.setOnClickListener(this);
        tvjavaee = (TextView)findViewById(R.id.textView5);
        tvjavaee.setOnClickListener(this);
        tvczxt = (TextView)findViewById(R.id.textView6);
        tvczxt.setOnClickListener(this);
        tvfzyx = (TextView)findViewById(R.id.textView10);
        tvfzyx.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //APPBAR 的监听事件
//        int id = item.getItemId();
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//           // Toast.makeText(MainActivity.this, textView.getText().toString(), Toast.LENGTH_SHORT).show();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_mytz) {
            Intent intent = new Intent(MainActivity.this, Mytiezi.class);
            intent.putExtra("view","我的帖子");
            startActivity(intent);
        } else if (id == R.id.nav_gtz) {
            Intent intent = new Intent(MainActivity.this, Mysc.class);
            intent.putExtra("view","我的收藏");
            startActivity(intent);
        } else if (id == R.id.nav_setpsword) {
            Intent intent = new Intent(MainActivity.this, Missmm.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            new  AlertDialog.Builder(this)
                    .setTitle("退出确认" )
                    .setMessage("确定退出登录么" )
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences preferences = getSharedPreferences("itke",MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("account",1+"");
                            editor.putString("psword",1+"");
                            editor.commit();
                            finish();
                        }
                    })
                    .setNegativeButton("否", null)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.textView){
            Intent intent = new Intent(MainActivity.this, Maths.class);
            intent.putExtra("view","math");
            startActivity(intent);
        }else if(v.getId() == R.id.textView1){
            Intent intent = new Intent(MainActivity.this, Maths.class);
            intent.putExtra("view","bcyy");
            startActivity(intent);
        }else if(v.getId() == R.id.textView2){
            Intent intent = new Intent(MainActivity.this, Maths.class);
            intent.putExtra("view","sjjg");
            startActivity(intent);
        }else if(v.getId() == R.id.textView3){
            Intent intent = new Intent(MainActivity.this, Maths.class);
            intent.putExtra("view","wljs");
            startActivity(intent);
        }else if(v.getId() == R.id.textView4){
            Intent intent = new Intent(MainActivity.this, Maths.class);
            intent.putExtra("view","ydkf");
            startActivity(intent);
        }else if(v.getId() == R.id.textView5){
            Intent intent = new Intent(MainActivity.this, Maths.class);
            intent.putExtra("view","javaee");
            startActivity(intent);
        }else if(v.getId() == R.id.textView6){
            Intent intent = new Intent(MainActivity.this, Maths.class);
            intent.putExtra("view","czxt");
            startActivity(intent);
        }else if(v.getId() == R.id.textView10){
            Intent intent = new Intent(MainActivity.this, Maths.class);
            intent.putExtra("view","fzyx");
            startActivity(intent);
    }
    }

}
