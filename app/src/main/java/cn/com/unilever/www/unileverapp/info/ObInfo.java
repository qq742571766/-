package cn.com.unilever.www.unileverapp.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @class
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/7/28 16:28
 */
public class ObInfo implements Parcelable {
    public static final Creator<ObInfo> CREATOR = new Creator<ObInfo>() {
        @Override
        public ObInfo createFromParcel(Parcel in) {
            ObInfo info = new ObInfo();
            info.tv_or = in.readString();
//            info.cb_or = in.readByte() != 0;
            return info;
        }

        @Override
        public ObInfo[] newArray(int size) {
            return new ObInfo[size];
        }
    };
    public String tv_or;
//    public boolean cb_or;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tv_or);
//        dest.writeByte((byte) (cb_or ? 1 : 0));
    }
}
