package cn.com.unilever.www.unileverapp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 我的基础适配器类，通过泛型<DataType>指定与该适配器相关联的数据的数据类型
 */
public abstract class MyBaseAdapter<DataType> extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    List<DataType> datas = new ArrayList<>();

    MyBaseAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    //从集合的第一个位置开始添加多组数据
    public void addDatasToFirst(List<DataType> datas) {
        this.datas.addAll(datas);
    }

    //清空所有数据
    public void clearDatas() {
        datas.clear();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public DataType getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
