package com.example.ahmet.pdkdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Log;

import com.example.ahmet.pdkdemo.BasicObject.Personel;
import com.example.ahmet.pdkdemo.BasicObject.TempPersonel;
import com.example.ahmet.pdkdemo.Database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Kamera nesnesi gelen btyte tipindeki veriyi alir
 * Bitmap tipine cevirir
 * Dosyaya istedgimiz en boy seklinde kaydedilir
 * Database e istenilen veri tipinde kayit islemi gerceklesir
 */


public class Kamera {
    private String tc; // personel tc si
    private String tarih; // gelen tarih
    private String imageFileUrl; // resim yolu
    private Context context; // MainActivty gelen contex
    private DatabaseHelper db; // database nesnesi
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Eger  constuct deger bu secilirse
     * @param tc : personel tc si
     * @param tarih : islem yapilan tarih
     */
    public Kamera(String tc, String tarih) {
        this.tc = tc;
        this.tarih = tarih;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param c : mainden contex
     * @param tc  perosonel tc
     * @param tarih bugunun tarihi
     */
    public Kamera(Context c, String tc, String tarih) {
        this.tc = tc;
        this.tarih = tarih;
        this.context = c;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   /**
     * kirpilir isimlendirilir
     * resim galeriye eklenir.
     * @param data : byte tipinde resim objesi
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void CropSaveGallery(byte[] data) {
        FileOutputStream outStream; // resim dosya cikti formati
        Bitmap bmp; // resim bitmap formatina getirlir
        String en, boy; // resim en boy
        imageFileUrl = "/sdcard/" + tc + "_" + tarih + ".jpg"; // resim dosyaya nasil kaydelilecek
        db = new DatabaseHelper(context); // veri tabanni acilir
        en = db.getColumnSettingEnBoy().get(0).toString(); // en getirlir
        boy = db.getColumnSettingEnBoy().get(1).toString(); // boy getirilir
        try {
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length); // byte tipindeki veri bitmap tipine cevirilir
            outStream = new FileOutputStream(String.format(imageFileUrl, System.currentTimeMillis())); // dosya formuna sokulur
            outStream.write(data); // yazdirlilir
            outStream.close(); // outputstream kapatilir
            bmp = Bitmap.createScaledBitmap(bmp, Integer.parseInt(en), Integer.parseInt(boy), false); // en boy belirlenir
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
            File f = new File(imageFileUrl);
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();

        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundEx", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        } finally {
        }

    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * resim dosya yolu
     * @return
     */
    public String getImageFileUrl() {
        return imageFileUrl;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * eger sunucu veya enternete problem varsa dosya yolu veri tabanina eklenir.
     */
    public void insertImageTemp(TempPersonel p) {
        DatabaseHelper db = new DatabaseHelper(context); // Database nesnesi olusturldu
        db.InsertImageTemporry(p,imageFileUrl); // resim yolu database kayit edildi
    }
}
