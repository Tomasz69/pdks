package com.example.ahmet.pdkdemo.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ahmet.pdkdemo.BasicObject.Personel;
import com.example.ahmet.pdkdemo.BasicObject.SendPersonel;
import com.example.ahmet.pdkdemo.BasicObject.Setting;
import com.example.ahmet.pdkdemo.BasicObject.TempPersonel;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Ahmet on 04.12.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context cntx;

    public static final String DATABASE_NAME = "pdk";

    ///        GECiCi TABLODA OLACAKLAR    a /////
    public static final String CONTACTS_TABLE_NAME = "gecicitable";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_TCNO = "tcno";
    public static final String CONTACTS_COLUMN_CIHAZNO = "cihazno";
    public static final String CONTACTS_COLUMN_DATE = "tarih";
    public static final String CONTACTS_COLUMN_IMAGEFILL = "resimyolu";
    public static final String CONTACTS_COLUMN_CARDNO = "kardno";
    public static final String CONTACTS_COLUMN_MESAI_TIP = "mesaidurumu";

    //        PERSONEL TABLOSU          b/////
    public static final String TABLE_NAME = "mypersonel";
    public static final String COLUMN_ID = "id";
    public static final String TCNO = "tcno";
    public static final String KARTNO = "kartno";
    public static final String ADSOYAD = "adsoyad";
    public static final String GOREV = "gorev";
    public static final String BOLUM = "bolum";
    public static final String MESAI = "mesai";


    //   SETTING  TABLOSU //////
    public static final String SETTING_TABLE_NAME = "settingTable";
    public static final String SETTING_COLUMN_ID = "id";
    public static final String SETTING_COLUMN_ADRES = "adres";
    public static final String SETTING_LNO = "lisansNo";
    public static final String SETTING_EN = "en";
    public static final String SETTING_BOY = "boy";
    public static final String SETTING_KOD = "kod";
    public static final String SETTING_KIOSK = "kiosk";
    public static final String SETTING_APPKAPA = "kapat";
    public static final String SETTING_TUMUNUGUNCELLE = "tumunug";
    public static final String SETTING_TUMUNUSIL= "tumunud";

    public static final String SETTING_KAMERA_DURUM = "kamera";

    public static final String SETTING_KISAYOL = "kisayol";


    /// personel tablosu son personel kontrolu icin ///
    public static final String PER_TABLE_NAME = "personelTable";
    public static final String PER_ID = "id";
    public static final String PER_TC = "tc";


    /// personel tablosu son personel kontrolu icin ///
    public static final String TIP_TABLE_NAME = "tippersonelTable";
    public static final String TIP_ID = "tipid";
    public static final String TIP_TC = "tiptc";
    public static final String TIP_TIP = "tiptip";


    //           SEND DATA TABLE       c/////
    public static final String SEND_TABLE_NAME = "sendmypersonel";
    public static final String SEND_COLUMN_ID = "send_id";
    public static final String SEND_TCNO = "send_tcno";
    public static final String SEND_KARTNO = "send_kartno";
    public static final String SEND_ADSOYAD = "send_adsoyad";
    public static final String SEND_GOREV = "send_gorev";
    public static final String SEND_BOLUM = "send_bolum";
    public static final String SEND_TARIH = "send_tarih";
    public static final String SEND_SAAT = "send_saat";
    public static final String SEND_MESAI = "send_mesai";


    String sqlSettingdrop = "DROP TABLE IF EXISTS " + SETTING_TABLE_NAME;
    String sqlSettingcreat = "CREATE TABLE " + SETTING_TABLE_NAME +
            " ( " + SETTING_COLUMN_ID + " INTEGER PRIMARY KEY," +
            SETTING_COLUMN_ADRES + " text, " +
            SETTING_LNO + " text, " +
            SETTING_EN + " text, " +
            SETTING_KISAYOL + " text, " +
            SETTING_KOD + " text, " +
            SETTING_TUMUNUGUNCELLE + " text, " +
            SETTING_TUMUNUSIL + " text, " +
            SETTING_KIOSK + " text, " +
            SETTING_KAMERA_DURUM + " text, " +
            SETTING_APPKAPA + " text, " +
            SETTING_BOY + " text) ";


    String sqlsendtabledrop = "DROP TABLE IF EXISTS " + SEND_TABLE_NAME;
    String sqlsendtablecreat = "CREATE TABLE " + SEND_TABLE_NAME +
            " ( " + SEND_COLUMN_ID + " INTEGER PRIMARY KEY," +
            SEND_TCNO + " text, " +
            SEND_KARTNO + " text, " +
            SEND_ADSOYAD + " text, " +
            SEND_GOREV + " text, " +
            SEND_BOLUM + " text, " +
            SEND_TARIH + " text, " +
            SEND_SAAT + " text, " +
            SEND_MESAI + " text) ";

    String sqlPersonelDrop = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String sqlPersonelCreat = "CREATE TABLE " + TABLE_NAME +
            " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            TCNO + " text, " +
            KARTNO + " text, " +
            ADSOYAD + " text, " +
            MESAI + " text) ";


    String sqlTemproryDrop = "DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME;
    String sqlTemproryCreat = "CREATE TABLE " + CONTACTS_TABLE_NAME +
            " ( " + CONTACTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            CONTACTS_COLUMN_TCNO + " text, " +
            CONTACTS_COLUMN_CARDNO + " text, " +
            CONTACTS_COLUMN_DATE + " text, " +
            CONTACTS_COLUMN_IMAGEFILL + " text, " +
            CONTACTS_COLUMN_MESAI_TIP + " text) ";

    String sqlPersonelDepoDrop = "DROP TABLE IF EXISTS " + PER_TABLE_NAME;
    String sqlPersonelDepoCreat = "CREATE TABLE " + PER_TABLE_NAME +
            " ( " + PER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            PER_TC + " text) ";


    String sqlPersonelTipDrop = "DROP TABLE IF EXISTS " + TIP_TABLE_NAME;
    String sqlPersonelTipCreat = "CREATE TABLE " + TIP_TABLE_NAME +
            " ( " + TIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            TIP_TC + " text, " +
            TIP_TIP + " text) ";

    public static final String BLUETOOTH_DEVICE_TABLE_NAME = "bldevice";
    public static final String BLUETOOTH_DEVICE_ID = "id";
    public static final String BLUETOOTH_DEVICE_MAC = "macdevice";


    String Bluetoothdrop = "DROP TABLE IF EXISTS " + BLUETOOTH_DEVICE_TABLE_NAME;
    String Bluetoothcreat = "CREATE TABLE " + BLUETOOTH_DEVICE_TABLE_NAME +
            " ( " + BLUETOOTH_DEVICE_ID + " INTEGER PRIMARY KEY," +
            BLUETOOTH_DEVICE_MAC + " text) ";



    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 2);
        cntx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlTemproryCreat);
        db.execSQL(sqlPersonelCreat);
        db.execSQL(sqlsendtablecreat);
        db.execSQL(sqlSettingcreat);
        db.execSQL(sqlPersonelDepoCreat);
        db.execSQL(sqlPersonelTipCreat);
        db.execSQL(Bluetoothcreat);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL(sqlTemproryDrop);
        db.execSQL(sqlPersonelDrop);
        db.execSQL(sqlsendtabledrop);
        db.execSQL(sqlSettingdrop);
        db.execSQL(sqlPersonelDepoDrop);
        db.execSQL(sqlPersonelTipDrop);
        db.execSQL(Bluetoothdrop);
        onCreate(db);

    }

    public void InsertData(String data){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BLUETOOTH_DEVICE_MAC, data);
        db.insert(BLUETOOTH_DEVICE_TABLE_NAME, null, contentValues);
        Log.i("DataBase", "Insert basarili");


    }

    public String getData(){
        String adres = null;


        try{
            String countQuery = "SELECT  * FROM " + BLUETOOTH_DEVICE_TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            while (cursor.moveToNext()){
                adres = cursor.getString(cursor.getColumnIndex(BLUETOOTH_DEVICE_MAC));
            }


        }catch (Exception e){

        }

        return  adres;

    }
    public void deleteData(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(BLUETOOTH_DEVICE_TABLE_NAME, null, null);
            Log.i("delete_personel", "silme_basarili");
        } catch (Exception e) {
            Log.i("delete_personel", e.getMessage());
        }
    }


    public int getCountMctable(){
        String countQuery = "SELECT  * FROM " + BLUETOOTH_DEVICE_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Log.i("gelendegerlersetting:", String.valueOf(cnt));
        cursor.close();
        return cnt;
    }
    public ArrayList<SendPersonel> InsertSendTable() {

        ArrayList<SendPersonel> sendItem = new ArrayList<SendPersonel>();


        try {

            String sqlinner = "Select * from " + TABLE_NAME + " a " + " inner join " + CONTACTS_TABLE_NAME + " b " + " on " +
                    "a." + TCNO + "=" + "b." + CONTACTS_COLUMN_TCNO;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(sqlinner, null);


            while (c.moveToNext()) {
                // sendItem.add(new SendPersonel(c.getString(c.getColumnIndex(TCNO)),c.getString(c.getColumnIndex(KARTNO)),c.getString(c.getColumnIndex(CONTACTS_COLUMN_DATE)),"","",""));

                Log.i("InternetsizPersoneller", String.valueOf(c.getInt(c.getColumnIndex(CONTACTS_COLUMN_ID))));
                sendItem.add(new SendPersonel(

                        String.valueOf(c.getInt(c.getColumnIndex(CONTACTS_COLUMN_ID))),
                        c.getString(c.getColumnIndex(TCNO)),
                        c.getString(c.getColumnIndex(CONTACTS_COLUMN_MESAI_TIP)),
                        c.getString(c.getColumnIndex(CONTACTS_COLUMN_DATE)),
                        c.getString(c.getColumnIndex(CONTACTS_COLUMN_IMAGEFILL)),
                        c.getString(c.getColumnIndex(MESAI))));

            }
            db.close();


        } catch (Exception e) {
            Log.e("InsertSendTable", e.getMessage());
        }

        return sendItem;

    }

    public int  getSettingTableid(String v){

        int id  = 0;

        try{
            String sql = "Select * from " + SETTING_TABLE_NAME  + " where "+ SETTING_KISAYOL + " = " + "'"+v+"'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(sql, null);


            while (c.moveToNext()) {
                id = c.getInt(c.getColumnIndex(SETTING_COLUMN_ID));
            }
        }catch (Exception e){
            Log.e("HAAAAATAAA",e.getMessage().toString());
        }

        return  id;
    }

    public boolean InsertPersonelTable(ArrayList<Personel> p) {

        boolean result_insert = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            for (int i = 0; i < p.size(); i++) {

                contentValues.put(TCNO, p.get(i).getTCno());
                contentValues.put(KARTNO, p.get(i).getKartno());
                contentValues.put(ADSOYAD, p.get(i).getAdSyoad());
                contentValues.put(MESAI, p.get(i).getMesaiDurumu());
                db.insert(TABLE_NAME, null, contentValues);
                Log.i("personel", p.get(i).getTCno());

            }

            Log.d("insert_durum_PER", "basarili");
            result_insert = true;

        } catch (Exception e) {
            Log.e("insert_table", e.getMessage());
        }

        return result_insert;

    }

    public void InsertImageTemporry(TempPersonel p, String url) {

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(CONTACTS_COLUMN_TCNO, p.getTckimlikno());
            contentValues.put(CONTACTS_COLUMN_CARDNO, p.getKartno());
            contentValues.put(CONTACTS_COLUMN_DATE, p.getDate());
            contentValues.put(CONTACTS_COLUMN_MESAI_TIP, p.getXmesai());
            contentValues.put(CONTACTS_COLUMN_IMAGEFILL, url);
            db.insert(CONTACTS_TABLE_NAME, null, contentValues);

            Log.i("InsertTemp", "Basarili " +p.getTckimlikno());

        } catch (Exception e) {
            Log.e("hata_insert", e.getMessage());
        }


    }


    public ArrayList<TempPersonel> getDataTempraryTable() {

        ArrayList<TempPersonel> p = new ArrayList<TempPersonel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            p.add(new TempPersonel(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TCNO)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_CIHAZNO)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_CARDNO)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_MESAI_TIP)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_DATE))));
            res.moveToNext();
        }


        return p;
    }

    public boolean deleteallTemprorry() {

        boolean result_delete_temprorry = false;

        Log.i("deleteallTemp", "suanburdayi");

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(CONTACTS_TABLE_NAME, null, null);
            result_delete_temprorry = true;
        } catch (Exception e) {
            return false;
        }
        return result_delete_temprorry;
    }

    public void deleteallPersonel() {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, null, null);
            Log.i("delete_personel", "silme_basarili");
        } catch (Exception e) {
            Log.i("delete_personel", e.getMessage());
        }
    }

    public int getCountTemprry() {
        String countQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int getCountTable() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    public SendPersonel InnerSendDataOnline(TempPersonel personel) {

        SendPersonel sendPersonel = new SendPersonel();

        String sqlOnlineSend = "SELECT  * FROM " + TABLE_NAME + " WHERE " + TCNO + " = " + "'"+personel.getTckimlikno()+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlOnlineSend, null);

        while (cursor.moveToNext()) {
            sendPersonel.setSEND_TCNO(personel.getTckimlikno());
            sendPersonel.setSEND_TARIH(personel.getDate());
            sendPersonel.setSEND_MESAI(cursor.getString(cursor.getColumnIndex(MESAI)));
            sendPersonel.setSEND_KARTNO(cursor.getString(cursor.getColumnIndex(KARTNO)));
        }

        return sendPersonel;

    }


    public void InsertSettingAll(String adres,String lisans,String kod,String kiosk,String kisayol,String sil,String guncell,String en,String boy,String kamera,String kapa){

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();


            contentValues.put(SETTING_COLUMN_ADRES,adres);
            contentValues.put(SETTING_LNO,lisans);
            contentValues.put(SETTING_EN, en);
            contentValues.put(SETTING_BOY, boy);
            contentValues.put(SETTING_KOD,kod);
            contentValues.put(SETTING_KAMERA_DURUM,kamera);
            contentValues.put(SETTING_KISAYOL, kisayol);
            contentValues.put(SETTING_KIOSK, kiosk);
            contentValues.put(SETTING_TUMUNUGUNCELLE, guncell);
            contentValues.put(SETTING_TUMUNUSIL, sil);
            contentValues.put(SETTING_APPKAPA,kapa);




            db.insert(SETTING_TABLE_NAME, null, contentValues);

        }
        catch (Exception e){
            Log.e("InsertAllSetting",e.getMessage().toString());
        }
    }
    public void InsertAllSetting(String kiosk,String kisayol,String sil,String guncell,String en,String boy){
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();


            contentValues.put(SETTING_EN, en);
            contentValues.put(SETTING_BOY, boy);
            contentValues.put(SETTING_KISAYOL, kisayol);
            contentValues.put(SETTING_KIOSK, kiosk);
            contentValues.put(SETTING_TUMUNUGUNCELLE, guncell);
            contentValues.put(SETTING_TUMUNUSIL, sil);



            db.insert(SETTING_TABLE_NAME, null, contentValues);

        }
        catch (Exception e){
            Log.e("InsertAllSetting",e.getMessage().toString());
        }
    }

    public void InsertSetting(String adres, String lisans, String en, String boy, String kisayol,String kod,String kiosk,String kamera) {
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();


            contentValues.put(SETTING_LNO, lisans);
            contentValues.put(SETTING_EN, en);
            contentValues.put(SETTING_BOY, boy);
            contentValues.put(SETTING_COLUMN_ADRES, adres);
            contentValues.put(SETTING_KISAYOL, kisayol);
            contentValues.put(SETTING_KOD,kod);
            contentValues.put(SETTING_KIOSK, kiosk);
            contentValues.put(SETTING_KAMERA_DURUM,kamera);


            db.insert(SETTING_TABLE_NAME, null, contentValues);
            Log.i("Insertsetting", "Basarili");

        } catch (Exception e) {
            Log.e("hata_insertsetting", e.getMessage());
        }
    }


    @SuppressLint("LongLogTag")
    public void UpdateTumunuSilveGuncelleSetting(String updatestring,String deletestring,int i){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(SETTING_TUMUNUGUNCELLE,updatestring);
            contentValues.put(SETTING_TUMUNUSIL,deletestring);


            db.update(SETTING_TABLE_NAME, contentValues, SETTING_COLUMN_ID + "=" + i, null);
            Log.i("UPDATEDeleteUpdatesettingPersonel", "Basarili");

        }catch (Exception e){

            Log.i("UPDATEDeleteUpdatesettingPersonel", e.getMessage().toString());
        }




    }

    public void deleteSetting() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(SETTING_TABLE_NAME, null, null);
            Log.i("delete_setting", "silme_basarili");
        } catch (Exception e) {
            Log.i("delete_setting", e.getMessage());
        }
    }

    public ArrayList<Setting> getElementSettings(){

        ArrayList<Setting> list = new ArrayList<>();

        String sqlOnlineSend = "SELECT  * FROM " + SETTING_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlOnlineSend, null);

        while (cursor.moveToNext()) {

             list.add(new Setting(
                     cursor.getString(cursor.getColumnIndex(SETTING_COLUMN_ADRES)),
                     cursor.getString(cursor.getColumnIndex(SETTING_EN)),
                     cursor.getString(cursor.getColumnIndex(SETTING_BOY)),
                     cursor.getString(cursor.getColumnIndex(SETTING_LNO)),
                     cursor.getString(cursor.getColumnIndex(SETTING_KOD))));
        }

        return  list;
    }

    public ArrayList<Setting> getElementSettingLisansAndAdres(){

        ArrayList<Setting> list = new ArrayList<>();

        String sqlOnlineSend = "SELECT  * FROM " + SETTING_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlOnlineSend, null);

        while (cursor.moveToNext()) {

            list.add(new Setting(
                    cursor.getString(cursor.getColumnIndex(SETTING_COLUMN_ADRES)),
                    cursor.getString(cursor.getColumnIndex(SETTING_LNO)),
                    cursor.getString(cursor.getColumnIndex(SETTING_KOD))
            ));
        }


        return  list;

    }


    public String getLisans(){
        String lisans = null;
        String sqlOnlineSend = "SELECT  * FROM " + SETTING_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlOnlineSend, null);

        while (cursor.moveToNext()) {
            lisans = cursor.getString(cursor.getColumnIndex(SETTING_LNO));
        }
        return lisans;
    }

    public void countSettingelement() {
        String countQuery = "SELECT  * FROM " + SETTING_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        Log.i("gelendegerlersetting:", String.valueOf(cnt));
        cursor.close();
    }

    public String getSettingElements() {
        String element = null;

        String sqlOnlineSend = "SELECT  * FROM " + SETTING_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlOnlineSend, null);

        while (cursor.moveToNext()) {
            element = cursor.getString(cursor.getColumnIndex(SETTING_LNO));
        }

        return element;

    }


    public ArrayList getColumnSettingEnBoy() {

        ArrayList<String> en_boy = new ArrayList<>();

        String sqlOnlineSend = "SELECT  * FROM " + SETTING_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sqlOnlineSend, null);

        while (cursor.moveToNext()) {
            en_boy.add(cursor.getString(cursor.getColumnIndex(SETTING_EN)));
            en_boy.add(cursor.getString(cursor.getColumnIndex(SETTING_BOY)));
        }
        return en_boy;

    }


    public int queryPersonel(String tc) {

        String countQuery = "SELECT  * FROM " + TABLE_NAME + " " + "where " + TCNO + " = " +"'"+tc+"'" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        Log.i("Peronsle",String.valueOf(cnt));
        return cnt;
    }

    /**
     * tc verip karno ve isim bilgileri getirlir.
     * @param tc
     * @return
     */
    public Personel getQueryPersonelKart(String tc) {
        Personel p = new Personel();

        String countQuery = "SELECT  * FROM " + TABLE_NAME + " " + "where " + TCNO + " = " + "'"+tc+"'" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        while (cursor.moveToNext()) {
            p.setKartno(cursor.getString(cursor.getColumnIndex(KARTNO)));
            p.setAdSyoad(cursor.getString(cursor.getColumnIndex(ADSOYAD)));
        }
        return p;
    }

    public String getPersonelKart(String tc) {
        String p = null;

        String countQuery = "SELECT  * FROM " + TABLE_NAME + " " + "where " + TCNO + " = " + "'"+tc+"'" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        while (cursor.moveToNext()) {
            p = cursor.getString(cursor.getColumnIndex(KARTNO));
        }
        return p;
    }


    /**
     * Kart sorgusu ile ad ve tc bilgileri gonderilir.
     * @param kartno
     * @return
     */
    public String getQueryPersonelTC(String kartno){

        String tcno = null;
        Log.i("BluetoothKartNo", kartno);
        String countQuery = "SELECT * FROM " + TABLE_NAME + " " + "where " + KARTNO + " = " + "'"+kartno+"'";

        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            while (cursor.moveToNext()) {
                tcno = cursor.getString(cursor.getColumnIndex(TCNO));
            }
        }
        catch (Exception e){
            Log.e("ErorrEgetPer",e.getMessage().toString());
        }


        return tcno;
    }

    /**
     * Gelen Personeli Eklenip veya guncelleme yapar
     *
     * @param p    : gelen personel
     * @param flag ekleme veya guncelleme karar veren durum.
     */
    @SuppressLint("LongLogTag")
    public void InsertOrUpdatePersonel(Personel p, int flag) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();



        try {
            if (flag == 0) {


                contentValues.put(TCNO, p.getTCno().toString());
                contentValues.put(KARTNO, p.getKartno().toString());
                contentValues.put(ADSOYAD, p.getAdSyoad());
                contentValues.put(MESAI, p.getMesaiDurumu());

                db.insert(TABLE_NAME, null, contentValues);
                Log.i("InsertsettingPersonel",p.getKartno().toString() );


            } else if (flag == 1) {

                if(!p.getMesaiDurumu().equals("")){
                    contentValues.put(TCNO, p.getTCno().toString());
                    contentValues.put(KARTNO, p.getKartno().toString());
                    contentValues.put(ADSOYAD, p.getAdSyoad().toString());
                    contentValues.put(MESAI, p.getMesaiDurumu());
                    db.update(TABLE_NAME, contentValues, "" + TCNO + " = '" + p.getTCno().toString() + "'", null);
                    Log.i("UPDATEsettingPersonel", "Basarili");
                }
                else{
                    deletepersonel(p.getTCno());
                }


            }
        } catch (Exception e) {
            Log.e("hataInsertOrUpdatePersonel", e.getMessage());
        }
    }

    private void deletepersonel(String tCno) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, TCNO + " = " + "'"+tCno+"'", null);

        } catch (Exception e) {

        }
    }

    /**
     * secilen gecmis personel silinir
     *
     * @param id
     */
    public void deleteSelectPersonel(String id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(CONTACTS_TABLE_NAME, CONTACTS_COLUMN_ID + " = " + Integer.parseInt(id), null);
            Log.i("delete_select_personel", "silme_basarili " + id);
        } catch (Exception e) {
            Log.e("delete_select_personel", e.getMessage());
        }
    }


    /**
     * bIRDEK PERSONEL KAYDEDER
     *
     * @param tc
     */
    public void InsertDepoPersonel(String tc) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {

            contentValues.put(PER_TC, tc);
            db.insert(PER_TABLE_NAME, null, contentValues);

            Log.i("InsertsettingPersonel", "Basarili");
        } catch (Exception e) {

        }
    }

    /**
     * SON PERSONELI GETIR
     *
     * @param
     * @return
     */
    public String getLastPersonel() {
        String lasttc = null;

        String countQuery = "SELECT * FROM " + "'" + PER_TABLE_NAME + "'" + " ORDER BY " + "'" + PER_ID + "'" + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        while (cursor.moveToNext()) {
            lasttc = cursor.getString(cursor.getColumnIndex(PER_TC));
        }


        return lasttc;
    }

    /**
     * SON PERSONELI SIL
     */
    public void deleteDepoPersonel() {
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(PER_TABLE_NAME, null, null);

        } catch (Exception e) {

        }
    }

    public int getCountDepoPersonel() {
        int cnt;
        try {
            String countQuery = "SELECT  * FROM " + PER_TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cnt = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            cnt = 0;
        }

        return cnt;
    }

    /**
     * varsa guncelle yoksa ekle tip kategrosinde
     *
     * @param tc
     * @param tip
     * @param flag
     */
    public void InsertOrTipPersonel(String tc, String tip, int flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            if (flag == 1) {


                contentValues.put(TIP_TC, tc);
                contentValues.put(TIP_TIP, tip);
                db.insert(TIP_TABLE_NAME, null, contentValues);

                Log.i("insert_tip", "Basarili");

            } else if (flag == 2) {
                contentValues.put(TIP_TIP, tip);
                db.update(TIP_TABLE_NAME, contentValues, "" + TIP_TC + " = '" + tc + "'", null);
                Log.i("upddate_tip", "Basarili");
            }
        } catch (Exception e) {
            Log.i("TipInsertOrUpdate", "hatali");
        }

    }

    /**
     * tc ye gore o personel var mi sorgula tip icin
     *
     * @param tc
     * @return
     */
    public int getTipTcPersonel(String tc) {
        int i = 0;
        String countQuery = "SELECT  * FROM " + TIP_TABLE_NAME + " " + "where " + TIP_TC + " = " + "'"+tc+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        i = cursor.getCount();
        cursor.close();
        return i;
    }

    /**
     * Belli peryiodlarla tum tip personelleri sil
     */
    public void deletealltippersonel() {
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TIP_TABLE_NAME, null, null);

        } catch (Exception e) {

        }
    }

    /**
     * tc si verilen tipi getir.
     *
     * @param tc
     * @return
     */
    public String getTip(String tc) {

        String tip = "";
        String countQuery = "SELECT  * FROM " + TIP_TABLE_NAME + " " + "where " + TIP_TC + " = " + "'"+tc+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        while (cursor.moveToNext()) {
            tip = cursor.getString(cursor.getColumnIndex(TIP_TIP));
        }
        return tip;

    }

    public String getKiosk(){
        String kiosk = null;
        try{
            String countQuery = "SELECT  * FROM " + SETTING_TABLE_NAME ;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            while (cursor.moveToNext()){
                kiosk = cursor.getString(cursor.getColumnIndex(SETTING_KIOSK));
            }


        }catch (Exception e){

        }
        return  kiosk;
    }

    public String getKameraState(){
        String kamera = null;
        try{
            String countQuery = "SELECT  * FROM " + SETTING_TABLE_NAME ;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            while (cursor.moveToNext()){
                kamera = cursor.getString(cursor.getColumnIndex(SETTING_KAMERA_DURUM));
            }


        }catch (Exception e){

        }
        return  kamera;
    }

    public String getKisayol(){
        String kisayol = null;
        try{
            String countQuery = "SELECT  * FROM " + SETTING_TABLE_NAME ;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            while (cursor.moveToNext()){
                kisayol = cursor.getString(cursor.getColumnIndex(SETTING_KISAYOL));
            }


        }catch (Exception e){

        }
        return  kisayol;
    }


    public String getSettingAppkapa(){
        String appkapa = null;


        try{
            String countQuery = "SELECT  * FROM " + SETTING_TABLE_NAME ;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            while (cursor.moveToNext()){
                appkapa = cursor.getString(cursor.getColumnIndex(SETTING_APPKAPA));
            }


        }catch (Exception e){

        }

        return  appkapa;
    }


    public String getSettingTumunusil(){
        String deleteallstring = null;


        try{
            String countQuery = "SELECT  * FROM " + SETTING_TABLE_NAME ;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            while (cursor.moveToNext()){
                deleteallstring = cursor.getString(cursor.getColumnIndex(SETTING_TUMUNUSIL));
            }


        }catch (Exception e){

        }

        return  deleteallstring;
    }

    public String getSettingTumunuupdate(){
        String updateallstring = null;


        try{
            String countQuery = "SELECT  * FROM " + SETTING_TABLE_NAME ;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            while (cursor.moveToNext()){
                updateallstring = cursor.getString(cursor.getColumnIndex(SETTING_TUMUNUGUNCELLE));
            }


        }catch (Exception e){

        }

        return  updateallstring;
    }

    public String getAdres(){

        String adres = null;


        try{
            String countQuery = "SELECT  * FROM " + SETTING_TABLE_NAME ;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            while (cursor.moveToNext()){
                adres = cursor.getString(cursor.getColumnIndex(SETTING_COLUMN_ADRES));
            }


        }catch (Exception e){

        }

        return  adres;
    }


    public int getcountSetting() {
        int cnt;
        try {
            String countQuery = "SELECT  * FROM " + SETTING_TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cnt = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            cnt = 0;
        }

        return cnt;
    }



    @SuppressLint("LongLogTag")
    public void UpdateSetting(String adres, String lisans, String en, String boy, String kisayol,String kod,String kiosk,String kamera) {


        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(SETTING_COLUMN_ADRES,adres);
            contentValues.put(SETTING_LNO,lisans);
            contentValues.put(SETTING_EN,en);
            contentValues.put(SETTING_BOY,boy);
            contentValues.put(SETTING_KISAYOL,kisayol);
            contentValues.put(SETTING_KOD,kod);
            contentValues.put(SETTING_KIOSK,kiosk);
            contentValues.put(SETTING_KAMERA_DURUM,kamera);


            db.update(SETTING_TABLE_NAME, contentValues, SETTING_COLUMN_ID + "=" + 1, null);
            Log.i("UPDATEDeleteUpdatesettingPersonel", "Basarili");

        }catch (Exception e){

            Log.i("UPDATEDeleteUpdatesettingPersonel", e.getMessage().toString());
        }


    }

    public void InsertOther(String tumunusil, String tumunuguncelle,String appkapat) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {

            contentValues.put(SETTING_TUMUNUGUNCELLE, tumunuguncelle);
            contentValues.put(SETTING_TUMUNUSIL,tumunusil);
            contentValues.put(SETTING_APPKAPA,appkapat);

            db.insert(SETTING_TABLE_NAME, null, contentValues);

            Log.i("InsertsettingPersonel", "Basarili");
        } catch (Exception e) {
            Log.i("InsertOther",e.getMessage().toString());
        }

    }

    public void UpdateOther(String update,String sil,String appkapa) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(SETTING_TUMUNUGUNCELLE,update);
            contentValues.put(SETTING_TUMUNUSIL,sil);
            contentValues.put(SETTING_APPKAPA,appkapa);



            db.update(SETTING_TABLE_NAME, contentValues, SETTING_COLUMN_ID + "=" + 1, null);
            Log.i("UPDATEother", "Basarili");

        }catch (Exception e){

            Log.i("UPDATEother", e.getMessage().toString());
        }
    }

    public void deleteRecordPersonel(String tCno) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, TCNO + " = " + "'"+tCno+"'", null);
            Log.i("deleterecord",tCno);

        } catch (Exception e) {
            Log.e("deleterecord","basarisiz");
        }
    }
}
