package cn.com.unilever.www.unileverapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.info.ObInfo;

public class MobileSoftAdapter extends MyBaseAdapter<ObInfo> {
    public Context context;
    private OnCheckedChangeListener l = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (Integer) buttonView.getTag();
            ObInfo info = datas.get(position);
            info.tv_or = datas.get(position).tv_or;
            datas.add(position, info);

        }
    };

    public MobileSoftAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_observation_list, null);
            holder.tv_or = (TextView) convertView.findViewById(R.id.tv_or);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_or.setText(datas.get(position).tv_or);
        return convertView;
    }

    class ViewHolder {
        TextView tv_or;
    }
}
