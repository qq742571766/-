package cn.com.unilever.www.unileverapp.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 我的基础适配器类，通过泛型<DataType>指定与该适配器相关联的数据的数据类型
 */
public abstract class MyBaseAdapter<DataType> extends BaseAdapter{
	protected Context context;
	protected LayoutInflater inflater;
	protected List<DataType> datas = new ArrayList<DataType>();

	public MyBaseAdapter(Context context){
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	//每次只添加一组数据
	public void addDataToAdapter(DataType data){
		datas.add(data);
	}

	//从集合的第一个位置开始添加多组数据
	public void addDatasToFirst(List<DataType> datas){
		this.datas.addAll(datas);
	}

	//从集合的最后一个位置开始追加多组数据
	public void addDatasToLast(List<DataType> datas){
		this.datas.addAll(datas.size(), datas);
	}

	//移除指定位置的数据
	public void removeData(int index){
		datas.remove(index);
	}

	//清空所有数据
	public void clearDatas(){
		datas.clear();
	}

	//返回集合内所有的数据
	public List<DataType> getAllDatas(){
		return datas;
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
