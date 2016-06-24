package com.example.ahmet.pdkdemo.BasicObject;

/**
 * Created by Ahmet on 09.12.2015.
 */
public class SendPersonel {


     private String SEND_TCNO ;
     private String SEND_TARIH ;
     private String SEND_MESAI ;
     private String SEND_URL;
     private String SEND_KARTNO;
     private String SEND_MESAI_tip;
     private String    SEND_ID;

    public SendPersonel(){}

    public SendPersonel(String tc ,String mesai,String imageurl,String tarih ){

        this.SEND_TCNO = tc;
        this.SEND_TARIH = tarih;
        this.SEND_MESAI = mesai;
        this.SEND_URL = imageurl;

    }


    public SendPersonel (String id,String tc,String tip,String tarih ,String resimurl,String mesai){
        this.SEND_TCNO = tc;
        this.SEND_TARIH = tarih;
        this.SEND_MESAI = mesai;
        this.SEND_URL = resimurl;
        this.SEND_MESAI_tip = tip;
        this.SEND_ID = id;

    }

    public String getSEND_TCNO() {
        return SEND_TCNO;
    }

    public void setSEND_TCNO(String SEND_TCNO) {
        this.SEND_TCNO = SEND_TCNO;
    }


    public String getSEND_TARIH() {
        return SEND_TARIH;
    }

    public void setSEND_TARIH(String SEND_TARIH) {
        this.SEND_TARIH = SEND_TARIH;
    }

    public String getSEND_MESAI() {
        return SEND_MESAI;
    }

    public void setSEND_MESAI(String SEND_MESAI) {
        this.SEND_MESAI = SEND_MESAI;
    }


    public String getSEND_URL() {return SEND_URL;}

    public void setSEND_URL(String SEND_URL) {this.SEND_URL = SEND_URL;}


    public String getSEND_KARTNO() {return SEND_KARTNO;}

    public void setSEND_KARTNO(String SEND_KARTNO) {this.SEND_KARTNO = SEND_KARTNO;}

    public String getSEND_MESAI_tip() {return SEND_MESAI_tip;}

    public void setSEND_MESAI_tip(String SEND_MESAI_tip) {this.SEND_MESAI_tip = SEND_MESAI_tip;}

    public String getSEND_ID() {
        return SEND_ID;
    }

    public void setSEND_ID(String SEND_ID) {
        this.SEND_ID = SEND_ID;
    }
}
