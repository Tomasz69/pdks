package com.example.ahmet.pdkdemo.BasicObject;

import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmet on 16.04.2016.
 */
public class HadnleObj extends Handler implements Parcelable {

    private Handler h ;


    public HadnleObj(Handler h, Context c){
        this.h = h;

    }

    public HadnleObj(Parcel in) {

    }




    public Handler getH() {
        return h;
    }

    public void setH(Handler h) {
        this.h = h;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(h);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public HadnleObj createFromParcel(Parcel in) {
            return new HadnleObj(in);
        }

        public HadnleObj[] newArray(int size) {
            return new HadnleObj[size];
        }
    };
}
