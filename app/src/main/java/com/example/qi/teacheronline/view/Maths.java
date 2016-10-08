package com.example.qi.teacheronline.view;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qi.teacheronline.MainActivity;
import com.example.qi.teacheronline.R;
import com.example.qi.teacheronline.controller.Getinfoof;
//import com.example.qi.teacheronline.util.Myappliction;
import com.example.qi.teacheronline.util.Settitle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by qi on 2016/1/31.
 */
public class Maths extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private TextView tv;
    private ListView listView;
    private SimpleAdapter simpleAdapter = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Map<String,Object>> list;
    private Getinfoof getInfoof;
    private int s=0;
    private int f=20;

    int lastItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maths);


        Toolbar toolbar = (Toolbar) findViewById(R.id.mathstoolbar);
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

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.mathsrl);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(this);

        listView = (ListView)findViewById(R.id.mathlistView);
        //Getinfoof getInfoof = new Getinfoof();         //从数据库获取数据生成arraylist 对象
       // simpleAdapter = new SimpleAdapter(this, getInfoof.setinfo(getIntent().getStringExtra("view")), R.layout.listitem, new String[]{"tittle","tzid", "info"}, new int[]{R.id.listview1,R.id.listitemid, R.id.listview2});
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSimpleAdapter();
                handler.sendEmptyMessage(0);
            }
        }).start();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = (TextView) view.findViewById(R.id.listitemid);
                    //Log.i("qwe","1");
                    Intent intent = new Intent(Maths.this, Tiezi.class);
                    //Log.i("qwe","2");
                    intent.putExtra("tzid", tv.getText().toString());
                    //Log.i("qwe","3");
                    startActivity(intent);
                    //Log.i("qwe","4");

                //Toast.makeText(getApplicationContext(), position+"qwe"+tv.getText().toString() , Toast.LENGTH_SHORT).show();
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
                lastItem = firstVisibleItem + visibleItemCount - 1 ;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mathsfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "你确定要发帖么？", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Maths.this, Fatie.class);
                                intent.putExtra("view", getIntent().getStringExtra("view").toString());
                                startActivity(intent);
                            }
                        }).show();
            }
        });


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshlist");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //APPBAR 的监听事件
        int id = item.getItemId();
        if(id == R.id.ab_search){
            onSearchRequested();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getSimpleAdapter() {
        getInfoof = new Getinfoof();         //从数据库获取数据生成arraylist 对象
        list = getInfoof.setinfo(getIntent().getStringExtra("view"),s,f);
        simpleAdapter = new SimpleAdapter(this,list, R.layout.listitem, new String[]{"tittle","tzid", "info"}, new int[]{R.id.listview1,R.id.listitemid, R.id.listview2});
    }

    @Override
    public void onRefresh(){
        //Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
        list.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                list.addAll(getInfoof.setinfo(getIntent().getStringExtra("view"), s, f));
                handler.sendEmptyMessage(1);
            }
        }).start();
        //Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
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
        if (action.equals("action.refreshlist"))
        {
            onRefresh();
        }
    }
};

//    public boolean onSearchRequested() {
//        // 除了输入查询的值，还可额外绑定一些数据
//        Bundle appSearchData = new Bundle();
//        appSearchData.putString("KEY", "text");
//
//        startSearch(null, false, appSearchData, false);
//        // 必须返回true。否则绑定的数据作废
//        return true;
//    }
}
