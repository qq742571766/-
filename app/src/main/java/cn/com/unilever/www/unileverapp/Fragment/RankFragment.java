package cn.com.unilever.www.unileverapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.activity.FunctionActivity;
import cn.com.unilever.www.unileverapp.config.MyConfig;

/**
 * @class 主管/员工
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/7/14 10:00
 */
public class RankFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private View view;
    private ManagerclassifyFragment managerclassifyFragment;
    private ErrorFragment errorFragment;
    private Context context;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rank, null,false);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.mToolbar);
//        toolbar.setTitle("异常填写");
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initview();
    }

    private void initview() {
        Button manager = (Button) view.findViewById(R.id.manager);
        Button staff = (Button) view.findViewById(R.id.staff);
        manager.setOnClickListener(this);
        staff.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manager:
                if (managerclassifyFragment == null) {
                    managerclassifyFragment = new ManagerclassifyFragment();
                }
                ((FunctionActivity) getActivity()).changFragment(managerclassifyFragment);
                break;
            case R.id.staff:
                MyConfig.type = "STAFFTAG";
                if (errorFragment == null) {
                    errorFragment = new ErrorFragment();
                }
                ((FunctionActivity) getActivity()).changFragment(errorFragment);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
