package cn.com.unilever.www.unileverapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.activity.FunctionActivity;
import cn.com.unilever.www.unileverapp.config.MyConfig;
import cn.com.unilever.www.unileverapp.data.list_josn;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @class 历史列表
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/8/2 16:56
 */
public class HistoryFM extends Fragment implements View.OnClickListener,View.OnTouchListener {
    private View view;
    private TextView xiangqing1;
    private FrameLayout rl;
    private TextView xiangqing2;
    private RelativeLayout rl_title;
    private Button button;
    private ErrorCollectFragment fm;
    private list_josn josn;

    @Override
    public void onAttach(Context context) {
        josn = getArguments().getParcelable("josn");
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_h, null);
        view.setOnTouchListener(this);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.mToolbar);
        toolbar.setTitle("历史列表");
        initWidget();
        return view;
    }

    private void initWidget() {
        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.imageView_f);
        Picasso.with(getActivity())
                .load(MyConfig.url + "/errorImg/" + josn.getUrl())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imageView);
        TextView textView_title = (TextView) view.findViewById(R.id.textView_title);
        textView_title.setText(josn.getErrorContent());
        TextView f_date = (TextView) view.findViewById(R.id.f_date);
        f_date.setText(josn.getErrorTime().split(" ")[0]);
        TextView textView_author_name = (TextView) view.findViewById(R.id.textView_author_name);
        textView_author_name.setText(josn.getCreateUserId());
        TextView tv_errorType = (TextView) view.findViewById(R.id.tv_errorType);
        tv_errorType.setText(josn.getErrorType());
        TextView tv_area = (TextView) view.findViewById(R.id.tv_area);
        tv_area.setText(josn.getArea());
        TextView tv_factory = (TextView) view.findViewById(R.id.tv_factory);
        tv_factory.setText(josn.getBaseFactory());
        TextView tv_errorTime = (TextView) view.findViewById(R.id.tv_errorTime);
        tv_errorTime.setText(josn.getErrorTime());
        TextView tv_errorContent = (TextView) view.findViewById(R.id.tv_errorContent);
        tv_errorContent.setText(josn.getErrorContent());
        TextView tv_ren = (TextView) view.findViewById(R.id.tv_ren);
        tv_ren.setText(josn.getCloseUserId());
        TextView tv_fa = (TextView) view.findViewById(R.id.tv_fa);
        tv_fa.setText(josn.getError_close_idea());
        xiangqing1 = (TextView) view.findViewById(R.id.xiangqing1);
        xiangqing2 = (TextView) view.findViewById(R.id.xiangqing2);
        rl = (FrameLayout) view.findViewById(R.id.rl);
        rl_title = (RelativeLayout) view.findViewById(R.id.rl_title);
        rl_title.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_title:
                if (xiangqing2.getVisibility() == View.GONE && rl.getVisibility() == View.GONE) {
                    xiangqing1.setVisibility(View.GONE);
                    xiangqing2.setVisibility(View.VISIBLE);
                    rl.setVisibility(View.VISIBLE);
                } else {
                    xiangqing1.setVisibility(View.VISIBLE);
                    xiangqing2.setVisibility(View.GONE);
                    rl.setVisibility(View.GONE);
                }
                break;
            case R.id.button:
                if (fm == null) {
                    fm = new ErrorCollectFragment();
                }
                ((FunctionActivity) getActivity()).changFragment(fm);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
