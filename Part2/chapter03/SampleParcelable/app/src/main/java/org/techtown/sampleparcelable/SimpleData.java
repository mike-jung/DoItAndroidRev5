package org.techtown.sampleparcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2016-12-13.
 */

public class SimpleData implements Parcelable {
    // 숫자 데이터
    int number;

    // 문자열 데이터
    String message;

    /**
     * 데이터 2개를 이용하여 초기화하는 생성자
     *
     * @param num
     * @param msg
     */
    public SimpleData(int num, String msg) {
        number = num;
        message = msg;
    }

    /**
     * 다른 Parcel 객체를 이용해 초기화하는 생성자
     *
     * @param src
     */
    public SimpleData(Parcel src) {
        number = src.readInt();
        message = src.readString();
    }

    /**
     * 내부의 CREATOR 객체 생성
     */
    @SuppressWarnings("unchecked")
    public static final Creator CREATOR = new Creator() {

        public SimpleData createFromParcel(Parcel in) {
            return new SimpleData(in);
        }

        public SimpleData[] newArray(int size) {
            return new SimpleData[size];
        }

    };


    public int describeContents() {
        return 0;
    }

    /**
     * 데이터를 Parcel 객체로 쓰기
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(message);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
