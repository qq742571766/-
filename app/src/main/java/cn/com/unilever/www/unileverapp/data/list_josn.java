package cn.com.unilever.www.unileverapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @class
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/7/27 15:39
 */
public class list_josn implements Parcelable {

    public static final Creator<list_josn> CREATOR = new Creator<list_josn>() {
        @Override
        public list_josn createFromParcel(Parcel in) {
            list_josn josn = new list_josn();
            josn.area = in.readString();
            josn.createUserId = in.readString();
            josn.errorTime = in.readString();
            josn.errorType = in.readString();
            josn.baseFactory = in.readString();
            josn.errorDate = in.readString();
            josn.errorContent = in.readString();
            josn.key = in.readString();
            josn.closeUserId = in.readString();
            josn.errorRole = in.readString();
            josn.error_close_idea = in.readString();
            josn.url = in.readString();
            josn.isSolve = in.readString();
            return josn;
        }

        @Override
        public list_josn[] newArray(int size) {
            return new list_josn[size];
        }
    };
    public String area;
    public String createUserId;
    public String errorTime;
    public String errorType;
    public String baseFactory;
    public String errorDate;
    public String errorContent;
    public String key;
    public String closeUserId;
    public String errorRole;
    public String error_close_idea;
    public String url;
    public String isSolve;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(String errorTime) {
        this.errorTime = errorTime;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getBaseFactory() {
        return baseFactory;
    }

    public void setBaseFactory(String baseFactory) {
        this.baseFactory = baseFactory;
    }

    public String getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(String errorDate) {
        this.errorDate = errorDate;
    }

    public String getErrorContent() {
        return errorContent;
    }

    public void setErrorContent(String errorContent) {
        this.errorContent = errorContent;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCloseUserId() {
        return closeUserId;
    }

    public void setCloseUserId(String closeUserId) {
        this.closeUserId = closeUserId;
    }

    public String getErrorRole() {
        return errorRole;
    }

    public void setErrorRole(String errorRole) {
        this.errorRole = errorRole;
    }

    public String getError_close_idea() {
        return error_close_idea;
    }

    public void setError_close_idea(String error_close_idea) {
        this.error_close_idea = error_close_idea;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsSolve() {
        return isSolve;
    }

    public void setIsSolve(String isSolve) {
        this.isSolve = isSolve;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(area);
        dest.writeString(createUserId);
        dest.writeString(errorTime);
        dest.writeString(errorType);
        dest.writeString(baseFactory);
        dest.writeString(errorDate);
        dest.writeString(errorContent);
        dest.writeString(key);
        dest.writeString(closeUserId);
        dest.writeString(errorRole);
        dest.writeString(error_close_idea);
        dest.writeString(url);
        dest.writeString(isSolve);
    }
}
