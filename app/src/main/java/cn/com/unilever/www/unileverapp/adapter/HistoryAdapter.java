package cn.com.unilever.www.unileverapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.config.MyConfig;
import cn.com.unilever.www.unileverapp.data.list_josn;
import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.mViewHolder> {
    private static ImageView views;
    private List<list_josn> datas;
    private OnButtonClickListener listener = null;
    private Context context;

    public void adds(List<list_josn> datas) {
        this.datas = datas;
    }

    @Override
    public mViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_error_list, null);
        this.context = parent.getContext();
        final mViewHolder holder = new mViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    list_josn josn = datas.get(position);
                    listener.OnButtonClick(josn);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        list_josn josn = datas.get(position);
        Picasso.with(context)
                .load(MyConfig.url + "/errorImg/" + josn.url)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.thumbnail_pic_s);
        holder.title.setText(josn.getErrorContent());
        holder.author_name.setText(josn.getCreateUserId());
        holder.date.setText(josn.getErrorTime());
    }

    @Override
    public int getItemCount() {
        if (datas == null || datas.size() <= 0) {
        } else {
            return datas.size();
        }
        return 0;
    }

    public void setOnButtonClickListener(OnButtonClickListener l) {
        this.listener = l;
    }

    public interface OnButtonClickListener {
        void OnButtonClick(list_josn info);
    }

    static class mViewHolder extends RecyclerView.ViewHolder {
        CircleImageView thumbnail_pic_s;
        TextView title;
        TextView date;
        TextView author_name;
        View newsview;

        mViewHolder(View itemView) {
            super(itemView);
            newsview = itemView;
            //图片
            thumbnail_pic_s = (CircleImageView) itemView.findViewById(R.id.imageView_f);
            //标题
            title = (TextView) itemView.findViewById(R.id.textView_title);
            //时间
            date = (TextView) itemView.findViewById(R.id.f_date);
            //上传者
            author_name = (TextView) itemView.findViewById(R.id.textView_author_name);
            HistoryAdapter.views = thumbnail_pic_s;
        }
    }
}