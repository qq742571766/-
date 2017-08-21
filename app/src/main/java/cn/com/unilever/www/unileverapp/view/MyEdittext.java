package cn.com.unilever.www.unileverapp.view;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;

/**
 * @class Edittext
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/8/6 20:33
 */
public class MyEdittext extends android.support.v7.widget.AppCompatEditText {

    public MyEdittext(Context context) {
        super(context);
    }

    public MyEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(null);
    }
}
