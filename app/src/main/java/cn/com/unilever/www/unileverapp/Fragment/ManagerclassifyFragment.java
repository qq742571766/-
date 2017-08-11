package cn.com.unilever.www.unileverapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.com.unilever.www.unileverapp.R;
import cn.com.unilever.www.unileverapp.activity.FunctionActivity;
import cn.com.unilever.www.unileverapp.config.MyConfig;

/**
 * @class 主管类别
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/7/14 10:10
 */
public class ManagerclassifyFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private SMATFragment smat;
    private View view;
    private ErrorFragment errorFragment;
    private DCAFragment dca;
    private ErrorCollectFragment errorcollectfragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_managerclass, null);
        view.setOnTouchListener(this);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.mToolbar);
        toolbar.setTitle("异常填写");
        initview();
        return view;
    }

    private void initview() {
        ImageView SMAT = (ImageView) view.findViewById(R.id.SMAT);
        ImageView DAC = (ImageView) view.findViewById(R.id.DCA);
        ImageView EMAT = (ImageView) view.findViewById(R.id.EMAT);
        ImageView TAG = (ImageView) view.findViewById(R.id.TAG);
        ImageView btn_shujus = (ImageView) view.findViewById(R.id.btn_shujus);
        SMAT.setOnClickListener(this);
        DAC.setOnClickListener(this);
        EMAT.setOnClickListener(this);
        TAG.setOnClickListener(this);
        btn_shujus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shujus:
                if (errorcollectfragment == null) {
                    errorcollectfragment = new ErrorCollectFragment();
                }
                ((FunctionActivity) getActivity()).changFragment(errorcollectfragment);
                break;
            case R.id.SMAT:
                MyConfig.type = "SMAT";
                if (smat == null) {
                    smat = new SMATFragment();
                }
                ((FunctionActivity) getActivity()).changFragment(smat);
                break;
            case R.id.DCA:
                MyConfig.type = "DCA";
                if (dca == null) {
                    dca = new DCAFragment();
                }
                ((FunctionActivity) getActivity()).changFragment(dca);
                break;
            case R.id.EMAT:
                MyConfig.type = "审计";
//                if (errorFragment == null) {
                errorFragment = new ErrorFragment();
//                }
                ((FunctionActivity) getActivity()).changFragment(errorFragment);
                break;
            case R.id.TAG:
                MyConfig.type = "TAG";
//                if (errorFragment == null) {
                errorFragment = new ErrorFragment();
//                }
                ((FunctionActivity) getActivity()).changFragment(errorFragment);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
