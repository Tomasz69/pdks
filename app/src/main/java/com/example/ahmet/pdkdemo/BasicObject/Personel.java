package com.example.ahmet.pdkdemo.BasicObject;

/**
 * Created by Ahmet on 05.12.2015.
 */
public class Personel {

    private String TCno;
    private String Kartno;
    private String MesaiDurumu;
    private String AdSyoad;
    private String Bolum;


    public Personel(){}


    public String getTCno() {
        return TCno;
    }

    public void setTCno(String TCno) {
        this.TCno = TCno;
    }

    public String getKartno() {
        return Kartno;
    }

    public void setKartno(String kartno) {
        Kartno = kartno;
    }

    public String getMesaiDurumu() {
        return MesaiDurumu;
    }

    public void setMesaiDurumu(String mesaiDurumu) {
        MesaiDurumu = mesaiDurumu;
    }

    public String getAdSyoad() {
        return AdSyoad;
    }

    public void setAdSyoad(String adSyoad) {
        AdSyoad = adSyoad;
    }

    public String getBolum() {
        return Bolum;
    }

    public void setBolum(String bolum) {
        Bolum = bolum;
    }
}
