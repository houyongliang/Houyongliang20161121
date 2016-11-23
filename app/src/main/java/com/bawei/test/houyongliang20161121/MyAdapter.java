package com.bawei.test.houyongliang20161121;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static com.baidu.location.b.g.l;
import static com.baidu.location.b.g.p;

/**
 * Created by mis on 2016/11/21.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    /*设置适配器*/
    private  Context context;
    private List<Bean.ResultBean.DataBean> list;
    private final LayoutInflater inflater;

    public MyAdapter(Context context,List<Bean.ResultBean.DataBean> list){
        this.context=context;
        this.list=list;
        inflater = LayoutInflater.from(context);
    }
    /*创建条目*/
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.ryview_item, parent, false);
        return new MyViewHolder(view);
    }
/*设置 条目内容*/
    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
        holder.tv_item.setText(list.get(position).getContent());
    }
/*获取条目个数*/
    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }
/*viewHolder 创建并获取控件*/
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item= (TextView) itemView.findViewById(R.id.tv_item);
        }
    }
}
