package com.bawei.test.houyongliang20161121;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;


public class RecyclerViewActivity extends AppCompatActivity {

    private static String  TAG = "RecyclerViewActivity";
    private RecyclerView ryview;
    private String url="http://japi.juhe.cn/joke/content/list.from?key= 874ed931559ba07aade103eee279bb37 &page=2&pagesize=10&sort=asc&time=1418745237";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        /*获取恐惧爱你*/
        ryview = (RecyclerView) findViewById(R.id.ryview);
        initData();
    }
/*初始化数据*/
    private void initData() {
        //请求网络数据
        OkHttp.getAsync(url, new OkHttp.DataCallBack() {

            private MyAdapter adapter;

            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(RecyclerViewActivity.this, "亲，网络数据加载失败。。", Toast.LENGTH_SHORT).show();
            }
/*请求成功*/
            @Override
            public void requestSuccess(String result) throws Exception {
                Log.e(TAG, "requestSuccess: "+result);
                Bean bean = new Gson().fromJson(result, Bean.class);
                final List<Bean.ResultBean.DataBean> data = bean.getResult().getData();
               /*更新数据*/
                ryview.setLayoutManager(new LinearLayoutManager(RecyclerViewActivity.this));
                adapter = new MyAdapter(RecyclerViewActivity.this, data);
                ryview.setAdapter(adapter);
                /*条目点击事件*/
                ryview.addOnItemTouchListener(new RecyclerViewClickListener(RecyclerViewActivity.this, ryview,
                        new RecyclerViewClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Toast.makeText(RecyclerViewActivity.this, data.get(position).getContent(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        }));
            }
        });
    }
}
