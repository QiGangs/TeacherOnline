package com.example.qi.teacheronline.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.qi.teacheronline.R;
import com.example.qi.teacheronline.controller.Getmysc;
import com.example.qi.teacheronline.controller.Getmytz;
import com.example.qi.teacheronline.model.Userinfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/12.
 */
public class Mysc extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private ListView listView;
    private SimpleAdapter simpleAdapter = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Map<String,Object>> list;
    private Getmysc getmysc;
    private int s=0;
    private int f=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mysctoolbar);
        toolbar.setTitle(getIntent().getStringExtra("view"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.myscsrl);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(this);

        listView = (ListView)findViewById(R.id.mysclistView);
        //Getinfoof getInfoof = new Getinfoof();         //从数据库获取数据生成arraylist 对象
        // simpleAdapter = new SimpleAdapter(this, getInfoof.setinfo(getIntent().getStringExtra("view")), R.layout.listitem, new String[]{"tittle","tzid", "info"}, new int[]{R.id.listview1,R.id.listitemid, R.id.listview2});
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSimpleAdapter();
                handler.sendEmptyMessage(0);
            }
        }).start();
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.listitemid);
                Intent intent = new Intent(Mysc.this, Tiezi.class);
                intent.putExtra("flag", "2");
                intent.putExtra("tzid", tv.getText().toString());
                startActivity(intent);
                //              Toast.makeText(getApplicationContext(), position+"qwe"+tv.getText().toString() , Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            //Toast.makeText(getApplicationContext(), f+"", Toast.LENGTH_SHORT).show();
                            f = f + 20;
                            onRefresh();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshsc");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    public void onRefresh(){
        //Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
        list.clear();
        SharedPreferences preferences = getSharedPreferences("itke", MODE_PRIVATE);
        final String acconut= preferences.getString("account",null).trim().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                list.addAll(getmysc.getmysc(acconut, s, f));
                handler.sendEmptyMessage(1);
            }
        }).start();

        //Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
    }

    public void getSimpleAdapter() {
        getmysc = new Getmysc();         //从数据库获取数据生成arraylist 对象
        SharedPreferences preferences = getSharedPreferences("itke", MODE_PRIVATE);
        final String acconut= preferences.getString("account",null).trim().toString();
        list = getmysc.getmysc(acconut, s, f);
        simpleAdapter = new SimpleAdapter(this,list, R.layout.listitem3, new String[]{"tittle","tzid",}, new int[]{R.id.listview1,R.id.listitemid});
    }

    android.os.Handler handler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    listView.setAdapter(simpleAdapter);
                    break;
                case 1:
                    simpleAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }

        }
    };

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshsc"))
            {
                onRefresh();
            }
        }
    };

}
