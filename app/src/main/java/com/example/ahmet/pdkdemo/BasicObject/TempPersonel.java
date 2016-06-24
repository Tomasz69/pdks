package com.example.ahmet.pdkdemo.BasicObject;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ahmet on 07.12.2015.
 */
public class TempPersonel extends Personel {

    private String tckimlikno ;
    private String kartno  ;
    private String cihazno ;
    private String xmesai  ;
    private String date;


    public TempPersonel(){}

    public TempPersonel(String tc, String cihz, String kard, String mesai, String date){
        this.tckimlikno = tc;
        this.cihazno = cihz;
        this.kartno = kard;
        this.xmesai = mesai;
        this.date = date;
    }


    public String getTckimlikno() {
        return tckimlikno;
    }

    public void setTckimlikno(String tckimlikno) {
        this.tckimlikno = tckimlikno;
    }

    public String getKartno() {
        return kartno;
    }

    public void setKartno(String kartno) {
        this.kartno = kartno;
    }

    public String getCihazno() {
        return cihazno;
    }

    public void setCihazno(String cihazno) {
        this.cihazno = cihazno;
    }

    public String getXmesai() {
        return xmesai;
    }

    public void setXmesaiTipi(String xmesai) {
        this.xmesai = xmesai;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
