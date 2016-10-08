package com.example.qi.teacheronline.view;

import android.app.SearchManager;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.qi.teacheronline.R;
import com.example.qi.teacheronline.controller.Getinfolike;
import com.example.qi.teacheronline.controller.Getmytz;
import com.example.qi.teacheronline.model.Userinfo;

import java.util.List;
import java.util.Map;

/**
 * Created by qigang on 16/4/4.
 */
public class Searchact extends AppCompatActivity {
    private ListView listView;
    private SimpleAdapter simpleAdapter = null;
    private List<Map<String,Object>> list;
    private Getinfolike getinfolike;
    private int s=0;
    private int f=20;

    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchact);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
        }

//        Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
//        if (appData != null) {
//            String testValue = appData.getString("KEY");
//            Toast.makeText(this, testValue, Toast.LENGTH_SHORT).show();
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        toolbar.setTitle(query);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        listView = (ListView)findViewById(R.id.searchlistView);
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
                Intent intent = new Intent(Searchact.this, Tiezi.class);
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
                            //Toast.makeText(Searchact.this,"1",Toast.LENGTH_SHORT).show();
                            onRefresh();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

    }

//    private void doMySearch(String query) {
//        // TODO 自动生成的方法存根
//
//        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
//    }


    public void onRefresh(){
        //Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
        list.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                list.addAll(getinfolike.getinfo(query, s, f));
                handler.sendEmptyMessage(1);
            }
        }).start();

        //Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
    }

    public void getSimpleAdapter() {
        getinfolike = new Getinfolike();         //从数据库获取数据生成arraylist 对象
        list = getinfolike.getinfo(query, s, f);
        simpleAdapter = new SimpleAdapter(this,list, R.layout.listitem, new String[]{"tittle","tzid", "info"}, new int[]{R.id.listview1,R.id.listitemid, R.id.listview2});
    }

    android.os.Handler handler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    listView.setAdapter(simpleAdapter);
                    break;
                case 1:
                    simpleAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    onRefresh();
                    break;
            }
        }
    };

}
