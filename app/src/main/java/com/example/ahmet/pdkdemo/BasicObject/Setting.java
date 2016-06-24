package com.example.ahmet.pdkdemo.BasicObject;

/**
 * Created by Ahmet on 09.03.2016.
 */
public class Setting {

    private  String en;
    private  String boy;
    private  String adres;
    private  String lisans;
    private  String kod;


    public Setting(String adres,String en,String boy,String lisans,String kod){
        this.en = en;
        this.boy = boy;
        this.lisans = lisans;
        this.adres = adres;
        this.kod = kod;

    }

    public Setting(String adres,String lisans,String kod){
        this.adres = adres;
        this.lisans = lisans;
        this.kod = kod;
    }


    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getBoy() {
        return boy;
    }

    public void setBoy(String boy) {
        this.boy = boy;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getLisans() {
        return lisans;
    }

    public void setLisans(String lisans) {
        this.lisans = lisans;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }
}
